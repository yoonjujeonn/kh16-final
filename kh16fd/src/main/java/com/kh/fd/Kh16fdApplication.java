package com.kh.fd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//아래 설정이 있어야 스프링 암호화 설정을 무시하고 진행 가능함
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Kh16fdApplication {

	public static void main(String[] args) {
		SpringApplication.run(Kh16fdApplication.class, args);
	}

}
