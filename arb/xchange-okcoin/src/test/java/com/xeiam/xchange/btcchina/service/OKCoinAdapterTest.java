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
package com.xeiam.xchange.btcchina.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.okcoin.OKCoinAdapters;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinMarketDepth;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTicker;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTrade;
import com.xeiam.xchange.btcchina.service.marketdata.OKCoinDepthJSONTest;
import com.xeiam.xchange.btcchina.service.marketdata.OKCoinTickerJSONTest;
import com.xeiam.xchange.btcchina.service.marketdata.OKCoinTradesJSONTest;
import com.xeiam.xchange.utils.DateUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests the OKCoinAdapter class
 */
public class OKCoinAdapterTest {

  @Test
  public void testOrderAdapterWithDepth() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = OKCoinDepthJSONTest.class.getResourceAsStream("/marketdata/example-depth-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    OKCoinMarketDepth OKCoinDepth = mapper.readValue(is, OKCoinMarketDepth.class);

    List<LimitOrder> asks = OKCoinAdapters.adaptOrders(OKCoinDepth.getAsks(), "CNY", OrderType.ASK);

    // Verify all fields filled
    assertThat(asks.get(0).getLimitPrice().getAmount().doubleValue()).isEqualTo(1.0e+14);
    assertThat(asks.get(0).getType()).isEqualTo(OrderType.ASK);
    assertThat(asks.get(0).getTradableAmount().doubleValue()).isEqualTo(0.031);
    assertThat(asks.get(0).getTradableIdentifier()).isEqualTo("BTC");
    assertThat(asks.get(0).getTransactionCurrency()).isEqualTo("CNY");

  }

  @Test
  public void testTradeAdapter() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = OKCoinTradesJSONTest.class.getResourceAsStream("/marketdata/example-trades-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    OKCoinTrade[] OKCoinTrades = mapper.readValue(is, OKCoinTrade[].class);

    Trades trades = OKCoinAdapters.adaptTrades(OKCoinTrades, "CNY", "BTC");
    assertThat(trades.getTrades().size()).isEqualTo(738);

    // Verify all fields filled
    assertThat(trades.getTrades().get(0).getPrice().getAmount().doubleValue() == 545);
    assertThat(trades.getTrades().get(0).getTradableAmount().doubleValue() == 0.37);
    assertThat(DateUtils.toUTCString(trades.getTrades().get(0).getTimestamp())).isEqualTo("2013-07-25 18:22:55 GMT");
    System.out.println(trades.getTrades().get(0).toString());

  }

  @Test
  public void testTickerAdapter() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = OKCoinTickerJSONTest.class.getResourceAsStream("/marketdata/example-ticker-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    OKCoinTicker OKCoinTicker = mapper.readValue(is, OKCoinTicker.class);

    Ticker ticker = OKCoinAdapters.adaptTicker(OKCoinTicker, "CNY", "BTC");
    System.out.println(ticker.toString());

    assertThat(ticker.getLast().toString()).isEqualTo("CNY 546.00");
    assertThat(ticker.getLow().toString()).isEqualTo("CNY 545.00");
    assertThat(ticker.getHigh().toString()).isEqualTo("CNY 547.77");
    assertThat(ticker.getVolume()).isEqualTo(new BigDecimal("2593.89900000"));

  }
}
