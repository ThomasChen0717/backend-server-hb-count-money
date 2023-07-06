package admin.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

@Slf4j
public class JwtUtil {
    public static final String TOKEN_LOGIN_NAME = "loginName";
    public static final String TOKEN_LOGIN_ID = "userId";
    public static final String TOKEN_SUCCESS = "success:";
    public static final String TOKEN_FAIL = "fail:";
    /**
     * 过期时间15分钟
     */
    private static final long EXPIRE_TIME = 15*60*60*1000;

    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "joijsdfjlsjfljfljl5135313135";

    /**
     * 生成签名,15分钟后过期
     * @param username
     * @param userId
     * @return
     */
    public static String sign(String username,String userId){
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create().withHeader(header).withClaim(TOKEN_LOGIN_NAME,username)
                .withClaim(TOKEN_LOGIN_ID,userId).withExpiresAt(date).sign(algorithm);
    }


    public static String verify(String token){
        String result = TOKEN_SUCCESS;
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            result += jwt.getClaims().get(TOKEN_LOGIN_NAME).asString();
            return result; // success:username
        } catch (IllegalArgumentException e) {
            log.error("JwtUtil::Verify Failure(Illegal Argument):验证失败(非法参数)");
            return TOKEN_FAIL+e.getMessage();
        } catch (JWTVerificationException e) {
            log.error("JwtUtil::Verify Failure:验证失败");
            return TOKEN_FAIL+e.getMessage();
        }catch (Exception e){
            log.error("JwtUtil::Verify Failure:验证失败");
            return TOKEN_FAIL+e.getMessage();
        }

    }
}
