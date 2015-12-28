package com.devin.bangsheng.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("order")
public class Order extends BaseModel{
	/**
	 * 订单号
	 */
	private String orderno;
	/**
	 * 寄件网点编号
	 */
	private String netcode;
	/**
	 * 寄件网点名称
	 */
	private String netname ;
	/**
	 * 重量
	 */
	private String weight;
	/**
	 * 代收款金额
	 */
	private String cod;
	/**
	 * 件数
	 */
	private String goodscount;
	/**
	 * 收件地址
	 */
	private String address;
	/**
	 * 收件人
	 */
	private String people;
	/**
	 * 收件人联系电话
	 */
	private String peopletel;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 电商编号
	 */
	private String dssn;
	/**
	 * 电商名称
	 */
	private String dsname;
	/**
	 * 订单时间
	 */
	private String emaildate;
	/**
	 * 运单状态
	 */
	private String status;
	/**
	 * 授权支付方式(单指代收货款)	00不限制    01现金   02刷卡
	 */
	private String sqpayway;
	/**
	 * 到付标志	0否，1是 
	 */
	private String istopay;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getNetcode() {
		return netcode;
	}
	public void setNetcode(String netcode) {
		this.netcode = netcode;
	}
	public String getNetname() {
		return netname;
	}
	public void setNetname(String netname) {
		this.netname = netname;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getGoodscount() {
		return goodscount;
	}
	public void setGoodscount(String goodscount) {
		this.goodscount = goodscount;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPeople() {
		return people;
	}
	public void setPeople(String people) {
		this.people = people;
	}
	public String getPeopletel() {
		return peopletel;
	}
	public void setPeopletel(String peopletel) {
		this.peopletel = peopletel;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDssn() {
		return dssn;
	}
	public void setDssn(String dssn) {
		this.dssn = dssn;
	}
	public String getDsname() {
		return dsname;
	}
	public void setDsname(String dsname) {
		this.dsname = dsname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmaildate() {
		return emaildate;
	}
	public void setEmaildate(String emaildate) {
		this.emaildate = emaildate;
	}
	public String getSqpayway() {
		return sqpayway;
	}
	public void setSqpayway(String sqpayway) {
		this.sqpayway = sqpayway;
	}
	public String getIstopay() {
		return istopay;
	}
	public void setIstopay(String istopay) {
		this.istopay = istopay;
	}
	@Override
	public String toString() {
		return "Order [orderno=" + orderno + ", netcode=" + netcode
				+ ", netname=" + netname + ", weight=" + weight + ", cod="
				+ cod + ", goodscount=" + goodscount + ", address=" + address
				+ ", people=" + people + ", peopletel=" + peopletel + ", memo="
				+ memo + ", dssn=" + dssn + ", dsname=" + dsname + ", status="
				+ status + ", sqpayway=" + sqpayway + ", istopay=" + istopay
				+ "]";
	}
	
}
