/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.request;

/**
 *
 * @author DELL
 */
public class MoMoPaymentRequest {
    private String partnerCode;
    private String accessKey; 
    private String requestType;
    private String ipnUrl;
    private String orderId;
    private long amount;
    private String orderInfo;
    private String requestId;
    private String redirectUrl;
    private String lang;
    private String extraData;
    private String signature;

    public MoMoPaymentRequest() {
    }

    public MoMoPaymentRequest(String partnerCode, String accessKey, String requestType, String ipnUrl, String orderId, long amount, String orderInfo, String requestId, String redirectUrl, String lang, String extraData, String signature) {
        this.partnerCode = partnerCode;
        this.accessKey = accessKey;
        this.requestType = requestType;
        this.ipnUrl = ipnUrl;
        this.orderId = orderId;
        this.amount = amount;
        this.orderInfo = orderInfo;
        this.requestId = requestId;
        this.redirectUrl = redirectUrl;
        this.lang = lang;
        this.extraData = extraData;
        this.signature = signature;
    }

    
    
    
    /**
     * @return the partnerCode
     */
    public String getPartnerCode() {
        return partnerCode;
    }

    /**
     * @param partnerCode the partnerCode to set
     */
    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    /**
     * @return the requestType
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    /**
     * @return the ipnUrl
     */
    public String getIpnUrl() {
        return ipnUrl;
    }

    /**
     * @param ipnUrl the ipnUrl to set
     */
    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the amount
     */
    public long getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }

    /**
     * @return the orderInfo
     */
    public String getOrderInfo() {
        return orderInfo;
    }

    /**
     * @param orderInfo the orderInfo to set
     */
    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    /**
     * @return the requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the redirectUrl
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * @param redirectUrl the redirectUrl to set
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @return the extraData
     */
    public String getExtraData() {
        return extraData;
    }

    /**
     * @param extraData the extraData to set
     */
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return the accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * @param accessKey the accessKey to set
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    
}
