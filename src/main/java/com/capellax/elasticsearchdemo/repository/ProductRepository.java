package com.capellax.elasticsearchdemo.repository;

import com.capellax.elasticsearchdemo.model.Product;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    @Query("{\"match_phrase_prefix\": {\"name\": \"?0\"}}")
    List<Product> findByName(String name);

    List<Product> findByPriceBetween(Double min, Double max);


}
