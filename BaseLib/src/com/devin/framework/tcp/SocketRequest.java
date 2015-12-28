package com.devin.framework.tcp;

public interface SocketRequest extends Request<byte[]>{
	
	public String getUrl();
	
	public int getPort();
}
