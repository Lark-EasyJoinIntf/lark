package com.easyjoin.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * 唯一性ID生成器
 * @author Nico.Li
 * @version 1.0
 * @Date 2008-3-15
 */
public class IdCreatorUtil {

	/**
	 * 生成整数类型ID
	 * @return int
	 */
	public static int ConfirmIntId() {
		Random random = new Random();
		int i = random.nextInt();
		return i < 0 ? -i : i;
	}

	/**
	 * 生成长整数类型ID
	 * @return long
	 */
	public static long ConfirmLongId() {
		Random random = new Random();
		return random.nextLong();
	}
	
	/**
	 * 在给定的字符串内随机产生指定长度的字符串
	 * @param base 给定的字符范围
	 * @param sLen 产生字符串长度
	 * @return
	 * @see Jun 7, 2013
	 * @author think
	 */
	public static String ConfirmId(String base, int sLen){
		StringBuffer temp = new StringBuffer(128);
		int rad = base.length()+1;
		int indx = base.length()-1;
		int i, p;
		for (i = 0; i < sLen; i++) {
			p = (int) (Math.random() * rad);
			if (p > indx)
				p = indx;
			temp.append(base.substring(p, p + 1));
		}
		return temp.toString();
	}
	
	/**
	 * 生成指定长度的数字序列
	 * @param sLen 指定数字序列长度
	 * @return String
	 */
	public static String ConfirmNumberId(int sLen){
		String base = "1234567890";
		return IdCreatorUtil.ConfirmId(base, sLen);
	}
	
	/**
	 * 生成字母组合的字符串ID
	 * @param sLen 指定生成ID的长度
	 * @return String
	 */
	public static String ConfirmStringId(int sLen) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		return IdCreatorUtil.ConfirmId(base, sLen);
	}

	/**
	 * 生成字符串及数字组合的字符串ID
	 * @param sLen 指定生成ID的长度
	 * @return String
	 */
	public static String ConfirmNumStrId(int sLen) {
		String base = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
		return IdCreatorUtil.ConfirmId(base, sLen);
	}
	
	/**
	 * 生成以字母开头字母、数字及特殊字符组合的字符串，
	 * 按长度的2字母:1数字:1特殊字符的方式分配
	 * @param sLen 指定生成ID的长度
	 * @return String
	 */
	public static String ConfirmPwd(int sLen) {
		int baseLen = sLen/4;
		int numLen = sLen%4>0?(sLen/4+1):sLen/4;
		int charaLen = sLen/2-1;
		String base = "_={}~!@#$%^*?";
		String str1 = IdCreatorUtil.ConfirmStringId(1);
		String str = IdCreatorUtil.ConfirmStringId(charaLen)+
			IdCreatorUtil.ConfirmNumberId(numLen)+IdCreatorUtil.ConfirmId(base, baseLen);
		String[] ids = str.split("");
		Arrays.sort(ids);
		StringBuffer bf = new StringBuffer(128);
		for(String b : ids){
			bf.append(b);
		}
		return str1+bf.toString();
	}
	
	/**
	 * 通过字符串及当前日期时间生成唯一性ID
	 * @param sLen 指定生成ID日期之后部分的长度
	 * @return String
	 */
	public static String ConfirmStringDatetime(int sLen) {
		String temp = ConfirmStringId(sLen);
		return  String.valueOf(System.currentTimeMillis())+temp;
	}
	
	/**
	 * 通过数字序列及当前日期时间生成唯一性ID 格式：yyyyMMddHHmmssXXXX
	 * @param sLen 指定生成ID日期之后部分数字序列的长度
	 * @return String
	 */
	public static String ConfirmDateSerializeId(String idstr, int sLen){
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuffer buffer = new StringBuffer(128);
		if(idstr!=null && idstr.length()>0){
			buffer.append(idstr);
			buffer.append("-");
		}
		buffer.append(sdf.format(new Date()));
		if(sLen > 0){
			buffer.append("-");
			buffer.append(IdCreatorUtil.ConfirmNumberId(sLen));
		}
		return buffer.toString();
	}

	public static int getNextVal(String seq){
		
		return 0;
	}
	public static void main(String[] args) {
		
		System.out.println(IdCreatorUtil.ConfirmPwd(8));
//		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
//		String str = sdf.format(new Date());

		System.out.println(IdCreatorUtil.ConfirmNumStrId(4));
		
		
	}
	
}
