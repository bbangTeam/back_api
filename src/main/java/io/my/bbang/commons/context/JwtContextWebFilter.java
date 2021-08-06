package io.my.bbang.commons.context;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class JwtContextWebFilter implements WebFilter {

    @NotNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, WebFilterChain chain) {
        JwtContext context = new JwtContext(exchange);
        return chain.filter(exchange)
                .contextWrite(JwtContextHolder.withJwtContext(Mono.just(context)));
    }
    
}
