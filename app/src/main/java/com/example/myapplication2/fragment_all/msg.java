package com.example.myapplication2.fragment_all;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication2.AlyUtil;
import com.example.myapplication2.DataDisplay;
import com.example.myapplication2.R;

public class msg extends Fragment {
    Context mcontext;
     WebView webView;
    TextView tv_humi,tv_tem;
    AlyUtil alyutil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_msg, container, false);
        mcontext=view.getContext();
        return view;
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //天气预报web
        webView=view.findViewById(R.id.wb_weather);
        webView.loadUrl("https://m.moji.com/weather/china/guangxi/hezhou");
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webView.getSettings().setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);////优先使用缓存
       // webview.clearCache(true);可清除webview缓存

        tv_humi=view.findViewById(R.id.tv_humi);
        tv_tem=view.findViewById(R.id.tv_tem);

        alyutil=new AlyUtil();
        //显示温湿度
        DataDisplay dataDisplay=new DataDisplay();
        dataDisplay.Display(tv_tem,tv_humi,null,null,mcontext);

    }
}
