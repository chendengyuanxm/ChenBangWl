package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;

/**
 * 结算汇总返回报文
 *
 *
 * @author Devin_chen
 * @date 2015年8月26日
 * 
 * @version 1.0
 */
public class SettleResp extends AbstractWlResponse{
	
	public static class SetleRespWlBody extends WlBody {
		/**
		 * 订单总数
		 */
		private String ordercnt;
		/**
		 * 妥投总数
		 */
		private String tuotoucnt ;
		/**
		 * 现金总金额
		 */
		private String cashamount;
		/**
		 * 刷卡总金额
		 */
		private String cardpayamount;
		public String getOrdercnt() {
			return ordercnt;
		}
		public void setOrdercnt(String ordercnt) {
			this.ordercnt = ordercnt;
		}
		public String getTuotoucnt() {
			return tuotoucnt;
		}
		public void setTuotoucnt(String tuotoucnt) {
			this.tuotoucnt = tuotoucnt;
		}
		public String getCashamount() {
			return cashamount;
		}
		public void setCashamount(String cashamount) {
			this.cashamount = cashamount;
		}
		public String getCardpayamount() {
			return cardpayamount;
		}
		public void setCardpayamount(String cardpayamount) {
			this.cardpayamount = cardpayamount;
		}
	}

	@Override
	public Class getBodyClass() {
		return SetleRespWlBody.class;
	}
}
