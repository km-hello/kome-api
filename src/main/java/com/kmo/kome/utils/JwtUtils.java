package com.kmo.kome.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 * <p>
 * 负责 Token 的生成、解析和校验。
 * 适配 JJWT 0.12.x+ 版本 API。
 */
@Component
public class JwtUtils {

    // 从 application.yml 读取密钥
    @Value("${jwt.secret}")
    private String secret;

    // 过期时间 (毫秒)
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 获取加密密钥
     * <p>
     * 使用 UTF-8 编码将字符串转换为字节，生成 HMAC-SHA 密钥。
     * 注意: secret 字符串长度必须大于等于 32 个字符 (针对 HS256 算法需求)
     *
     * @return SecretKey 对象
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT Token。
     *
     * @param userId 用户的唯一标识符，用于设置 Token 的主题 (Subject)
     * @return 生成的 JWT Token 字符串
     */
    public String generateToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId)) // 主题 (Subject) 设置为 userId 的字符串形式
                .issuedAt(new Date()) // 设置签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
                .signWith(getSigningKey(), Jwts.SIG.HS256) // 使用 Jwts.SIG.HS256 指定算法签名
                .compact();
    }

    /**
     * 从提供的 JWT Token 中解析并获取用户的唯一标识符 (userId)。
     *
     * @param token JWT Token，用于解析和提取用户 ID
     * @return 解析后的用户唯一标识符 (userId)，以 Long 类型返回
     */
    public Long getUserIdFromToken(String token) {
        String subject = parseToken(token).getPayload().getSubject();
        return Long.parseLong(subject);
    }

    /**
     * 校验 Token 是否有效
     *
     * @param token JWT Token
     * @return true=有效, false=无效(过期或篡改)
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            // 如果解析失败（过期、签名错误、格式错误），说明 Token 无效
            return false;
        }
    }

    /**
     * 解析 Token 的内部通用方法
     *
     * @param token JWT Token
     * @return 解析后的 Jws 对象
     */
    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // 设置验签密钥
                .build()                     // 构建解析器
                .parseSignedClaims(token);   // 解析并验证签名
    }
}
