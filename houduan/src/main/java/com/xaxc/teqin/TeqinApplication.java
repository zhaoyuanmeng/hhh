package com.xaxc.teqin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan("com.xaxc.teqin.mapper")
@SpringBootApplication
public class TeqinApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeqinApplication.class, args);
	}

}
