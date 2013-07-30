package com.bus.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Employee;
import com.bus.dto.Hrimage;
import com.bus.dto.common.ContractImg;

public class CommonBean {

	@PersistenceContext
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return this.em;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.em = entityManager;
	}

	/**
	 * Save the contract image file
	 * @param img
	 */
	@Transactional
	public void saveContractImg(ContractImg img) throws Exception {
		if(img.getId() != null){
			em.merge(img);
		}else{
			em.persist(img);
		}
	}

	/**
	 * Save the profile picture in side Employee.setImage
	 * @param e
	 */
	@Transactional
	public void saveProfilePic(Employee e) throws Exception{
		Hrimage img = e.getImage();
		if(img.getId() == null){
			em.persist(img);
			em.flush();
			em.merge(e);
		}else{
			em.merge(img);
		}
	}

}
