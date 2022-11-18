package com.byd.auth.manage.service.configuration.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "http.async")
public class AsyncRestTemplateProperties {
    // Determines the timeout in milliseconds until a connection is established.
    private int CONNECT_TIMEOUT;

    // The timeout when requesting a connection from the connection manager.
    private int REQUEST_TIMEOUT;

    // The timeout for waiting for data
    private int SOCKET_TIMEOUT;

    // Maximum total number of connections in the pool
    private int MAX_TOTAL_CONNECTIONS;

    // Maximum number of connections to a particular host
    private int DEFAULT_MAX_PER_ROUTE;

    public int getCONNECT_TIMEOUT() {
        return CONNECT_TIMEOUT;
    }

    public void setCONNECT_TIMEOUT(int CONNECT_TIMEOUT) {
        this.CONNECT_TIMEOUT = CONNECT_TIMEOUT;
    }

    public int getREQUEST_TIMEOUT() {
        return REQUEST_TIMEOUT;
    }

    public void setREQUEST_TIMEOUT(int REQUEST_TIMEOUT) {
        this.REQUEST_TIMEOUT = REQUEST_TIMEOUT;
    }

    public int getSOCKET_TIMEOUT() {
        return SOCKET_TIMEOUT;
    }

    public void setSOCKET_TIMEOUT(int SOCKET_TIMEOUT) {
        this.SOCKET_TIMEOUT = SOCKET_TIMEOUT;
    }

    public int getMAX_TOTAL_CONNECTIONS() {
        return MAX_TOTAL_CONNECTIONS;
    }

    public void setMAX_TOTAL_CONNECTIONS(int MAX_TOTAL_CONNECTIONS) {
        this.MAX_TOTAL_CONNECTIONS = MAX_TOTAL_CONNECTIONS;
    }

    public int getDEFAULT_MAX_PER_ROUTE() {
        return DEFAULT_MAX_PER_ROUTE;
    }

    public void setDEFAULT_MAX_PER_ROUTE(int DEFAULT_MAX_PER_ROUTE) {
        this.DEFAULT_MAX_PER_ROUTE = DEFAULT_MAX_PER_ROUTE;
    }
}
