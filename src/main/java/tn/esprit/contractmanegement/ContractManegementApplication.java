package tn.esprit.contractmanegement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ContractManegementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractManegementApplication.class, args);
    }

}
