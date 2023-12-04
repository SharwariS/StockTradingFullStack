document.addEventListener('DOMContentLoaded', function () {
    var stompClient = null;

    function connectWebSocket() {
        var socket = new SockJS('/ws');  // Adjust the WebSocket endpoint

        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
        // This below method is only used to log if the socket is connected
//            console.log('Connected to WebSocket 1-data');

            // Subscribe to the topic for streaming data
            stompClient.subscribe('/topic/stocks', function (message) {

                // Handle incoming streaming data
                var data = JSON.parse(message.body);
                updateChart(data);  // update the chart
            });
        });
    }

    function updateChart(data) {

        var timestamp = new Date(data.timestamp).getTime();

        // Update the Highcharts chart
        chart.series[0].addPoint([timestamp, data.AAPLPrice], true, true);  // AAPL
    }

    // Initialize Highcharts chart
    var chart = Highcharts.chart('stock-streaming-graph', {
        chart: {
            type: 'spline',
            animation: Highcharts.svg, // don't animate in old IE
            marginRight: 10,
        },
        title: {
            text: 'Real-time Stock Data Streaming',
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 150,
        },
        yAxis: {
            title: {
                text: 'Stock Price',
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080',
            }],
        },
        series: [{
            name: 'AAPL',
            data: [],
        }]
    });

    // Connect to WebSocket and handle streaming data
    connectWebSocket();
});
