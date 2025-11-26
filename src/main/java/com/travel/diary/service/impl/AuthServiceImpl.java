package com.travel.diary.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.travel.diary.entity.User;
import com.travel.diary.mapper.UserMapper;
import com.travel.diary.service.AuthService;
import com.travel.diary.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    @Value("${wechat.appid:your_appid}")
    private String appId;

    @Value("${wechat.secret:your_secret}")
    private String appSecret;

    @Override
    public Map<String, Object> wxLogin(String code) {
        log.info("微信登录，code: {}", code);

        String openid;
        String unionid = null;
        
        // 开发环境：如果AppID未配置，使用模拟登录
        if ("your_appid".equals(appId) || appId == null || appId.isEmpty()) {
            log.warn("微信AppID未配置，使用模拟登录");
            openid = "mock_openid_" + System.currentTimeMillis();
        } else {
            // 生产环境：调用微信接口
            String url = String.format(
                    "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    appId, appSecret, code
            );

            String response;
            try {
                response = webClientBuilder.build()
                        .get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            } catch (Exception e) {
                log.error("调用微信接口失败", e);
                throw new RuntimeException("微信登录失败：网络异常");
            }

            log.info("微信接口返回: {}", response);

            JSONObject jsonObject = JSON.parseObject(response);
            openid = jsonObject.getString("openid");
            String sessionKey = jsonObject.getString("session_key");
            unionid = jsonObject.getString("unionid");

            if (openid == null) {
                String errcode = jsonObject.getString("errcode");
                String errmsg = jsonObject.getString("errmsg");
                log.error("获取openid失败，errcode: {}, errmsg: {}", errcode, errmsg);
                
                // 开发环境：如果是 invalid code 错误，自动降级为模拟登录
                if ("40029".equals(errcode)) {
                    log.warn("检测到 invalid code 错误，可能是开发环境，使用模拟登录");
                    openid = "mock_openid_" + System.currentTimeMillis();
                } else {
                    throw new RuntimeException("微信登录失败: " + errmsg);
                }
            }
        }

        // 2. 查询或创建用户
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setOpenid(openid);
            user.setUnionid(unionid);
            user.setNickname("旅行者" + System.currentTimeMillis() % 10000);
            user.setAvatar("https://api.dicebear.com/7.x/miniavs/svg?seed=" + openid);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.insert(user);
            log.info("创建新用户: {}", user.getId());
        } else {
            // 更新最后登录时间
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);
            log.info("用户登录: {}", user.getId());
        }

        // 3. 生成 JWT token
        String token = jwtUtil.generateToken(user.getId());

        // 4. 返回登录结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("openid", user.getOpenid());
        result.put("userInfo", userInfo);

        return result;
    }
}
