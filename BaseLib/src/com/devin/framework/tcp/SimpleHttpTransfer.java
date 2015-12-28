package com.devin.framework.tcp;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;

import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.task.DvAsyncTask;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;

public class SimpleHttpTransfer extends AbsTransfer{
	
	private Logger logger = LoggerFactory.getLogger(SimpleSocketTransfer.class);

	public SimpleHttpTransfer(Object subscriber) {
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
	
	public <T> void sendRequest(final Context context, final Request<T> request) {
		DvAsyncTask<String, Integer, Object> task = new DvAsyncTask<String, Integer, Object>() {
			
			ProgressDialog pd;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = UIHelper.createProgressbar(context, "请稍候...");
				pd.show();
			}

			@Override
			protected Object doInBackground(String... params) {
				HttpRequest httpRequest = null;
				try{
					httpRequest = (HttpRequest) request;
				}catch(Exception e) {
					logger.error("request must instance of HttpRequest!!");
					return e;
				}
				logger.debug("======================================================");
				HttpRequestBase request0 = httpRequest.createRequest();
				HttpClient httpClient = HttpUtil.getHttpClient();
				try {
					HttpResponse httpResp = httpClient.execute(request0);
					int statusCode = httpResp.getStatusLine().getStatusCode();
					String respMsg = httpResp.getStatusLine().getReasonPhrase();
					logger.debug("reponseCode=" + statusCode + " [" + respMsg + "]");
					if(statusCode == HttpStatus.SC_OK) {
						String respString = EntityUtils.toString(httpResp.getEntity());
						logger.debug("http response=" + respString);
						if(DvStrUtils.isEmpty(respString)) {
							throw new Exception("服务器返回数据为空");
						}
						Response resp = request.createResponse(respString);
						onResult(request.getId(), resp);
					}else {
						HttpException exception = new HttpException(respMsg);
						onResult(request.getId(), exception);
					}
					logger.debug("======================================================");
				} catch (Exception e) {
					HttpException exception = new HttpException(e.getLocalizedMessage());
					onResult(request.getId(), exception);
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				pd.dismiss();
			}
		};
		task.execute("");
	}

	@Override
	public <T> void sendRequest(final Request<T> request) {
		DvAsyncTask<String, Integer, Object> task = new DvAsyncTask<String, Integer, Object>() {
			
			ProgressDialog pd;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected Object doInBackground(String... params) {
				HttpRequest httpRequest = null;
				try{
					httpRequest = (HttpRequest) request;
				}catch(Exception e) {
					logger.error("request must instance of HttpRequest!!");
					return e;
				}
				logger.debug("======================================================");
				HttpRequestBase request0 = httpRequest.createRequest();
				HttpClient httpClient = HttpUtil.getHttpClient();
				try {
					HttpResponse httpResp = httpClient.execute(request0);
					int statusCode = httpResp.getStatusLine().getStatusCode();
					String respMsg = httpResp.getStatusLine().getReasonPhrase();
					logger.debug("reponseCode=" + statusCode + " [" + respMsg + "]");
					if(statusCode == HttpStatus.SC_OK) {
						String respString = EntityUtils.toString(httpResp.getEntity());
						logger.debug("http response=" + respString);
						if(DvStrUtils.isEmpty(respString)) {
							throw new Exception("服务器异常，返回数据为空");
						}
						Response resp = request.createResponse(respString);
						onResult(request.getId(), resp);
					}else {
						HttpException exception = new HttpException(respMsg);
						onResult(request.getId(), exception);
					}
					logger.debug("======================================================");
				} catch (Exception e) {
					e.printStackTrace();
					HttpException exception = new HttpException(e.getLocalizedMessage());
					onResult(request.getId(), exception);
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
			}
		};
		task.execute("");
	}

	@Override
	public <T> void sendRequest(Request<T> request, Object tag) {
	}

}
