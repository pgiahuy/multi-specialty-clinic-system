/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hb.configs.MoMoConfigs;
import com.hb.dto.request.MoMoPaymentRequest;
import com.hb.dto.response.MoMoPaymentResponse;
import com.hb.service.MomoPaymentService;
import com.hb.utils.MomoSignatureUtil;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class MomoPaymentServiceImpl implements MomoPaymentService {

    @Autowired
    private MoMoConfigs momoConfig;

    @Override
    public MoMoPaymentResponse createPayment(String orderId, long amount, String orderInfo) throws Exception {
        String extraData = "";
      
        
        String uniqueOrderId = orderId;
        String requestId = momoConfig.getPartnerCode() + System.currentTimeMillis();
         String rawSignature = "accessKey=" + momoConfig.getAccessKey()
                + "&amount=" + amount
                + "&extraData=" + extraData
                + "&ipnUrl=" + momoConfig.getNotifyUrl()
                + "&orderId=" + uniqueOrderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + momoConfig.getPartnerCode()
                + "&redirectUrl=" + momoConfig.getReturnUrl()
                + "&requestId=" + requestId
                + "&requestType=" + momoConfig.getRequestType();

        String signature = MomoSignatureUtil.sign(rawSignature, momoConfig.getSecretKey());
        MoMoPaymentRequest request = new MoMoPaymentRequest();
        request.setPartnerCode(momoConfig.getPartnerCode());
        request.setAccessKey(momoConfig.getAccessKey());
        request.setRequestId(requestId);
        request.setAmount(amount);
        request.setOrderId(uniqueOrderId);
        request.setOrderInfo(orderInfo);
        request.setRedirectUrl(momoConfig.getReturnUrl());
        request.setIpnUrl(momoConfig.getNotifyUrl());
        request.setExtraData(extraData);
        request.setRequestType(momoConfig.getRequestType());
        request.setSignature(signature);
        request.setLang("vi");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(momoConfig.getEndpoint());
            post.setHeader("Content-Type", "application/json; charset=UTF-8");
            post.setEntity(new StringEntity(requestJson, StandardCharsets.UTF_8));

            try (CloseableHttpResponse httpResponse = client.execute(post)) {
                String responseJson = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
                return objectMapper.readValue(responseJson, MoMoPaymentResponse.class);
            }
        }
    }

    @Override
    public boolean verifySignature(Map<String, String> params) throws Exception {
        String rawSignature = "accessKey=" + momoConfig.getAccessKey()
                + "&amount=" + params.get("amount")
                + "&extraData=" + params.get("extraData")
                + "&message=" + params.get("message")
                + "&orderId=" + params.get("orderId")
                + "&orderInfo=" + params.get("orderInfo")
                + "&orderType=" + params.get("orderType")
                + "&partnerCode=" + params.get("partnerCode")
                + "&payType=" + params.get("payType")
                + "&requestId=" + params.get("requestId")
                + "&responseTime=" + params.get("responseTime")
                + "&resultCode=" + params.get("resultCode")
                + "&transId=" + params.get("transId");

        String expectedSignature = MomoSignatureUtil.sign(rawSignature, momoConfig.getSecretKey());
        return expectedSignature.equals(params.get("signature"));
    }
}
