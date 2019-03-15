package com.example.ly.slidedeleteview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ly.slidedeleteview.R;
import com.example.ly.slidedeleteview.Util.Utils;
import com.example.ly.slidedeleteview.domain.User;
import com.example.ly.slidedeleteview.view.SlidingButtonView;

import java.util.List;
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
