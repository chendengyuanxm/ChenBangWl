package com.devin.framework.http;

public class InfoResult
{
    private boolean success;
    private String errorCode;
    private String desc;
    private Object extraObj;

    private InfoResult(Builder builder)
    {
        this.success = builder.success;
        this.errorCode = builder.errorCode;
        this.desc = builder.desc;
        this.extraObj = builder.extraObj;
    }
    
    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public Object getExtraObj()
    {
        return extraObj;
    }

    public void setExtraObj(Object extraObj)
    {
        this.extraObj = extraObj;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    @Override
	public String toString() {
		return "InfoResult [success=" + success + ", errorCode=" + errorCode
				+ ", desc=" + desc + ", extraObj=" + extraObj + "]";
	}
    
	public static class Builder
    {
        private boolean success;
        private String errorCode;
        private String desc;
        private Object extraObj;
        
        public Builder success(boolean success)
        {
            this.success = success;
            return this;
        }
        
        public Builder errorCode(String errorCode)
        {
            this.errorCode = errorCode;
            return this;
        }
        
        public Builder desc(String desc)
        {
            this.desc = desc;
            return this;
        }
        
        public Builder extraObj(Object extraObj)
        {
            this.extraObj = extraObj;
            return this;
        }
        
        public InfoResult build()
        {
            return new InfoResult(this);
        }
    }
}
