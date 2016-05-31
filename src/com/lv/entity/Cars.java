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
@Table(name = "cars")
public class Cars implements Serializable {
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

	@Column(name = "carbh", nullable = false)
	private String carbh;

	@Column(name = "carno", nullable = false)
	private String carno;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "color", nullable = false)
	private String color;

	@Column(name = "owner", nullable = false)
	private String owner;
	
	@Column(name = "ownersex", nullable = false)
	private int ownersex;

	@Column(name = "ownersfz", nullable = false)
	private String ownersfz;

	@Column(name = "ownerphone", nullable = false)
	private String ownerphone;

	@Column(name = "state", nullable = false)
	private int state;

	@Column(name = "image", nullable = false)
	private String image;

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

	/** carbh */
	public void setCarbh(String carbh) {
		this.carbh = carbh;
	}

	/** carbh */
	public String getCarbh() {
		return carbh;
	}

	/** carno */
	public void setCarno(String carno) {
		this.carno = carno;
	}

	/** carno */
	public String getCarno() {
		return carno;
	}

	/** type */
	public void setType(String type) {
		this.type = type;
	}

	/** type */
	public String getType() {
		return type;
	}

	/** color */
	public void setColor(String color) {
		this.color = color;
	}

	/** color */
	public String getColor() {
		return color;
	}

	/** owner */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/** owner */
	public String getOwner() {
		return owner;
	}

	public int getOwnersex() {
		return ownersex;
	}

	public void setOwnersex(int ownersex) {
		this.ownersex = ownersex;
	}

	/** ownersfz */
	public void setOwnersfz(String ownersfz) {
		this.ownersfz = ownersfz;
	}

	/** ownersfz */
	public String getOwnersfz() {
		return ownersfz;
	}

	/** ownerphone */
	public void setOwnerphone(String ownerphone) {
		this.ownerphone = ownerphone;
	}

	/** ownerphone */
	public String getOwnerphone() {
		return ownerphone;
	}

	/** state */
	public void setState(int state) {
		this.state = state;
	}

	/** state */
	public int getState() {
		return state;
	}

	/** image */
	public void setImage(String image) {
		this.image = image;
	}

	/** image */
	public String getImage() {
		return image;
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
