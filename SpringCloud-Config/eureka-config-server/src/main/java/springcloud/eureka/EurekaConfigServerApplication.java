package springcloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class EurekaConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaConfigServerApplication.class, args);
    }
}
