package com.ihrm.demo;

import com.mysql.cj.PingTarget;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

public class CreateJwtTest {
    @Test
    public  void text01() {
        /*
        *   通过jjwt创建token
        * */
        JwtBuilder jwtBuilder = Jwts.builder().setId("123")
                .setSubject("赫敏")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "heimin");
        System.out.println(jwtBuilder.compact());
    }

    /*
    *   解析jjwtToken字符串
    * */
    @Test
    public void text02(){
        String token="\n"+"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjMiLCJzdWIiOiLotavmlY8iLCJpYXQiOjE2NTM0ODUwMTB9.WW0XGmc2LiKsV4EfqOcS2CP-XA-bvuiLDWdiUv4k414";
        Claims claims = Jwts.parser().setSigningKey("heimin").parseClaimsJws(token).getBody();
        //私有数据存放在claims中
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
    }

    /*
    * 通过jjwt创建token,通过claims存储更多的信息
    * */
    @Test
    public void test03(){
        JwtBuilder claim = Jwts.builder().setId("1234")
                .setSubject("赫敏11")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "superHemin")
                .claim("companyId", "7777")
                .claim("companyName", "中教控股教育集团");
        String token = claim.compact();
        System.out.println(token);
    }

    /*
    *   解析jwtToken字符串里面的自定义内容
    * */
    @Test
    public void text04(){
        String token="\n"+"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0Iiwic3ViIjoi6LWr5pWPMTEiLCJpYXQiOjE2NTM0ODU4NTMsImNvbXBhbnlJZCI6Ijc3NzciLCJjb21wYW55TmFtZSI6IuS4reaVmeaOp-iCoeaVmeiCsumbhuWboiJ9.1dqQDGnSCV7AJhNoABAXj1PClN8v6Q8ecDBtRG1-sbE";
        Claims claims = Jwts.parser().setSigningKey("superHemin").parseClaimsJws(token).getBody();
        //私有数据存放在claims中
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());

        //解析自定义claim中的内容
        String companyId = (String)claims.get("companyId");
        String companyName = (String) claims.get("companyName");
        System.out.println(companyId+":"+companyName);

    }
}
