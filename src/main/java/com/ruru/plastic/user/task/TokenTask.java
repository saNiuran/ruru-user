package com.ruru.plastic.user.task;

import com.ruru.plastic.user.enume.AppTypeEnum;
import com.ruru.plastic.user.enume.UserTypeEnum;
import com.ruru.plastic.user.redis.RedisService;
import com.ruru.plastic.user.utils.TokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.ruru.plastic.user.bean.Constants.REDIS_KEY_USER_TOKEN;

@Component
public class TokenTask {
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private RedisService redisService;
    @Autowired
    private PushTask pushTask;

    /**
     * 创建user Token
     *
     * @param userId     用户编号
     * @param appType    终端软件类型编号
     * @param deviceCode 终端设备编号
     */
    public String createToken(Long userId, Integer appType, String deviceCode, Integer userType) {
//        String oldToken = (String) template.opsForHash().get(Constants.REDIS_KEY_USER_TOKEN + mobile,"token");
//        if (null != oldToken) {
//            DecodedJWT jwt = TokenUtil.deToken(oldToken);
//            String oldDeviceInfo = jwt.getClaim("deviceCode").asString();
//            if (oldDeviceInfo != null && !oldDeviceInfo.equals(deviceCode)) {
////                if (!oldDeviceInfo.substring(0, 3).equals("wx_")) {   //前3位是这个的话，说明是微信登录，就不发推送了
//                    //alias-手机号
//                    PushBody pushBody = new PushBody();
//                    pushBody.setUserIds(Collections.singletonList(userId));
//                    pushBody.setPushType(PushTypeEnum.Alias.getValue());
//                    pushBody.setTarget(new String[]{mobile});
//                    pushBody.setNotifyTitle("【如如原料】通知");
//                    pushBody.setMsgTitle("异地登录");
//                    pushBody.setMsgContent("您的账号已于异地登录，如不是您亲自操作，请及时修改密码");
//                    Map<String, String> extras = new HashMap<>(5);
////                    extras.put("notifyCode", NotifyCode.异地登录.getCode());  //todo
//                    extras.put("relationId", "");
//                    extras.put("title", "异地登录");
//                    extras.put("content", "您的账号已于异地登录，如不是您亲自操作，请及时修改密码");
//                    pushBody.setExtras(extras);
//                    try {
//                        pushTask.sendMQ("USER_NOTIFY", "sendNotify", JSON.toJSONString(pushBody).getBytes(RemotingHelper.DEFAULT_CHARSET));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
////                }
//            }
//        }

        String token = "";
        if(userType.equals(UserTypeEnum.User.getValue())) {
            token = TokenUtil.createToken(userId, appType, deviceCode);
        }else if(userType.equals(UserTypeEnum.Admin.getValue())){
            token = TokenUtil.createAdminToken(userId, appType, deviceCode);
        }

        redisService.removeUserInfo(userId, userType, "token");
        redisService.removeUserInfo(userId, userType, "appType");
        redisService.removeUserInfo(userId, userType, "deviceCode");

        if(StringUtils.isNotEmpty(token)) {
            redisService.setUserInfo(userId, userType, "token", token);
            redisService.setUserInfo(userId, userType, "appType", appType.toString());
            redisService.setUserInfo(userId, userType, "token", token);
            redisService.extendUserInfo(userId, userType);
        }else{
            redisService.expireUserInfo(userId,userType);
        }

        return token;
    }

    public void invalidToken(Long userId, Integer userType) {
        redisService.removeUserInfo(userId, userType, "token");
    }
}
