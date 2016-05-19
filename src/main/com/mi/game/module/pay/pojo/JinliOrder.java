package com.mi.game.module.pay.pojo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.alibaba.fastjson.JSONObject;
import com.mi.game.util.RSAEncrypt;

public class JinliOrder {
	// 商户订单号
	private String outOrderNo;
	// 玩家playerId
	private String playerId;
	// 商品名称
	private String subject;
	// 商户apiKey
	private String apiKey;
	// 需支付金额，dealPrice值需要等于totalFee
	private BigDecimal totalFee;
	// 商户总金额，dealPrice值需要等于totalFee
	private BigDecimal dealPrice;
	// 订单提交时间，格式为yyyyMMddHHmmss，由商户服务器提供，客户端调起支付收银台时需要使用这个值
	private String submitTime;
	// 订单失效时间，可选，格式为yyyyMMddHHmmss，如果有值必须参与签名
	private String expireTime;
	// 服务器通知地址，可选，不能超过1024个字符，如果有该字段，必须参与签名
	private String notifyURL;

	// 商户自定义订单号
	private String orderID;

	/**
	 * @param outOrderNo
	 *            商户订单号 [必填]
	 * @param playerId
	 *            玩家playerId [必填]
	 * @param subject
	 *            商品名称 [必填]
	 * @param apiKey
	 *            商户apiKey [必填]
	 * @param totalFee
	 *            需支付金额，dealPrice值需要等于totalFee [必填]
	 * @param dealPrice
	 *            商户总金额，dealPrice值需要等于totalFee [必填]
	 * @param submitTime
	 *            订单提交时间，格式为yyyyMMddHHmmss，由商户服务器提供，客户端调起支付收银台时需要使用这个值 [必填]
	 * @param expireTime
	 *            订单失效时间，可选，格式为yyyyMMddHHmmss，如果有值必须参与签名 [可选]
	 * @param notifyURL
	 *            服务器通知地址，不能超过1024个字符，如果有该字段，必须参与签名(如果商户需要自定义参数，可以在创建订单时以
	 *            "http://www.partner.com/notifyReceiver?param1=value1&param2=value2"
	 *            的形式定义url) [可选]
	 */
	public JinliOrder(String outOrderNo, String playerId, String subject, String apiKey, BigDecimal totalFee, BigDecimal dealPrice, Date submitTime, Date expireTime,
			String notifyURL) {
		this.outOrderNo = outOrderNo;
		this.playerId = playerId;
		this.subject = subject;
		this.apiKey = apiKey;
		this.totalFee = totalFee;
		this.dealPrice = dealPrice;
		this.submitTime = toString(submitTime);
		this.expireTime = toString(expireTime);
		this.notifyURL = notifyURL;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getSubject() {
		return subject;
	}

	public String getApiKey() {
		return apiKey;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public static Timestamp toTimestamp(String submitTime) {
		submitTime = new StringBuilder().append(
				submitTime.substring(0, 4) + "-" + submitTime.substring(4, 6) + "-" + submitTime.substring(6, 8) + " " + submitTime.substring(8, 10) + ":"
						+ submitTime.substring(10, 12) + ":" + submitTime.substring(12, 14)).toString();
		return Timestamp.valueOf(submitTime);
	}

	public static String toString(Date date) {
		return toString(date, "yyyyMMddHHmmss");
	}

	public static String toString(Date date, String format) {
		if (date == null || StringUtils.isBlank(format)) {
			return "";
		}
		return FastDateFormat.getInstance(format).format(date);
	}

	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderNo", outOrderNo);
		result.put("submitTime", submitTime);
		result.put("orderID", orderID);
		return result;
	}

	public static String wrapCreateOrder(JinliOrder order, String privateKey, String deliverType) throws Exception {
		JSONObject jsonReq = new JSONObject();
		String expireTime = order.getExpireTime();
		String notifyURL = order.getNotifyURL();

		StringBuilder signContent = new StringBuilder();
		signContent.append(order.getApiKey());
		jsonReq.put("api_key", order.getApiKey());

		signContent.append(order.getDealPrice());
		jsonReq.put("deal_price", order.getDealPrice().toString());
		signContent.append(deliverType);
		jsonReq.put("deliver_type", deliverType);

		if (!StringUtils.isBlank(expireTime)) {
			signContent.append(expireTime);
			jsonReq.put("expire_time", expireTime);
		}

		if (!StringUtils.isBlank(notifyURL)) {
			signContent.append(notifyURL);
			jsonReq.put("notify_url", notifyURL);
		}
		signContent.append(order.getOutOrderNo());
		jsonReq.put("out_order_no", order.getOutOrderNo());
		signContent.append(order.getSubject());
		jsonReq.put("subject", order.getSubject());
		signContent.append(order.getSubmitTime());
		jsonReq.put("submit_time", order.getSubmitTime());
		signContent.append(order.getTotalFee());
		jsonReq.put("total_fee", order.getTotalFee().toString());
		String sign = RSAEncrypt.sign(signContent.toString(), privateKey);
		jsonReq.put("sign", sign);
		// player_id不参与签名
		jsonReq.put("player_id", order.getPlayerId());
		return jsonReq.toJSONString();
	}

}
