package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main class that runs the program
 * When you run the program, the server and client start,
 * the application receives the trade messages at the backend,
 * and displays a graph on the webpage - localhost:8080
 * with the title Real-Time Stock Data Streaming
 */
@SpringBootApplication
public class StocksTradingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocksTradingAppApplication.class, args);
    }
}