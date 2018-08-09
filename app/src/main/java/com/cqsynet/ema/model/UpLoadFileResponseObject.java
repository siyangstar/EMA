package com.cqsynet.ema.model;

/**
 * 上传图片接口返回对象
 */
public class UpLoadFileResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public String serverurl;
        public String url;
    }

}
