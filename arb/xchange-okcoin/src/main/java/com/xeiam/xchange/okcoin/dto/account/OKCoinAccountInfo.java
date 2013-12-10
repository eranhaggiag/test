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
package com.xeiam.xchange.okcoin.dto.account;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xeiam.xchange.okcoin.dto.OKCoinValue;

/**
 * @author David Yam
 */
public class OKCoinAccountInfo {

  private final OKCoinProfile profile;
  private final Map<String, OKCoinValue> balances;
  private final Map<String, OKCoinValue> frozens;

  /**
   * Constructor
   * 
   * @param profile account profile
   * @param balances balances for the various currencies
   * @param frozens balances for the various frozen currencies
   */
  // todo: document frozens
  public OKCoinAccountInfo(@JsonProperty("profile") OKCoinProfile profile, @JsonProperty("balance") Map<String, OKCoinValue> balances, @JsonProperty("frozen") Map<String, OKCoinValue> frozens) {

    this.profile = profile;
    this.balances = balances;
    this.frozens = frozens;
  }

  /**
   * Get the associated profile.
   * 
   * @return the profile
   */
  public OKCoinProfile getProfile() {

    return profile;
  }

  /**
   * Get the balances.
   * 
   * @return the balances
   */
  public Map<String, OKCoinValue> getBalances() {

    return balances;
  }

  /**
   * Get the frozen balances.
   * 
   * @return the frozen balances
   */
  // todo: as above - document 'frozen'
  public Map<String, OKCoinValue> getFrozens() {

    return frozens;
  }

  @Override
  public String toString() {

    return String.format("OKCoinAccountInfo{profile=%s, balances=%s, frozens=%s}", profile, balances, frozens);
  }

}
