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
package com.xeiam.xchange.okcoin.service.polling;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.btctrade.BTCTrade;
import com.xeiam.xchange.btctrade.dto.marketdata.BTCTradeMarketDepth;
import com.xeiam.xchange.btctrade.dto.marketdata.BTCTradeTrade;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.ExchangeInfo;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.okcoin.OKCoinUtils;
import com.xeiam.xchange.okcoin.service.BTCTradeAdapters;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.xeiam.xchange.utils.Assert;
import si.mazi.rescu.RestProxyFactory;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Implementation of the market data service for BTCChina
 * </p>
 * <ul>
 * <li>Provides access to various market data values</li>
 * </ul>
 */
public class BtcTradePollingMarketDataService extends BasePollingExchangeService implements PollingMarketDataService {

  private final BTCTrade btcTrade;

  /**
   * Constructor
   *
   * @param exchangeSpecification The {@link com.xeiam.xchange.ExchangeSpecification}
   */
  public BtcTradePollingMarketDataService(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);
    this.btcTrade = RestProxyFactory.createProxy(BTCTrade.class, exchangeSpecification.getSslUri());
  }

  @Override
  public Ticker getTicker(String tradableIdentifier, String currency) throws IOException {

    verify(tradableIdentifier, currency);

    // Request data
//    OKCoinTicker OKCoinTicker = btcTrade.getTicker(tradableIdentifier.toLowerCase(),currency.toLowerCase());
//
//    Adapt to XChange DTOs
//    return OKCoinAdapters.adaptTicker(OKCoinTicker, currency, tradableIdentifier);

      return null;
  }

  @Override
  public OrderBook getOrderBook(String tradableIdentifier, String currency, Object... args) throws IOException {

    verify(tradableIdentifier, currency);
      String cookie = "wafenterurl=/; wafcookie=a4a5d66e18a5ff043cc918ae7baa895d; __utma=255594459.83845952.1386058914.1386058914.1386058914.1; __utmc=255594459; __utmz=255594459.1386058914.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); wafverify=3201bdbfe368081ad6a925500f648ca7; IESESSION=alive; pgv_pvi=4082541568; pgv_si=s2862679040; USER_PW=1dd1bb7a5568cba4a8b89c155a1c17c3; PHPSESSID=552e4500a229094bf692228dc3c3167a; CNZZDATA5342694=cnzz_eid%3D1106160560-1386058920-http%253A%252F%252Fwww.btcclubs.com%26ntime%3D1386206045%26cnzz_a%3D5%26ltime%3D1386198554784%26rtime%3D2";
    // Request data
      BTCTradeMarketDepth okCoinDepth = btcTrade.getFullDepth(cookie,tradableIdentifier.toLowerCase());

      // Adapt to XChange DTOs
    List<LimitOrder> asks = BTCTradeAdapters.adaptOrders(okCoinDepth.getAsks(), currency, OrderType.ASK);
    List<LimitOrder> bids = BTCTradeAdapters.adaptOrders(okCoinDepth.getBids(), currency, OrderType.BID);

    return new OrderBook(null, asks, bids);
  }

  @Override
  public Trades getTrades(String tradableIdentifier, String currency, Object... args) throws IOException {

    verify(tradableIdentifier, currency);

    BTCTradeTrade[] btcTradeTrades = null;

    if (args.length == 0) {
      btcTradeTrades = btcTrade.getTrades(0);// default values: offset=0, limit=100
    }
    else if (args.length == 1) {
      Integer sinceTransactionID = (Integer) args[0];
      btcTradeTrades = btcTrade.getTrades(sinceTransactionID); // default values: limit=100
    }

    else {
      throw new ExchangeException("Invalid argument length. Must be 0, or 1");
    }
    // Adapt to XChange DTOs
    //TODO handleAdapter
//    return OKCoinAdapters.adaptTrades(btcTradeTrades, currency, tradableIdentifier);
      return null;
  }

  @Override
  public ExchangeInfo getExchangeInfo() throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  /**
   * Verify
   * 
   * @param tradableIdentifier The tradable identifier (e.g. BTC in BTC/USD)
   * @param currency
   */
  private void verify(String tradableIdentifier, String currency) {

    Assert.notNull(tradableIdentifier, "tradableIdentifier cannot be null");
    Assert.notNull(currency, "currency cannot be null");
    Assert.isTrue(
            OKCoinUtils.isValidCurrencyPair(new CurrencyPair(tradableIdentifier, currency)), "currencyPair is not valid:" + tradableIdentifier + " " + currency);

  }

  @Override
  public List<CurrencyPair> getExchangeSymbols() {

    return OKCoinUtils.CURRENCY_PAIRS;
  }

}
