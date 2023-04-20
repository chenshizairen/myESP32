package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication2.fragment_all.foodpost;
import com.example.myapplication2.fragment_all.msg;
import com.example.myapplication2.fragment_all.webcam;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> mFragments;   //存放视图
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private List<String> mtitle;  //存放底部标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
        private void initView() {
            mTabLayout = findViewById(R.id.tablayout);
            viewPager = findViewById(R.id.viewpager);

            mFragments=new ArrayList<>();
            mFragments.add(new msg());
            mFragments.add(new foodpost());
            mFragments.add(new webcam());

            mtitle=new ArrayList<String>();
            mtitle.add("主页");
            mtitle.add("投食");
            mtitle.add("视频");

            //实例化适配器
            MyAdapt adapt = new MyAdapt(getSupportFragmentManager(), mFragments, mtitle);
            viewPager.setAdapter(adapt);

            //设置打开时的默认页面
            viewPager.setCurrentItem(0);

            //将 ViewPager 绑定到 TabLayout上
            mTabLayout.setupWithViewPager(viewPager);//给tab设置一个viewpager


        }
}