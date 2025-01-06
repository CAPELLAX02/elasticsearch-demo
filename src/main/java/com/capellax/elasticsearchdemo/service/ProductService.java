package com.capellax.elasticsearchdemo.service;

import com.capellax.elasticsearchdemo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product saveProduct(Product product);
    Optional<Product> getProductById(String id);
    Page<Product> getAllProducts(Pageable pageable);
    void deleteProductById(String id);
    List<Product> findByName(String name);
    List<Product> findByPriceRange(Double min, Double max);

}
