package com.capellax.elasticsearchdemo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder(HttpHost.create(elasticsearchUrl)).build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) throws IOException {
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);

        if (!client.ping().value()) {
            throw new RuntimeException("❌ Elasticsearch connection failed.");
        }
        System.out.println("✅ Elasticsearch connection successful!");

        return client;
    }

}
