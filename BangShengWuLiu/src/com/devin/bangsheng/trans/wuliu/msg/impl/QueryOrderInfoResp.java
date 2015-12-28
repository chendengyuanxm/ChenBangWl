package com.devin.bangsheng.trans.wuliu.msg.impl;

import java.util.List;

import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;

/**
 * 查询运单信息返回报文
 *
 *
 * @author Devin_chen
 * @date 2015年8月26日
 * 
 * @version 1.0
 */
public class QueryOrderInfoResp extends AbstractWlResponse{
	
	public static class QueryOrderInfoRespWlBody extends WlBody {
		
		private List<Order> orders;
//		/**
//		 * 寄件网点编号
//		 */
//		private String netcode;
//		/**
//		 * 寄件网点名称
//		 */
//		private String netname ;
//		/**
//		 * 重量
//		 */
//		private String weight;
//		/**
//		 * 代收款金额
//		 */
//		private String cod;
//		/**
//		 * 件数
//		 */
//		private String goodscount;
//		/**
//		 * 收件地址
//		 */
//		private String address;
//		/**
//		 * 收件人
//		 */
//		private String people;
//		/**
//		 * 收件人联系电话
//		 */
//		private String peopletel;
//		/**
//		 * 快件状态
//		 */
//		private String status;
//		/**
//		 * 备注
//		 */
//		private String memo;
//		/**
//		 * 电商编号
//		 */
//		private String dssn;
//		/**
//		 * 电商名称
//		 */
//		private String dsname;
//		private String dsorderno;
//		/**
//		 * 
//		 */
//		private String sqpayway;
//		private String istopay;
//		public String getNetcode() {
//			return netcode;
//		}
//		public void setNetcode(String netcode) {
//			this.netcode = netcode;
//		}
//		public String getNetname() {
//			return netname;
//		}
//		public void setNetname(String netname) {
//			this.netname = netname;
//		}
//		public String getWeight() {
//			return weight;
//		}
//		public void setWeight(String weight) {
//			this.weight = weight;
//		}
//		public String getCod() {
//			return cod;
//		}
//		public void setCod(String cod) {
//			this.cod = cod;
//		}
//		public String getGoodscount() {
//			return goodscount;
//		}
//		public void setGoodscount(String goodscount) {
//			this.goodscount = goodscount;
//		}
//		public String getAddress() {
//			return address;
//		}
//		public void setAddress(String address) {
//			this.address = address;
//		}
//		public String getPeople() {
//			return people;
//		}
//		public void setPeople(String people) {
//			this.people = people;
//		}
//		public String getPeopletel() {
//			return peopletel;
//		}
//		public void setPeopletel(String peopletel) {
//			this.peopletel = peopletel;
//		}
//		public String getStatus() {
//			return status;
//		}
//		public void setStatus(String status) {
//			this.status = status;
//		}
//		public String getMemo() {
//			return memo;
//		}
//		public void setMemo(String memo) {
//			this.memo = memo;
//		}
//		public String getDssn() {
//			return dssn;
//		}
//		public void setDssn(String dssn) {
//			this.dssn = dssn;
//		}
//		public String getDsname() {
//			return dsname;
//		}
//		public void setDsname(String dsname) {
//			this.dsname = dsname;
//		}
//		public String getDsorderno() {
//			return dsorderno;
//		}
//		public void setDsorderno(String dsorderno) {
//			this.dsorderno = dsorderno;
//		}
//		public String getSqpayway() {
//			return sqpayway;
//		}
//		public void setSqpayway(String sqpayway) {
//			this.sqpayway = sqpayway;
//		}
//		public String getIstopay() {
//			return istopay;
//		}
//		public void setIstopay(String istopay) {
//			this.istopay = istopay;
//		}

		public List<Order> getOrders() {
			return orders;
		}

		public void setOrders(List<Order> orders) {
			this.orders = orders;
		}
	}

	@Override
	public Class getBodyClass() {
		return QueryOrderInfoRespWlBody.class;
	}
}
