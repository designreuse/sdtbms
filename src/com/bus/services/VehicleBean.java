package com.bus.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.bus.dto.vehicleprofile.VehicleCheck;
import com.bus.dto.vehicleprofile.VehicleFiles;
import com.bus.dto.vehicleprofile.VehicleMiles;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.util.ExcelFileSaver;
import com.bus.util.HRUtil;

public class VehicleBean extends EMBean {

	/**
	 * Get vehicles if pagenum != -1, use lotsize, else all selected
	 * 
	 * @param pagenum
	 * @param lotsize
	 * @return
	 */
	public Map getVehicles(int pagenum, int lotsize) throws Exception {
		Map map = new HashMap<String, Object>();
		if (pagenum == -1 || pagenum == 0) {
			List<VehicleProfile> list = em
					.createQuery(
							"SELECT q FROM VehicleProfile q ORDER BY datepurchase DESC")
					.getResultList();
			Long count = (Long) em.createQuery(
					"SELECT count(q) FROM VehicleProfile q").getSingleResult();
			map.put("list", list);
			map.put("count", count);
		} else {
			List<VehicleProfile> list = em
					.createQuery(
							"SELECT q FROM VehicleProfile q ORDER BY datepurchase DESC")
					.setFirstResult(pagenum * lotsize - lotsize)
					.setMaxResults(lotsize).getResultList();
			Long count = (Long) em.createQuery(
					"SELECT count(q) FROM VehicleProfile q").getSingleResult();
			map.put("list", list);
			map.put("count", count);
		}
		return map;
	}

