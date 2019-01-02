package com.entity.entities;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class LimitUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 该方法需要调用权限服务提供的token验证接口
     * @param key 值为登录信息的 手机号码:随机值
     * @return
     */
    public boolean isLogin(String key, String token){
        String val = stringRedisTemplate.opsForValue().get(key);
        if(token.equals(val))
            return true;
        else
            return false;
    }

    private static String ACCOUNT_KEY = AppPropUtil.get("lark.user.attr.keyname");
    private static String EXPIRATION_TIME = AppPropUtil.get("lark.user.expiration.time");
    /**
     *
     * @param account
     * @param user
     * @return
     */
    public String setLoginInfo(String account, JSONObject user){
        Set<String> keys = stringRedisTemplate.keys(account + ":*");
        stringRedisTemplate.delete(keys);
        String sessionid = account+":"+ UUID.randomUUID().toString();
        user.put(ACCOUNT_KEY, sessionid);
        long ttlMillis = StringUtils.isEmpty(EXPIRATION_TIME) ? 0 : Long.parseLong(EXPIRATION_TIME);
        String jwt = JWTTokenUtil.getInstance().createJWTToken(user.toJSONString(), ttlMillis);
        stringRedisTemplate.opsForValue().set(sessionid, jwt);
        return jwt;
    }
}
