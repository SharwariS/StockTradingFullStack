package com.example.demo.controller;

import com.example.demo.model.MarketData;
import com.example.demo.service.AlpacaService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * This class handles the request from client and
 * provides them response in return
 */
@Controller
public class StocksController {

    private final AlpacaService alpacaService;

    public StocksController(AlpacaService alpacaService) {
        this.alpacaService = alpacaService;
    }

    /**
     * This method handles the streaming data
     * @param symbol - stock symbol
     * @return
     */
    @MessageMapping("/subscribe")
    @SendTo("/topic/stocks")
    public MarketData subscribe(String symbol) {
        // The AlpacaService will handle the WebSocket connection and streaming data
        return alpacaService.getRealTimeMarketData(symbol);
    }

}