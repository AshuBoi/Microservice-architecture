package com.ashuboi.photoapp.api.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyPostFilter implements GlobalFilter, Ordered {
    final Logger logger = LoggerFactory.getLogger(MyPostFilter.class);
    // The main difference between preFilter and postFilter will be in the way we implement and return value form this method
    // We will still need to make this filter method pass the execution to the next filter in chain
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Last Global Post Filter Executed");
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
