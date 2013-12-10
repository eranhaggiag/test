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
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.ExchangeInfo;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.okcoin.OKCoin;
import com.xeiam.xchange.okcoin.OKCoinAdapters;
import com.xeiam.xchange.okcoin.OKCoinUtils;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinMarketDepth;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTicker;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTrade;
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
public class OKCoinPollingMarketDataService extends BasePollingExchangeService implements PollingMarketDataService {

  private final OKCoin okcoin;

  /**
   * Constructor
   * 
   * @param exchangeSpecification The {@link ExchangeSpecification}
   */
  public OKCoinPollingMarketDataService(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);
    this.okcoin= RestProxyFactory.createProxy(OKCoin.class, exchangeSpecification.getSslUri());
  }

  @Override
  public Ticker getTicker(String tradableIdentifier, String currency) throws IOException {

    verify(tradableIdentifier, currency);

    // Request data
    OKCoinTicker OKCoinTicker = okcoin.getTicker(tradableIdentifier.toLowerCase(),currency.toLowerCase());

    // Adapt to XChange DTOs
    return OKCoinAdapters.adaptTicker(OKCoinTicker, currency, tradableIdentifier);
  }

  @Override
  public OrderBook getOrderBook(String tradableIdentifier, String currency, Object... args) throws IOException {

    verify(tradableIdentifier, currency);

    // Request data
      OKCoinMarketDepth okCoinDepth = okcoin.getFullDepth(tradableIdentifier.toLowerCase(),currency.toLowerCase());

      // Adapt to XChange DTOs
    List<LimitOrder> asks = OKCoinAdapters.adaptOrders(okCoinDepth.getAsks(), currency, OrderType.ASK);
    List<LimitOrder> bids = OKCoinAdapters.adaptOrders(okCoinDepth.getBids(), currency, OrderType.BID);

    return new OrderBook(null, asks, bids);
  }

  @Override
  public Trades getTrades(String tradableIdentifier, String currency, Object... args) throws IOException {

    verify(tradableIdentifier, currency);

    OKCoinTrade[] OKCoinTrades = null;

    if (args.length == 0) {
      OKCoinTrades = okcoin.getTrades();// default values: offset=0, limit=100
    }
    else if (args.length == 1) {
      Integer sinceTransactionID = (Integer) args[0];
      OKCoinTrades = okcoin.getTrades(sinceTransactionID); // default values: limit=100
    }

    else {
      throw new ExchangeException("Invalid argument length. Must be 0, or 1");
    }
    // Adapt to XChange DTOs
    return OKCoinAdapters.adaptTrades(OKCoinTrades, currency, tradableIdentifier);
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
