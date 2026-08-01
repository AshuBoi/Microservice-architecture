package com.ashuboi.photoapp.api.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;


@Component
public class MyPreFilter implements GlobalFilter, Ordered {
    final Logger logger = LoggerFactory.getLogger(MyPreFilter.class);

    // At the end of this function we need to pass ServerWebExchange object to the next filter in chain
    // so we return chain.filter and exchange to the filter in chain
    // From this server web exchange object, we can read the details of each request, process these details
    // If needed add new details, and then pass this server web exchange object to the next filter in chain
    // once of prefilter in the filter chain are executed, spring cloud api gateway will route the request to a
    // destination microservice and to make it as a pre-filter instead of post-filter.
    // We do not need to annotate anything Spring cloud api gateway will figure out whether it is pre or post filter based on logic
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("First MyPreFilter is executed..");

        String requestPath = exchange.getRequest().getPath().toString();
        logger.info("Request Path : {}", requestPath);

        HttpHeaders headers = exchange.getRequest().getHeaders();

        Set<String> headerNames = headers.keySet();

        headerNames.forEach((headerName) -> {
            String headerValue = headers.getFirst(headerName);
            logger.info("headerName : {}", headerValue);
        });

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
