# StockTradingFullStack

The aim of this application is to take streaming data for any given stock from the IEX Exchange and display the real-time stock price updates per second.

The project consists of a Java backend and a JavaScript frontend with Highcharts library to display the charts.

The class StocksTradingAppApplication is the main class that runs the program. Once you run the program, you should see a graph on localhost:8080.
At this time, there are server logs in form of print statements and console output for the frontend to ensure the data is communicating as expected from server to client 
and vice versa. 

The application will require you to add your API_KEY and API_SECRET in the AlpacaService.java file on line 66. 

The project streams live data using the Alpaca API with the 'iex' feed. Note that the live data only streams from Monday - Friday 8 AM EST - 4 PM EST. 
![StockTradingFrontEnd](https://github.com/SharwariS/StockTradingFullStack/assets/59146359/0631c67e-027b-41e0-9adc-e440a6d178ae)
