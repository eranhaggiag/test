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
package com.xeiam.xchange.okcoin;

import com.xeiam.xchange.okcoin.dto.account.request.OKCoinGetAccountInfoRequest;
import com.xeiam.xchange.okcoin.dto.account.request.OKCoinRequestWithdrawalRequest;
import com.xeiam.xchange.okcoin.dto.account.response.OKCoinGetAccountInfoResponse;
import com.xeiam.xchange.okcoin.dto.account.response.OKCoinRequestWithdrawalResponse;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinMarketDepth;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTicker;
import com.xeiam.xchange.okcoin.dto.marketdata.OKCoinTrade;
import com.xeiam.xchange.okcoin.dto.trade.request.OKCoinBuyOrderRequest;
import com.xeiam.xchange.okcoin.dto.trade.request.OKCoinCancelOrderRequest;
import com.xeiam.xchange.okcoin.dto.trade.request.OKCoinGetOrdersRequest;
import com.xeiam.xchange.okcoin.dto.trade.request.OKCoinSellOrderRequest;
import com.xeiam.xchange.okcoin.dto.trade.response.OKCoinBooleanResponse;
import com.xeiam.xchange.okcoin.dto.trade.response.OKCoinGetOrdersResponse;
import si.mazi.rescu.ParamsDigest;

import javax.ws.rs.*;
import java.io.IOException;

@Path("/")
public interface OKCoin {

  @GET
  @Path("api/ticker.do?symbol={first}_{second}")
  @Produces("application/json")
  public OKCoinTicker getTicker(@PathParam("first") String first, @PathParam("second") String second) throws IOException;

  @GET
  @Path("api/depth.do?symbol={first}_{second}")
  @Produces("application/json")
  public OKCoinMarketDepth getFullDepth(@PathParam("first") String first, @PathParam("second") String second) throws IOException;

  // (return last 100 trade records.)
  @GET
  @Path("api/trades.do")
  @Produces("application/json")
  public OKCoinTrade[] getTrades() throws IOException;

  // return 100 trade records starting from id $since.
  @GET
  @Path("api/trades.do")
  @Produces("application/json")
  public OKCoinTrade[] getTrades(@QueryParam("since") int time) throws IOException;

  @POST
  @Path("api_trade_v1.php")
  @Consumes("application/json")
  @Produces("application/json")
  public OKCoinGetAccountInfoResponse getAccountInfo(@HeaderParam("Authorization") ParamsDigest authorization, @HeaderParam("Json-Rpc-Tonce") long jsonRpcTonce,
      OKCoinGetAccountInfoRequest getAccountInfoRequest) throws IOException;

  @POST
  @Path("api_trade_v1.php")
  @Consumes("application/json")
  @Produces("application/json")
  public OKCoinRequestWithdrawalResponse requestWithdrawal(@HeaderParam("Authorization") ParamsDigest authorization, @HeaderParam("Json-Rpc-Tonce") long jsonRpcTonce,
      OKCoinRequestWithdrawalRequest requestWithdrawalRequest) throws IOException;

  @POST
  @Path("api_trade_v1.php")
  @Consumes("application/json")
  @Produces("application/json")
  public OKCoinGetOrdersResponse getOrders(@HeaderParam("Authorization") ParamsDigest authorization, @HeaderParam("Json-Rpc-Tonce") long jsonRpcTonce, OKCoinGetOrdersRequest getOrdersRequest)
      throws IOException;

  @POST
  @Path("api_trade_v1.php")
  @Consumes("application/json")
  @Produces("application/json")
  public OKCoinBooleanResponse cancelOrder(@HeaderParam("Authorization") ParamsDigest authorization, @HeaderParam("Json-Rpc-Tonce") long jsonRpcTonce, OKCoinCancelOrderRequest cancelOrderRequest)
      throws IOException;

  @POST
  @Path("api_trade_v1.php")
  @Consumes("application/json")
  @Produces("application/json")
  public OKCoinBooleanResponse buyOrder(@HeaderParam("Authorization") ParamsDigest authorization, @HeaderParam("Json-Rpc-Tonce") long jsonRpcTonce, OKCoinBuyOrderRequest buyOrderRequest)
      throws IOException;

  @POST
  @Path("api_trade_v1.php")
  @Consumes("application/json")
  @Produces("application/json")
  public OKCoinBooleanResponse sellOrder(@HeaderParam("Authorization") ParamsDigest authorization, @HeaderParam("Json-Rpc-Tonce") long jsonRpcTonce, OKCoinSellOrderRequest sellOrderRequest)
      throws IOException;

}
