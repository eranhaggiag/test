package com.programa17.bitcoin.excel.controller;

import com.programa17.bitcoin.excel.BTCEExhangeCreatorArb;
import com.programa17.bitcoin.excel.model.OrderBookUtils;
import com.programa17.bitcoin.excel.model.PositionProvider;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.examples.btce.BTCEExamplesUtils;
import com.xeiam.xchange.service.polling.PollingAccountService;
import org.joda.money.CurrencyUnit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/2/13
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeController implements PositionProvider{

    private final Map<String,Exchange> exchangeMap= new HashMap<String, Exchange>();
    private Map<String, PollingAccountService> pollingAccountServiceMap = new HashMap<String, PollingAccountService>();

    public ExchangeController() {
        Exchange btce = BTCEExhangeCreatorArb.createExchange();
        exchangeMap.put("BTCE", btce);
        pollingAccountServiceMap.put("BTCE",btce.getPollingAccountService());
        //okCoin
    }


    @Override
    public Float getPosition(String exchangeName, String currency) {
        PollingAccountService pollingAccountService = pollingAccountServiceMap.get(exchangeName);
        float v = 0;
        try {
            v = pollingAccountService.getAccountInfo().getBalance(CurrencyUnit.getInstance(currency)).getAmount().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    public Float getBid(String exchangeName, String firstCurrency, String secondCurency, float amount) {
        OrderBook orderBook = null;
        float priceForAmount = -1;
        try {
            Exchange exchange = exchangeMap.get(exchangeName);
            orderBook = exchange.getPollingMarketDataService().getOrderBook(firstCurrency, secondCurency);
            priceForAmount = OrderBookUtils.getPriceForAmount(orderBook.getBids(), amount);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return priceForAmount;
    }

    public Float getAsk(String exchangeName, String firstCurrency, String secondCurency, float amount) {
        OrderBook orderBook = null;
        float priceForAmount = -1;
        try {
            Exchange exchange = exchangeMap.get(exchangeName);
            orderBook = exchange.getPollingMarketDataService().getOrderBook(firstCurrency, secondCurency);
            priceForAmount = OrderBookUtils.getPriceForAmount(orderBook.getAsks(), amount);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return priceForAmount;
    }
}
