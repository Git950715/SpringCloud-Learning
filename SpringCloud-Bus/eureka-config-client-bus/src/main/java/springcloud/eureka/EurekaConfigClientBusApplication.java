package springcloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EurekaConfigClientBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaConfigClientBusApplication.class, args);
    }
    
}
