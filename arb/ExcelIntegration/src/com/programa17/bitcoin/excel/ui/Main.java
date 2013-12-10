package com.programa17.bitcoin.excel.ui;

import com.programa17.bitcoin.excel.controller.ExchangeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/1/13
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private JTextField amountCheckTf;
    private JTextField buyBtcProfit;
    private JTextField sellBtcProfit;
    private JTextField buyAt;
    private JButton resetButton;
    private JButton resetButton1;
    private JTextField sellAt;
    private JTextField buyBtcHigh;
    private JTextField sellBtcHigh;
    private JTextField btceBTCAmount;
    private JTextField btceBtcLtcBidAsk;
    private JTextField okCoinBtcCnyBidAsk;
    private JTextField okCoinLtcCnyBidAsk;
    private JTextField okCoinLtcBtcBidAsk;
    private JTextField btceLTCAmount;
    private JTextField btceUSDAmount;
    private JButton refreshButton;
    private JPanel panel;
    private JTextField amountDOTf;
    private final ExchangeController positionController;
    private MainModel model;

    public Main() {
        positionController = new ExchangeController();
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btceBTCAmount.setText(positionController.getPosition("BTCE", "BTC").toString());
                btceLTCAmount.setText(positionController.getPosition("BTCE", "LTC").toString());
                btceUSDAmount.setText(positionController.getPosition("BTCE", "USD").toString());
            }
        });
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Float bid = positionController.getBid("BTCE", "LTC", "BTC", 30);
                Float ask = positionController.getAsk("BTCE", "LTC", "BTC", 30);
                btceBtcLtcBidAsk.setText(bid + "/" + ask);
            }
        }, 0,1);
    }

    public void setData() {
        btceBTCAmount.setText(model.getBtceBTCAmount());
    }

    public void getData() {
        model.setBtceBTCAmount(btceBTCAmount.getText());
    }

    public boolean isModified(MainModel data) {
        if (btceBTCAmount.getText() != null ? !btceBTCAmount.getText().equals(data.getBtceBTCAmount()) : data.getBtceBTCAmount() != null)
            return true;
        return false;
    }


    public JPanel getPanel() {
        return panel;
    }

    public static void main(String[] args) {
        JFrame liteCoinArb = new JFrame("LiteCoinArb");
        Main main = new Main();
        liteCoinArb.getContentPane().add(main.getPanel());
        liteCoinArb.setSize(500,500);
        liteCoinArb.setVisible(true);
    }
}
