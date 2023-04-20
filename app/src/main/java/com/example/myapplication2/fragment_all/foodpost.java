package com.example.myapplication2.fragment_all;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import com.example.myapplication2.AlyUtil;
import com.example.myapplication2.DataDisplay;
import com.example.myapplication2.MainActivity;
import com.example.myapplication2.R;

import java.util.Timer;
import java.util.TimerTask;

public class foodpost extends Fragment {
    Context mcontext;
    public Notification notification;
    private Spinner sp_set;
    private TextView tv_food;
    private Button bt_sure,bt_clear,bt_cancel;
    AlyUtil alyutil;


    public Integer shengyu=60;
    public int foodmode=0;
    private static final String[]str=new String[]{"少量投食",//1
            "中量投食",//2
            "大量投食",//3
            };
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_food, container, false);
        mcontext=view.getContext();
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        {
            tv_food=view.findViewById(R.id.tv_food);
            sp_set=view.findViewById(R.id.sp_set);
            bt_sure=view.findViewById(R.id.bt_sure);
            bt_cancel=view.findViewById(R.id.bt_cancel);
            bt_clear=view.findViewById(R.id.bt_clear);

            //显示重量
            DataDisplay dataDisplay=new DataDisplay();
            dataDisplay.Display(null,null,null,tv_food,mcontext);


            //设置投食量
            //创建适配器，第一个参数是上下文，第二个是引用安卓内部布局，可以重写，第三个是给的数组
            ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,str);
            //送入适配器
            sp_set.setAdapter(arrayAdapter);
            //spinner.setSelection(0,false);//初始化禁用首次调用OnItemSelectedListener事件时的下拉列表项
            sp_set.setPrompt("请选择要投放的范围");
            sp_set.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    String data = (String) sp_set.getItemAtPosition(position);
                    switch (data){
                        case "少量投食":
                            foodmode=1;
                            break;
                        case "中量投食":
                            foodmode=2;
                            break;
                        case  "大量投食":
                            foodmode=3;
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });




            bt_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alyutil=new AlyUtil();
                    String information=getResources().getString(R.string.layloadfood);
                    alyutil.pub_payload1 = String.format(information,foodmode);
                    alyutil.SendMassege();
                    Toast.makeText(getActivity(), "投食成功", Toast.LENGTH_SHORT).show();

                }
            });
            //一键清仓
            bt_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   对话框二次确定是否清仓
                    AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
                    normalDialog.setIcon(R.drawable.warning);
                    normalDialog.setTitle("注意");
                    normalDialog.setMessage("是否确定开启一键清仓");

                    normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alyutil=new AlyUtil();
                            alyutil.pub_payload1 ="{id: 123452452,params: { duoji: 4},method:\"thing.event.property.post\"}";
                            alyutil.SendMassege();
                            Toast.makeText(getActivity(), "开始清仓", Toast.LENGTH_SHORT).show();
                        }
                    });

                    normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    // 显示
                    normalDialog.show();
                }
            });
            //取消一键清仓
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alyutil=new AlyUtil();
                    alyutil.pub_payload1 ="{id: 123452452,params: { duoji: 5},method:\"thing.event.property.post\"}";
                    alyutil.SendMassege();
                    Toast.makeText(getActivity(), "取消清仓", Toast.LENGTH_SHORT).show();
                }
            });

//            scheduleAtFixedRate(task, delay,period)
//            task-所要执行的任务 delay-执行任务的延迟时间，单位毫秒 period-执行一次task的时间间隔


//
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    shengyu= AlyUtil.weight;
                    if(shengyu<=50){
                        initView("通知","您的爱宠要挨饿啦，快点加点东西吧！");
                    }
                }
            },0,10000);//10秒检测一次剩余重量，剩余量不足则发通知

        }
    }
    public void initView(String title,String content) {
        //高版本的模拟器或手机还需要开启渠道才能显示通知
        NotificationChannel notificationChannel = null;
        // 获取系统的通知管理器(必须设置channelId)
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("001", "channel_name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "001");
        //实例化一个意图，当点击通知时会跳转执行这个意图
        Intent intent = new Intent(getActivity(), MainActivity.class);
        //将意图进行封装
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //设置Notification的点击之后执行的意图
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.eat2);//通知图片
        builder.setContentTitle(title);//通知标题
        builder.setContentText(content);//通知文本
        notification = builder.build();
        // 发送通知(Notification与NotificationManager的channelId必须对应)
        notificationManager.notify(0, notification);
    }


}

