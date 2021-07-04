package io.my.bbang.commons.context;

import java.io.Serializable;

import org.springframework.web.server.ServerWebExchange;

import io.my.bbang.user.domain.User;
import reactor.core.publisher.Mono;

final public class JwtContext implements Serializable {
   private final Mono<String> jwt;
   private User user;

   public JwtContext(final ServerWebExchange exchange) {
       jwt = Mono.just(exchange)
                    .map(ServerWebExchange::getRequest)
                    .map(request -> {
                        String jwt = request.getHeaders().get("Authorization").toString().substring(8).replace("]", "");
                        return jwt;
                    });
   }

   public Mono<String> getJwt() { return jwt; }

   public void setUser(User user) {
        this.user = user;
   }

   public User getUser() {
       return user;
   }
}
