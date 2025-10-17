package vn.com.bds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("vn.com.bds.infrastructure.repository.entity")
public class BdsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdsApplication.class, args);
	}

}
