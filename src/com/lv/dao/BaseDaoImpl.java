package com.lv.dao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

@SuppressWarnings("unchecked")
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {
	
	protected final transient Log log = LogFactory
			.getLog(BaseDaoImpl.class);

	public Object queryForObject(final String queryString,
			final String condition1, final String condition2) {
		List<Object> list = getHibernateTemplate().find(queryString,
				condition1, condition2);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Object queryForObject(final String queryString,
			final String condition) {
		List<Object> list = getHibernateTemplate().find(queryString,
				condition);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Object> createQuery(final String queryString) {
		return (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<Object>() {
					public Object doInHibernate(org.hibernate.Session session)
							throws org.hibernate.HibernateException {
						Query query = session.createQuery(queryString);
						List<Object> rows = query.list();
						return rows;
					}
				});
	}

	public Object save(final Object model) {
		return getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(org.hibernate.Session session)
					throws org.hibernate.HibernateException {
				session.save(model);
				return null;
			}
		});
	}

	public void update(final Object model) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(org.hibernate.Session session)
					throws org.hibernate.HibernateException {
				session.update(model);
				return null;
			}
		});
	}

	public void delete(final Object model) {
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(org.hibernate.Session session)
					throws org.hibernate.HibernateException {
				session.delete(model);
				return null;
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public List<Object> queryPageList(final String queryString,
			final int firstResult, final int maxResults, final Map map) {
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session s)
							throws HibernateException, SQLException {
						StringBuffer strb=new StringBuffer(queryString);
						if(map!=null){//添加条件及占位符
							Iterator it = map.entrySet().iterator();
							int size=0;
							while (it.hasNext()) {
								Map.Entry entry = (Map.Entry) it.next();
								Object key = entry.getKey();
								Object value = entry.getValue();
								if(key.toString()!=null && !"".equals(key.toString()) && value.toString()!=null && !"".equals(value.toString()))
									if(size==0){
										strb.append(" where ");
									}
								if(size==0){
									strb.append(" "+key+" like :"+key+" ");
								}else{
									strb.append(" and "+key+" like :"+key+" ");
								}
								size++;
							}
						}
						log.info("BaseDaoImpl---->queryPageList---->queryString---->"+strb.toString());
						Query query = s.createQuery(strb.toString());
						if(map!=null){//添加参数
							Iterator it1 = map.entrySet().iterator();
							while (it1.hasNext()) {
								Map.Entry entry = (Map.Entry) it1.next();
								Object key = entry.getKey();
								Object value = entry.getValue();
								if(key.toString()!=null && !"".equals(key.toString()) && value.toString()!=null && !"".equals(value.toString())){
									query.setString(key.toString(), "%"+value.toString()+"%");
								}
							}
						}
						// query.setFirstResult(firstResult);
						// query.setMaxResults(maxResults);
						List<Object> list = query.list();
						return list;
					}
				});
	}

	public int getTotalCount(String querySql) {
		List<Object> list = getHibernateTemplate().find(querySql);
		if (list.size() > 0) {
			return list.size();
		}
		return 0;
	}
}
