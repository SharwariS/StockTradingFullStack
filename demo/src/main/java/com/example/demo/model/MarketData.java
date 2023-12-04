package com.example.demo.model;

/**
 * This is the Market Data class that takes the stock symbol and
 * the stock price from the backend
 * and passes it to display on the frontend
 */
public class MarketData {

    private String symbol;
    private double price;

    /**
     * Default constructor
     */
    public MarketData() {
    }

    /**
     * Parameterized Constructor for the class
     * @param symbol - stock symbol
     * @param price - stock price
     */
    public MarketData(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    /**
     * Getters
     * @return
     */
    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Setters
     * @param symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}