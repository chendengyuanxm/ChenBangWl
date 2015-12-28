package com.devin.framework.http.parse;

import android.sax.RootElement;

import com.devin.framework.http.InfoResult;
public class SimpleXmlParser extends XmlParser
{
    /** 需要回传的值*/
    private Object extraObj;
    public SimpleXmlParser(Object extraObj)
    {
        this.extraObj = extraObj;
    }
    
    public SimpleXmlParser()
    {
    }
    
    @Override
    public void parseResponse(InfoResult infoResult, RootElement element)
    {
        infoResult.setExtraObj(extraObj);
    }
}
