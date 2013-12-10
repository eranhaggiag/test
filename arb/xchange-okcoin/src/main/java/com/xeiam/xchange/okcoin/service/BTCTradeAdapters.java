package com.xeiam.xchange.okcoin.service;

import com.xeiam.xchange.btctrade.dto.marketdata.BtcTradeOrder;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.currency.MoneyUtils;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import org.joda.money.BigMoney;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/5/13
 * Time: 4:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class BTCTradeAdapters {
    public static List<LimitOrder> adaptOrders(List<BtcTradeOrder> orders, String currency, Order.OrderType orderType) {

        List<LimitOrder> limitOrders = new ArrayList<LimitOrder>();

        for (BtcTradeOrder order : orders) {
            limitOrders.add(adaptOrder(order.getAmount(), order.getPrice(), currency, orderType));
        }

        return limitOrders;
    }

    public static LimitOrder adaptOrder(BigDecimal amount, BigDecimal price, String currency, Order.OrderType orderType) {

        // place a limit order
        String tradableIdentifier = Currencies.BTC;
        BigMoney limitPrice = MoneyUtils.parse(currency + " " + price);

        return new LimitOrder(orderType, amount, tradableIdentifier, currency, "", null, limitPrice);

    }


}
