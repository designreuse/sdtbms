package com.bus.services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleFiles;
import com.bus.dto.vehicleprofile.VehicleMiles;
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

	/**
	 * Save mile by vehicle
	 * @param mile
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicleMile(VehicleMiles mile) throws Exception {
		if(mile.getVehicle() == null || mile.getVehicle().getId() == null)
			throw new Exception("No vid provided.");
		VehicleProfile vp = em.find(VehicleProfile.class, mile.getVehicle().getId());
		mile.setVehicle(vp);
		mile.calculate();
		em.persist(mile);
	}

	/**
	 * update existing vehicle miles row given VehicleMiles Object
	 * @param editmile
	 * @throws Exception
	 */
	@Transactional
	public void updateVehicleMiles(VehicleMiles editmile) throws Exception {
		VehicleMiles vm = em.find(VehicleMiles.class, editmile.getId());
		vm.copy(editmile);
		vm.calculate();
		em.merge(vm);
	}

	/**
	 * 
	 * @param vid
	 * @throws Exception
	 */
	@Transactional
	public void removeVehicleMiles(String vid) throws Exception{
		VehicleMiles vm = em.find(VehicleMiles.class, Integer.parseInt(vid));
		em.remove(vm);
	}

	/**
	 * save the vehicle file ,mostly image files linked to VehicleCheck object
	 * @param vf
	 * @throws Exception
	 */
	@Transactional
	public VehicleFiles saveVehicleFile(VehicleFiles vf) throws Exception{
		if(vf.getId() == null){
			em.persist(vf);
			em.flush();
			return vf;
		}else{
			em.merge(vf);
			return vf;
		}
	}

	/**
	 * save the VehicleCheck object
	 * @param check
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicleCheck(VehicleCheck check) throws Exception{
		em.persist(check);
	}

	/**
	 * Get Vehicle Check for 一保 and 二保
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeMaintennance() throws Exception{
		try{
			return em.createQuery("SELECT q FROM VehicleCheck q WHERE ctype=? OR ctype=? ORDER BY cdate DESC")
					.setParameter(1, "一保").setParameter(2, "二保").getResultList();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	public VehicleCheck getVehicleCheckById(int id) throws Exception{
		return em.find(VehicleCheck.class, id);
	}

	/**
	 * update vehicle check
	 * @param vc
	 * @throws Exception
	 */
	@Transactional
	public void updateVehicleCheck(VehicleCheck vc) throws Exception{
		em.merge(vc);
	}

	/**
	 * Get types for 小修，中修，大修
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeRepaire() throws Exception{
		try{
			return em.createQuery("SELECT q FROM VehicleCheck q WHERE ctype=? OR ctype=?  OR ctype=? ORDER BY cdate DESC")
					.setParameter(1, "小修").setParameter(2, "中修").setParameter(3, "大修").getResultList();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 综合
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeFullCheck() throws Exception{
		try{
			return em.createQuery("SELECT q FROM VehicleCheck q WHERE ctype=? ORDER BY cdate DESC")
					.setParameter(1, "综合").getResultList();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 附件
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeExtras() throws Exception{
		try{
			return em.createQuery("SELECT q FROM VehicleCheck q WHERE ctype=? ORDER BY cdate DESC")
					.setParameter(1, "附件").getResultList();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 年审
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeAnnul()  throws Exception{
		try{
			return em.createQuery("SELECT q FROM VehicleCheck q WHERE ctype=? ORDER BY cdate DESC")
					.setParameter(1, "年审").getResultList();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Delete vehicle check record and its file
	 * @param checkId
	 * @throws Exception
	 */
	@Transactional
	public void removeVehicleCheck(String checkId)  throws Exception{
		VehicleCheck vc = em.find(VehicleCheck.class, Integer.parseInt(checkId));
		VehicleFiles image = vc.getImage();
		em.remove(vc);
		em.flush();
		File delF = new File(image.getIpath());
		if(delF.exists())
			delF.delete();
		em.remove(image);
	}

}
