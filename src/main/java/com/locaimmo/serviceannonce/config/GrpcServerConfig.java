package com.locaimmo.serviceannonce.config;

import net.devh.boot.grpc.server.config.GrpcServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(GrpcServerConfig.class);
    
    private final GrpcServerProperties grpcServerProperties;

    public GrpcServerConfig(GrpcServerProperties grpcServerProperties) {
        this.grpcServerProperties = grpcServerProperties;
    }

    @PostConstruct
    public void logGrpcServerConfig() {
        logger.info("gRPC Server Configuration:");
        logger.info("  Port: {}", grpcServerProperties.getPort());
        logger.info("  Address: {}", grpcServerProperties.getAddress());
    }
}
