package com.programa17.bitcoin.excel;

import com.xeiam.xchange.BTCTradeExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.btce.v3.BTCEExchange;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.okcoin.OKCoinExchange;
import com.xeiam.xchange.service.polling.PollingMarketDataService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 11/29/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataExcelProvider {


    private static Map<String, Exchange> exchangeMap = new HashMap<String, Exchange>();
    private static Map<String, PollingMarketDataService> pollingMarketDataServiceMap = new HashMap<String, PollingMarketDataService>();
    private static Map<String, Class> exchangeToClass = new HashMap<String, Class>();

    static {
        exchangeToClass.put("BTCE", BTCEExchange.class);
        exchangeToClass.put("OKCOIN", OKCoinExchange.class);
        exchangeToClass.put("BTCTrade", BTCTradeExchange.class);
        exchangeMap.put("BTCE", BTCEExhangeCreatorArb.createExchange());
    }

    public static void setup(String exchange) {
        exchangeMap.put(exchange, ExchangeFactory.INSTANCE.createExchange(exchangeToClass.get(exchange).getName()));

        // Interested in the public polling market data feed (no authentication)

    }


    public static float getPrice(String exchangeName, String first, String second, String type) throws IOException {
        Ticker ticker = getPollingMarketDataService(exchangeName).getTicker(first, second);

        float result = -1;
        if (type.equalsIgnoreCase("Bid")) {
            result = ticker.getBid().getAmount().floatValue();
        } else if (type.equalsIgnoreCase("Ask")) {
            result = ticker.getAsk().getAmount().floatValue();
        } else if (type.equalsIgnoreCase("Last")) {
            result = ticker.getLast().getAmount().floatValue();
        }
        return result;


    }

    static PollingMarketDataService getPollingMarketDataService(String exchangeName) {
        if(!pollingMarketDataServiceMap.containsKey(exchangeName)) {
            pollingMarketDataServiceMap.put(exchangeName, getExchange(exchangeName).getPollingMarketDataService());
        }

        return pollingMarketDataServiceMap.get(exchangeName);
    }

    static Exchange getExchange(String exchangeName) {
        if (!exchangeMap.containsKey(exchangeName)) setup(exchangeName);
        return exchangeMap.get(exchangeName);
    }


    public static float getPriceDepth(String exchangeName, String first, String second, String type, int depth) throws IOException {
        PollingMarketDataService marketDataService = getPollingMarketDataService(exchangeName);


        // Get the latest custom size order book (3 entries) data for BTC/USD
        OrderBook orderBook = marketDataService.getOrderBook(first, second);

        float result = -1;
        if (type.equalsIgnoreCase("Bid")) {
            result = orderBook.getBids().get(depth).getLimitPrice().getAmount().floatValue();
        } else if (type.equalsIgnoreCase("Ask")) {
            result = orderBook.getAsks().get(depth).getLimitPrice().getAmount().floatValue();
        }
        return result;
    }
}
