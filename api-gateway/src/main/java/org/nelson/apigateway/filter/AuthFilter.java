package org.nelson.apigateway.filter;

import lombok.AllArgsConstructor;
import org.nelson.apigateway.util.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.nelson.apigateway.filter.AuthFilter.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<Config> {

    private RouteValidator routeValidator;
    private JwtUtils jwtUtils;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())){
                if (!exchange.getRequest().getHeaders().containsKey(AUTHORIZATION)){
                    throw new RuntimeException("Missing Authorization Header");
                }
                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtils.validateToken(authHeader);
                }catch (Exception e){
                    throw new RuntimeException("Token is not valid");
                }
            }
            return chain.filter(exchange);
        };
    }


    public static class Config{}

}
