package com.example.demo.service;

import com.example.demo.model.MarketData;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.concurrent.ExecutionException;

/**
 * This class handles the backend, connects to the websocket url,
 * parses the JSON data received to send it to the frontend for
 * displaying as a graph
 */
@Service
public class AlpacaService {
    String apiKey = "apikey";
    String apiSecret = "apiSecret";
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * This is the constructor for the class
     * @param messagingTemplate
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public AlpacaService(SimpMessagingTemplate messagingTemplate) throws IOException, ExecutionException, InterruptedException {
        this.messagingTemplate = messagingTemplate;
        connectToAlpacaWebSocket();
    }

    /**
     * This method gets you the real time market data for a given stock symbol
     * @param symbol - string value of stock symbol
     * @return
     */
    public MarketData getRealTimeMarketData(String symbol) {
        // return a default MarketData object
        return new MarketData(symbol, 0.0);
    }

    /**
     * This method connects to the AlpacaWebSocket to
     * start sending and receiving data
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void connectToAlpacaWebSocket() throws IOException, ExecutionException, InterruptedException {
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketSession webSocketSession = webSocketClient
                .doHandshake(new AlpacaWebSocketHandler(messagingTemplate), "wss://stream.data.alpaca.markets/v2/iex") // the alpaca websocket url
                .get();

        // Perform authentication
        String authMessage = "{\"action\":\"auth\",\"key\":\"apiKeyValue\",\"secret\":\"apiSecretValue\"}";
//        String authMessage = "{\"action\":\"auth\",\"key\":[\"apiKey\"],\"secret\":[\"apiSecret\"]}";
        webSocketSession.sendMessage(new TextMessage(authMessage));

        // Subscribe to real-time trade data (IEX format)
        String subscribeMessage = "{\"action\":\"subscribe\",\"trades\":[\"AAPL\"]}";
        webSocketSession.sendMessage(new TextMessage(subscribeMessage));
    }

    public static class AlpacaWebSocketHandler extends TextWebSocketHandler {

        private final SimpMessagingTemplate messagingTemplate;

        /**
         * This is a WebSocketHandler method to send messages to the client
         * @param messagingTemplate
         */
        public AlpacaWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
            this.messagingTemplate = messagingTemplate;
        }

        /**
         * This method ensures we are sending the data to the client
         * for the frontend
         * @param session - The WebSocket session that is open
         * @param message
         * @throws Exception
         */
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            String payload = message.getPayload();
//            System.out.println("handleTextMessage: Received message: " + payload);

            // Convert the payload to MarketData
            MarketData marketData = parseAlpacaMessage(payload);
//          System.out.println("inside handleTextMessage " + payload);

            // Send the market data to the WebSocket topic
            messagingTemplate.convertAndSend("/topic/stocks2", marketData);
            // This log is just to ensure we are sending data from the server successfully
            System.out.println("Sent complete");
        }

        /**
         * This method parses the data we receive from the Alpaca websocket url
         * @param payload - The string payload with format
         * [{"T":"t","S":"AAPL","i":6790,"x":"V","p":188.57,"s":100,"c":["@"],"z":"C","t":"2023-12-04T17:38:36.428955792Z"}]
         * @throws Exception
         * @return
         */
        private MarketData parseAlpacaMessage(String payload) throws Exception{
           // try {
            JSONObject jsonMessage = new JSONObject(payload.substring(1, payload.length()-1));
                System.out.println("This is inside the JSON message : " + jsonMessage);

            // Check the message type
                    String messageType = jsonMessage.optString("T", ""); // here messageType = "t"

                    String symbol = jsonMessage.optString("S", ""); // Stock symbol
                    long tradeTimestamp = jsonMessage.optLong("t", 0); // Trade timestamp (in nanoseconds)
                    double tradePrice = jsonMessage.optDouble("p", 0.0); // Trade price

                    // Convert timestamp to Instant for better representation
                    Instant instant = Instant.ofEpochSecond(0, tradeTimestamp);

                    System.out.println("Trade: Symbol=" + symbol + ", Timestamp=" + instant + ", Price=" + tradePrice );

                return new MarketData(symbol, tradePrice);

        }

    }
}
