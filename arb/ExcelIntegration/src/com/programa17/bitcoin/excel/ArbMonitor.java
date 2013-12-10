package com.programa17.bitcoin.excel;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.dto.marketdata.Ticker;
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
public class ArbMonitor {

    public static void main(String[] args) throws InterruptedException, IOException {
        PollingMarketDataService btce = DataExcelProvider.getPollingMarketDataService("BTCE");
        PollingMarketDataService okcoin = DataExcelProvider.getPollingMarketDataService("OKCOIN");

        while (true) {
            try {
                Ticker btcltc = btce.getTicker("LTC", "BTC");
                Ticker btccny = okcoin.getTicker("BTC", "CNY");
                Ticker ltccny= okcoin.getTicker("LTC", "CNY");

                float ltccnyBid = ltccny.getBid().getAmount().floatValue();
                float ltccnyAsk = ltccny.getAsk().getAmount().floatValue();
                float btccnyBid = btccny.getBid().getAmount().floatValue();
                float btccnyAsk = btccny.getAsk().getAmount().floatValue();
                float okLTCBid = ltccnyBid / btccnyAsk;
                float okLTCAsk = ltccnyAsk / btccnyBid;
//            float okLTCAsk = ltccny.getAsk().getAmount().divide(btccny.getBid().getAmount()).floatValue();

                float btcBid = btcltc.getBid().getAmount().floatValue();
                float btcAsk = btcltc.getAsk().getAmount().floatValue();
                float buyBtcProfit = (okLTCBid - btcAsk)/okLTCBid;
                float sellBtcProfit = (btcBid - okLTCAsk)/okLTCBid;

//            System.out.println("btcAsk = " + btcAsk);
//            System.out.println("okLTCBid = " + okLTCBid);

                System.out.println(DateTime.now().toString() + ","+buyBtcProfit*100 + ", " + sellBtcProfit*100 + "," + btcBid + "," + btcAsk + "," + okLTCBid + "," + okLTCAsk + "," + ltccnyBid + "," + ltccnyAsk + "," + btccnyBid + "," +  btccnyAsk);
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
