# SlideDeleteView
模仿qq消息列表删除功能，向左滑动带出删除按钮，点击删除可以实现删除。

在《Android精彩编程200例》中看到这个功能的实现，整理记录下来

使用自定义控件+ListView显示

![image](https://github.com/Studently/SlideDeleteView/tree/master/gif/效果演示.gif)

首先自定义一个SlidingButtonView

/**
 * 滑动按钮自定义控件 
 */

public class SlidingButtonView extends HorizontalScrollView {

    private TextView lTextView_Delete;      //删除按钮
    private int lScrollWidth;               //横向滑动的范围
    private Boolean first = false;            //标记第一次进入获取删除按钮控件


    public interface OnSlideListener{
        public void onSlide(SlidingButtonView View);
    }


    public SlidingButtonView(Context context) {
        this(context, null);
    }

    public SlidingButtonView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    //测量过程
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //第一次进入获取删除按钮控件
        if(!first){
            lTextView_Delete = (TextView) findViewById(R.id.tv_delete);
            first = true;
        }

    }

    //布局过程
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //默认隐藏删除按钮
        if(changed){
            this.scrollTo(0,0);
            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            lScrollWidth = lTextView_Delete.getWidth();
        }

    }

    /**
     *滑动手指抬起时的手势判断
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP://手指抬起，可能右滑动
            case MotionEvent.ACTION_CANCEL://手指按下，滑动后触发
                changeScrollx();            //根据滑动距离判断是否显示删除按钮
                return true;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据滑动距离判断是否显示删除按钮
     */
    public void changeScrollx(){
        //触摸滑动的距离大于删除按钮宽度的一半
        if(getScrollX() >= (lScrollWidth/2)){
            //显示删除按钮
            this.smoothScrollTo(lScrollWidth, 0);
        }else{
            //隐藏删除按钮
            this.smoothScrollTo(0, 0);
        }
    }

}

接下来是自定义ListView的adapter：MyAdapter.java

//自定义ListView适配器
public class MyAdapter extends ArrayAdapter<User>{

    //listView填充的数据
    private List<User> userList;
    //listview 子项布局id
    private int resourceId;
    //上下文
    private Context context;

    private SlidingButtonView slidingButtonView;

    /**
     * 构造函数
     * @param context 上下文
     * @param resource ListView子项布局id
     * @param objects ListView填充的数据
     */
    public MyAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resourceId=resource;
        this.userList=objects;

    }


    /**
     * 该方法在每个子项被滚动到屏幕时，被调用
     * @param position 子项在listview中的位置
     * @param convertView 将之前加载好的布局进行缓存
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user=userList.get(position);//获得当前子项对应的数据
        View view;//子项布局

        //为子项加载我们传入的布局id
        view= LayoutInflater.from(context).inflate(resourceId,parent,false);
            //初始化MyViewHolder对象
        TextView deleteView;//删除控件
        ImageView imageView;//图片
        TextView nameView;//姓名
        TextView message;//消息
        ViewGroup layout_content;//除了删除按钮的布局
        deleteView=view.findViewById(R.id.tv_delete);
        imageView=view.findViewById(R.id.img);
        nameView=view.findViewById(R.id.name);
        message=view.findViewById(R.id.message);
        layout_content=view.findViewById(R.id.layout_content);

        imageView.setImageResource(user.getImage());
        nameView.setText(user.getName());
        message.setText(user.getMessage());
        //将内容布局设置成屏幕宽度，这样滑动之前看不到删除按钮
        layout_content.getLayoutParams().width= Utils.getScreenWidth(context);

        //点击删除按钮
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.remove(position);//根据子项位置，删除子项数据
                notifyDataSetChanged();//刷新
            }
        });
        return view;
    }
}

MyAdapter.java文件中使用一个Utils类

public class Utils {

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        //获取窗口管理服务
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        //屏幕参数对象
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}



设置listview子项的布局xml：item_listview.xml

<?xml version="1.0" encoding="utf-8"?>
<com.example.ly.slidedeleteview.view.SlidingButtonView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/slidingbutton_view"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:layout_marginBottom="1dp"
    android:background="@android:color/white">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <!--删除按钮-->
        <TextView
            android:id="@+id/tv_delete"
            android:layout_width="80dp"
            android:layout_height="match_parent"
            android:layout_toRightOf="@+id/layout_content"
            android:background="@drawable/btn_click_red_bg"
            android:gravity="center"
            android:text="删 除"
            android:textColor="#DDFFFFFF" />

        <RelativeLayout
            android:id="@+id/layout_content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center_vertical">
            <!--图标-->
            <ImageView
                android:id="@+id/img"
                android:layout_width="50dp"
                android:layout_height="50dp" />
            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:layout_toRightOf="@+id/img"
                android:orientation="vertical"
                android:gravity="center_vertical">
                <!--名称-->
                <TextView
                    android:id="@+id/name"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginLeft="20dp"
                    android:textColor="#000000"
                    android:textSize="15dp"
                    />
                <TextView
                    android:id="@+id/message"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginLeft="20dp"
                    android:layout_marginRight="15dp"
                    android:textSize="10dp"
                    android:text="123"
                    android:singleLine="true"
                    />
            </LinearLayout>


        </RelativeLayout>

    </RelativeLayout>
</com.example.ly.slidedeleteview.view.SlidingButtonView>

在item_listview.xml文件中删除按钮使用的文件drawable下的btn_click_red_bg.xml

<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android" >
    <item android:state_pressed="true">
        <layer-list >
            <item >
		        <shape >
		            <solid android:color="#FF0000"/>
		        </shape>
		    </item>
            <item >
                <shape >
		            <solid android:color="#22000000"/>
		        </shape>
            </item>
        </layer-list>
    </item>
    <item >
        <shape >
            <solid android:color="#FF0000"/>
        </shape>
    </item>
</selector>

其中selector、shape、layer-list作用：

selector：选择器，根据按钮状态来操作

shape：设置形状，颜色填充等

layer-list：控件效果的层叠
