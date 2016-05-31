package com.lv.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lv.dao.BaseDao;
import com.lv.entity.Cars;

@Service
@Component
public class CarsService{

	@Autowired
	private BaseDao baseDao;
	
	public List<Object> queryPageList(final String querySql, final int firstResult,final int maxResults,final Map map){
		return baseDao.queryPageList(querySql, firstResult, maxResults,map);
	}
	
	/**
	 * 查询是否唯一
	 * @param accountnum 账号
	 * @return
	 */
	public Cars getUniqueObj(String carno){
		String querySql="from "+Cars.class.getSimpleName()+" where carno=? ";
		Cars car=(Cars) baseDao.queryForObject(querySql,carno);
		return car;
	}
	
	/**
	 * 查询是否唯一
	 * @param accountnum 账号
	 * @return
	 */
	public Cars getObj(String carbh){
		String querySql="from "+Cars.class.getSimpleName()+" where carbh=? ";
		Cars car=(Cars) baseDao.queryForObject(querySql,carbh);
		return car;
	}
	
	public List<Object> getCarsLists(){
		StringBuffer sff = new StringBuffer();
		/**getSimpleName()返回源代码中给出的底层类的简称。*/
		sff.append("select a from ").append(Cars.class.getSimpleName()).append(" a ");
		List<Object> list = baseDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public void save(Cars car){
		baseDao.save(car);
	}
	
	@Transactional
	public void update(Cars car){
		baseDao.update(car);
	}
	
	@Transactional
	public void delete (Cars car){
		baseDao.delete(car);
	}
	
	public int getTotalCount(String querySql){
		return baseDao.getTotalCount(querySql);
	}
	
}
