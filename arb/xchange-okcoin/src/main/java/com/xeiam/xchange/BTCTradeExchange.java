/**
 * Copyright (C) 2012 - 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange;

import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.okcoin.service.polling.BtcTradePollingMarketDataService;
import com.xeiam.xchange.service.polling.PollingMarketDataService;

import java.io.IOException;

/**
 * <p>
 * Exchange implementation to provide the following to applications:
 * </p>
 * <ul>
 * <li>A wrapper for the BTCChina exchange API</li>
 * </ul>
 */
public class BTCTradeExchange extends BaseExchange implements Exchange {

  /**
   * Default constructor for ExchangeFactory
   */
  public BTCTradeExchange() {

  }

  /**
   * @return A default configuration for this exchange
   */
  public static Exchange newInstance() {

    Exchange exchange = new BTCTradeExchange();
    exchange.applySpecification(exchange.getDefaultExchangeSpecification());
    return exchange;
  }

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {

    super.applySpecification(exchangeSpecification);
    this.pollingMarketDataService = new BtcTradePollingMarketDataService(exchangeSpecification);
//    this.pollingTradeService = new OKCoinPollingTradeService(exchangeSpecification);
//    this.pollingAccountService = new OKCoinPollingAccountService(exchangeSpecification);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("http://www.btcclubs.com");
    exchangeSpecification.setHost("http://www.btcclubs.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("BTCTrade");
    exchangeSpecification.setExchangeDescription("BTCTrade is a Bitcoin exchange located in China.");

    return exchangeSpecification;
  }

    public static void main(String[] args) throws IOException {
        Exchange exchange = BTCTradeExchange.newInstance();
        PollingMarketDataService pollingMarketDataService1 = exchange.getPollingMarketDataService();
        OrderBook orderBook = pollingMarketDataService1.getOrderBook("BTC", "CNY");
        float v = orderBook.getAsks().get(0).getLimitPrice().getAmount().floatValue();
        System.out.println("v = " + v);
    }
}
