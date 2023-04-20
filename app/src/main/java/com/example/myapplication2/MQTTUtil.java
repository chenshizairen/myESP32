package com.example.myapplication2;

import android.content.Context;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MQTTUtil {
    private String productKey, deviceName, deviceSecret, regionId, topic,getdata="hh1"; //基础配置

    private MqttClient mqttClient;


    public MQTTUtil(String productKey, String deviceName, String deviceSecret, String regionId) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.deviceSecret = deviceSecret;
        this.regionId = regionId; //这里设置的是物联网平台的区域，例如“华东2（上海）”的值为“cn-shanghai”，具体地区对应的值请查看阿里云的文档
        topic ="/a12dwawLvlp/${deviceName}/user/data";
    }


    //连接
    public boolean connect() {
        try {
            //配置 服务器地址 客户端id 用户名 密码
            String clientId = "java" + System.currentTimeMillis();
            String timestamp = String.valueOf(System.currentTimeMillis());
            Map<String, String> params = new HashMap<>(16);
            params.put("productKey", productKey);
            params.put("deviceName", deviceName);
            params.put("clientId", clientId);
            params.put("timestamp", timestamp);
            String targetServer = "tcp://" + productKey + ".iot-as-mqtt." + regionId + ".aliyuncs.com:1883";
            String mqttClientId = clientId + "|securemode=3,signmethod=hmacsha1,timestamp=" + timestamp + "|";
            String mqttUsername = deviceName + "&" + productKey;
            String mqttPassword = sign(params, deviceSecret, "hmacsha1"); //获取密码
            //使用MQTT进行连接
            MemoryPersistence persistence = new MemoryPersistence();
            mqttClient = new MqttClient(targetServer, mqttClientId, persistence);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setMqttVersion(4);
            connectOptions.setCleanSession(false);
            connectOptions.setAutomaticReconnect(false);
            connectOptions.setUserName(mqttUsername);
            connectOptions.setPassword(mqttPassword.toCharArray());
            connectOptions.setKeepAliveInterval(60);
            mqttClient.connect(connectOptions);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //发布 topic是要发布的topic地址，只需补全剩下的地址即可 payload或许有一定的格式，可以在类中定义格式，本例子偷懒不写了=W=
    public boolean publish(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes("utf-8"));
            /*
                这里出现了Qos，稍微科普一下，后面就不再重复了
                参数0代表Sender发送的一条消息，Receiver最多能收到一次，也就是说Sender尽力向Receiver发送消息，如果发送失败，也就算了
                参数1代表Sender发送的一条消息，Receiver至少能收到一次，也就是说Sender向Receiver发送消息，如果发送失败，会继续重试，直到Receiver收到消息为止，但是因为重传的原因，Receiver有可能会收到重复的消息
                参数2代表Sender发送的一条消息，Receiver确保能收到而且只收到一次，也就是说Sender尽力向Receiver发送消息，如果发送失败，会继续重试，直到Receiver收到消息为止，同时保证Receiver不会因为消息重传而收到重复的消息
            */
            message.setQos(1);
            if (mqttClient.isConnected()){
                mqttClient.publish(topic, message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //订阅
    public boolean listen(Context context, String topic) {

        try {
            //注册广播

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //System.out.println(message.toString());
                    //System.out.println(message.toString().length());
                    if (message.toString().length()>320)
                    {
                        //System.out.println(message.toString());
                        getdata =message.toString();
                    }

                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            MqttTopic mqttTopic = mqttClient.getTopic(topic);
            mqttClient.subscribe(topic, 1);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //关闭连接
    public boolean close(Context context) {
        try {
          //  EventBus.getDefault().unregister(context);//注销广播
            mqttClient.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private String sign(Map<String, String> params, String deviceSecret, String signMethod) {
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            if ("sign".equalsIgnoreCase(key)) {
                continue;
            }
            canonicalizedQueryString.append(key).append(params.get(key));
        }
        try {
            String key = deviceSecret;
            return encryptHMAC(signMethod, canonicalizedQueryString.toString(), key);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String encryptHMAC(String signMethod, String content, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes("utf-8"), signMethod);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] data = mac.doFinal(content.getBytes("utf-8"));
        return bytesToHexString(data);
    }

    private String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i=0; i<bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    public String getData()
    {
        return getdata;
    }
}
