/**
 * Copyright (C) 2012 - 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.okcoin.service.polling;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.okcoin.OKCoin;
import com.xeiam.xchange.okcoin.service.OKCoinDigest;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;
import com.xeiam.xchange.service.polling.PollingAccountService;
import com.xeiam.xchange.utils.Assert;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * <p>
 * Implementation of the market data service for BTCChina
 * </p>
 * <ul>
 * <li>Provides access to various market data values</li>
 * </ul>
 */
public class OKCoinPollingAccountService extends BasePollingExchangeService implements PollingAccountService {

  /**
   * Configured from the super class reading of the exchange specification
   */
  private final OKCoin okCoin;
  private ParamsDigest signatureCreator;

  /**
   * Constructor
   * 
   * @param exchangeSpecification The {@link ExchangeSpecification}
   */
  public OKCoinPollingAccountService(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);

    Assert.notNull(exchangeSpecification.getSslUri(), "Exchange specification URI cannot be null");
    this.okCoin= RestProxyFactory.createProxy(OKCoin.class, exchangeSpecification.getSslUri());
    signatureCreator = OKCoinDigest.createInstance(exchangeSpecification.getApiKey(), exchangeSpecification.getSecretKey());
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {

/*
    BTCChinaResponse<BTCChinaAccountInfo> response = okCoin.getAccountInfo(signatureCreator, BTCChinaUtils.getRandom(), new BTCChinaGetAccountInfoRequest());
    return BTCChinaAdapters.adaptAccountInfo(response);
*/
      return null;

  }

  @Override
  public String withdrawFunds(BigDecimal amount, String address) throws IOException {

/*
    BTCChinaResponse<BTCChinaID> response = btcchina.requestWithdrawal(signatureCreator, BTCChinaUtils.getRandom(), new BTCChinaRequestWithdrawalRequest(CurrencyUnit.of("BTC"), amount));
    return response.getResult().getId();
*/
      return "";

  }

  @Override
  public String requestBitcoinDepositAddress(String... arguments) throws IOException {

/*
    BTCChinaResponse<BTCChinaAccountInfo> response = btcchina.getAccountInfo(signatureCreator, BTCChinaUtils.getRandom(), new BTCChinaGetAccountInfoRequest());

    return response.getResult().getProfile().getBtcDepositAddress();

*/
      return "";
  }

}
