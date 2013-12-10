package com.programa17.bitcoin.arb;

import com.programa17.bitcoin.excel.model.OrderBookUtils;
import com.xeiam.xchange.OnlineTest;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/1/13
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Category(OnlineTest.class)
public class OrderBookUtilTest {


    @Test
    public void testGetPriceForAmount() throws Exception {
        ArrayList<LimitOrder> limitOrders = new ArrayList<LimitOrder>();
        limitOrders.add(new LimitOrder(Order.OrderType.ASK, BigDecimal.valueOf(0.1), "BTC", "USD", "123", new Date(), BigMoney.of(CurrencyUnit.USD, BigDecimal.valueOf(1050))));
        limitOrders.add(new LimitOrder(Order.OrderType.ASK, BigDecimal.valueOf(0.2), "BTC", "USD", "123", new Date(), BigMoney.of(CurrencyUnit.USD, BigDecimal.valueOf(1054))));
        float priceForAmount = OrderBookUtils.getPriceForAmount(limitOrders, 0.05f);
        Assert.assertEquals(1050,priceForAmount,0.1);
        priceForAmount = OrderBookUtils.getPriceForAmount(limitOrders, 0.2f);
        Assert.assertEquals(1052,priceForAmount,0.1);

    }
}
