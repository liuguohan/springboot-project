package com.biyouche.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biyouche.controller.BaseController;

/**
 * @author hucong
 *
 */
@RestController
@RequestMapping("/sms")
public class SmsController extends BaseController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SmsController.class);

	 /**
	 * 获取验证码
	 * @author hucong
	 * @param type
	 * @return code
	 */
	@PostMapping("/send")
	public String getCode(@RequestBody String content) {
		
		LOGGER.info("获取验证码请求参数:" + content);
		return "10000";
	 }
}
