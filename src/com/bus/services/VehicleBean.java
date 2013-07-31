package com.bus.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.vehicleprofile.VehicleProfile;

public class VehicleBean  extends EMBean{

	/**
	 * Get vehicles  if pagenum != -1, use lotsize, else all selected
	 * @param pagenum
	 * @param lotsize
	 * @return
	 */
	public Map getVehicles(int pagenum, int lotsize) throws Exception{
		Map map = new HashMap<String,Object>();
		if(pagenum == -1 || pagenum == 0){
			List<VehicleProfile> list = em.createQuery("SELECT q FROM VehicleProfile q ORDER BY datepurchase DESC").getResultList();
			Long count = (Long) em.createQuery("SELECT count(q) FROM VehicleProfile q").getSingleResult();
			map.put("list", list);
			map.put("count", count);
		}else{
			List<VehicleProfile> list = em.createQuery("SELECT q FROM VehicleProfile q ORDER BY datepurchase DESC")
					.setFirstResult(pagenum*lotsize-lotsize).setMaxResults(lotsize).getResultList();
			Long count  = (Long) em.createQuery("SELECT count(q) FROM VehicleProfile q").getSingleResult();
			map.put("list", list);
			map.put("count", count);
		}
		return map;
	}

	/**
	 * Select vehicles by statement
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 */
	public Map getVehicles(int pagenum, int lotsize, String statement) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Create new vehicle
	 * @param profile
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicle(VehicleProfile profile) throws Exception{
		if(profile.getId() == null)
			em.persist(profile);
		else{
			em.merge(profile);
		}
	}

	/**
	 * Delete vehicle profile given its ID, onlu for vehicles no referenced
	 * @param parseInt
	 * @throws Exception
	 */
	@Transactional
	public void deleteVehicle(int id) throws Exception{
		VehicleProfile vp = em.find(VehicleProfile.class, id);
		em.remove(vp);
	}

	/**
	 * Get vehicle profile by id
	 * @param parseInt
	 * @return
	 * @throws Exception
	 */
	public VehicleProfile getVehicleProfileById(int id) throws Exception{
		return em.find(VehicleProfile.class, id);
	}

}
