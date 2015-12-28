package com.newland.pay.tools.pensigner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import com.devin.framework.util.DvFileUtils;

import android.graphics.Bitmap;


public class JBigConvert {
	
    static {
        System.loadLibrary("jbigconvert");
    }
	
	native static void convert(String input,String output);
	
	public static void convertToJBIG(Bitmap bmp,String outputFile) throws IOException{
		File temp = File.createTempFile("PBM", new Random().nextFloat() + "");
		convertToPBM(bmp,temp.getPath());
		File file = new File(outputFile);
		int end = outputFile.lastIndexOf(File.separator);
        String folderPaht = outputFile.substring(0, end);
        File folder = new File(folderPaht);
        if (!folder.exists()) {
        	folder.mkdirs();
        }
        file.createNewFile();
		convert(temp.getPath(),outputFile);
		temp.delete();
	}

	public static void convertToPBM(Bitmap bmp, String outputFile) throws IOException{
		FileOutputStream fos = null;
		
		try{
			fos = new FileOutputStream(outputFile);
			int width = bmp.getWidth();
			int height = bmp.getHeight();
			
			String header = "P1\n" + width + " " + height + "\n";
			fos.write(header.getBytes());
			for(int i = 0 ; i < height ; i++){
				byte[] line = new byte[width * 2];
				for(int j = 0 ;  j < width ; j++){
					int color = bmp.getPixel(j, i);
					color  = 0x00ffffff & color; //alpha值忽视掉
					if(color == 0xffffff){
						line[j * 2] = 0x30;
					}else{
						line[j * 2] = 0x31;
					}
					line[j*2 + 1] = ' ';
				}
				fos.write(line);
				fos.write('\n');
			}
			fos.flush();
		}finally{
			try{
				fos.close();
			}catch (Exception e) {
			}
		}
	}
}
