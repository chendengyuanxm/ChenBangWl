package com.devin.framework.tcp;

public interface Request<T> {
	
	public int getId();
	
	public T createRequest();
	
	public Response createResponse(String resp);
	
}
