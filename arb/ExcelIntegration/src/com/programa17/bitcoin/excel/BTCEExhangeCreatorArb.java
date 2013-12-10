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
package com.programa17.bitcoin.excel;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.btce.v3.BTCEExchange;
import com.xeiam.xchange.currency.MoneyUtils;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.PollingTradeService;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * @author Matija Mazi
 */
public class BTCEExhangeCreatorArb {

  public static Exchange createExchange() {

    ExchangeSpecification exSpec = new ExchangeSpecification(BTCEExchange.class);
    exSpec.setApiKey("BWOP2ZIW-3BRI8KU8-7CH0UVX6-V00L54MI-EOLV0MM2");
      exSpec.setSecretKey("07db10401a06b10a9bce2125fdc5995e5c159430edbae92dbd422537bba158d6");
//    exSpec.setSecretKey("804ace4f64b9cac6b6584f70af3ab66c8415538c8c006c8728c937d680cafece");
//    exSpec.setApiKey("7A2REWZK-PVJK1CRF-374Z6J13-SO2R3EIX-EMF3OCBR");
      exSpec.setSslUri("https://btc-e.com");
      return ExchangeFactory.INSTANCE.createExchange(exSpec);
  }

    public static void main(String[] args) throws IOException {
        Exchange exchange = createExchange();
        PollingTradeService pollingTradeService = exchange.getPollingTradeService();
        BigDecimal bigDecimal = new BigDecimal(0.30100).round(new MathContext(5));
        pollingTradeService.placeLimitOrder(new LimitOrder(Order.OrderType.BID,new BigDecimal(1),"LTC","BTC","",null, BigMoney.of(CurrencyUnit.USD,bigDecimal)));
    }
}
