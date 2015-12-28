package com.devin.framework.http.parse;

import com.alibaba.fastjson.JSONObject;
import com.devin.framework.http.InfoResult;
public class SimpleJsonParser extends JsonParser
{
    /** 需要回传的值*/
    private Object extraObj;
    public SimpleJsonParser(Object extraObj)
    {
        this.extraObj = extraObj;
    }
    
    public SimpleJsonParser()
    {
    }
    
    @Override
    public void parseResponse(InfoResult infoResult, JSONObject jsonObject)
    {
        infoResult.setExtraObj(jsonObject);
    }
}
