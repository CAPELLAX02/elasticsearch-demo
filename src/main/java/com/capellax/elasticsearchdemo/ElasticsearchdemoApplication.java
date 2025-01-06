package com.capellax.elasticsearchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;

@SpringBootApplication
public class ElasticsearchdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticsearchdemoApplication.class, args);
	}

}
