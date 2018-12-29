package com.lark.gateway.filter;

import io.netty.buffer.ByteBufAllocator;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.*;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebExchangeDataBinder;
import org.springframework.web.reactive.result.method.annotation.RequestBodyArgumentResolver;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Configuration
public class GlobalRequestFilter implements GlobalFilter, Ordered {
    private Logger logger = LoggerFactory.getLogger(GlobalRequestFilter.class);
    private final static String REQUEST_RECORDER_LOG_BUFFER = "RequestRecorderGlobalFilter.request_recorder_log_buffer";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest originalRequest = exchange.getRequest();
        Flux<DataBuffer> body = originalRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();//缓存读取的request body信息
        body.subscribe(dataBuffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
            DataBufferUtils.release(dataBuffer);
            bodyRef.set(charBuffer.toString());
        });//读取request body到缓存
        String bodyStr = bodyRef.get();//获取request body



        URI originalRequestUrl = originalRequest.getURI();

        //只记录http的请求
        String scheme = originalRequestUrl.getScheme();
        if ((!"http".equals(scheme) && !"https".equals(scheme))) {
            return chain.filter(exchange);
        }

        //RecorderServerHttpResponseDecorator response = new RecorderServerHttpResponseDecorator(exchange.getResponse());
        ServerWebExchange ex = exchange.mutate()
                .request(new RecorderServerHttpRequestDecorator(exchange.getRequest()))
                //.response(response)
                .build();
/*
        response.subscribe(
                Mono.defer(() -> recorderRouteRequest(ex)).then(
                        Mono.defer(() -> recorderResponse(ex))
                )
        );*/
        return recorderOriginalRequest(ex)
                .then(chain.filter(ex))
                .then();
    }

    /*private Mono<Void> writeLog(ServerWebExchange exchange) {
        StringBuffer logBuffer = exchange.getAttribute(REQUEST_RECORDER_LOG_BUFFER);
        logBuffer.append("\n------------ end at ")
                .append(System.currentTimeMillis())
                .append("------------\n\n");

        logger.info(logBuffer.toString());
        return Mono.empty();
    }*/

    private Mono<Void> recorderOriginalRequest(ServerWebExchange exchange) {
        StringBuffer logBuffer = new StringBuffer("\n------------开始时间 ")
                .append(System.currentTimeMillis())
                .append("------------");
        exchange.getAttributes().put(REQUEST_RECORDER_LOG_BUFFER, logBuffer);

        ServerHttpRequest request = exchange.getRequest();
        return recorderRequest(request, request.getURI(), logBuffer.append("\n原始请求：\n"));
    }

   /* private Mono<Void> recorderRouteRequest(ServerWebExchange exchange) {
        URI requestUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        StringBuffer logBuffer = exchange.getAttribute(REQUEST_RECORDER_LOG_BUFFER);

        return recorderRequest(exchange.getRequest(), requestUrl, logBuffer.append("代理请求：\n"));
    }*/

    private Mono<Void> recorderRequest(ServerHttpRequest request, URI uri, StringBuffer logBuffer) {
        if (uri == null) {
            uri = request.getURI();
        }

        HttpMethod method = request.getMethod();
        HttpHeaders headers = request.getHeaders();

        /*logBuffer.append(method.toString()).append(' ').append(uri.toString()).append('\n');
        logBuffer.append("------------请求头------------\n");
        headers.forEach((name, values) -> {
            values.forEach(value -> {
                logBuffer.append(name).append(":").append(value).append('\n');
            });
        });*/

        Charset bodyCharset = null;
        if (hasBody(method)) {
            long length = headers.getContentLength();
            if (length > 0) {
               /* logBuffer.append("------------body 长度:").append(length).append(" contentType:");*/
                MediaType contentType = headers.getContentType();
                /*if (contentType == null) {
                    logBuffer.append("null，不记录body------------\n");
                } else if (!shouldRecordBody(contentType)) {
                    logBuffer.append(contentType.toString()).append("，不记录body------------\n");
                } else */
                if(contentType != null && shouldRecordBody(contentType)){
                    bodyCharset = getMediaTypeCharset(contentType);
                    //logBuffer.append(contentType.toString()).append("------------\n");
                }
            }
        }


        if (bodyCharset != null) {
            Flux<DataBuffer> body = request.getBody();

            body.subscribe(buffer -> {
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                DataBufferUtils.release(buffer);
                try {
                    String bodyString = new String(bytes, "utf-8");
                    System.out.println(bodyString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            return doRecordBody(logBuffer, body, bodyCharset)
                    .then(Mono.defer(() -> {
                        //logBuffer.append("\n------------ end ------------\n\n");
                        return Mono.empty();
                    }));
        } else {
            //logBuffer.append("------------ end ------------\n\n");
            return Mono.empty();
        }
    }

    /*private Mono<Void> recorderResponse(ServerWebExchange exchange) {
        RecorderServerHttpResponseDecorator response = (RecorderServerHttpResponseDecorator)exchange.getResponse();
        StringBuffer logBuffer = exchange.getAttribute(REQUEST_RECORDER_LOG_BUFFER);

        HttpStatus code = response.getStatusCode();
        logBuffer.append("响应：").append(code.value()).append(" ").append(code.getReasonPhrase()).append('\n');

        HttpHeaders headers = response.getHeaders();
        logBuffer.append("------------响应头------------\n");
        headers.forEach((name, values) -> {
            values.forEach(value -> {
                logBuffer.append(name).append(":").append(value).append('\n');
            });
        });

        Charset bodyCharset = null;
        MediaType contentType = headers.getContentType();
        if (contentType == null) {
            logBuffer.append("------------ contentType = null，不记录body------------\n");
        } else if (!shouldRecordBody(contentType)) {
            logBuffer.append("------------不记录body------------\n");
        } else {
            bodyCharset = getMediaTypeCharset(contentType);
            logBuffer.append("------------body------------\n");
        }

        if (bodyCharset != null) {
            return doRecordBody(logBuffer, null*//*response.copy()*//*, bodyCharset)
                    .then(Mono.defer(() -> writeLog(exchange)));
        } else {
            return writeLog(exchange);
        }
    }*/

    @Override
    public int getOrder() {
        //在GatewayFilter之前执行
        return - 1;
    }

    private boolean hasBody(HttpMethod method) {
        //只记录这3种谓词的body
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH)
            return true;
        return false;
    }

    //记录简单的常见的文本类型的request的body和response的body
    private boolean shouldRecordBody(MediaType contentType) {
        String type = contentType.getType();
        String subType = contentType.getSubtype();

        if ("application".equals(type)) {
            return "json".equals(subType) || "x-www-form-urlencoded".equals(subType) || "xml".equals(subType) || "atom+xml".equals(subType) || "rss+xml".equals(subType);
        } else if ("text".equals(type)) {
            return true;
        }

        //暂时不记录form
        return false;
    }

    private Mono<Void> doRecordBody(StringBuffer logBuffer, Flux<DataBuffer> body, Charset charset) {

        return DataBufferUtils.join(body).doOnNext(buffer -> {
            CharBuffer charBuffer = charset.decode(buffer.asByteBuffer());
            logBuffer.append(charBuffer.toString());
            DataBufferUtils.release(buffer);
        }).then();
    }

    private Charset getMediaTypeCharset(@Nullable MediaType mediaType) {
        if (mediaType != null && mediaType.getCharset() != null) {
            return mediaType.getCharset();
        }
        else {
            return StandardCharsets.UTF_8;
        }
    }

}
