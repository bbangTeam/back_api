package io.my.bbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableMongoAuditing
@SpringBootApplication
@EnableReactiveMongoRepositories
public class BbangApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BbangApiApplication.class, args);
	}

}
