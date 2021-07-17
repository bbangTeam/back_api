package io.my.bbang.commons.context;

import com.mongodb.Function;

import reactor.util.context.Context;

import reactor.core.publisher.Mono;

public class JwtContextHolder {
    private static final Class<?> JWT_CONTEXT_KEY = JwtContext.class;


    public static Mono<JwtContext> getContext() {
        return Mono.deferContextual(ctx -> Mono.just(ctx))
                .filter( c -> c.hasKey(JWT_CONTEXT_KEY))
                .flatMap( c-> c.<Mono<JwtContext>>get(JWT_CONTEXT_KEY));
        // return Mono.subscriberContext()
        //         .filter( c -> c.hasKey(JWT_CONTEXT_KEY))
        //         .flatMap( c-> c.<Mono<JwtContext>>get(JWT_CONTEXT_KEY));
    }

    
    public static Function<Context, Context> clearContext() {
        return context -> context.delete(JWT_CONTEXT_KEY);
    }

    
    public static Context withJwtContext(Mono<? extends JwtContext> jwtContext) {
        return Context.of(JWT_CONTEXT_KEY, jwtContext);
    }
}
