package com.devin.bangsheng.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.devin.bangsheng.common.Const;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.tcp.Request;
import com.devin.framework.tcp.Response;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;

public class SocketUtils {
	
	private static Logger log = LoggerFactory.getLogger(SocketUtils.class);
	private static final int TIMEOUT = 40000;
	private static final int CONECT_TIMEOUT = 20000;
	
	public static byte[] send(byte[] req) throws Exception {
		Socket socket = new Socket();
		socket.setSoTimeout(TIMEOUT);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			log.info("正在连接socket["+Const.ysUrl + ":" + Const.ysPort + "]...");
			socket.connect(new InetSocketAddress(Const.ysUrl, Const.ysPort), CONECT_TIMEOUT);
			log.info("连接socket成功...");
			bos = new BufferedOutputStream(socket.getOutputStream());
			bos.write(req, 0, req.length);
			bos.flush();
			
			bis = new BufferedInputStream(socket.getInputStream());
			byte[] respLenbs = new byte[2];
			bis.read(respLenbs);
			int respLen = ISOUtils.unPackIntFromBytes(respLenbs, 0, 2, true);
			log.debug("长度大小："+respLen);
			byte[] buffer = new byte[respLen-2];
			bis.read(buffer, 0, buffer.length);
			log.debug("返回的数据："+Dump.getHexDump(buffer));
			
			return buffer;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			log.error("SOCKET返回超时");
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally{
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	public static byte[] send1(byte[] req) throws Exception {
		Socket socket = new Socket();
		socket.setSoTimeout(TIMEOUT);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			log.info("正在连接socket["+Const.ysUrl + ":" + Const.ysPort + "]...");
			socket.connect(new InetSocketAddress(Const.ysUrl, Const.ysPort), CONECT_TIMEOUT);
			log.info("连接socket成功...");
			bos = new BufferedOutputStream(socket.getOutputStream());
			bos.write(req, 0, req.length);
			bos.flush();
			
			bis = new BufferedInputStream(socket.getInputStream());
			byte[] respLenbs = new byte[2];
			bis.read(respLenbs);
			int respLen = com.newland.mtype.util.ISOUtils.unPackIntFromBytes(respLenbs, 0, 2, true);
			log.debug("长度大小："+respLen);
			byte[] buffer = new byte[respLen];
			bis.read(buffer, 0, buffer.length);
			log.debug("返回的数据："+Dump.getHexDump(buffer));
			
			return buffer;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			log.error("SOCKET返回超时");
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally{
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
