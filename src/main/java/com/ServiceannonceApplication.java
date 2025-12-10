package com;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.CountDownLatch;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"com.locaimmo.serviceannonce", "com"})
public class ServiceannonceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServiceannonceApplication.class, args);
        
        // Check if gRPC services are registered
        Map<String, Object> grpcServices = context.getBeansWithAnnotation(GrpcService.class);
        System.out.println("Found " + grpcServices.size() + " gRPC service(s): " + grpcServices.keySet());
        
        // Check all gRPC-related beans
        String[] allBeans = context.getBeanDefinitionNames();
        System.out.println("\n=== Checking for gRPC-related beans ===");
        for (String beanName : allBeans) {
            if (beanName.toLowerCase().contains("grpc")) {
                System.out.println("Found gRPC bean: " + beanName + " -> " + context.getType(beanName));
            }
        }
        
        // Check if gRPC server bean exists (try both possible names)
        boolean serverFound = false;
        try {
            Object grpcServer = context.getBean("shadedNettyGrpcServerLifecycle");
            System.out.println("✓ gRPC Server Lifecycle bean found: " + grpcServer.getClass().getName());
            serverFound = true;
        } catch (Exception e) {
            try {
                Object grpcServer = context.getBean("grpcServerLifecycle");
                System.out.println("✓ gRPC Server Lifecycle bean found: " + grpcServer.getClass().getName());
                serverFound = true;
            } catch (Exception e2) {
                System.err.println("WARNING: gRPC Server Lifecycle bean not found!");
            }
        }
        
        // Try to find GrpcServerFactory (try both possible names)
        try {
            Object factory = context.getBean("shadedNettyGrpcServerFactory");
            System.out.println("✓ gRPC Server Factory found: " + factory.getClass().getName());
        } catch (Exception e) {
            try {
                Object factory = context.getBean("grpcServerFactory");
                System.out.println("✓ gRPC Server Factory found: " + factory.getClass().getName());
            } catch (Exception e2) {
                System.err.println("WARNING: gRPC Server Factory not found!");
            }
        }
        
        if (serverFound) {
            System.out.println("\n✓✓✓ gRPC Server should be running on port 9090! ✓✓✓");
            System.out.println("Try connecting with BloomRPC to localhost:9090");
        }
        
        // Keep the application running
        CountDownLatch latch = new CountDownLatch(1);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server...");
            context.close();
            latch.countDown();
        }));
        
        try {
            System.out.println("Application is running. gRPC server should be on port 9090. Press Ctrl+C to stop...");
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            context.close();
        }
    }

}

