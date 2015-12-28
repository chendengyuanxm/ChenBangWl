package com.devin.framework.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.content.Context;

import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.task.DvAsyncTask;
import com.devin.framework.util.DvISOUtils;

public class SimpleSocketTransfer extends AbsTransfer{
	
	private Logger logger = LoggerFactory.getLogger(SimpleSocketTransfer.class);
	
	public SimpleSocketTransfer(Object subscriber) {
		super(subscriber);
	}

	@Override
	public void cancel(Object tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelAll() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public <T> void sendRequest(final Request<T> request) {
		DvAsyncTask<String, Integer, Object> task = new DvAsyncTask<String, Integer, Object>() {

			@Override
			protected Object doInBackground(String... params) {
				Socket socket = new Socket();
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try {
					SocketRequest socketRequest = (SocketRequest) request;
					String ip = socketRequest.getUrl();
					int port = socketRequest.getPort();
					byte[] req = (byte[]) socketRequest.createRequest();
					logger.info("正在连接socket["+ip + ":" + port + "]...");
					socket.connect(new InetSocketAddress(ip, port), 60000);
					logger.info("连接socket成功");
					bos = new BufferedOutputStream(socket.getOutputStream());
					bos.write(req);
					bos.flush();
					
					bis = new BufferedInputStream(socket.getInputStream());
					byte[] respLenbs = new byte[2];
					bis.read(respLenbs);
					int respLen = DvISOUtils.unPackIntFromBytes(respLenbs, 0, 2, true);
					logger.debug("长度大小："+respLen);
					byte[] buffer = new byte[respLen+2];
					bis.read(buffer, 2, respLen);
					System.arraycopy(respLenbs, 0, buffer, 0, 2);
					String resp = DvISOUtils.hexString(buffer);
					logger.debug("返回的数据："+resp);
					Response response = request.createResponse(resp);
					onResult(request.getId(), response);
				}catch (SocketTimeoutException e) {
					e.printStackTrace();
					logger.error("SOCKET返回超时");
					HttpException exception = new HttpException("SOCKET返回超时");
					onResult(request.getId(), exception);
				}catch(IOException e) {
					e.printStackTrace();
					HttpException exception = new HttpException(e.getLocalizedMessage());
					onResult(request.getId(), exception);
				}catch(Exception e) {
					e.printStackTrace();
					HttpException exception = new HttpException(e.getLocalizedMessage());
					onResult(request.getId(), exception);
				}
				return null;
			}
			
		};
		task.execute("");
	}

	@Override
	public <T> void sendRequest(Request<T> request, Object tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void sendRequest(Context context, Request<T> request) {
		// TODO Auto-generated method stub
		
	}

}
