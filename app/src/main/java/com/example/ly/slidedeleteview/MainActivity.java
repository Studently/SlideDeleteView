package com.example.ly.slidedeleteview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ly.slidedeleteview.adapter.MyAdapter;
import com.example.ly.slidedeleteview.domain.User;
import com.example.ly.slidedeleteview.view.SlidingButtonView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private MyAdapter adapter;
    private List<User> userList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listView.setAdapter(adapter);
    }

    //初始化控件
    private void init(){
        listView=findViewById(R.id.list_view);
        initData();
        adapter=new MyAdapter(MainActivity.this,R.layout.item_listview,userList);

    }

    //初始化userList对象
    private void initData(){
        for(int i=0;i<20;i++){
            User user=new User();
            user.setImage(R.mipmap.ic_launcher);
            user.setName("小"+i);
            user.setMessage("你滑动删除我呀！");
            userList.add(user);
        }
    }
}
