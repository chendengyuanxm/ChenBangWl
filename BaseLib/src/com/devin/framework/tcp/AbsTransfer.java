package com.devin.framework.tcp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.os.Message;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.devin.framework.base.DvBaseApplication;

import de.greenrobot.event.EventBus;

public abstract class AbsTransfer implements ITransfer{
	
	// 存储所有的订阅者
    private List<Object> subscribers = new ArrayList<Object>();
    // Volley请求队列
    private static RequestQueue requestQueue = Volley.newRequestQueue(DvBaseApplication.getInstance());
    // Default EventBus
    private EventBus mEventBus;
    
    // 请求的tags
    private Set<Object> requestTags = new HashSet<Object>();
    
    /**
     * Constructor with a subscriber
     * @param subscriber
     */
    public AbsTransfer(Object subscriber)
    {
        this(subscriber, new EventBus());
    }
    
    /**
     * Constructor with custom EventBus
     * @param eventBus
     */
    public AbsTransfer(Object subscriber, EventBus eventBus)
    {
        if (eventBus == null)
        {
            mEventBus = EventBus.getDefault();
        }
        else
        {
            mEventBus = eventBus;
        }
        register(subscriber);
    }
    
    @Override
    public void register(Object subscriber)
    {
        if (!subscribers.contains(subscriber))
        {
            mEventBus.register(subscriber);
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(Object subscriber)
    {
        if (subscribers.contains(subscriber))
        {
            mEventBus.unregister(subscriber);
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void unregisterAll()
    {
        for (Object subscriber : subscribers)
        {
            mEventBus.unregister(subscriber);
        }
        subscribers.clear();
    }
    
    /**
     * 负责封装结果内容, post给订阅者
     * @param action 任务标识
     * @param response 响应结果 
     *                 instanceof VolleyError表示网络请求出错
     *                 instanceof InfoResult表示网络请求成功
     */
    @Override
    public void onResult(int action, Object response)
    {
        Message msg = new Message();
        msg.what = action;
        msg.obj = response;
        mEventBus.post(msg);
    }
    
    /**
     * EventBus订阅者事件通知的函数, UI线程
     * 
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
        mEventBus.post(msg);
    }
}
