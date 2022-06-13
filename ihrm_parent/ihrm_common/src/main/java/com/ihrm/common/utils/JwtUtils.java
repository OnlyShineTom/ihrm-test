package com.ihrm.common.utils;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    //签名私钥
    private String key;
    //签名的失效时间
    private Long ttl;

    /*
        设置认证token
            id:登录用户id
            subject:登录用户名
    */
    public String createJwt(String id, String name, Map<String,Object> map){
        //1.设置失效时间
        long now=System.currentTimeMillis();//当前毫秒
        long exp=now+ttl;
        //2.创建jwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder().setId(id).setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
        //3.根据map设置claims
        jwtBuilder.setClaims(map);

        jwtBuilder.setExpiration(new Date(exp));
        //4.创建token
        String token=jwtBuilder.compact();
        return token;
    }


    //解析token字符串获取claims
    public Claims parseJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }

}
