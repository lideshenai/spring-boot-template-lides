package com.company.project.utils;

import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Service
public class JavaWebToken {
	
	  private static Logger log = LoggerFactory.getLogger(JavaWebToken.class);
	  
	  private static final String SECRET = "companiontek";
	
	//该方法使用HS256算法和Secret:bankgl生成signKey
    private static Key getKeyInstance() {
        //We will sign our JavaWebToken with our ApiKey secret
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    //使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
    public static String createJavaWebToken(String subject,String phone,Date expirationTime) {
    	//生成登录令牌
    	return Jwts.builder().setSubject(subject)
        .claim("phone", phone).setIssuedAt(new Date()).setExpiration(expirationTime)
        .signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();

    }

    //解析Token，同时也能验证Token，当验证失败返回null
    public static Claims parserJavaWebToken(String jwt) {
        try {
        	  final Claims claims = Jwts.parser().setSigningKey(SECRET)
                      .parseClaimsJws(jwt).getBody();
        	  return claims;
        } catch (Exception e) {
            log.error("json web token verify failed");
            return null;
        }
    }
    
    //解析Token返回staffId，当验证失败返回null
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
    }

}
