package com.example.myapplication2;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.fragment_all.foodpost;

import java.util.Calendar;

public class DataDisplay {
     String temp;
     static AlyUtil alyutil;
     static com.example.myapplication2.fragment_all.foodpost foodpost;
     String humi;
    static Handler handler;

    public void Display(TextView nowtemp,TextView nowhunmi,TextView nowtime,TextView nowwight,Context mcontext){
        alyutil=new AlyUtil();
        handler=new Handler();
        Runnable runnable = new Runnable() //创建新线程执行操作
        {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                //月份在Calendar中是从0开始的,因此要加1
                int month=calendar.get(Calendar.MONTH)+1;
                String time = calendar.get(Calendar.YEAR)
                        + "-" + month + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE) + ":"
                        + calendar.get(Calendar.SECOND);

                alyutil.SubscribeMassege(mcontext);
                temp = String.valueOf(AlyUtil.temp);
                humi = String.valueOf(AlyUtil.hum);
                if (AlyUtil.temp >=50&& AlyUtil.hum <30){
                    foodpost.initView("注意","您室内温湿度异常，请及时查看监控情况");
                }

                if(nowtime==null&&nowwight==null)//只需要温湿度
                {
                    nowtemp.setText("当前温度: " +temp + "℃ ");
                    nowhunmi.setText("当前湿度: " + humi+ "% ");
                }else if(nowwight==null)//需要温湿度和时间
                {
                    nowtemp.setText("当前温度: " + temp + "℃ ");
                    nowhunmi.setText("当前湿度: " + humi + "% ");
                    nowtime.setText(time);
                    if(alyutil.hinder==1){
                        vibrator();
                    }
                } else if (nowhunmi==null&&nowtemp==null&&nowtime==null) {
                    String a= String.valueOf(AlyUtil.gweight1);
                    nowwight.setText(a+"g");

                }

                handler.postDelayed(this,1000);
            }
            private void vibrator() {
                {   Vibrator vibrator;//实例化震动服务
                    vibrator = (Vibrator)mcontext.getSystemService(Context.VIBRATOR_SERVICE);
                    Toast.makeText(mcontext, "有物体靠近",Toast.LENGTH_SHORT).show();
                    long[] pattern = { 500,500,500,500};
                    //震动500ms,等待500ms，再震动500ms,再等待500ms，即震动两次
                    vibrator.vibrate(pattern, -1);// -1表示只执行一次，不循环

                }
            }
        };
        handler.postDelayed(runnable,1000);//1s获取订阅信息，也就是获取云平台数据
    }
}


