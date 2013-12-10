package com.programa17.bitcoin.excel;

import com.programa17.bitcoin.excel.model.OrderBookUtils;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/1/13
 * Time: 8:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArbMonitor2 {

    public static void main(String[] args) throws InterruptedException, IOException {
        PollingMarketDataService btce = DataExcelProvider.getPollingMarketDataService("BTCE");
        PollingMarketDataService okcoin = DataExcelProvider.getPollingMarketDataService("OKCOIN");

        while (true) {
            try {
                OrderBook btcltc= btce.getOrderBook("LTC", "BTC");
                OrderBook btccny = okcoin.getOrderBook("BTC", "CNY");
                OrderBook ltccny= okcoin.getOrderBook("LTC", "CNY");

                float btcAmount = 3;
                float ltcAmount = 90;
                float commissionPercent=0.005f;
                float ltccnyBid = OrderBookUtils.getPriceForAmount(ltccny.getBids(), ltcAmount);
                float ltccnyAsk = OrderBookUtils.getPriceForAmount(ltccny.getAsks(), ltcAmount);
                float btccnyBid = OrderBookUtils.getPriceForAmount(btccny.getBids(), btcAmount);
                float btccnyAsk = OrderBookUtils.getPriceForAmount(btccny.getAsks(), btcAmount);
                float okLTCBid = ltccnyBid / btccnyAsk;
                float okLTCAsk = ltccnyAsk / btccnyBid;
//            float okLTCAsk = ltccny.getAsk().getAmount().divide(btccny.getBid().getAmount()).floatValue();

                float btceBid = btcltc.getBids().get(0).getLimitPrice().getAmount().floatValue();
                float btceAsk = btcltc.getAsks().get(0).getLimitPrice().getAmount().floatValue();
                float buyBtcProfit = (okLTCBid - btceAsk)/okLTCBid-commissionPercent;
                float sellBtcProfit = (btceBid - okLTCAsk)/okLTCBid-commissionPercent;

//            System.out.println("btceAsk = " + btceAsk);
//            System.out.println("okLTCBid = " + okLTCBid);

                System.out.println(DateTime.now().toString() + ","+buyBtcProfit*100 + ", " + sellBtcProfit*100 + "," + btceBid + "," + btceAsk + "," + okLTCBid + "," + okLTCAsk + "," + ltccnyBid + "," + ltccnyAsk + "," + btccnyBid + "," +  btccnyAsk);
            } catch (ExchangeException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotAvailableFromExchangeException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotYetImplementedForExchangeException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Thread.sleep(3000);
        }
    }
}
