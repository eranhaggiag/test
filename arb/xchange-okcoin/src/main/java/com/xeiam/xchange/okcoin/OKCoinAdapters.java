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
package com.xeiam.xchange.okcoin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xeiam.xchange.okcoin.dto.OKCoinValue;

import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTicker;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTrade;
import com.xeiam.xchange.okcoin.dto.trade.OKCoinOrder;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.currency.MoneyUtils;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.dto.trade.Wallet;
import com.xeiam.xchange.utils.DateUtils;

/**
 * Various adapters for converting from OKCoin DTOs to XChange DTOs
 */
public final class OKCoinAdapters {

  /**
   * private Constructor
   */
  private OKCoinAdapters() {

  }

  /**
   * Adapts a List of OKCoinOrders to a List of LimitOrders
   * 
   * @param OKCoinOrders
   * @param currency
   * @param orderType
   * @param id
   * @return
   */
  public static List<LimitOrder> adaptOrders(List<BigDecimal[]> OKCoinOrders, String currency, OrderType orderType) {

    List<LimitOrder> limitOrders = new ArrayList<LimitOrder>();

    for (BigDecimal[] OKCoinOrder : OKCoinOrders) {
      limitOrders.add(adaptOrder(OKCoinOrder[1], OKCoinOrder[0], currency, orderType));
    }

    return limitOrders;
  }

  /**
   * Adapts a OKCoinOrder to a LimitOrder
   * 
   * @param amount
   * @param price
   * @param currency
   * @param orderTypeString
   * @param id
   * @return
   */
  public static LimitOrder adaptOrder(BigDecimal amount, BigDecimal price, String currency, OrderType orderType) {

    // place a limit order
    String tradableIdentifier = Currencies.BTC;
    BigMoney limitPrice = MoneyUtils.parse(currency + " " + price);

    return new LimitOrder(orderType, amount, tradableIdentifier, currency, "", null, limitPrice);

  }

  /**
   * Adapts a OKCoinTrade to a Trade Object
   * 
   * @param OKCoinTrade A OKCoin trade
   * @return The XChange Trade
   */
  public static Trade adaptTrade(OKCoinTrade OKCoinTrade, String currency, String tradableIdentifier) {

    BigDecimal amount = OKCoinTrade.getAmount();
    BigMoney price = MoneyUtils.parse(currency + " " + OKCoinTrade.getPrice());
    Date date = DateUtils.fromMillisUtc(OKCoinTrade.getDate() * 1000L);

    return new Trade(null, amount, tradableIdentifier, currency, price, date, OKCoinTrade.getTid());
  }

  /**
   * Adapts a OKCoinTrade[] to a Trades Object
   * 
   * @param OKCoinTrades The OKCoin trade data
   * @return The trades
   */
  public static Trades adaptTrades(OKCoinTrade[] OKCoinTrades, String currency, String tradableIdentifier) {

    List<Trade> tradesList = new ArrayList<Trade>();
    for (OKCoinTrade OKCoinTrade : OKCoinTrades) {
      tradesList.add(adaptTrade(OKCoinTrade, currency, tradableIdentifier));
    }
    return new Trades(tradesList);
  }

  public static String getPriceString(BigMoney price) {

    return price.getAmount().stripTrailingZeros().toPlainString();
  }

  /**
   * Adapts a OKCoinTicker to a Ticker Object
   * 
   * @param OKCoinTicker
   * @return
   */
  public static Ticker adaptTicker(OKCoinTicker OKCoinTicker, String currency, String tradableIdentifier) {

    BigMoney last = MoneyUtils.parse(currency + " " + OKCoinTicker.getTicker().getLast());
    BigMoney high = MoneyUtils.parse(currency + " " + OKCoinTicker.getTicker().getHigh());
    BigMoney low = MoneyUtils.parse(currency + " " + OKCoinTicker.getTicker().getLow());
    BigMoney buy = MoneyUtils.parse(currency + " " + OKCoinTicker.getTicker().getBuy());
    BigMoney sell = MoneyUtils.parse(currency + " " + OKCoinTicker.getTicker().getSell());
    BigDecimal volume = OKCoinTicker.getTicker().getVol();

    return TickerBuilder.newInstance().withTradableIdentifier(tradableIdentifier).withLast(last).withHigh(high).withLow(low).withBid(buy).withAsk(sell).withVolume(volume).build();
  }

