package org.msa.gatewayserver.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.gatewayserver.service.AccountService;
import org.springframework.boot.autoconfigure.gson.GsonProperties;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenCheckFilter implements WebFilter {

    private final ObjectMapper objectMapper;
    private final AccountService accountService;

    // filter 함수는 요청이 들어왔을 때 request와 response를 다음 filter 또는 컨트롤러나 마이크로 서비스와 같이 다음 Step으로 데이터를 전달하기 위한 함수이다.
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        boolean success = false;

        Object tokenTemp = request.getHeaders().get("token");
        Object accountIdTemp = request.getHeaders().get("accountId");
        String token = "";
        String accountId = "";

        if(tokenTemp != null) {
            token = tokenTemp.toString().replace("[", "").replace("]", "");
        }

        if(accountIdTemp != null) {
            accountId = accountIdTemp.toString().replace("[", "").replace("]", "");
        }

        log.info("Token: {}", token);

        success = accountService.existsByAccountIdAndToken(accountId, token);

        log.info("User Authentication Check Result: {}", success);

        if(!success) {
            try {
                return errorResponse(exchange);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> errorResponse(ServerWebExchange exchange) throws JsonProcessingException {
        ServerHttpResponse response = exchange.getResponse();
        GsonJsonParser gsonJsonParser = new GsonJsonParser();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("code", "401");
        paramMap.put("message", "Unauthorized Token");

        String json = objectMapper.writeValueAsString(paramMap);

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.writeWith(Mono.just(buffer));
    }


}