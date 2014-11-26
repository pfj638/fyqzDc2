package com.fyqz.dc.dao.impl;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.fyqz.dc.dao.BaseDao;
import com.fyqz.dc.dto.Page;

/**
 * @file:   com.fyqz.wg.dao.impl.BaseDaoImpl.java 
 * @brief:   基础dao实现类
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

	private Class<T> entityClass;
    /**
     * sql 语句查询方式
     */
    protected static final String QUERY_TYPE_SQL = "sql";
    /**
     * hql 语句查询方式
     */
    protected static final String QUERY_TYPE_HQL = "hql";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDaoImpl() throws HibernateException {

		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class) params[0];
	}

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {

		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {

		this.hibernateTemplate = hibernateTemplate;
	}

	public Serializable save(Object entity) throws HibernateException {

		// TODO Auto-generated method stub
		return hibernateTemplate.save(entity);
	}

	public T get(String id) throws HibernateException {

		// TODO Auto-generated method stub
		return (T) hibernateTemplate.get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String hql) throws HibernateException {

		// TODO Auto-generated method stub
		return hibernateTemplate.find(hql);
	}

	public void update(Object entity) throws HibernateException {

		// TODO Auto-generated method stub
		hibernateTemplate.update(entity);
	}

	public void delete(Object entity) throws HibernateException {

		// TODO Auto-generated method stub
		hibernateTemplate.delete(entity);
	}
	
	/**
	 * 
	 * show: 查询对象
	 * @param id
	 * @return
	 * @throws HibernateException
	 * @exception
	 */
	public T load(String id) throws HibernateException {

		// TODO Auto-generated method stub
		return (T) hibernateTemplate.load(entityClass, id);
	}

	public List<T> loadAll() throws HibernateException {

		// TODO Auto-generated method stub
		return hibernateTemplate.loadAll(entityClass);
	}


	/**
	 * 
	 * show: 通过HQL进行列表查询操作
	 * @param hql    String
	 * @param values Object[]
	 * @param offset  查询全部输-1
	 * @param size    查询全部输-1
	 * @return 返回List对象,每个对象中存放Object[]或相应对象
	 * @throws DataAccessException
	 * @exception
	 */
	public List findByHQL(final String hql, final Object[] values, final int offset,
			final int length) throws DataAccessException {

		List list = getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				if (length != -1) {
					query.setMaxResults(length);
					query.setFirstResult(offset);
				}
				List list = query.list();
				return list;
			}
		});
		return list;
	}

	/**
	 * 
	 * show: 通过SQL列表查询操作
	 * @param sql String
	 * @param values Object[]
	 * @param startRow int 查询全部输-1 开始行
	 * @param length int 查询全部输-1 查询条数
	 * @return 返回List,对象中元素为obj;
	 * @throws DataAccessException
	 * @exception
	 */
	public List<HashMap> findBySQL(final String sql, final Object[] values, final int startRow,
			final int length) throws DataAccessException {

		List<HashMap> list = getHibernateTemplate().execute(new HibernateCallback<List<HashMap>>() {

			public List<HashMap> doInHibernate(Session session) throws HibernateException,
					SQLException {

				SQLQuery query = session.createSQLQuery(sql.toString());
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
				if (length != -1) {
					query.setMaxResults(length);
					query.setFirstResult(startRow);
				}
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		return list;
	}

	/**
	 * 
	 * show: 通过SQL更新操作
	 * @param sql	      String
	 * @param values	  Object[]
	 * @return 返回修改结果1为修改成功 0为修改失败
	 * @throws DataAccessException
	 * @exception
	 */
	public int updateBySQL(final String sql, final Object[] values) throws DataAccessException {

		int result = 0;
		result = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session session) throws HibernateException, SQLException {

				SQLQuery query = session.createSQLQuery(sql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.executeUpdate();
			}
		});
		return result;
	}


    /**
     * 
     * show: 查询对象 Load 
     * @param entityClass
     * @param id
     * @return
     * @throws DataAccessException
     * @exception
     */
    @Override
    public <K> K load(Class<K> entityClass, Serializable id) throws DataAccessException {
        K obj = getHibernateTemplate().load(entityClass, id);
        if (!Hibernate.isInitialized(obj)) {
            Hibernate.initialize(obj);
        }
        return obj;
    }

    /**
     * 
     * show: 新增   或者  修改
     * @param Object
     * @return
     * @throws DataAccessException
     * @exception
     */
	public void saveOrUpdate(Object entity) throws DataAccessException {

		getHibernateTemplate().saveOrUpdate(entity);
	}
	
    public List<Object[]> queryByPage(final Page page, final String queryStr,
            final String queryType, final List<Object> params)
            throws HibernateException {

        List<Object[]> result = null;
        result = getHibernateTemplate().execute(
                new HibernateCallback<List<Object[]>>() {

                    public List<Object[]> doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        Query query = null;
                        if (queryType.equals(QUERY_TYPE_SQL)) {
                            query = session.createSQLQuery(queryStr);
                        } else {
                            query = session.createQuery(queryStr);
                        }
                        if (null != params) {
                            for (int i = 0; i < params.size(); i++) {
                                query.setParameter(i, params.get(i));
                            }
                        }
                        // 如果不想分页，可以把 pageSize 设为-1
                        if (null != page && page.getMaxResults() != -1) {
                            query.setFirstResult(page.getFirstResult());
                            query.setMaxResults(page.getMaxResults());
                        }
                        return (List<Object[]>) query.list();
                    }
                });
        return result;
    }
    
	public Integer queryTotleRows(final String queryStr,
            final String queryType, final List<Object> params)
            throws HibernateException {

        final Integer result = getHibernateTemplate().execute(
                new HibernateCallback<Integer>() {
                    public Integer doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        Query query = null;
                        if (queryType.equals(QUERY_TYPE_SQL)) {
                            query = session.createSQLQuery(queryStr);
                        } else {
                            query = session.createQuery(queryStr);
                        }
                        if (null != params) {
                            for (int i = 0; i < params.size(); i++) {
                                query.setParameter(i, params.get(i));
                            }
                        }
                        if (queryType.equals(QUERY_TYPE_SQL)) {
                            return ((java.math.BigInteger) (query.list().get(0))).intValue();
                        } else {
                            return Integer.parseInt(query.list().get(0).toString());
                        }
                    }
                });
        return result;
    }
	
	public List<T> queryByGeneric(final Page page, final String queryStr,
            final String queryType, final List<Object> params)
            throws HibernateException {

        List<T> result = null;
        result = getHibernateTemplate().execute(
                new HibernateCallback<List<T>>() {

                    public List<T> doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        Query query = null;
                        if (queryType.equals(QUERY_TYPE_SQL)) {
                            query = session.createSQLQuery(queryStr);
                        } else {
                            query = session.createQuery(queryStr);
                        }
                        if (null != params) {
                            for (int i = 0; i < params.size(); i++) {
                                query.setParameter(i, params.get(i));
                            }
                        }
                        // 如果不想分页，可以把 pageSize 设为-1
                        if (null != page && page.getMaxResults() != -1) {
                            query.setFirstResult(page.getFirstResult());
                            query.setMaxResults(page.getMaxResults());
                        }
                        return (List<T>) query.list();
                    }
                });
        return result;
    }
}

 