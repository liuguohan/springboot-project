package com.biyouche.utils;


import java.io.UnsupportedEncodingException;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class CommonUtils {
	private static final Log log = LogFactory.getLog(CommonUtils.class);

	private static final String LOGIN_PWD = "@byc2018";
	
	private static final String NAME_START = "用户";
	



	/**
	 * 登录密码加密
	 * @param redisKey
	 * @return
	 */
	public static String encodeLoginPwd(String redisKey) {
		return MD5.md5(redisKey+LOGIN_PWD);
	}

	/**
	 * 获取初始化昵称
	 * @return
	 */
	public static String getNickName() {
		StringBuffer num = new StringBuffer();
		for(int j = 0; j< 10; j++){
			num.append(((int)(Math.random()*10)*1));
		}
		return NAME_START+num;
	}

}
