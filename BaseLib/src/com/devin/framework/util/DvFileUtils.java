package com.devin.framework.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class DvFileUtils {
	
	/** 默认下载文件地址. */
	private static  String downPathRootDir = File.separator + "download" + File.separator;
	
    /** 默认下载图片文件地址. */
	private static  String downPathImageDir = downPathRootDir + "cache_images" + File.separator;
    
    /** 默认下载文件地址. */
	private static  String downPathFileDir = downPathRootDir + "cache_files" + File.separator;
    
	/**MB  单位B*/
	private static int MB = 1024*1024;
	
	/**缓存文件夹的大小100M  单位B*/
	private static int cacheSize = 100*MB;
	
    /**剩余空间大于200M才使用缓存*/
	private static int freeSdSpaceNeededToCache = 200*MB;
	
	/**缓存空间当前的大小，临时*/
	private static int dirSize = -1;
	
	/**统计程序下载的图片数，超过10张检查sd卡*/
	private static int downCount = 0;
	
	public static void createFolder(String sPath) {
		File file = new File(sPath);
		if (file.exists()) {
			return;
		} else {
			file.mkdirs();
		}
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 */
	public static void deleteFolder(String sPath) {
		File file = new File(sPath);
		if (!file.exists()) {
			return;
		} else {
			if (file.isFile()) { 
				deleteFile(sPath);
			} else { 
				deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 */
	public static void deleteFile(String sPath) {
		File file = new File(sPath);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 */
	public static void deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				// 删除子文件
				deleteFile(files[i].getAbsolutePath());
			} else {
				// 删除子目录
				deleteDirectory(files[i].getAbsolutePath());
			}
		}
		// 删除当前目录
		dirFile.delete();
	}
	 
	/**
	 * 描述：从sd卡中的文件读取到byte[].
	 *
	 * @param path sd卡中文件路径
	 * @return byte[]
	 */
	public static byte[] getByteArrayFromSD(String path) {  
		byte[] bytes = null; 
		ByteArrayOutputStream out = null;
	    try {
	    	File file = new File(path);  
	    	//SD卡是否存在
			if(!isCanUseSD()){
				 return null;
		    }
			//文件是否存在
			if(!file.exists()){
				 return null;
			}
	    	
	    	long fileSize = file.length();  
	    	if (fileSize > Integer.MAX_VALUE) {  
	    	      return null;  
	    	}  

			FileInputStream in = new FileInputStream(path);  
		    out = new ByteArrayOutputStream(1024);  
			byte[] buffer = new byte[1024];  
			int size=0;  
			while((size=in.read(buffer))!=-1) {  
			   out.write(buffer,0,size);  
			}  
			in.close();  
            bytes = out.toByteArray();  
   
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	    return bytes;
    }  
	
	/**
	 * 描述：将byte数组写入文件.
	 *
	 * @param path the path
	 * @param content the content
	 * @param create the create
	 */
	 public static void writeByteArrayToSD(String path, byte[] content,boolean create){  
	    
		 FileOutputStream fos = null;
		 try {
	    	File file = new File(path);  
	    	//SD卡是否存在
			if(!isCanUseSD()){
				 return;
		    }
			//文件是否存在
			if(!file.exists()){
				if(create){
					File parent = file.getParentFile();
					if(!parent.exists()){
						parent.mkdirs();
						file.createNewFile();
					}
				}else{
				    return;
				}
			}
			fos = new FileOutputStream(path);  
			fos.write(content);  
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
   }  
	 
	/**
	 * 描述：SD卡是否能用.
	 *
	 * @return true 可用,false不可用
	 */
	public static boolean isCanUseSD() { 
	    try { 
	        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
    } 

	/**
    * 计算存储目录下的文件大小，
    * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
    * 那么删除40%最近没有被使用的文件
    */
    public static boolean removeCache() {
    	
       try {
		   if(!isCanUseSD()){
				return false;
		   }
		   
		   File path = Environment.getExternalStorageDirectory();
		   File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
	       File[] files = fileDirectory.listFiles();
	       if (files == null) {
	            return true;
	       }
	       if(dirSize==-1){
	    	   dirSize+=1;
	    	   for (int i = 0; i < files.length; i++) {
		            dirSize += files[i].length();
		       }
	       }
	       
	       //当前大小大于预定缓存空间
	       if (dirSize > cacheSize) {
	           int removeFactor = (int) ((0.4 * files.length) + 1);
	           Arrays.sort(files, new FileLastModifSort());
	           for (int i = 0; i < removeFactor; i++) {
	        	   dirSize -= files[i].length();
	               files[i].delete();
	           }
	        }
	       
	   } catch (Exception e) {
		   e.printStackTrace();
		   return false;
	   }
                                                       
       return true;
    }
	
    
    /**
     * 计算sdcard上的剩余空间
     */
    public static int freeSpaceOnSD() {
       StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
       double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
       return (int) sdFreeMB;
    }
	
    /**
     * 根据文件的最后修改时间进行排序
     */
    public static class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 
     * 描述：缓存文件夹的大小
     * @return
     * @throws 
     */
	public static int getCacheSize() {
		return cacheSize;
	}

	/**
	 * 
	 * 描述：设置缓存文件夹的大小
	 * @param cacheSize   B
	 * @throws 
	 */
	public static void setCacheSize(int cacheSize) {
		DvFileUtils.cacheSize = cacheSize;
	}

	/**
	 * 
	 * 描述：剩余空间大于多少B才使用缓存
	 * @return
	 * @throws 
	 */
	public static int getFreeSdSpaceNeededToCache() {
		return freeSdSpaceNeededToCache;
	}

	/**
	 * 
	 * 描述：剩余空间大于多少B才使用缓存
	 * @param freeSdSpaceNeededToCache
	 * @throws 
	 */
	public static void setFreeSdSpaceNeededToCache(int freeSdSpaceNeededToCache) {
		DvFileUtils.freeSdSpaceNeededToCache = freeSdSpaceNeededToCache;
	}
	
	/**
     * 删除所有缓存文件
    */
    public static boolean removeAllFileCache() {
    	
       try {
		   if(!isCanUseSD()){
				return false;
		   }
		   
		   File path = Environment.getExternalStorageDirectory();
		   File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
	       File[] files = fileDirectory.listFiles();
	       if (files == null) {
	            return true;
	       }
           for (int i = 0; i < files.length; i++) {
               files[i].delete();
           }
	   } catch (Exception e) {
		   e.printStackTrace();
		   return false;
	   }
       return true;
    }
    
    /**
     * 
     * 描述：读取Assets目录的文件内容
     * @param context
     * @param name
     * @return
     * @throws 
     */
    public static String readAssetsByName(Context context,String name,String encoding){
    	String text = null;
    	InputStreamReader inputReader = null;
    	BufferedReader bufReader = null;
    	try {  
    		 inputReader = new InputStreamReader(context.getAssets().open(name));
    		 bufReader = new BufferedReader(inputReader);
    		 String line = null;
    		 StringBuffer buffer = new StringBuffer();
    		 while((line = bufReader.readLine()) != null){
    			 buffer.append(line);
    		 }
    		 text = new String(buffer.toString().getBytes(), encoding);
         } catch (Exception e) {  
        	 e.printStackTrace();
         } finally{
			try {
				if(bufReader!=null){
					bufReader.close();
				}
				if(inputReader!=null){
					inputReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	return text;
    }
    
    /**
     * 
     * 描述：读取Raw目录的文件内容
     * @param context
     * @param id
     * @return
     * @throws 
     */
    public static String readRawByName(Context context,int id,String encoding){
    	String text = null;
    	InputStreamReader inputReader = null;
    	BufferedReader bufReader = null;
        try {
			inputReader = new InputStreamReader(context.getResources().openRawResource(id));
			bufReader = new BufferedReader(inputReader);
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while((line = bufReader.readLine()) != null){
				 buffer.append(line);
			}
            text = new String(buffer.toString().getBytes(),encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(bufReader!=null){
					bufReader.close();
				}
				if(inputReader!=null){
					inputReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return text;
    }
    
}