	/**
	 * Select vehicles by statement
	 * 
	 * @param pagenum
	 * @param lotsize
	 * @param statement
	 * @return
	 */
	public Map getVehicles(int pagenum, int lotsize, String statement) {
		Map map = new HashMap<String, Object>();
		List<VehicleProfile> list = em.createQuery(statement)
				.setFirstResult(pagenum * lotsize - lotsize)
				.setMaxResults(lotsize).getResultList();
		String substatement = statement.substring(statement.indexOf("FROM"),
				statement.indexOf("ORDER BY"));
		substatement = "SELECT count(q) " + substatement;
		Long count = (Long) em.createQuery(substatement).getSingleResult();
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 * Create new vehicle
	 * 
	 * @param profile
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicle(VehicleProfile profile) throws Exception {
		if (profile.getId() == null) {
			profile.setStatus("A");
			em.persist(profile);
		} else {
			em.merge(profile);
		}
	}

	/**
	 * Delete vehicle profile given its ID, onlu for vehicles no referenced
	 * 
	 * @param parseInt
	 * @throws Exception
	 */
	@Transactional
	public void deleteVehicle(int id) throws Exception {
		VehicleProfile vp = em.find(VehicleProfile.class, id);
		em.remove(vp);
	}

	/**
	 * Get vehicle profile by id
	 * 
	 * @param parseInt
	 * @return
	 * @throws Exception
	 */
	public VehicleProfile getVehicleProfileById(int id) throws Exception {
		return em.find(VehicleProfile.class, id);
	}

	/**
	 * Save mile by vehicle
	 * 
	 * @param mile
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicleMile(VehicleMiles mile) throws Exception {
		if (mile.getVehicle() == null || mile.getVehicle().getId() == null)
			throw new Exception("No vid provided.");
		VehicleProfile vp = em.find(VehicleProfile.class, mile.getVehicle()
				.getId());
		mile.setVehicle(vp);
		mile.calculate();
		em.persist(mile);
	}

	/**
	 * update existing vehicle miles row given VehicleMiles Object
	 * 
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
	public void removeVehicleMiles(String vid) throws Exception {
		VehicleMiles vm = em.find(VehicleMiles.class, Integer.parseInt(vid));
		em.remove(vm);
	}

	/**
	 * save the vehicle file ,mostly image files linked to VehicleCheck object
	 * 
	 * @param vf
	 * @throws Exception
	 */
	@Transactional
	public VehicleFiles saveVehicleFile(VehicleFiles vf) throws Exception {
		if (vf.getId() == null) {
			em.persist(vf);
			em.flush();
			return vf;
		} else {
			em.merge(vf);
			return vf;
		}
	}

	/**
	 * save the VehicleCheck object
	 * 
	 * @param check
	 * @throws Exception
	 */
	@Transactional
	public void saveVehicleCheck(VehicleCheck check) throws Exception {
		em.persist(check);
	}

	/**
	 * Get Vehicle Check for 一保 and 二保
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeMaintennance(Integer id)
			throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE (ctype=? OR ctype=?) AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "一保").setParameter(3, id).setParameter(2, "二保")
					.getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	public VehicleCheck getVehicleCheckById(int id) throws Exception {
		return em.find(VehicleCheck.class, id);
	}

	/**
	 * update vehicle check
	 * 
	 * @param vc
	 * @throws Exception
	 */
	@Transactional
	public void updateVehicleCheck(VehicleCheck vc) throws Exception {
		em.merge(vc);
	}

	/**
	 * Get types for 小修，中修，大修
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeRepaire(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE (ctype=? OR ctype=?  OR ctype=?) AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "小修").setParameter(2, "中修")
					.setParameter(3, "大修").setParameter(4, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 综合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeFullCheck(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE ctype=? AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "综合").setParameter(2, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeExtras(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE ctype=? AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "附件").setParameter(2, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Get types for 年审
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<VehicleCheck> getVehicleCheckByTypeAnnul(Integer id) throws Exception {
		try {
			return em
					.createQuery(
							"SELECT q FROM VehicleCheck q WHERE ctype=? AND vid=? ORDER BY cdate DESC")
					.setParameter(1, "年审").setParameter(2, id).getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ArrayList<VehicleCheck>();
		}
	}

	/**
	 * Delete vehicle check record and its file
	 * 
	 * @param checkId
	 * @throws Exception
	 */
	@Transactional
	public void removeVehicleCheck(String checkId) throws Exception {
		VehicleCheck vc = em
				.find(VehicleCheck.class, Integer.parseInt(checkId));
		VehicleFiles image = vc.getImage();
		em.remove(vc);
		em.flush();
		File delF = new File(image.getIpath());
		if (delF.exists())
			delF.delete();
		em.remove(image);
	}

	/**
	 * Throw the vehicle，报废. change status to E FROM A and give a throw date
	 * 
	 * @param vid
	 * @param dateVal
	 * @throws Exception
	 */
	@Transactional
	public void throwVehicle(String vid, Date dateVal) throws Exception {
		VehicleProfile vp = em
				.find(VehicleProfile.class, Integer.parseInt(vid));
		if (vp.getStatus() != null && vp.getStatus().equals("E"))
			return;
		vp.setStatus("E");
		vp.setThrowdate(dateVal);
		em.merge(vp);
	}

	/**
	 * Get VehicleProfile by VID , or null if not exists
	 * 
	 * @param vid
	 * @return
	 */
	public VehicleProfile getVehicleProfileByVid(String vid) {
		try {
			return (VehicleProfile) em
					.createQuery("SELECT q FROM VehicleProfile q WHERE vid=?")
					.setParameter(1, vid).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public String saveVehicleProfilesFromFile(ExcelFileSaver saver)
			throws Exception {
		String str = "";
		int cast = 0;
		try {
			while (saver.hasNextLine()) {
				if (saver.strLine.contains("车牌号")) {
					String fullDetail = saver.strLine;
					while (saver.hasNextLine()
							&& !saver.strLine.contains("汽车技术档案卡")) {
						fullDetail += "," + saver.strLine;
					}
					VehicleProfile vp = new VehicleProfile();
					vp.setVid(saver.getValueFromName(fullDetail, "车牌号"));
					if (vp.getVid() == null || vp.getVid().equals("")){
						str += "第" + cast + "台车辆档案的车牌为空，不添加.<br/>";
						continue;
					}
					vp.setCompany(saver.getValueFromName(fullDetail, "车属单位"));
					vp.setCompanyaddr(saver.getValueFromName(fullDetail, "地址"));
					vp.setDatejoin(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "入户日期"),
							"yyyy/MM/dd"));
					vp.setPtaxnumber(saver.getValueFromName(fullDetail,
							"购置凭证税号"));
					vp.setSource(saver.getValueFromName(fullDetail, "车辆来源"));
					vp.setDateuse(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "运行日期"),
							"yyyy/MM/dd"));
					vp.setDatepurchase(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "购进时间"),
							"yyyy年MM月"));
					vp.setServicetype(saver
							.getValueFromName(fullDetail, "使用性质"));
					vp.setEnginenum(saver.getValueFromName(fullDetail, "发动机号"));
					String price = saver.getValueFromName(fullDetail, "车价+购置费");
					if (price != null && !price.equals("")) {
						String ps[] = price.split("\\+");
						vp.setVehicleprice(ps[0]);
						if (ps.length > 1)
							vp.setSubprice(ps[1]);
					}
					vp.setFramenum(saver.getValueFromName(fullDetail, "车架号码"));
					vp.setColor(saver.getValueFromName(fullDetail, "车身颜色"));
					vp.setDateproduction(HRUtil.parseDate(
							saver.getValueFromName(fullDetail, "车辆出厂日期"),
							"yyyy/MM/dd"));
					vp.setModel(saver.getValueFromName(fullDetail, "车辆厂牌/型号"));
					vp.setVtype(saver.getValueFromName(fullDetail, "车辆类型"));
					vp.setProductionaddr(saver.getValueFromName(fullDetail,
							"车辆制造企业名称"));
					vp.setBasednum(saver.getValueFromName(fullDetail, "底盘型号"));
					vp.setVlevel(saver.getValueFromName(fullDetail, "评定等级"));
					vp.setMadein(saver.getValueFromName(fullDetail, "车辆产地"));
					vp.setEnginemodel(saver.getValueFromName(fullDetail,
							"发动机号型号"));
					vp.setSits(saver.getValueFromName(fullDetail, "座位"));

					String tyre = saver
							.getValueFromName(fullDetail, "轮胎数/轮胎形式");
					if (tyre != null && !tyre.equals("")) {
						String tyres[] = tyre.split("/");
						if (tyres.length < 2)
							tyres = tyre.split("∕");
						vp.setTyrenum(tyres[0]);
						if (tyres.length > 1)
							vp.setTyremodel(tyres[1]);
					}else{
						vp.setTyrenum(saver.getValueFromName(fullDetail, "轮胎数"));
					}
					vp.setBodysize(saver.getValueFromName(fullDetail, "车辆外型尺寸"));
					vp.setFueltype(saver.getValueFromName(fullDetail, "燃料"));
					vp.setVpower(saver.getValueFromName(fullDetail, "排量/功率"));
					vp.setSubsides(saver.getValueFromName(fullDetail, "车厢配置"));

					String speed = saver.getValueFromName(fullDetail, "最高车速");
					vp.setVspeed(saver.removeNoneNumber(speed));
					vp.setTurntype(saver.getValueFromName(fullDetail, "转向型式"));
					vp.setTurnmethod(saver.getValueFromName(fullDetail, "转向器式"));
					vp.setMovebreak(saver
							.getValueFromName(fullDetail, "行车制动形式"));
					vp.setWheelbase(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "轴距"))));
					vp.setStopbreak(saver
							.getValueFromName(fullDetail, "驻车制动形式"));
					vp.setFrontwheel(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "前轮距"))));
					vp.setTotalweight(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "总质量"))));
					vp.setBackwheel(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "后轮距"))));
					vp.setSubweight(Integer.parseInt(saver
							.removeNoneNumber(saver.getValueFromName(
									fullDetail, "整车备质量"))));
					vp.setHangmodel(saver.getValueFromName(fullDetail, "悬挂形式"));
					vp.setDrivemode(saver.getValueFromName(fullDetail, "驱动形式"));
					vp.setAircond(saver.getValueFromName(fullDetail, "空调"));
					
					vp.setStatus("A");
					
					System.out.println("##############CAST :" + (++cast) + " "
							+ vp.getVid());

					if (getVehicleProfileByVid(vp.getVid()) == null) {
						em.persist(vp);
					} else {
						System.out.println(vp.getVid() + " Already exists.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback(); // to terminate transactions;
		}
		return str;
	}

}
