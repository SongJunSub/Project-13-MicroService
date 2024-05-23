package org.msa.authenticationserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang.time.DateUtils;
import org.msa.authenticationserver.dto.AccountDTO;

import java.util.Date;

public class JWTUtil {

    public static String generate(AccountDTO accountDTO) {
        Date date = new Date();

        return JWT.create()
                .withSubject(accountDTO.getAccountId())
                .withExpiresAt(DateUtils.addSeconds(date, 10))
                .withIssuedAt(date)
                .sign(Algorithm.HMAC512("secretKey"));
    }

}