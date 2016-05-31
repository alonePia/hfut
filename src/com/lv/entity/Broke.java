package com.lv.entity;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "broke")
public class Broke implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*
	 * TABLE：使用一个特定的数据库表格来保存主键。 SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
	 * IDENTITY：主键由数据库自动生成（主要是自动增长型）
	 * AUTO：主键由程序控制(也是默认的,在指定主键时，如果不指定主键生成策略，默认为AUTO)
	 */
	private int id;

	@Column(name = "brokebh", nullable = false)
	private String brokebh;

	@Column(name = "carbh", nullable = false)
	private String carbh;

	@Column(name = "userbh", nullable = false)
	private String userbh;

	@Column(name = "brokeAddress", nullable = false)
	private String brokeAddress;

	@Column(name = "brokeTime", nullable = false)
	private Date brokeTime;

	@Column(name = "points", nullable = false)
	private int points;

	@Column(name = "describes", nullable = false)
	private String describes;

	@Column(name = "result", nullable = false)
	private String result;

	@Column(name = "czsj", nullable = false)
	private Date czsj;

	/** id */
	public void setId(int id) {
		this.id = id;
	}

	/** id */
	public int getId() {
		return id;
	}

	/** brokebh */
	public void setBrokebh(String brokebh) {
		this.brokebh = brokebh;
	}

	/** brokebh */
	public String getBrokebh() {
		return brokebh;
	}

	/** carbh */
	public void setCarbh(String carbh) {
		this.carbh = carbh;
	}

	/** carbh */
	public String getCarbh() {
		return carbh;
	}

	/** userbh */
	public void setUserbh(String userbh) {
		this.userbh = userbh;
	}

	/** userbh */
	public String getUserbh() {
		return userbh;
	}

	/** brokeAddress */
	public void setBrokeAddress(String brokeAddress) {
		this.brokeAddress = brokeAddress;
	}

	/** brokeAddress */
	public String getBrokeAddress() {
		return brokeAddress;
	}

	/** brokeTime */
	public void setBrokeTime(Date brokeTime) {
		this.brokeTime = brokeTime;
	}

	/** brokeTime */
	public Date getBrokeTime() {
		return brokeTime;
	}

	/** points */
	public void setPoints(int points) {
		this.points = points;
	}

	/** points */
	public int getPoints() {
		return points;
	}

	/** describes */
	public void setDescribes(String describes) {
		this.describes = describes;
	}

	/** describes */
	public String getDescribes() {
		return describes;
	}

	/** result */
	public void setResult(String result) {
		this.result = result;
	}

	/** result */
	public String getResult() {
		return result;
	}

	/** czsj */
	public void setCzsj(Date czsj) {
		this.czsj = czsj;
	}

	/** czsj */
	public Date getCzsj() {
		return czsj;
	}

}
