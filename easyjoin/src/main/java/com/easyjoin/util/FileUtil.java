/**
 * 创建日期 @May 29, 2010
 */
package com.easyjoin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 李晓成
 * @version: 1.0
 * @since May 29, 2010
 */
public class FileUtil {
	
	public static void creatDir(String path){
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
	}
	public boolean saveFile(File file, StringBuffer buf){
		try {
			if(file.exists()){
				file.delete();
			}
			OutputStream out = new FileOutputStream(file.getAbsolutePath());
			out.write(buf.toString().getBytes());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean saveFile(File file, String content){
		try {
			if(file.exists()){
				file.delete();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean saveFile(String fileName, String data){
		try {
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 从文本文件中读取数据
	 * @param file
	 * @return
	 * @see Jul 9, 2013
	 * @author think
	 */
	public static List<String> readTxtFile(File file){
		List<String> list= new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				list.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return list;
	}
	 
	public static String readFile(String fileName){
		StringBuffer sb = new StringBuffer(512);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			reader.close();
			return sb.toString(); //new String(sb.toString().getBytes(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		/*try {
			InputStream in = new FileInputStream(fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        byte[] buf = new byte[1024];
	        int len = -1;
			while ((len = in.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			buf = baos.toByteArray();
			baos.close();
			in.close();
	        return new String(buf, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;*/
	}
	
	public static void main(String[] args) {
		//FileUtil.saveFile("D:/test.json", "uoufoasufoasufo");
		String str = FileUtil.readFile("E:/JavaDev/smilewps/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/easyjoin/files/provider.json");
		System.out.println(str);
	}
}
