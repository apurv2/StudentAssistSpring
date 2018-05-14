package com.studentAssist.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Repository
@Transactional
public abstract class AbstractDao {

	@Autowired
	private SessionFactory _sessionFactory;

	protected Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public <T extends Object, Type extends Serializable> T getByKey(Class entity, Type key) {
		return (T) getSession().get(entity, key);
	}

	public void persist(Object entity) {
		getSession().persist(entity);
	}

	public <T extends Object> T save(Object entity) {
		return (T) getSession().save(entity);
	}

	public void delete(Object entity) {
		getSession().delete(entity);
	}

	protected Criteria getCriteria(Class persistentClass) {
		return getSession().createCriteria(persistentClass);
	}

	protected Criteria getCriteria(Class entity, String alias) {
		return getSession().createCriteria(entity, alias);
	}

	protected void saveOrUpdate(Object entity) {
		getSession().saveOrUpdate(entity);
	}
}
