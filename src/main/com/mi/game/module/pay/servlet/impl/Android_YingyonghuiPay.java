package com.mi.game.module.pay.servlet.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.core.util.ConfigUtil;
import com.mi.game.module.base.bean.init.server.ServerInfoData;
import com.mi.game.module.pay.PayModule;
import com.mi.game.module.pay.define.PlatFromConstants;
import com.mi.game.module.pay.pojo.PayOrderEntity;
import com.mi.game.module.pay.servlet.BasePay;
import com.mi.game.util.HaiMaBase64;
import com.mi.game.util.Logs;
import com.mi.game.util.RSAEncrypt;
import com.mi.game.util.Utilities;

/**
 * 应用汇支付成功callback
 * 
 * 调试未通过
 * 
 */
public class Android_YingyonghuiPay extends BasePay {

	public static final String APP_KEY = ConfigUtil.getString("yyh.cpKey");
	public static final String APP_ID = ConfigUtil.getString("yyh.appId");

	public static void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String resultMsg = "SUCCESS";
		try {
			// 请求参数
			Map<String, String> requestParams = getRequestMap(request);
			// 业务数据
			String transdata = requestParams.get("transdata");
			// 签名参数
			String signature = requestParams.get("sign");
			Logs.pay.error("应用汇平台callback数据：" + requestParams);
			Logs.pay.error("应用汇平台callback签名(signature)：" + signature);
			// 签名验证
			if (!validSign(transdata, signature, APP_KEY)) {
				Logs.pay.error("订单：签名验证错误");
				writerResult(response, "FAILURE");
				return;
			}

			JSONObject jsonData = JSON.parseObject(transdata);
			// 游戏合作商自定义参数
			String callbackInfo = jsonData.getString("cpprivate");
			// 如果是支付服务器,则分发回调请求
			if (PAYCENTER) {
				// 透传信息
				String cpUserInfo = callbackInfo;
				if (StringUtils.isEmpty(cpUserInfo)) {
					Logs.pay.error("未找到平台透传消息,分发支付回调请求失败!");
					writerResult(response, "0");
					return;
				}
				String serverID = cpUserInfo.split("-")[0];
				ServerInfoData serverInfo = PayModule.serverListMap.get(serverID);
				String address = serverInfo.getUrl();
				Map<String, String> params = new HashMap<String, String>();
				Enumeration<?> names = request.getParameterNames();
				while (names.hasMoreElements()) {
					String name = (String) names.nextElement();
					params.put(name, request.getParameter(name));
				}
				String result = sendRequest(address, params);
				response.getWriter().write(result);
				return;
			}

			// 充值订单号
			String orderId = jsonData.getString("exorderno");

			// 交易结果
			String orderStatus = jsonData.getString("result");

			if (!"0".equals(orderStatus)) {
				Logs.pay.error("订单：" + orderId + " 支付结果不为0");
				writerResult(response, resultMsg);
				return;
			}

			PayOrderEntity orderEntity = payModule.getPayOrderEntity(orderId);

			if (orderEntity == null) {
				Logs.pay.error("订单：" + orderId + "未找到");
				writerResult(response, resultMsg);
				return;
			}

			// 订单状态不是初始状态
			if (orderEntity.getState() != 0) {
				Logs.pay.error("订单：" + orderId + " 不是初始状态");
				writerResult(response, resultMsg);
				return;
			}

			// 订单初始状态
			if (orderEntity.getState() == 0) {
				Logs.pay.error("订单：" + orderId + "支付完成，开始添加玩家元宝");
				orderEntity.setState(1);
				orderEntity.setCallbackTime(System.currentTimeMillis());
				orderEntity.setPayPlatForm(PlatFromConstants.PLATFORM_YYH);
				payModule.savePayOrderEntity(orderEntity);
				// 处理充值
				payModule.payGold(orderEntity.getPlayerID(), orderEntity, null, PlatFromConstants.PLATFORM_YYH, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resultMsg = "FAILURE";
		}
		writerResult(response, resultMsg);
	}

	/**
	 * 
	 * @param transdata
	 *            同步过来的transdata数据
	 * @param sign
	 *            同步过来的sign数据
	 * @param key
	 *            应用的密钥(商户可从商户自服务系统获取)
	 * @return 验证签名结果 true:验证通过 false:验证失败
	 */
	public static boolean validSign(String transdata, String sign, String key) {
		try {
			String md5Str = Utilities.encrypt(transdata);
			String decodeBaseStr = HaiMaBase64.decode(key);
			String[] decodeBaseVec = decodeBaseStr.replace('+', '#').split("#");
			String privateKey = decodeBaseVec[0];
			String modkey = decodeBaseVec[1];
			String reqMd5 = RSAEncrypt.decrypt(sign, new BigInteger(privateKey), new BigInteger(modkey));
			if (md5Str.equals(reqMd5)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void writerResult(HttpServletResponse response, String resultMsg) throws IOException {
		Logs.pay.error("返回平台数据：" + resultMsg);
		response.getWriter().write(resultMsg);
	}

	public static void main(String[] args) {
		String sign = "5f208943bc44e8915e4d53a85c37e9ce 827466af10f027be516120b418983cd8 79147bce575e721c96c9e84d14c7d142";
		String transdata = "{\"exorderno\":\"12345\",\"transid\":\"01515012020465753356\",\"waresid\":1,\"appid\":\"500002386\",\"feetype\":2,\"money\":600,\"count\":1,\"result\":0,\"transtype\":0,\"transtime\":\"2015-01-20 20:47:20\",\"cpprivate\":\"1-12345\",\"paytype\":401}";
		String key = "Nzk1NzdBQzU3RDJDQ0I0QjIzMDU0Mjc5NUMyMDA5OTU2N0FCMEI1Mk1UQXdOVFl6T0RBek9UUXdOREkzTkRnek1ETXJNVGM1TVRVeE5qTTVNRGM0TURFMU1EVTRNRGMwT0RBNU5ETXlOekF3TmpjeU5EYzBORGd4";
		validSign(transdata, sign, key);

	}

}
