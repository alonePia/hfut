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
@Table(name = "repair")
public class Repair implements Serializable {
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

	@Column(name = "repairbh", nullable = false)
	private String repairbh;

	@Column(name = "carbh", nullable = false)
	private String carbh;

	@Column(name = "state", nullable = false)
	private int state;

	@Column(name = "startTime", nullable = false)
	private Date startTime;

	@Column(name = "endTime", nullable = false)
	private Date endTime;

	@Column(name = "describes", nullable = false)
	private String describes;

	@Column(name = "result", nullable = true)
	private String result;

	@Column(name = "userbh", nullable = true)
	private String userbh;

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
	
	public String getRepairbh() {
		return repairbh;
	}

	public void setRepairbh(String repairbh) {
		this.repairbh = repairbh;
	}

	/** carbh */
	public void setCarbh(String carbh) {
		this.carbh = carbh;
	}

	/** carbh */
	public String getCarbh() {
		return carbh;
	}

	/** state */
	public void setState(int state) {
		this.state = state;
	}

	/** state */
	public int getState() {
		return state;
	}

	/** startTime */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/** startTime */
	public Date getStartTime() {
		return startTime;
	}

	/** endTime */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/** endTime */
	public Date getEndTime() {
		return endTime;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	/** result */
	public void setResult(String result) {
		this.result = result;
	}

	/** result */
	public String getResult() {
		return result;
	}

	/** userbh */
	public void setUserbh(String userbh) {
		this.userbh = userbh;
	}

	/** userbh */
	public String getUserbh() {
		return userbh;
	}

	/** czsj */
	public void setCzsj(Date czsj) {
		this.czsj = czsj;
	}

	/** czsj */
	public Date getCzsj() {
		return czsj;
	}

	@Override
	public String toString() {
		return "Repair [id=" + id + ", repairbh=" + repairbh + ", carbh="
				+ carbh + ", state=" + state + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", describes=" + describes
				+ ", result=" + result + ", userbh=" + userbh + ", czsj="
				+ czsj + "]";
	}

	
	
}
