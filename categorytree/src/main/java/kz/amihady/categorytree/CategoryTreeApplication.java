package kz.amihady.categorytree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CategoryTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoryTreeApplication.class, args);
	}

}
