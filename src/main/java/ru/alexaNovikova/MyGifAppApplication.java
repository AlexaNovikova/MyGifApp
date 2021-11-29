package ru.alexaNovikova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MyGifAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyGifAppApplication.class, args);
	}

}
