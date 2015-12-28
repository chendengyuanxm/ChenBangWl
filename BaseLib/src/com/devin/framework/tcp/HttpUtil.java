package com.devin.framework.tcp;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.devin.framework.base.DvBaseApplication;

public class HttpUtil {

	/**
	 * 数据编码字符集
	 */
	public static final String EncodingCharset = HTTP.UTF_8;

	private static HttpClient customHttpClient;

	public static final int connectTimeout = 20000; // 连接超时
	public static final int socketTimeout = 60000; // socket超时
	public static final int connectePoolTimeout = 5000;// 连接池连接超时

	private HttpUtil() {
	}
	
	//保留一个HTTP的连接器，以便切换测试
	private static HttpClient defaultClient;
	public static synchronized HttpClient getDefaultClient() {
		if(defaultClient == null) {
			defaultClient = new DefaultHttpClient();
			HttpParams mParams = defaultClient.getParams();
			mParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,  HttpVersion.HTTP_1_1);
			mParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, EncodingCharset);
			mParams.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, EncodingCharset);
			mParams.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
			mParams.setLongParameter(ConnManagerPNames.TIMEOUT, connectePoolTimeout);
			mParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
			mParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeout);
		}else {
			// 关闭连接池中无效的连接
			defaultClient.getConnectionManager().closeExpiredConnections();
			// 关闭连接池中空闲的连接
			defaultClient.getConnectionManager().closeIdleConnections(60,
					TimeUnit.SECONDS);
		}
		return defaultClient;
	}
	
	public static synchronized HttpClient getHttpClient() {
		if (customHttpClient == null) {
//			// 设置协议相关参数
			customHttpClient = new DefaultHttpClient();
			HttpParams params = customHttpClient.getParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,  HttpVersion.HTTP_1_1);
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, EncodingCharset);
			params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, EncodingCharset);
			params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
			params.setLongParameter(ConnManagerPNames.TIMEOUT, connectePoolTimeout);
			params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeout);
			params.setParameter("Accept", "text/xml;charset=UTF-8");
			try {
				// 设置Scheme
				SchemeRegistry schReg = customHttpClient.getConnectionManager().getSchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				InputStream ins = DvBaseApplication.getInstance().getApplicationContext()
						.getAssets().open("server.crt"); // 下载的证书放到项目中的assets目录中
				CertificateFactory cerFactory = CertificateFactory
						.getInstance("X.509");
				Certificate cer = cerFactory.generateCertificate(ins);
				KeyStore trustStore = KeyStore.getInstance("PKCS12", "BC");
				trustStore.load(null, null);
				trustStore.setCertificateEntry("trust", cer);
				SSLSocketFactory sf = new SSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				schReg.register(new Scheme("https", sf, 443));
				
				customHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
						schReg), params);
			} catch (Exception e) {
				customHttpClient = new DefaultHttpClient(params);
			}

		} else {
			// 关闭连接池中无效的连接
			customHttpClient.getConnectionManager().closeExpiredConnections();
			// 关闭连接池中空闲的连接
			customHttpClient.getConnectionManager().closeIdleConnections(60,
					TimeUnit.SECONDS);
		}
		return customHttpClient;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
