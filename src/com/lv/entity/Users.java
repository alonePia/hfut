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
@Table(name = "users")
public class Users implements Serializable {
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

	@Column(name = "userbh", nullable = false)
	private String userbh;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "accountnum", nullable = false)
	private String accountNum;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "type", nullable = false)
	private int type;

	@Column(name = "sex")
	private int sex;

	@Column(name = "age")
	private int age;

	@Column(name = "sfz", nullable = false)
	private String sfz;

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "address", nullable = true)
	private String address;

	@Column(name = "permission", nullable = true)
	private int permission;

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

	/** userbh */
	public void setUserbh(String userbh) {
		this.userbh = userbh;
	}

	/** userbh */
	public String getUserbh() {
		return userbh;
	}

	/** username */
	public void setUsername(String username) {
		this.username = username;
	}

	/** username */
	public String getUsername() {
		return username;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/** type */
	public void setType(int type) {
		this.type = type;
	}

	/** type */
	public int getType() {
		return type;
	}

	/** sex */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/** sex */
	public int getSex() {
		return sex;
	}

	/** age */
	public void setAge(int age) {
		this.age = age;
	}

	/** age */
	public int getAge() {
		return age;
	}

	/** sfz */
	public void setSfz(String sfz) {
		this.sfz = sfz;
	}

	/** sfz */
	public String getSfz() {
		return sfz;
	}

	/** phone */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/** phone */
	public String getPhone() {
		return phone;
	}

	/** address */
	public void setAddress(String address) {
		this.address = address;
	}

	/** address */
	public String getAddress() {
		return address;
	}

	/** permission */
	public void setPermission(int permission) {
		this.permission = permission;
	}

	/** permission */
	public int getPermission() {
		return permission;
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
