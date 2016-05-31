package com.lv.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lv.dao.BaseDao;
import com.lv.entity.Users;

@Service
@Component
public class UsersService{

	@Autowired
	private BaseDao baseDao;
	
	public List<Object> queryPageList(final String querySql, final int firstResult,final int maxResults,final Map map){
		return baseDao.queryPageList(querySql, firstResult, maxResults,map);
	}
	
	public Users get(String username,String password){
		String querySql="from "+Users.class.getSimpleName()+" where accountnum=? and password=? ";
		Users user=(Users) baseDao.queryForObject(querySql,username,password);
		return user;
	}
	
	/**
	 * 查询是否唯一
	 * @param accountnum 账号
	 * @return
	 */
	public Users getUniqueObj(String accountnum){
		String querySql="from "+Users.class.getSimpleName()+" where accountnum=? ";
		Users user=(Users) baseDao.queryForObject(querySql,accountnum);
		return user;
	}
	
	/**
	 * 查询是否唯一
	 * @param accountnum 账号
	 * @return
	 */
	public Users getObj(String userbh){
		String querySql="from "+Users.class.getSimpleName()+" where userbh=? ";
		Users user=(Users) baseDao.queryForObject(querySql,userbh);
		return user;
	}
	
	public List<Object> getUsersLists(){
		StringBuffer sff = new StringBuffer();
		/**getSimpleName()返回源代码中给出的底层类的简称。*/
		sff.append("select a from ").append(Users.class.getSimpleName()).append(" a ");
		List<Object> list = baseDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public void save(Users user){
		baseDao.save(user);
	}
	
	@Transactional
	public void update(Users user){
		baseDao.update(user);
	}
	
	@Transactional
	public void delete (Users user){
		baseDao.delete(user);
	}
	
	public int getTotalCount(String querySql){
		return baseDao.getTotalCount(querySql);
	}
	
}
