package com.lv.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lv.dao.BaseDao;
import com.lv.entity.Broke;

@Service
@Component
public class BrokeService{

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
	public Broke getUniqueObj(String brokebh){
		String querySql="from "+Broke.class.getSimpleName()+" where brokebh=? ";
		Broke broke=(Broke) baseDao.queryForObject(querySql,brokebh);
		return broke;
	}
	
	public List<Object> getUsersLists(){
		StringBuffer sff = new StringBuffer();
		/**getSimpleName()返回源代码中给出的底层类的简称。*/
		sff.append("select a from ").append(Broke.class.getSimpleName()).append(" a ");
		List<Object> list = baseDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public void save(Broke broke){
		baseDao.save(broke);
	}
	
	@Transactional
	public void update(Broke broke){
		baseDao.update(broke);
	}
	
	@Transactional
	public void delete (Broke broke){
		baseDao.delete(broke);
	}
	
	public int getTotalCount(String querySql){
		return baseDao.getTotalCount(querySql);
	}
	
}
