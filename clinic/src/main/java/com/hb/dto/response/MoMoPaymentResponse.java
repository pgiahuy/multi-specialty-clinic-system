/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.dto.response;

/**
 *
 * @author DELL
 */
public class MoMoPaymentResponse {
    private String partnerCode;
    private String requestId;
    private String orderId;
    private long amount;
    private String responseTime;
    private String message;
    private int resultCode;
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;

    public MoMoPaymentResponse() {
    }

    public MoMoPaymentResponse(String partnerCode, String requestId, String orderId, long amount, String responseTime, String message, int resultCode, String payUrl, String deeplink, String qrCodeUrl) {
        this.partnerCode = partnerCode;
        this.requestId = requestId;
        this.orderId = orderId;
        this.amount = amount;
        this.responseTime = responseTime;
        this.message = message;
        this.resultCode = resultCode;
        this.payUrl = payUrl;
        this.deeplink = deeplink;
        this.qrCodeUrl = qrCodeUrl;
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
     * @return the responseTime
     */
    public String getResponseTime() {
        return responseTime;
    }

    /**
     * @param responseTime the responseTime to set
     */
    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return the payUrl
     */
    public String getPayUrl() {
        return payUrl;
    }

    /**
     * @param payUrl the payUrl to set
     */
    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    /**
     * @return the deeplink
     */
    public String getDeeplink() {
        return deeplink;
    }

    /**
     * @param deeplink the deeplink to set
     */
    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    /**
     * @return the qrCodeUrl
     */
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    /**
     * @param qrCodeUrl the qrCodeUrl to set
     */
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
