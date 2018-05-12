package com.company.project.auth;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.project.auth.LoginUser;
import com.google.common.cache.LoadingCache;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * jwt 三部分：
 * Header 头部		包含token类型和采用的加密算法。
 * Payload 负载		claim：是一些实体（通常指的用户）的状态和额外的元数据
 * Signature 签名		创建签名需要使用编码后的header和payload以及一个秘钥
 * 				HMACSHA256(
 * 				base64UrlEncode(header) + "." +
 * 				base64UrlEncode(payload),
 * 				secret)  
 * secret	秘钥
 * @author LiDes
 *	2018年5月12日
 */
@Service
public class JavaWebToken {
	
	  private static Logger log = LoggerFactory.getLogger(JavaWebToken.class);
	  
	  private static final String SECRET = "companiontek";
	  //过期时间
	 @Value("${token.expire.seconds}")
	  private static Integer expirationTime;
	  
	//该方法使用HS256算法和Secret:bankgl生成signKey
    private static Key getKeyInstance() {
        //We will sign our JavaWebToken with our ApiKey secret
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

   
    public static String createJavaWebToken(String username) {
    	Long time = expirationTime*60*1000L;
    	Date date = new Date(time+new Date().getTime());
    	//生成登录令牌
    	return Jwts.builder()    	
        .claim("username", username)
        //设置创建时间
        .setIssuedAt(new Date())
        //设置过期时间
        .setExpiration(date)
        //使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
        .signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();

    }

    //解析Token，同时也能验证Token，当验证失败返回null.claims自定义需要加的内容
    public static Claims parserJavaWebToken(String jwt) {    	
        try {
        	  final Claims claims = Jwts.parser().setSigningKey(SECRET)
                      .parseClaimsJws(jwt).getBody();
        	  return claims;
        } catch (Exception e) {
            log.error("json web token verify failed");
            e.printStackTrace();
            return null;
        }
    }
    
/*    //解析Token返回staffId，当验证失败返回null
    public static Integer parserStaffIdByToken(HttpServletRequest request) {
        try {
        	  String token=request.getHeader("token");
        	  final Claims claims = Jwts.parser().setSigningKey(SECRET)
                      .parseClaimsJws(token).getBody();
    		  return Integer.parseInt(claims.getSubject());

        } catch (Exception e) {
        	  log.error("json web token verify failed");
        }
        return null;
    }*/

}
