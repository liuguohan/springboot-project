package com.biyouche.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.biyouche.constants.UserConstant;
import com.biyouche.dao.user.UserMapper;
import com.biyouche.domain.user.User;
import com.biyouche.domain.user.UserBlack;
import com.biyouche.utils.CommonUtils;
import com.biyouche.utils.DesUtils;
import com.biyouche.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.biyouche.dao.BlogTypeMapper;
import com.biyouche.domain.BlogType;
import com.biyouche.rabbitmq.producer.RabbitMQProducer;
import com.biyouche.redis.annotations.Cacheable;
import com.biyouche.redis.enums.ExpireTime;
import com.biyouche.service.UserService;
import com.biyouche.utils.CommonUtils;


@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private BlogTypeMapper blogTypeMapper;
    @Autowired
    private UserMapper userMapper;

    @Value("${userqueue}")
    String queue;

    @Bean
    Queue queue() {
        return new Queue(queue,false);
    }

    @Autowired
    RabbitMQProducer producer;

    public void sendMsg(String msg) {
        producer.sendTo(queue, msg + " at " + new Date());
    }

    @Cacheable(key = "queryBlogType", expire = ExpireTime.FIVE_MIN)
    public List<BlogType> queryBlogType() {
        sendMsg("rabbitmq test > .....");
        return blogTypeMapper.getAll();
    }

    @RabbitListener(queues = "${userqueue}")
    public void handler(String message) {
        LOGGER.info("Consumer> " + message);
    }


    /**
     * 登录操作
     *
     * @param content
     * @autor pht
     */
    @Cacheable(key = "login", expire = ExpireTime.FIVE_MIN)
    public void login(String content) {
        sendMsg("rabbitmq 登录操作");
        //解析客户端传递的json
        Map<String, Object> contentMap = JSON.parseObject(content, Map.class);
        if (ValidatorUtils.isEmpty(contentMap)) {
            LOGGER.info("请求参数解析失败");
            return;
        }

        String contentString = contentMap.get("content") + "";
        if (ValidatorUtils.isNull(contentString)) {
            LOGGER.info("请求参数为空");
            return;
        }
        Map<String, Object> loginMap = JSON.parseObject(contentString, Map.class);
        if (ValidatorUtils.isEmpty(loginMap)) {
            LOGGER.info("请求参数为空");
            return;
        }
        //获取登录手机号
        String userMobile = loginMap.get("user_mobile") + "";
        //校验手机号
        ValidatorUtils.isLoginMobile(userMobile);
        //获取密码(客户端加密过的密码)
        String userPassword = loginMap.get("user_password") + "";
        //解密
        try {
            userPassword = DesUtils.decrypt(userPassword, "ABCDEFGH");
        } catch (Exception e) {
            LOGGER.info("密码解解密失败");
            e.printStackTrace();
        }
        //密码校验
        if (!ValidatorUtils.isPassword(userPassword)) {
            LOGGER.info("密码格式无效");
            return;
        }
        //通过用户名查询数据库
        User user = userMapper.selectByloginMobile(userMobile);

        //登录校验
        checkLogin(user, userPassword);

        LOGGER.info("用户校验成功,登录成功:" + content);

        //生成令牌信息
//        String accessId = CommonUtils.getRandom(userMobile);
//        String dataKey = CommonUtils.getDesKey();
//        String accessKey = CommonUtils.encodeBase64String(CommonUtils.getRandom(dataKey));

    }

    /**
     * 检查登录
     * @param user
     * @param userPassword
     */
    private void checkLogin(User user, String userPassword) {
        if (ValidatorUtils.isNull(user) || user.getUserId() == null) {
            LOGGER.info("用户不存在");
            return;
        }
        //校验密码
        if (!CommonUtils.encodeLoginPwd(userPassword).equals(user.getUserPassword())) {
            //密码不正确
            LOGGER.info("用户名或密码不正确");
            return;
        }
        //登录状态判断
        if (user.getLoginLock() == UserConstant.LOGIN_LOCK_YES) {
            LOGGER.error("用户已被锁定");
            return;
        }
        if (user.getLoginLock() == UserConstant.LOGIN_LOCK_DELETED) {
            LOGGER.error("用户已被注销");
            return;
        }
        //查询用户是否存在于黑名单
        int count = userMapper.checkUserBlackByuserId(user.getUserId());
        if (count > 0) {
            LOGGER.error("用户已被加入黑名单");
            return;
        }

    }

    /**
     * 注册
     *
     * @param content
     * @autor hucong
     */
    @SuppressWarnings("rawtypes")
    public void register(String content) {
        Map map = JSON.parseObject(content, Map.class);
        User user = new User();
        LOGGER.info("注册请求参数" + map);
        String code = map.get("code") + "";
        String recommecd_mobile = map.get("recommecd_mobile") + "";
        String user_mobile = map.get("user_mobile") + "";
        String pwd = map.get("pwd") + "";
        String device_type = map.get("device_type") + "";
        //更新验证码
        int recommendId = 0;
        //检验推荐人
        user.setLoginMobile(user_mobile);
        user.setUserPassword(CommonUtils.encodeLoginPwd(pwd));//需要加密
        user.setRecommendMobile(recommecd_mobile);//需要校验
        user.setRecommendId(recommendId + "");
        user.setNickName(CommonUtils.getNickName());//生成用户初始化昵称
        user.setDeviceType(Byte.valueOf(device_type));
        user.setAvatarUrl("");
        user.setUserQrcode("");
        LOGGER.info("用户参数" + user);
        int i = userMapper.register(user);
        LOGGER.info("插入数据库返回状态" + i);

    }
}
