package com.devin.bangsheng.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.newland.mtype.util.ISOUtils;

public class HttpUtils {
	
	public static byte[] httpPost(String httpUrl, byte[] sendData, int timeout, int readTimeout) {
		try {
			String pathUrl = httpUrl;
			System.out.println(ISOUtils.hexString(sendData));
			// 建立连接
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = sendData;
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/octet-stream");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.setConnectTimeout(timeout);
			httpConn.setReadTimeout(readTimeout);
			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();

			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
				// 处理响应流，必须与服务器响应流输出的编码一致
				InputStream input = httpConn.getInputStream();
				byte[] result = new byte[input.available()];
				input.read(result, 0, result.length);
				return result;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
