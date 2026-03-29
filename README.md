# Market Data Service

## Overview

The Market Data Service is a Spring Boot application designed to fetch, store, and manage financial market data for various stocks. It periodically retrieves the latest stock prices from the Alpha Vantage API, stores them in a PostgreSQL database, and provides a resilient and scalable solution for market data management.

## Features

- **Scheduled Data Sync:** Automatically fetches the latest stock prices at regular intervals.
- **Rate Limiting:** Implements a rate limiter to adhere to the API provider's request quotas (5 requests per minute).
- **Resilient Operations:** Uses a retry mechanism to handle transient network errors when fetching data.
- **Historical Data:** Capable of fetching and storing historical price data.
- **RESTful API:** Exposes endpoints for interacting with the stored market data (documented with Swagger).
- **Database Integration:** Uses Spring Data JPA to persist data in a PostgreSQL database.

## Technologies Used

- **Backend:** Java 21, Spring Boot
- **Data:** Spring Data JPA, PostgreSQL
- **API:** Spring Web, SpringDoc OpenAPI (Swagger)
- **Build:** Apache Maven
- **Utilities:**
  - Lombok
  - MapStruct
  - Google Guava (for Rate Limiting)
  - Spring Retry

## API Documentation

Once the application is running, you can access the Swagger UI to view and test the available API endpoints.

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
