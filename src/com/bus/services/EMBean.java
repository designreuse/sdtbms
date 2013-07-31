package com.bus.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EMBean {

	@PersistenceContext
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return this.em;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}
}
