package com.devin.framework.http;

import android.content.Context;

import com.android.volley.Request;

public interface ITransfer
{
    /**
     * 注册一个订阅者
     * 
     * @param receiver
     */
    void register(Object receiver);

    /**
     * 解绑一个订阅者
     * 
     * @param receiver
     */
    void unregister(Object receiver);

    /**
     * 解绑所有订阅者
     */
    void unregisterAll();
    
    /**
     * 取消某一个网络请求
     * @param tag 某次请求的唯一标识
     */
    void cancel(Object tag);
    
    /**
     * 取消所有网络请求
     */
    void cancelAll();
    
    /**
     * 发送网络请求
     * @param <T>
     * @param request
     */
    public <T> void sendRequest(Request<T> request);
    
    /**
     * 发送网络请求, 并给这个请求设置TAG
     * @param <T>
     * @param request
     * @param tag
     */
    public <T> void sendRequest(Request<T> request, Object tag);
    
    /**
     * 封装结果内容, post给订阅者
     * @param action 
     * @param response
     */
    void onResult(int action, Object response);
}
