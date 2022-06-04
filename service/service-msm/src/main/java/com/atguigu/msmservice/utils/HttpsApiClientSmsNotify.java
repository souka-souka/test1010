//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.atguigu.msmservice.utils;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.enums.WebSocketApiType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

public class HttpsApiClientSmsNotify extends ApacheHttpClient{
    public final static String HOST = "miitangs10.market.alicloudapi.com";
    static HttpsApiClientSmsNotify instance = new HttpsApiClientSmsNotify();
    public static HttpsApiClientSmsNotify getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTPS);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }



    public void sendMail(String reqNo , String phoneNumber , String smsSignId , String smsTemplateNo , String paramMap , ApiCallback callback) {
        String path = "/v1/tools/sms/notify/sender";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("reqNo" , reqNo , ParamPosition.BODY , true);
        request.addParam("phoneNumber" , phoneNumber , ParamPosition.BODY , true);
        request.addParam("smsSignId" , smsSignId , ParamPosition.BODY , false);
        request.addParam("smsTemplateNo" , smsTemplateNo , ParamPosition.BODY , true);
        request.addParam("paramMap" , paramMap , ParamPosition.BODY , false);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse sendMailSyncMode(String reqNo , String phoneNumber , String smsSignId , String smsTemplateNo , String paramMap) {
        String path = "/v1/tools/sms/notify/sender";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("reqNo" , reqNo , ParamPosition.BODY , true);
        request.addParam("phoneNumber" , phoneNumber , ParamPosition.BODY , true);
        request.addParam("smsSignId" , smsSignId , ParamPosition.BODY , false);
        request.addParam("smsTemplateNo" , smsTemplateNo , ParamPosition.BODY , true);
        request.addParam("paramMap" , paramMap , ParamPosition.BODY , false);

        return sendSyncRequest(request);
    }

}