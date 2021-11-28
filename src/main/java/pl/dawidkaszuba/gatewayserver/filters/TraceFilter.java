package pl.dawidkaszuba.gatewayserver.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;

@Order(1)
@Component
public class TraceFilter implements GlobalFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(requestHeaders)) {
            LOGGER.debug("Tracing id found in tracing filter: {}. ", filterUtility.getCorrelationId(requestHeaders));
        } else {
            String correlationID = getCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationID);
            LOGGER.debug("Tracing id generated in tracing filter: {}.", correlationID);
        }
        return chain.filter(exchange);
    }
    private boolean isCorrelationIdPresent(HttpHeaders requestHeadres) {
        if (filterUtility.getCorrelationId(requestHeadres) != null) {
            return true;
        } else {
            return false;
        }
    }

    private String getCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
