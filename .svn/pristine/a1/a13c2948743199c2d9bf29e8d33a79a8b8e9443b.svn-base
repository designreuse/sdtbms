package com.bus.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.Employee;
import com.bus.dto.Hrimage;
import com.bus.dto.common.ContractImg;

public class CommonBean extends EMBean{

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

	
	public Employee getEmployeeById(String _id) {
		Integer id = Integer.parseInt(_id);
		if (id == null)
			return null;
		Employee e = em.find(Employee.class, id);
		return e;
	}

	public Employee getEmployeeByWorkerId(String _id){
		try{
			Employee e = (Employee) em.createQuery("SELECT q FROM Employee q WHERE workerid=?").setParameter(1, _id).getSingleResult();
			return e;
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Employee> getEmployeeByName(String name, boolean all) {
		try {
			List<Employee> list = null;
			if (!all)
				list = em
						.createQuery(
								"SELECT q FROM Employee q WHERE status=? AND fullname LIKE ?")
						.setParameter(1, "A").setParameter(2, "%" + name + "%")
						.getResultList();
			else
				list = em
						.createQuery(
								"SELECT q FROM Employee q WHERE fullname LIKE ?")
						.setParameter(1, "%" + name + "%").getResultList();
			return list;
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			return null;
		}

	}
}
