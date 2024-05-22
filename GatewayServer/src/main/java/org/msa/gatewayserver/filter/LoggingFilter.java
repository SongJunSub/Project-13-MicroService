package org.msa.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class LoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 함수 안에 request, response 객체를 exchange 객체를 통해 가져온다.
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory dataBufferFactory = response.bufferFactory();

        // request 객체에 존재하는 요청 데이터를 스트리밍하기 위해 ServerHttpRequest라는 클래스를 통해 기능 재정의가 진행된 객체이다.
        ServerHttpRequest decoratedRequest = getDecoratedRequest(request);

        // response 객체에 존재하는 응답 데이터를 읽어오기 위해 ServerHttpResponseDecorator라는 클래스를 통해 기능 재정의가 진행된 객체이다.
        ServerHttpResponseDecorator decoratedResponse = getDecoratedResponse(response, request, dataBufferFactory);

        // 이 chain 객체를 통해 filter 함수를 진행하게 되면 다음 filter 또는 마이크로 서비스로 데이터를 전달하게 되는데,
        // 여기 안에 파라미터로 들어가는 decoratedRequest, decoratedResponse 객체는 실제로 데이터가 전달 처리되는 시점에 재정의 된 기능이 수행되게 된다.
        // 실제로 요청 데이터를 다음 filter나 마이크로 서비스에 전달할 때 request로 들어온 JSON 데이터를 출력하는 부분을 decoratedRequest가 재정의 된 기능에 개발해야 된다.
        // response JSON 데이터 출력 기능은 decoratedResponse가 재정의 된 기능에 개발해야 된다.
        return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build());
    }

    private ServerHttpResponseDecorator getDecoratedResponse(ServerHttpResponse response, ServerHttpRequest request, DataBufferFactory dataBufferFactory) {
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if(body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory().join(dataBuffers);
                        byte[] content = new byte[joinedBuffers.readableByteCount()];

                        joinedBuffers.read(content);

                        String responseBody = new String(content, StandardCharsets.UTF_8);

                        log.info("Request ID: {}, Method: {}, URI: {}, \nResponse Body: {}", request.getId(), request.getMethod(), request.getURI(), responseBody);

                        return dataBufferFactory.wrap(responseBody.getBytes());
                    }).switchIfEmpty(Flux.defer(() -> {
                        System.out.println("If Empty");

                        return Flux.just();
                    }))).onErrorResume(err -> {
                        log.error("Error while decorating Response: {}", err.getMessage());

                        return Mono.empty();
                    });
                }
                else {
                    System.out.println("Not Flux");
                }

                return super.writeWith(body);
            }
        };
    }

    private ServerHttpRequest getDecoratedRequest(ServerHttpRequest request) {
        // ServerHttpRequestDecorator 클래스를 통해 getBody가 재정의된다.
        // request 데이터를 스트리밍 하기 위해 재정의하는 부분이다.
        return new ServerHttpRequestDecorator(request) {
            @Override
            public Flux<DataBuffer> getBody() {
                log.info("Request ID: {}, Method: {}, URI: {}", request.getId(), request.getMethod(), request.getURI());

                // super.getBody()는 실제 요청이 들어온 파라미터 정보이다. 이 부분을 스트리밍 하기 위해 dataBuffer 객체로 추출한다.
                return super.getBody().publishOn(Schedulers.boundedElastic()).doOnNext(dataBuffer -> {
                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        Channels.newChannel(byteArrayOutputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());

                        String body = byteArrayOutputStream.toString(StandardCharsets.UTF_8);

                        log.info("Request ID: {}, Request Body: {}", request.getId(), body);
                    }
                    catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
            }
        };
    }

}