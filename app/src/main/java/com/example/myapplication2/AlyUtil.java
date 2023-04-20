package com.example.myapplication2;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class AlyUtil {
    public static int temp;
    public static int hum;
    public  static int weight;
    static int hinder;
    static String gweight1;
    String productKey="a12dwawLvlp";//三元组
    String deviceName="SG90";
    String deviceSecret="456cebc13318d60b7915475267931fd4";
    String regionId="cn-shanghai";
    String PUBtopic="/a12dwawLvlp/SG90/user/sr";//设备属性上报的topic,topic(主题),发布
    String LOWtopic="/a12dwawLvlp/SG90/user/get";//本设备订阅的topic
    public String pub_payload1 ="{id: 123452452,params: { duoji: 0,Temperature: 26.2, Humidity: 60.4,weight: 0,hinder:0},method:\"thing.event.property.post\"}";//数据上传的jason格式

    public MQTTUtil mqttutil=null;

    public void connect()
    {
        if (mqttutil==null){
            mqttutil = new MQTTUtil(productKey,deviceName,deviceSecret,regionId);
            mqttutil.connect();
        }
    }
    public void SendMassege(){
        if (mqttutil==null){
            mqttutil = new MQTTUtil(productKey,deviceName,deviceSecret,regionId);
            mqttutil.connect();
        }
        mqttutil.publish(PUBtopic,pub_payload1);

    }
    public void SubscribeMassege(Context main){
        if (mqttutil == null) {
            mqttutil = new MQTTUtil(productKey, deviceName, deviceSecret, regionId);
            mqttutil.connect();
        }
        mqttutil.listen(main, LOWtopic);
        String dt = mqttutil.getData();
        if (dt.length() > 350) {//dt.length()>200
            Gson gson = new Gson();
            AlyunData alyunData = new AlyunData();
            alyunData = gson.fromJson(dt, AlyunData.class);
            temp=alyunData.getItems().getTem().getValue();
            hum=alyunData.getItems().getHumi().getValue();
            weight=alyunData.getItems().getweight().getValue();
            Log.v("test22", String.valueOf(weight)) ;
            gweight1= String.valueOf(weight);
            hinder=alyunData.getItems().getHinder().getValue();

        }

    }

}
