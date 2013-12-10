package com.programa17.bitcoin.excel.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }

    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "http://www.btcclubs.com/btc_sum.js?t=6208495";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        con.setRequestProperty("Cookie", "wafenterurl=/; wafcookie=a4a5d66e18a5ff043cc918ae7baa895d; __utma=255594459.83845952.1386058914.1386058914.1386058914.1; __utmc=255594459; __utmz=255594459.1386058914.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); wafverify=3201bdbfe368081ad6a925500f648ca7; IESESSION=alive; pgv_pvi=4082541568; pgv_si=s2862679040; USER_PW=1dd1bb7a5568cba4a8b89c155a1c17c3; PHPSESSID=552e4500a229094bf692228dc3c3167a; CNZZDATA5342694=cnzz_eid%3D1106160560-1386058920-http%253A%252F%252Fwww.btcclubs.com%26ntime%3D1386206045%26cnzz_a%3D5%26ltime%3D1386198554784%26rtime%3D2");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://www.okcoin.com/trade/sellBtcSubmit.do?random=64";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
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

        String urlParameters = "tradeAmount=1&tradeCnyPrice=333&tradePwd=asdfgh&symbol=1";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
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

        //print result
        System.out.println(response.toString());

    }

}