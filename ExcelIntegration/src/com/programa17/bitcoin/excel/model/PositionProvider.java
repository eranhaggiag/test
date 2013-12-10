package com.programa17.bitcoin.excel.model;

import com.xeiam.xchange.Exchange;

/**
 * Created with IntelliJ IDEA.
 * User: Eran
 * Date: 12/2/13
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PositionProvider {
    Float getPosition(String exchangeName, String currency);
}
