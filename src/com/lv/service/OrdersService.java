package com.lv.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lv.dao.BaseDao;
import com.lv.entity.Orders;

@Service
@Component
public class OrdersService{

	@Autowired
	private BaseDao baseDao;
	
	public List<Object> queryPageList(final String querySql, final int firstResult,final int maxResults,final Map map){
		return baseDao.queryPageList(querySql, firstResult, maxResults,map);
	}
	
	/*public Orders get(String username,String password){
		String querySql="from "+Orders.class.getSimpleName()+" where accountnum=? and password=? ";
		Orders order=(Orders) baseDao.queryForObject(querySql,username,password);
		return order;
	}*/
	
	/**
	 * 查询是否唯一
	 * @param accountnum 账号
	 * @return
	 */
	public Orders getObj(String orderbh){
		String querySql="from "+Orders.class.getSimpleName()+" where orderbh=? ";
		Orders order=(Orders) baseDao.queryForObject(querySql,orderbh);
		return order;
	}
	
	public List<Object> getOrdersLists(){
		StringBuffer sff = new StringBuffer();
		/**getSimpleName()返回源代码中给出的底层类的简称。*/
		sff.append("select a from ").append(Orders.class.getSimpleName()).append(" a ");
		List<Object> list = baseDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public void save(Orders order){
		baseDao.save(order);
	}
	
	@Transactional
	public void update(Orders order){
		baseDao.update(order);
	}
	
	@Transactional
	public void delete (Orders order){
		baseDao.delete(order);
	}
	
	public int getTotalCount(String querySql){
		return baseDao.getTotalCount(querySql);
	}
	
}
