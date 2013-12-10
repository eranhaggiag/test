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
public class ArbMonitor3 {

    public static void main(String[] args) throws InterruptedException, IOException {
        PollingMarketDataService btcTrade= DataExcelProvider.getPollingMarketDataService("BTCTrade");
        PollingMarketDataService okcoin = DataExcelProvider.getPollingMarketDataService("OKCOIN");

        while (true) {
            try {
                OrderBook btctradeLTC= btcTrade.getOrderBook("LTC", "CNY");
                OrderBook btctradeBTC= btcTrade.getOrderBook("BTC", "CNY");
                OrderBook btccny = okcoin.getOrderBook("BTC", "CNY");
                OrderBook ltccny= okcoin.getOrderBook("LTC", "CNY");
                System.out.print("");
                float btcAmount = 3;
                float ltcAmount = 90;
                float commissionPercent=0;
                float expectedProfit = 0.01f;
                boolean isValid=true;

                float ltccnyBid = OrderBookUtils.getPriceForAmount(ltccny.getBids(), ltcAmount);
                float ltccnyAsk = OrderBookUtils.getPriceForAmount(ltccny.getAsks(), ltcAmount);
                float btccnyBid = OrderBookUtils.getPriceForAmount(btccny.getBids(), btcAmount);
                float btccnyAsk = OrderBookUtils.getPriceForAmount(btccny.getAsks(), btcAmount);
                float btctradeLTCBid = OrderBookUtils.getPriceForAmount(btctradeLTC.getBids(), ltcAmount);
                float btctradeLTCAsk = OrderBookUtils.getPriceForAmount(btctradeLTC.getAsks(), ltcAmount);
                float btctradeBTCBid = OrderBookUtils.getPriceForAmount(btctradeBTC.getBids(), btcAmount);
                float btctradeBTCAsk  = OrderBookUtils.getPriceForAmount(btctradeBTC.getAsks(), btcAmount);
                float okLTCBid = ltccnyBid / btccnyAsk;
                float okLTCAsk = ltccnyAsk / btccnyBid;
                float btcTradeBid = btctradeLTCBid/ btctradeBTCAsk;
                float btcTradeAsk = btctradeLTCAsk/ btctradeBTCBid;

                float buyBtcProfit = (okLTCBid - btcTradeAsk)/okLTCBid-commissionPercent;
                float sellBtcProfit = (btcTradeBid- okLTCAsk)/okLTCBid-commissionPercent;

                if((ltccnyAsk - ltccnyBid<0)||(btccnyAsk - btccnyBid<0)||(btctradeLTCAsk - btctradeLTCBid<0) || (btccnyAsk - btccnyBid<0) )   {
                    isValid=false;
                }

                if ((buyBtcProfit>= expectedProfit || sellBtcProfit> expectedProfit) && isValid) {
                    System.out.println();
                    System.out.println(DateTime.now().toString() + ","+buyBtcProfit*100 + ", " + sellBtcProfit*100 + "," + btcTradeBid + "," + btcTradeAsk + "," + okLTCBid + "," + okLTCAsk + "," + ltccnyBid + "," + ltccnyAsk + "," + btccnyBid + "," +  btccnyAsk+ "," + btctradeLTCBid + "," + btctradeLTCAsk + "," + btctradeBTCBid + "," +  btctradeBTCAsk);
                }
            } catch (ExchangeException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotAvailableFromExchangeException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NotYetImplementedForExchangeException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
//            Thread.sleep(3000);
        }
    }
}
