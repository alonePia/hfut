package com.lv.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lv.dao.BaseDao;
import com.lv.entity.Repair;

@Service
@Component
public class RepairService{

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
	public Repair getUniqueObj(String repairbh){
		String querySql="from "+Repair.class.getSimpleName()+" where repairbh=? ";
		Repair repair=(Repair) baseDao.queryForObject(querySql,repairbh);
		return repair;
	}
	
	public List<Object> getUsersLists(){
		StringBuffer sff = new StringBuffer();
		/**getSimpleName()返回源代码中给出的底层类的简称。*/
		sff.append("select a from ").append(Repair.class.getSimpleName()).append(" a ");
		List<Object> list = baseDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public void save(Repair repair){
		baseDao.save(repair);
	}
	
	@Transactional
	public void update(Repair repair){
		baseDao.update(repair);
	}
	
	@Transactional
	public void delete (Repair repair){
		baseDao.delete(repair);
	}
	
	public int getTotalCount(String querySql){
		return baseDao.getTotalCount(querySql);
	}
	
}
