package br.com.bpkedu.spring_security_by_example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "br.com.bpkedu.spring_security_by_example.domain")
@EnableJpaRepositories(basePackages = "br.com.bpkedu.spring_security_by_example.repository")
public class SpringSecurityByExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityByExampleApplication.class, args);
	}

}
