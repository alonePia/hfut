package com.lv.entity;

import java.util.Date;

public class OrderOthers {
	private int id;
	private String orderbh;
	// 用户信息
	private String userbh;
	private String username;
	private String accountnum;
	private int sex;
	private int age;
	private String phone;
	private String address;
	private String usersfz;
	private Date applyDate;// 客户账号申请时间

	// 车辆信息
	private String carbh;
	private String owner;
	private String carno;
	private String type;
	private String color;
	private String image;
	private String ownerphone;
	private String ownersfz;
	private Date startTime;
	private Date endTime;
	private Date czsj;

	// 租赁信息
	private int isback;
	private Double price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderbh() {
		return orderbh;
	}

	public void setOrderbh(String orderbh) {
		this.orderbh = orderbh;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getOwnerphone() {
		return ownerphone;
	}

	public void setOwnerphone(String ownerphone) {
		this.ownerphone = ownerphone;
	}

	public int getIsback() {
		return isback;
	}

	public void setIsback(int isback) {
		this.isback = isback;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCzsj() {
		return czsj;
	}

	public void setCzsj(Date czsj) {
		this.czsj = czsj;
	}

	public String getUserbh() {
		return userbh;
	}

	public void setUserbh(String userbh) {
		this.userbh = userbh;
	}

	public String getAccountnum() {
		return accountnum;
	}

	public void setAccountnum(String accountnum) {
		this.accountnum = accountnum;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsersfz() {
		return usersfz;
	}

	public void setUsersfz(String usersfz) {
		this.usersfz = usersfz;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getCarbh() {
		return carbh;
	}

	public void setCarbh(String carbh) {
		this.carbh = carbh;
	}

	public String getOwnersfz() {
		return ownersfz;
	}

	public void setOwnersfz(String ownersfz) {
		this.ownersfz = ownersfz;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderOthers [id=" + id + ", orderbh=" + orderbh + ", userbh="
				+ userbh + ", username=" + username + ", accountnum="
				+ accountnum + ", sex=" + sex + ", age=" + age + ", phone="
				+ phone + ", address=" + address + ", usersfz=" + usersfz
				+ ", applyDate=" + applyDate + ", carbh=" + carbh + ", owner="
				+ owner + ", carno=" + carno + ", type=" + type + ", color="
				+ color + ", image=" + image + ", ownerphone=" + ownerphone
				+ ", ownersfz=" + ownersfz + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", czsj=" + czsj + ", isback="
				+ isback + ", price=" + price + "]";
	}

}
