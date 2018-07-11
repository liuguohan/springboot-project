package com.biyouche.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ValidatorUtils {
	
//	private static final Log log = LogFactory.getLog(ValidatorUtils.class);

	public static boolean isEmpty(String str) {

		return ((str == null) || (str.trim().length() == 0));
	}
	
	public static boolean isNotEmpty(String str) {
		if(str==null){
			return false;
		}

		return ((str != null) && (str.trim().length() > 0)&&!"null".equals(str.trim()));
	}
	
	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Map result){
		if(result!=null&&!result.isEmpty()&&result.size()>0){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(@SuppressWarnings("rawtypes") Map result){		
		return !isNotEmpty(result);
	}
	
	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") List result){
		if(result!=null&&!result.isEmpty()&&result.size()>0){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(@SuppressWarnings("rawtypes") List result){		
		return !isNotEmpty(result);
	}
	
	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Set result){
		if(result!=null&&!result.isEmpty()&&result.size()>0){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(@SuppressWarnings("rawtypes") Set result){		
		return !isNotEmpty(result);
	}

	public static boolean isNotNull(String str) {
		if (str == null) {
			return false;
		}
		return ((str != null) && (str.trim().length() > 0) && !"null"
				.equals(str.trim()));
	}
	
	public static boolean isNotNull(Object obj){
		if(obj==null){
			return false;
		}
		return isNotNull(obj+"");
	}
	
	public static boolean isNull(Object obj){
		return !isNotNull(obj);
	}

	public static boolean isNull(String str) {
		return !isNotNull(str);
	}
	
	public static boolean isNotNull(Integer str) {
		if (str == null) {
			return false;
		}
		return true;
	}

	public static boolean isNull(Integer str) {
		return !isNotNull(str);
	}
	
	public static boolean isNotNull(Long str) {
		if (str == null) {
			return false;
		}
		return true;
	}

	public static boolean isNull(Long str) {
		return !isNotNull(str);
	}
	
	public static boolean isNotNull(Date str) {
		if (str == null) {
			return false;
		}
		return true;
	}

	public static boolean isNull(Date str) {
		return !isNotNull(str);
	}
	

	public static boolean isBlank(String str) {

		int length = 0;

		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkString(String str, String regex) {

		return str.matches(regex);
	}
	
	public static boolean isMd5(String md5) {

		if (md5.length() != 32) {
			return false;
		}

		return checkString(md5, "[0-9A-Fa-f]+");
	}
	
}
