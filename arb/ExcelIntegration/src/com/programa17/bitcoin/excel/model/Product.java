package com.programa17.bitcoin.excel.model;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/2/13
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Product {
    String firstCurrency;
    String secondCurrency;
    String exchangeName;
    float amount;
    float bidPrice;
    float askPrice;


    public Product(String firstCurrency, String secondCurrency, String exchangeName) {
        this.firstCurrency = firstCurrency;
        this.secondCurrency = secondCurrency;
        this.exchangeName = exchangeName;
    }

    public String getFirstCurrency() {
        return firstCurrency;
    }

    public String getSecondCurrency() {
        return secondCurrency;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public float getAmount() {
        return amount;
    }

    public float getBidPrice() {
        return bidPrice;
    }

    public float getAskPrice() {
        return askPrice;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setBidPrice(float bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setAskPrice(float askPrice) {
        this.askPrice = askPrice;
    }
}
