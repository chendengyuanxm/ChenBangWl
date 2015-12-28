package com.newlandcomputer.jbig;

import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;



/**
 * @author Z
 *
 */
public class JbigUtils {
	 static {
		System.loadLibrary("JbigJNI");
	}
	 
	 public native static void DecodeJbig(String fileIn,String fileOut);
	 
	 public native static void DecodeJbig(byte[] data,String fileOut);
	 
	 public native static byte[] DecodeJbig(byte[] data);
	 
	 public static Bitmap decodeJbig(byte[] jbgData) throws Exception {
		if ((jbgData == null) || (jbgData.length < 20))
			throw new Exception("input data must be large than 20 bytes");
		
		byte[] pbmdata = DecodeJbig(jbgData);
		
		if ((pbmdata == null) || (pbmdata.length == 0))
			throw new FileNotFoundException("decode jbg file error");
		return pbmTojpg(pbmdata);
	}
	 
	private static Bitmap pbmTojpg(byte[] pbmBytes) {
		int length = pbmBytes.length;
		if (length < 25)
			return null;

		int index = 0;
		int[] indexs = new int[3];
		int count = 0;
		while (index < length) {
			if (pbmBytes[(index++)] == 10) {
				indexs[(count++)] = index;
				if (count >= 3) {
					break;
				}
			}
		}
		int fileTypeIndex = indexs[0];
		int widthIndex = indexs[1];
		int heightIndex = indexs[2];

		byte[] widthBytes = new byte[widthIndex - fileTypeIndex];
		System.arraycopy(pbmBytes, fileTypeIndex, widthBytes, 0, widthBytes.length);
		int width = Integer.parseInt(new String(widthBytes).trim());

		byte[] heightBytes = new byte[heightIndex - widthIndex];
		System.arraycopy(pbmBytes, widthIndex, heightBytes, 0, heightBytes.length);
		int height = Integer.parseInt(new String(heightBytes).trim());

		index = heightIndex;
		byte[] pixelBytes = new byte[length - index]; // 一个字节表示8个像素点
		System.arraycopy(pbmBytes, heightIndex, pixelBytes, 0, pixelBytes.length);

		index = 0;
		int bitcache = 0; 		// 当前像素点
		int bits_in_cache = 0; 	// 当前像素点索引
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (bits_in_cache < 1) {
					byte bits = pixelBytes[index++];

					bitcache = 0xFF & bits;
					bits_in_cache += 8;
				}

				bits_in_cache--;
				int bit = 0x1 & (bitcache >> bits_in_cache);

				pixels[(width * y + x)] = (bit == 0 ? Color.WHITE : Color.BLACK); // 0为白色，1为黑色
			}

			bitcache = 0;
			bits_in_cache = 0;
		}

		return Bitmap.createBitmap(pixels, width, height, Config.RGB_565);
	}
}
