package br.com.netdeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("br.com")
public class NetdealApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetdealApplication.class, args);
	}

}
