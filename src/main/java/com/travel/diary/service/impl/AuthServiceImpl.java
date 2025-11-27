package com.travel.diary.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.travel.diary.dto.PhoneAuthDTO;
import com.travel.diary.dto.WxLoginDTO;
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
    public Map<String, Object> wxLogin(WxLoginDTO loginDTO) {
        log.info("微信登录，code: {}, nickName: {}", loginDTO.getCode(), loginDTO.getNickName());
        
        String code = loginDTO.getCode();

        // 调用微信接口获取 openid
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
        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        String unionid = jsonObject.getString("unionid");

        if (openid == null) {
            String errcode = jsonObject.getString("errcode");
            String errmsg = jsonObject.getString("errmsg");
            log.error("获取openid失败，errcode: {}, errmsg: {}", errcode, errmsg);
            throw new RuntimeException("微信登录失败: " + errmsg);
        }

        // 2. 查询或创建用户
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setOpenid(openid);
            user.setUnionid(unionid);
            
            // 使用前端传来的用户信息，如果没有则使用默认值
            if (loginDTO.getNickName() != null && !loginDTO.getNickName().isEmpty()) {
                user.setNickname(loginDTO.getNickName());
            } else {
                user.setNickname("旅行者" + System.currentTimeMillis() % 10000);
            }
            
            if (loginDTO.getAvatarUrl() != null && !loginDTO.getAvatarUrl().isEmpty()) {
                user.setAvatar(loginDTO.getAvatarUrl());
            } else {
                user.setAvatar("https://api.dicebear.com/7.x/miniavs/svg?seed=" + openid);
            }
            
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.insert(user);
            log.info("创建新用户: {}, 昵称: {}", user.getId(), user.getNickname());
        } else {
            // 更新用户信息（如果前端传了新信息）
            boolean needUpdate = false;
            
            if (loginDTO.getNickName() != null && !loginDTO.getNickName().isEmpty() 
                && !loginDTO.getNickName().equals(user.getNickname())) {
                user.setNickname(loginDTO.getNickName());
                needUpdate = true;
            }
            
            if (loginDTO.getAvatarUrl() != null && !loginDTO.getAvatarUrl().isEmpty()
                && !loginDTO.getAvatarUrl().equals(user.getAvatar())) {
                user.setAvatar(loginDTO.getAvatarUrl());
                needUpdate = true;
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            if (needUpdate) {
                userMapper.updateById(user);
                log.info("用户登录并更新信息: {}, 昵称: {}", user.getId(), user.getNickname());
            } else {
                userMapper.updateById(user);
                log.info("用户登录: {}", user.getId());
            }
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

    @Override
    public String getPhoneNumber(PhoneAuthDTO phoneAuthDTO) {
        log.info("获取微信手机号，code: {}", phoneAuthDTO.getCode());
        
        // 获取 access_token
        String tokenUrl = String.format(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                appId, appSecret
        );
        
        String tokenResponse;
        try {
            tokenResponse = webClientBuilder.build()
                    .get()
                    .uri(tokenUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("获取access_token失败", e);
            throw new RuntimeException("获取手机号失败：网络异常");
        }
        
        log.info("access_token返回: {}", tokenResponse);
        JSONObject tokenJson = JSON.parseObject(tokenResponse);
        String accessToken = tokenJson.getString("access_token");
        
        if (accessToken == null) {
            String errmsg = tokenJson.getString("errmsg");
            log.error("获取access_token失败: {}", errmsg);
            throw new RuntimeException("获取手机号失败: " + errmsg);
        }
        
        // 使用 code 获取手机号
        String phoneUrl = String.format(
                "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s",
                accessToken
        );
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", phoneAuthDTO.getCode());
        
        String phoneResponse;
        try {
            phoneResponse = webClientBuilder.build()
                    .post()
                    .uri(phoneUrl)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("获取手机号失败", e);
            throw new RuntimeException("获取手机号失败：网络异常");
        }
        
        log.info("手机号返回: {}", phoneResponse);
        JSONObject phoneJson = JSON.parseObject(phoneResponse);
        
        if (phoneJson.getInteger("errcode") != 0) {
            String errmsg = phoneJson.getString("errmsg");
            log.error("获取手机号失败: {}", errmsg);
            throw new RuntimeException("获取手机号失败: " + errmsg);
        }
        
        JSONObject phoneInfo = phoneJson.getJSONObject("phone_info");
        String phoneNumber = phoneInfo.getString("phoneNumber");
        
        log.info("成功获取手机号: {}", phoneNumber);
        return phoneNumber;
    }
}
