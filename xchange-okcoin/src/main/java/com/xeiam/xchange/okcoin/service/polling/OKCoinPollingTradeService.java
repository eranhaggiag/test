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

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.okcoin.OKCoin;
import com.xeiam.xchange.okcoin.dto.OKCoinResponse;
import com.xeiam.xchange.okcoin.service.OKCoinTradeApi;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;
import com.xeiam.xchange.service.polling.PollingTradeService;
import com.xeiam.xchange.utils.Assert;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

import java.io.IOException;

public class OKCoinPollingTradeService extends BasePollingExchangeService implements PollingTradeService {

  /**
   * Configured from the super class reading of the exchange specification
   */
  private final OKCoin okCoin;
  private ParamsDigest signatureCreator;
  private OKCoinTradeApi tradeApi;

  /**
   * Constructor
   * 
   * @param exchangeSpecification
   */
  public OKCoinPollingTradeService(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);

    Assert.notNull(exchangeSpecification.getSslUri(), "Exchange specification URI cannot be null");
    this.okCoin= RestProxyFactory.createProxy(OKCoin.class, exchangeSpecification.getSslUri());
//    signatureCreator = OKCoinDigest.createInstance(exchangeSpecification.getApiKey(), exchangeSpecification.getSecretKey());
      tradeApi = new OKCoinTradeApi();
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {

/*
    OKCoinResponse<OKCoinOrders> response = OKCoin.getOrders(signatureCreator, OKCoinUtils.getRandom(), new OKCoinGetOrdersRequest());
    return OKCoinAdapters.adaptOpenOrders(response.getResult().getOrders());


*/
      return null;
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {

    String orderId = null;
    if ((limitOrder.getTradableIdentifier().equals("BTC") || limitOrder.getTradableIdentifier().equals("LTC")) && limitOrder.getTransactionCurrency().equals("CNY")) {

      OKCoinResponse<Boolean> response = null;

      if (limitOrder.getType() == Order.OrderType.BID) {

       tradeApi.buy(limitOrder.getTradableIdentifier(),limitOrder.getLimitPrice().getAmount().floatValue(), limitOrder.getTradableAmount().floatValue());

      }
      else if (limitOrder.getType() == Order.OrderType.ASK) {

        tradeApi.sell(limitOrder.getTradableIdentifier(),limitOrder.getLimitPrice().getAmount().floatValue(), limitOrder.getTradableAmount().floatValue());

      }
    }
      return "0";
  }

  @Override
  public boolean cancelOrder(String orderId) throws IOException {

/*
    OKCoinResponse<Boolean> response = OKCoin.cancelOrder(signatureCreator, OKCoinUtils.getRandom(), new OKCoinCancelOrderRequest(Long.parseLong(orderId)));
    return response.getResult();
*/
      return false;
  }

  @Override
  public Trades getTradeHistory(final Object... arguments) throws IOException {

    throw new NotYetImplementedForExchangeException();
  }

}
