package com.fyqz.dc.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;

import com.fyqz.dc.dto.Page;

//import org.hibernate.HibernateException;

/**
 * @file:   com.fyqz.wg.dao.impl.BaseDao.java 
 * @brief:   基础dao接口
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public interface BaseDao<T> {

	public Serializable save(Object entity)throws HibernateException;

	/**
	 * 
	 * show: 查询对象
	 * @param id
	 * @return
	 * @throws HibernateException
	 * @exception
	 */
	public T load(String id)throws HibernateException;

    /**
     * 
     * show: 查询对象 Load 
     * @param entityClass
     * @param id
     * @return
     * @throws DataAccessException
     * @exception
     */
    public <K> K load(Class<K> entityClass, Serializable id) throws DataAccessException;

	public List<T> loadAll()throws HibernateException;

	public T get(String id)throws HibernateException;

	public List<T> find(String hql)throws HibernateException;

	/**
	 * 
	 * show: 修改对象
	 * @param entity
	 * @throws HibernateException
	 * @exception
	 */
	public void update(Object entity)throws HibernateException;

	/**
	 * 
	 * show: 删除对象
	 * @param entity
	 * @throws HibernateException
	 * @exception
	 */
	public void delete(Object entity)throws HibernateException;

	public Integer queryTotleRows(final String queryStr, final String queryType,
			final List<Object> params) throws HibernateException;
	
	public List<Object[]> queryByPage(final Page page, final String queryStr,
			final String queryType, final List<Object> params) throws HibernateException;

	public List<T> queryByGeneric(final Page page, final String queryStr, final String queryType,
			final List<Object> params) throws HibernateException;
	

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
			final int length) throws DataAccessException;
	
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
			final int length) throws DataAccessException ;
	

	/**
	 * 
	 * show: 通过SQL更新操作
	 * @param sql	      String
	 * @param values	  Object[]
	 * @return 返回修改结果1为修改成功 0为修改失败
	 * @throws DataAccessException
	 * @exception
	 */
	public int updateBySQL(final String sql, final Object[] values) throws DataAccessException ;

	
	/**
	 * 
	 * show: 新增   或者  修改
	 * @param entity
	 * @throws DataAccessException
	 * @exception
	 */
	public void saveOrUpdate(Object entity) throws DataAccessException;

}
 