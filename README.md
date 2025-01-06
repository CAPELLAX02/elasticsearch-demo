# Elasticsearch Demo Project

---

## About the Project
This project is a Spring Boot based backend work that demonstrates how to integrate Elasticsearch for search and indexing. The purpose of this project is to explore the fundamentals of the Elasticsearch integration with Spring Boot via **Spring Data Elasticsearch**.

Elasticsearch is an open-source, distributed search and analytics engine, commonly used for log analytics, real-time application monitoring, and full-text search. It allows storing, searching, and analyzing large volumes of data quickly and in near real-time.

## Technologies Used

---

* Spring Boot 3.4.1
* Spring Data Elasticsearch
* Elasticsearch 8.11.1
* Docker & Docker Compose
* Java 17
* Postman (for API Testing)

## Getting Started

---

### Prerequisites
Make sure you have the following installed on your system:

* Docker
* Java 17
* Maven

### Project Setup

**1. Clone the Repository**

```bash
    git clone https://github.com/CAPELLAX02/elasticsearch-demo
```

```bash
    cd elasticsearch-demo
```


**2. Start Elasticsearch Container**

Elasticsearch runs inside a Docker container. Use docker-compose to start the container:

```bash
    docker compose up -d
```
Check if Elasticsearch is running correctly by executing:

```bash
    curl -X GET http://localhost:9200
```

Expected response:

```shell
{
  "name" : "7badd03166bd",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "XuwMmYjzRPWLSFVjoJxC2g",
  "version" : {
    "number" : "8.11.1",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "6f9ff581fbcde658e6f69d6ce03050f060d1fd0c",
    "build_date" : "2023-11-11T10:05:59.421038163Z",
    "build_snapshot" : false,
    "lucene_version" : "9.8.0",
    "minimum_wire_compatibility_version" : "7.17.0",
    "minimum_index_compatibility_version" : "7.0.0"
  },
  "tagline" : "You Know, for Search"
}
```

**3. Configurations**

The Spring Boot application is configured to connect to Elasticsearch as follows.

`src/main/resources/application.yml`:
```yaml
spring:
  application:
    name: elasticsearch-demo

  elasticsearch:
    uris: http://localhost:9200
    connection-timeout: 10s
    socket-timeout: 30s

server:
  port: 8080
```

The Docker Compose file ensures that Elasticsearch runs as a single-node cluster with security disabled:

`./docker-compose.yml`:
```yaml
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.1
    container_name: elasticsearch_container
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
      - "9300:9300"
    volumes:
      - elasticsearch_data:/usr/share/elasticsearch/data

volumes:
  elasticsearch_data:
    driver: local
```

**4. Running the Application**

After ensuring Elasticsearch is running, start the Spring Boot application:

```bash
    mvn spring-boot:run
```

The application should start on `http://localhost:8080`.

---

## API Endpoints

Once the application is running, you can use a tool like **Postman** (recommended) or **curl** to test the APIs.

**Create a Product**
```shell
curl -X POST "http://localhost:8080/api/products" -H "Content-Type: application/json" -d '{
  "name": "Gaming Laptop",
  "description": "Powerful gaming laptop with RTX 4090",
  "price": 2999.99
}'
```

**Get All Products** (with <u>pagination</u>)
```shell
curl -X GET "http://localhost:8080/api/products?page=0&size=5"
```
This endpoint retrieves products with pagination support. You can modify:

- `page (default: 0)` → Page number
- `size (default: 10)` → Number of items per page

**Get Product by ID**
```shell
curl -X GET "http://localhost:8080/api/products/`{productId}`"
```
Replace `{productId}` with a valid product ID, such as `lmrnOpQBL_iXKCKspde7`, if product with that ID exists.

**Search Products by Name** (letter-matching, case-ignored)
```shell
curl -X GET "http://localhost:8080/api/products/search?name=gaming"
```

**Search Products by Price Range**
```shell
curl -X GET "http://localhost:8080/api/products/price-range?min=50&max=100"
```

**Delete Product**
```shell
curl -X DELETE "http://localhost:8080/api/products/`{productId}`"
```

---

## Elasticsearch Query Testing

To verify that data is stored inside **Elasticsearch**, run:

```shell
curl -X GET "http://localhost:9200/products/_search?pretty=true"
```

This should return a list of indexed product as might follows:

```yaml
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 20,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "products",
        "_id" : "jmrmOpQBL_iXKCKs69ck",
        "_score" : 1.0,
        "_source" : {
          "_class" : "com.capellax.elasticsearchdemo.model.Product",
          "name" : "Mechanical Keyboard",
          "description" : "High-quality mechanical keyboard with RGB lighting",
          "price" : 200.5,
          "created_at" : "2025-01-06"
        }
      },
      {
        "_index" : "products",
        "_id" : "k2rnOpQBL_iXKCKsateJ",
        "_score" : 1.0,
        "_source" : {
          "_class" : "com.capellax.elasticsearchdemo.model.Product",
          "name" : "Smartphone Tripod",
          "description" : "Adjustable smartphone tripod for content creators",
          "price" : 25.99,
          "created_at" : "2025-01-06"
        }
      },
      ... // other products
    ]
  }
}

```