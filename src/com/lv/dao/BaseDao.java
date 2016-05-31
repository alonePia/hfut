package com.lv.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao {
	
	/**
	 * 分页查询
	 * @param hql sql语句 from Entity...
	 * @param firstResult 开始索引
	 * @param maxResults 最大显示条数
	 * @param map 查询条件
	 * @return
	 */
	public List<Object> queryPageList(final String querySql, final int firstResult,final int maxResults,final Map map);
	
	/**
	 * 获取单独对象
	 * @param queryString
	 * @return
	 */
	public Object queryForObject(final String queryString,final String condition1,final String condition2);

	/**
	 * 获取单独对象
	 * @param queryString
	 * @return
	 */
	public Object queryForObject(final String queryString,final String condition);
	
	/**
	 * 获取集合
	 * @param queryString
	 * @return
	 */
	public List<Object> createQuery(final String queryString);

	/**
	 * 保存
	 * @param model
	 * @return
	 */
	public Object save(final Object model);

	/**
	 * 修改
	 * @param model
	 */
	public void update(final Object model);

	/**
	 * 删除
	 * @param model
	 */
	public void delete(final Object model);
	
	/**
	 * 得到某张表的总数据
	 * @param querySql
	 * @return
	 */
	public int getTotalCount(final String querySql);

}
