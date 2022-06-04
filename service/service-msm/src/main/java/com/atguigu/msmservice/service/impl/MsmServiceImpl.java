package com.atguigu.msmservice.service.impl;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.DemoSmsNotify;
import com.atguigu.msmservice.utils.HttpApiClientSmsNotify;
import com.atguigu.msmservice.utils.HttpsApiClientSmsNotify;
import org.apache.http.HttpResponse;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MsmServiceImpl implements MsmService {

    //发送短信的方法
    @Override
    public boolean send(Map<String, Object> param, String phone) {
            //HTTP Client init
            HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
            httpParam.setAppKey("204067309");
            httpParam.setAppSecret("7t2oQaIKxfKfJjHzXoC4t09F8LoKiILx");
            HttpApiClientSmsNotify.getInstance().init(httpParam);


            //HTTPS Client init
            HttpClientBuilderParams httpsParam = new HttpClientBuilderParams();
            httpsParam.setAppKey("204067309");
            httpsParam.setAppSecret("7t2oQaIKxfKfJjHzXoC4t09F8LoKiILx");


        /**
         * HTTPS request use DO_NOT_VERIFY mode only for demo
         * Suggest verify for security
         */
        httpsParam.setRegistry(getNoVerifyRegistry());

        HttpsApiClientSmsNotify.getInstance().init(httpsParam);

        if (StringUtils.isEmpty(phone)) return false;
        ApiResponse response = HttpsApiClientSmsNotify.getInstance().sendMailSyncMode(param.get("code").toString(), phone , "default" , "0001" , "default");
        try {
            System.out.println(getResultString(response));
            System.out.println(response.getCode());
            System.out.println(response.getMessage());
            System.out.println(response.getFirstHeaderValue("x-ca-request-id"));
            System.out.println(response.getFirstHeaderValue("X-Ca-Error-Code"));

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return true;
    }


    private static String getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if(response.getCode() != 200){
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody() , SdkConstant.CLOUDAPI_ENCODING));

        return result.toString();
    }

    private static Registry<ConnectionSocketFactory> getNoVerifyRegistry() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        try {
            registryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE).build();
            registryBuilder.register(
                    "https",
                    new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(
                            KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
                                @Override
                                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                                    return true;
                                }
                            }).build(),
                            new HostnameVerifier() {
                                @Override
                                public boolean verify(String paramString, SSLSession paramSSLSession) {
                                    return true;
                                }
                            }));

        } catch (Exception e) {
            throw new RuntimeException("HttpClientUtil init failure !", e);
        }
        return registryBuilder.build();
    }


}
