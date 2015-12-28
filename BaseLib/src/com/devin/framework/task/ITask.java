package com.devin.framework.task;


/**
 * 任务接口定义
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-5-21]
 */
public interface ITask extends Runnable
{
    /**
     * 执行耗时任务
     * 
     * @return
     */
    Object doInBackground();
}
