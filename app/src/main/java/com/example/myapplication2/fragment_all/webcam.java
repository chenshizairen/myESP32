package com.example.myapplication2.fragment_all;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication2.AlyUtil;
import com.example.myapplication2.DataDisplay;
import com.example.myapplication2.R;

import java.util.Calendar;

public class webcam extends Fragment {

    Context mcontext;
    private WebView webView;
    private static final String[]str=new String[]{"快来吃饭了，喵",//1
            "快来吃饭了，汪",//2
            "我很快就回来了，再等一会",//3
            "做的很好",//4
            "不要到处乱跑呀",//5
            "多吃点，别饿着了"};//6
    private Spinner spinner;
    private TextView tv_select,tv_time,tv_temp,tv_humity;
    private AlyUtil alyutil;
    public int medianum=0;
    static int warninghumi;
    static int warningtemp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //onCreateView是创建该fragment对应的视图，必须在这里创建自己的视图并返回给调用者。
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_web, container, false);
        mcontext=view.getContext();
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tv_select= view.findViewById(R.id.tv_select);
        tv_time= view.findViewById(R.id.tv_time);
        tv_temp= view.findViewById(R.id.tv_temp);
        tv_humity= view.findViewById(R.id.tv_humity);
        Calendar calendar = Calendar.getInstance();
        //月份在Calendar中是从0开始的,因此要加1
        int month=calendar.get(Calendar.MONTH)+1;
        tv_time.setText(calendar.get(Calendar.YEAR)+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));

        webView=view.findViewById(R.id.web);
        webView.loadUrl("https://69677f2h72.zicp.fun/mjpeg/1");
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        //绑定
        spinner=getView().findViewById(R.id.spinner_medie);
        //创建适配器，第一个参数是上下文，第二个是引用安卓内部布局，可以重写，第三个是给的数组
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,str);
        //送入适配器
        spinner.setAdapter(arrayAdapter);
        //spinner.setSelection(0,false);//初始化禁用首次调用OnItemSelectedListener事件时的下拉列表项
        spinner.setPrompt("请选择要播放的音频");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String data = (String) spinner.getItemAtPosition(position);
                switch (data){
                    case "快来吃饭了，喵":
                        medianum=1;
                        break;
                    case  "快来吃饭了，汪":
                        medianum=2;
                        break;
                    case  "我很快就回来了，再等一会":
                        medianum=3;
                        break;
                    case  "做的很好":
                        medianum=4;
                        break;
                    case "不要到处乱跑呀":
                        medianum=5;
                        break;
                    case  "多吃点，别饿着了":
                        medianum=6;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button bt_select= view.findViewById(R.id.bt_select);
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //新建链接发指令
                alyutil=new AlyUtil();
                String information=getResources().getString(R.string.layloadmedia);
                alyutil.pub_payload1 = String.format(information,medianum);
                alyutil.SendMassege();
            }
        });
        alyutil=new AlyUtil();
        //显示温湿度和时间
        DataDisplay dataDisplay=new DataDisplay();
        dataDisplay.Display(tv_temp,tv_humity,tv_time,null,mcontext);

        Handler handler=new Handler();
        warningtemp= AlyUtil.temp;
        warninghumi= AlyUtil.hum;
        if(warninghumi<=10&&warningtemp>=45);{
            warning();
        }
    }
    private void warning() {
       foodpost foodpost=new foodpost();
       foodpost.initView("注意","您室内温湿度异常，请及时查看监控情况");
    }

}
