package com.programa17.bitcoin.excel.model;

import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/1/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderBookUtils {

    public static float getPriceForAmount(List<LimitOrder> orders, float amount) {
        float amountFound = 0;
        int i=0;
        float price =0;
        while(amountFound<amount) {
            LimitOrder limitOrder = orders.get(i);
            float tradableAmount = limitOrder.getTradableAmount().floatValue();
            if(amountFound+tradableAmount>amount) {
                tradableAmount = amount-amountFound;
            }
            price+=tradableAmount/amount*limitOrder.getLimitPrice().getAmount().floatValue();
            amountFound +=tradableAmount;
            i++;
        }

        return price;

    }
}
