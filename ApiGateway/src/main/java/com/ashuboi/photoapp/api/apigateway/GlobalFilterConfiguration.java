package com.ashuboi.photoapp.api.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFilterConfiguration {

    final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);


    // When it comes to pre-filters, the lower the order index, the higher is the priority for the pre-filter
    // But for post filters, the lower the value of order index, the lower is the execution priority
    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            logger.info("Second Pre Filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Third Post Filter executed");
            }));
        };
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            logger.info("third Pre Filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Second Post Filter executed");
            }));
        };
    }

    @Order(3)
    @Bean
    public GlobalFilter fourthPreFilter() {
        return (exchange, chain) -> {
            logger.info("fourth Pre Filter executed");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("My First Post Filter executed");
            }));
        };
    }
}
