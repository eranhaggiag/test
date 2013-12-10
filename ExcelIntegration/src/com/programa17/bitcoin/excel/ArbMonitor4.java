package com.programa17.bitcoin.excel;

import com.programa17.bitcoin.excel.model.OrderBookUtils;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.xeiam.xchange.service.polling.PollingTradeService;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.joda.time.DateTime;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/1/13
 * Time: 8:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArbMonitor4 {

    public static void main(String[] args) throws InterruptedException, IOException {
        PollingMarketDataService btce = DataExcelProvider.getPollingMarketDataService("BTCE");
        PollingMarketDataService okcoin = DataExcelProvider.getPollingMarketDataService("OKCOIN");

        Exchange btceExchange = DataExcelProvider.getExchange("BTCE");
        Exchange okCoinExchange= DataExcelProvider.getExchange("OKCOIN");

        PollingTradeService btceExchangePollingTradeService = btceExchange.getPollingTradeService();
        PollingTradeService okCoinExchangePollingTradeService = okCoinExchange.getPollingTradeService();

        FileWriter fileWriter = new FileWriter("c:/temp/arb" + new Date().getTime() + ".txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        boolean sendOrder = false;
        while (true) {
            try {
                OrderBook btcltc= btce.getOrderBook("LTC", "BTC");

                OrderBook btccny = okcoin.getOrderBook("BTC", "CNY");
                OrderBook ltccny= okcoin.getOrderBook("LTC", "CNY");
                System.out.print("");
                float btcAmount = 0.1f;
                float ltcAmount = 2;
                float commissionPercent=0.002f;
                float expectedBuyProfit = 0.004f;
                float expectedSellProfit = 0.002f;
                boolean isValid=true;

                float ltccnyBid = OrderBookUtils.getPriceForAmount(ltccny.getBids(), ltcAmount);
                float ltccnyAsk = OrderBookUtils.getPriceForAmount(ltccny.getAsks(), ltcAmount);
                float btccnyBid = OrderBookUtils.getPriceForAmount(btccny.getBids(), btcAmount);
                float btccnyAsk = OrderBookUtils.getPriceForAmount(btccny.getAsks(), btcAmount);

                float okLTCBid = ltccnyBid / btccnyAsk;
                float okLTCAsk = ltccnyAsk / btccnyBid;


                float btceBid = btcltc.getBids().get(0).getLimitPrice().getAmount().floatValue();
                float btceAsk = btcltc.getAsks().get(0).getLimitPrice().getAmount().floatValue();
                float buyBTCeProfit = (okLTCBid - btceAsk)/okLTCBid-commissionPercent;
                float sellBTCeProfit = (btceBid - okLTCAsk)/okLTCBid-commissionPercent;

                if((ltccnyAsk - ltccnyBid<0)||(btccnyAsk - btccnyBid<0)||(btceAsk - btceBid<0) )   {
                    isValid=false;
                }


                boolean shoudBuyBTCe = buyBTCeProfit >= expectedBuyProfit&&isValid;
                boolean shouldSellBTCe = sellBTCeProfit >= expectedSellProfit&& isValid;
                if ((shoudBuyBTCe || shouldSellBTCe)) {
                    System.out.println();
                    String x = DateTime.now().toString() + "," + buyBTCeProfit * 100 + ", " + sellBTCeProfit * 100 + "," + btceBid + "," + btceAsk + "," + okLTCBid + "," + okLTCAsk + "," + ltccnyBid + "," + ltccnyAsk + "," + btccnyBid + "," + btccnyAsk;
                    System.out.println(x);
                    bufferedWriter.write(x);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }



                float ltcAmountToExecute = 1;
                float btcAmountToExecute;
                if (shouldSellBTCe && sendOrder) {
                    btcAmountToExecute = ltcAmountToExecute*okLTCAsk; // we sell ltc at btce so we buy ltc at okcoin.
                    String s = btceExchangePollingTradeService.placeLimitOrder(new LimitOrder(Order.OrderType.ASK, new BigDecimal(ltcAmountToExecute).round(new MathContext(6)), "LTC", "BTC", "", null, BigMoney.of(CurrencyUnit.USD, new BigDecimal(btceBid).round(new MathContext(4)))));
                    String s1 = okCoinExchangePollingTradeService.placeLimitOrder(new LimitOrder(Order.OrderType.BID, new BigDecimal(ltcAmountToExecute).round(new MathContext(6)), "LTC", "CNY", "", null, BigMoney.of(CurrencyUnit.USD, new BigDecimal(ltccnyAsk).round(new MathContext(4)))));
                    String s2 = okCoinExchangePollingTradeService.placeLimitOrder(new LimitOrder(Order.OrderType.ASK, new BigDecimal(btcAmountToExecute).round(new MathContext(6)), "BTC", "CNY", "", null, BigMoney.of(CurrencyUnit.USD, new BigDecimal(btccnyBid).round(new MathContext(4)))));
                    System.out.println("s = " + s);
                    System.out.println("s1 = " + s1);
                    System.out.println("s2 = " + s2);
                    sendOrder =false;
                    SoundUtils.beep();
                }else if (shoudBuyBTCe && sendOrder) {
                    btcAmountToExecute = ltcAmountToExecute*okLTCBid; // we buy ltc at btce so we sell ltc at okcoin.
                    String s = btceExchangePollingTradeService.placeLimitOrder(new LimitOrder(Order.OrderType.BID, new BigDecimal(ltcAmountToExecute).round(new MathContext(6)), "LTC", "BTC", "", null, BigMoney.of(CurrencyUnit.USD, new BigDecimal(btceAsk).round(new MathContext(4)))));
                    String s1 = okCoinExchangePollingTradeService.placeLimitOrder(new LimitOrder(Order.OrderType.ASK, new BigDecimal(ltcAmountToExecute).round(new MathContext(6)), "LTC", "CNY", "", null, BigMoney.of(CurrencyUnit.USD, new BigDecimal(ltccnyBid).round(new MathContext(4)))));
                    String s2 = okCoinExchangePollingTradeService.placeLimitOrder(new LimitOrder(Order.OrderType.BID, new BigDecimal(btcAmountToExecute).round(new MathContext(6)), "BTC", "CNY", "", null, BigMoney.of(CurrencyUnit.USD, new BigDecimal(btccnyAsk).round(new MathContext(4)))));
                    System.out.println("s = " + s);
                    System.out.println("s1 = " + s1);
                    System.out.println("s2 = " + s2);
                    sendOrder =false;
                    SoundUtils.beep();

                }


            } catch (ExchangeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotAvailableFromExchangeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotYetImplementedForExchangeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
//            Thread.sleep(3000);
        }
    }
}
