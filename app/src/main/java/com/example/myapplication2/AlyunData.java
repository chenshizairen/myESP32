package com.example.myapplication2;

import com.google.gson.annotations.SerializedName;

public class AlyunData {


    @SerializedName("deviceType")
    private String deviceType;
    @SerializedName("iotId")
    private String iotId;
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("checkFailedData")
    private CheckFailedDataDTO checkFailedData;
    @SerializedName("productKey")
    private String productKey;
    @SerializedName("gmtCreate")
    private Long gmtCreate;
    @SerializedName("deviceName")
    private String deviceName;
    @SerializedName("items")
    private ItemsDTO items;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public CheckFailedDataDTO getCheckFailedData() {
        return checkFailedData;
    }

    public void setCheckFailedData(CheckFailedDataDTO checkFailedData) {
        this.checkFailedData = checkFailedData;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public ItemsDTO getItems() {
        return items;
    }

    public void setItems(ItemsDTO items) {
        this.items = items;
    }

    public static class CheckFailedDataDTO {
    }

    public static class ItemsDTO {
        @SerializedName("Humidity")//属性重命名，可以将json中的属性名转为我们自己自定义的属性名
        private HumiDTO humi;
        @SerializedName("Temperature")
        private TemDTO tem;
        @SerializedName("weight")
        private WeightDTO weight;

       @SerializedName("hinder")
        private HinderDTO hinder;



        public HumiDTO getHumi() {
            return humi;
        }

        public void setHumi(HumiDTO humi) {
            this.humi = humi;
        }

        public TemDTO getTem() {
            return tem;
        }

        public void setTem(TemDTO tem) {
            this.tem = tem;
        }

        public WeightDTO getweight() {
            return weight;
        }

        public void setweight(WeightDTO weight) {
            this.weight = weight;
        }

        public HinderDTO getHinder() {
            return hinder;
        }

        public void setHinder(HinderDTO hinder) {
            this.hinder = hinder;
        }

        public static class HumiDTO {
            @SerializedName("time")
            private Long time;
            @SerializedName("value")
            private Integer value;

            public Long getTime() {
                return time;
            }

            public void setTime(Long time) {
                this.time = time;
            }

            public Integer getValue() {
                return value;
            }

            public void setValue(Integer value) {
                this.value = value;
            }
        }

        public static class TemDTO {
            @SerializedName("time")
            private Long time;
            @SerializedName("value")
            private Integer value;

            public Long getTime() {
                return time;
            }

            public void setTime(Long time) {
                this.time = time;
            }

            public Integer getValue() {
                return value;
            }

            public void setValue(Integer value) {
                this.value = value;
            }
        }

        public static class WeightDTO {
            @SerializedName("time")
            private Long time;
            @SerializedName("value")
            private Integer value;

            public Long getTime() {
                return time;
            }

            public void setTime(Long time) {
                this.time = time;
            }

            public Integer getValue() {
                return value;
            }

            public void setValue(Integer value) {
                this.value = value;
            }
        }
        public static class HinderDTO {
            @SerializedName("time")
            private Long time;
            @SerializedName("value")
            private Integer value;

            public Long getTime() {
                return time;
            }

            public void setTime(Long time) {
                this.time = time;
            }

            public Integer getValue() {
                return value;
            }

            public void setValue(Integer value) {
                this.value = value;
            }
        }
    }
}
