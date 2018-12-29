package com.lark.zuul;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT token生成帮助类
 * @author Smile.Li
 * @since 2016年12月20日
 */
public class JWTTokenUtil {
	private static JWTTokenUtil instance;

	private JWTTokenUtil(){}
	
	public static JWTTokenUtil getInstance(){
		if(instance == null){
			instance = new JWTTokenUtil();
		}
		return instance;
	}
	
	/**
	 * 产生token
	 * @param obj 登录用户对象
	 * @param ttlMillis 有效时长：如10分钟，则为10*60*1000
	 * @return
	 */
	public String createJWTToken(Object obj, long ttlMillis){
		//We will sign our JWT with our ApiKey secret
		String secret = ApplicationConfig.get("jwt.secret")==null?"123456" : ApplicationConfig.get("jwt.secret");
		return create(obj, secret, ttlMillis);
	}

	private String create(Object obj, String secret, long ttlMillis) {
		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		 
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		 
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		 
		  //Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder()
		    .setIssuedAt(now)
		    .setIssuer("Smile")
		    .setNotBefore(now)
		    .setSubject(obj.toString())
		    .signWith(signatureAlgorithm, signingKey);
		 
		//if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    builder.setExpiration(exp);
		}
		 
		//Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}
	
	/**
	 * 解码
	 * @param token
	 * @param secret 密钥
	 * @return
	 */
	public Claims parseJWTToken(String token, String secret){
		try{
			Claims claims = Jwts.parser()        
				   .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
				   .parseClaimsJws(token).getBody();
			return claims;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 从token中获取用户信息
	 * 根据失效时间判断token是否失效，失效则返回null
	 * @param token
	 * @return
	 */
	public JSONObject getUserInfo(String token){

		String secret = ApplicationConfig.get("jwt.secret")==null?"123456" : ApplicationConfig.get("jwt.secret");
		Claims claims = parseJWTToken(token, secret);
		if(claims == null){
			return null;
		}
		//验证claims
		Date now = new Date();
		Date expDate = claims.getExpiration();
		Date notBfoDate = claims.getNotBefore();
		if(now.before(notBfoDate) || now.after(expDate)){
			return null;
		}
		return JSONObject.parseObject(claims.get(Claims.SUBJECT).toString());
	}


	/*public static void main(String[] args) {
		LoginUser user = new LoginUser();
		user.setTelphone("12345678901");
		user.setUserName("Smle.Le");
		String jwt = JWTTokenUtil.getInstance().createJWTToken(Utils.entityToJSONObject(user), 10*60*1000);
		//Claims claims = JWTTokenUtil.getInstance().parseJWTToken(jwt);
		System.out.println(JWTTokenUtil.getInstance().getUserInfo(jwt));
		
		//System.out.println(JWTTokenUtil.getInstance().createJWTToken("12345678901", 6*60*60*1000));
		//System.out.println(JWTTokenUtil.getInstance().createJWTToken("12345678901", 6*60*60*1000));
	}*/
}