  /**
   * Adapts a OKCoinAccountInfoResponse to AccountInfo Object
   * 
   * @param response
   * @return
   */
 /* public static AccountInfo adaptAccountInfo(OKCoinResponse<OKCoinAccountInfo> response) {

      OKCoinAccountInfo result = response.getResult();
    return new AccountInfo(result.getProfile().getUsername(), OKCoinAdapters.adaptWallets(result.getBalances(), result.getFrozens()));
  }*/

  // /**
  // * Adapts Map<String, OKCoinValue> balances, Map<String,OKCoinValue> frozens to List<Wallet>
  // *
  // * @param balances
  // * @param frozens
  // * @return
  // */
  // todo: can't have <> in javadoc
  /**
   * @param balances
   * @param frozens
   * @return
   */
  public static List<Wallet> adaptWallets(Map<String, OKCoinValue> balances, Map<String, OKCoinValue> frozens) {

    List<Wallet> wallets = new ArrayList<Wallet>();

    for (Map.Entry<String, OKCoinValue> entry : balances.entrySet()) {
      Wallet wallet;
      OKCoinValue frozen = frozens.get(entry.getKey());
      if (frozen != null) {
        wallet = adaptWallet(entry.getValue(), frozen);
        if (wallet != null) {
          wallets.add(wallet);
        }
      }
    }
    return wallets;

  }

  /**
   * Adapts OKCoinValue balance, OKCoinValue frozen to wallet
   * 
   * @param balance
   * @param frozen
   * @return
   */
  public static Wallet adaptWallet(OKCoinValue balance, OKCoinValue frozen) {

    if (balance != null && frozen != null) {
      BigDecimal balanceAmount = OKCoinUtils.valueToBigDecimal(balance);
      BigDecimal frozenAmount = OKCoinUtils.valueToBigDecimal(frozen);
      BigMoney cash = BigMoney.of(CurrencyUnit.of(balance.getCurrency()), balanceAmount.add(frozenAmount));
      return new Wallet(balance.getCurrency(), cash);
    }
    else {
      return null;
    }
  }

  // /**
  // * Adapts List<OKCoinOrder> to OpenOrders
  // *
  // * @param orders
  // * @return
  // */
  // todo: can't have <> in javadoc
  /**
   * @param orders
   * @return
   */
  public static OpenOrders adaptOpenOrders(List<OKCoinOrder> orders) {

    List<LimitOrder> limitOrders = new ArrayList<LimitOrder>();
    if (orders != null) {
      for (OKCoinOrder order : orders) {
        System.out.println(order);
        if (order.getStatus().equals("open")) {
          LimitOrder limitOrder = adaptLimitOrder(order);
          if (limitOrder != null) {
            limitOrders.add(limitOrder);
          }
        }
      }
    }

    return new OpenOrders(limitOrders);
  }

  /**
   * Adapts OKCoinOrder to LimitOrder
   * 
   * @param order
   * @return
   */
  public static LimitOrder adaptLimitOrder(OKCoinOrder order) {

    Order.OrderType orderType = order.getType().equals("bid") ? Order.OrderType.BID : Order.OrderType.ASK;
    BigDecimal amount = order.getAmount();
    String id = Long.toString(order.getId());
    Date date = new Date(order.getDate() * 1000);
    BigMoney price = BigMoney.of(CurrencyUnit.of(order.getCurrency()), order.getPrice());

    return new LimitOrder(orderType, amount, "BTC", "CNY", id, date, price);
  }

}
