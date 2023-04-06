package com.ruru.plastic.user.net;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.enume.ResponseEnum;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.enume.UserTypeEnum;
import com.ruru.plastic.user.exception.CommonException;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.redis.RedisService;
import com.ruru.plastic.user.service.AdminUserService;
import com.ruru.plastic.user.service.UserService;
import com.ruru.plastic.user.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AdminUserService adminUserService;

    // 在业务处理器处理请求之前被调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        AdminRequired adminRequired = method.getAnnotation(AdminRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (loginRequired != null) {
            // 判断是否存在令牌信息，如果存在，则允许登录
            String accessToken = request.getHeader(Constants.ACCESS_TOKEN);
            if (StringUtils.isEmpty(accessToken)) {
                throw new CommonException(500, "token错误！");
            } else {
                // jwt校验token
                DecodedJWT jwt;
                jwt = TokenUtil.deToken(accessToken);
                Long userId = jwt.getClaim("userId").asLong();
                // Redis校验token
                String redisToken = redisService.getUserInfo(userId, UserTypeEnum.User.getValue(), "token");
                if (StringUtils.isEmpty(redisToken) || !accessToken.equals(redisToken)) {
                    throw new CommonException(ResponseEnum.ERROR_TOKEN);
                }

                User user = userService.getUserById(userId);
                //用户不存在
                if (user == null) {
                    throw new CommonException(401, "用户不存在！");
                }
                //用户不可用
                if (StatusEnum.不可用.getValue().equals(user.getStatus())) {
                    throw new CommonException(401, "用户被禁止登陆");
                }
                int appType = jwt.getClaim("appType").asInt();
                if (!(appType+"").equals(request.getHeader("appType"))) {
                    throw new CommonException(401, "终端不匹配！");
                }

                // 当前登录用户@CurrentUser
                request.setAttribute("CURRENT_USER", user);
                redisService.extendUserInfo(userId,UserTypeEnum.User.getValue());

                return true;
            }
        } else if (adminRequired != null) {
            // 判断是否存在令牌信息，如果存在，则允许登录
            String accessToken = request.getHeader(Constants.ACCESS_TOKEN);
            if (StringUtils.isEmpty(accessToken)) {
                throw new CommonException(500, "token错误！");
            } else {
                // jwt校验token
                DecodedJWT jwt;
                jwt = TokenUtil.deAdminToken(accessToken);
                Long userId = jwt.getClaim("adminUserId").asLong();
                // Redis校验token
                String redisToken = redisService.getUserInfo(userId, UserTypeEnum.Admin.getValue(), "token");
                if (StringUtils.isEmpty(redisToken) || !accessToken.equals(redisToken)) {
                    throw new CommonException(ResponseEnum.ERROR_TOKEN);
                }

                AdminUser adminUser = adminUserService.getAdminUserById(userId);
                //用户不存在
                if (adminUser == null) {
                    throw new CommonException(401, "用户不存在！");
                }
                //用户不可用
                if (StatusEnum.不可用.getValue().equals(adminUser.getStatus())) {
                    throw new CommonException(401, "用户被禁止登陆");
                }
                int appType = jwt.getClaim("appType").asInt();
                if (!(appType+"").equals(request.getHeader("appType"))) {
                    throw new CommonException(401, "终端不匹配！");
                }

                // 当前登录用户@CurrentUser
                request.setAttribute("CURRENT_ADMIN_USER", adminUser);
                redisService.extendUserInfo(userId,UserTypeEnum.Admin.getValue());

                return true;
            }
        } else {
            return true;
        }
    }

    // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
    }

}
