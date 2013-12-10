package com.xeiam.xchange.okcoin.service;

import com.xeiam.xchange.okcoin.OKCoinUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/6/13
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class OKCoinTradeApi {

    private  HttpsURLConnection sellBtcConn = null;
    private HttpsURLConnection buyBtcConn = null;
    private final String USER_AGENT = "Mozilla/5.0";

    public OKCoinTradeApi() {

        try {
            String s;
            URL url;
            s = "https://www.okcoin.com/trade/sellBtcSubmit.do?random=" + OKCoinUtils.getRandom();
            url = new URL(s);
            sellBtcConn = (HttpsURLConnection) url.openConnection();

            s = "https://www.okcoin.com/trade/buyBtcSubmit.do?random=" + OKCoinUtils.getRandom();
            url = new URL(s);
            buyBtcConn = (HttpsURLConnection) url.openConnection();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean buySell(String mainCurrency, float price, float amount,boolean isBuy) {
        HttpsURLConnection connection;
        if (!isBuy) {
            connection = sellBtcConn;
        } else {
            connection = buyBtcConn;
        }
        int symbol=-1;
        if(mainCurrency.equals("BTC")) {
            symbol=0;
        } else  if(mainCurrency.equals("LTC")) {
            symbol=1  ;
        }
        if (symbol>=0) {
            try {
                doAction(connection,price,amount,symbol);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return false;
            }
        } else {
            System.out.println("Invalid currency" + mainCurrency); //TODO replace with log or return result
            return false;
        }

        return true;

    }

    private StringBuffer doAction(HttpsURLConnection con, float price, float amount,int symbol) throws IOException {

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String s = "JSESSIONID=CF873A6928E729B0A6676B7D659D6538; coin_session_id_o=1936f381-8825-41ad-a73c-c7150bd0559cokcoin; coin_session_nikename=eran; Hm_lvt_45e8f68df9bb8a9fc29ce78c80080330=1385999085,1386058568,1386088336,1386337273; Hm_lpvt_45e8f68df9bb8a9fc29ce78c80080330=1386337449";
        con.setRequestProperty("Cookie", s);
//        con.setRequestProperty("tradeAmount", "1");
//        con.setRequestProperty("symbol", "1");
//        con.setRequestProperty("tradeCnyPrice", "300");
//        con.setRequestProperty("tradePwd", "asdfgh");

        String urlParameters = "tradeAmount=" + amount + "&tradeCnyPrice=" + price + "&tradePwd=asdfgh&symbol=" + symbol;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("response = " + response.toString());
        return response;
    }

    public static void main(String[] args) {
        OKCoinTradeApi okCoinTradeApi = new OKCoinTradeApi();
        okCoinTradeApi.sell("LTC", 1000, 1);
    }

    public boolean sell(String symbol, float price, float amount) {
        return buySell(symbol, price, amount, false);
    }
    public boolean buy(String symbol, float price, float amount) {
        return buySell(symbol, price, amount, true);
    }
}
