package com.bus.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.bus.dto.Department;
import com.bus.dto.Ethnic;
import com.bus.dto.Fixoptions;
import com.bus.dto.Position;
import com.bus.dto.Qualification;
import com.bus.dto.Workertype;

public class SelectBoxOptions {

	public static List<SelectBoxOption> getMarriage(){
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		list.add(new SelectBoxOption("未婚","未婚"));
		list.add(new SelectBoxOption("已婚","已婚"));
		list.add(new SelectBoxOption("离异","离异"));
		return list;
	}

	public static List<SelectBoxOption> getEthnics() {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		list.add(new SelectBoxOption("汉族","汉族"));
		list.add(new SelectBoxOption("壮族","壮族"));
		list.add(new SelectBoxOption("畲族","畲族"));
		list.add(new SelectBoxOption("瑶族","瑶族"));
		list.add(new SelectBoxOption("土家族","土家族"));
		list.add(new SelectBoxOption("回族","回族"));
		return list;
	}

	public static List<SelectBoxOption> getPoliticalStatus() {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		list.add(new SelectBoxOption("群众","群众"));
		list.add(new SelectBoxOption("无党派","无党派"));
		list.add(new SelectBoxOption("团员","团员"));
		list.add(new SelectBoxOption("民革党员","民革党员"));
		list.add(new SelectBoxOption("共产党员","共产党员"));
		list.add(new SelectBoxOption("民主党员","民主党员"));
		list.add(new SelectBoxOption("中共党员","中共党员"));
		return list;
	}

	public static List<SelectBoxOption> getQualification() {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		list.add(new SelectBoxOption("高中","高中"));
		list.add(new SelectBoxOption("中专","中专"));
		list.add(new SelectBoxOption("大专","大专"));
		list.add(new SelectBoxOption("大学","大学"));
		list.add(new SelectBoxOption("硕士","硕士"));
		list.add(new SelectBoxOption("博士","博士"));
		return list;
	}

	public static List<SelectBoxOption> getDepartment(List<Department> departments) {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		if(departments == null){
			list.add(new SelectBoxOption("人力资源部","0"));
			list.add(new SelectBoxOption("财务","1"));
			list.add(new SelectBoxOption("监控","2"));
		}else{
			for(Department d:departments){
				list.add(new SelectBoxOption(d.getName(),d.getId()+""));
			}
		}
		return list;
	}

	public static List<SelectBoxOption> getJobLevel() {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		list.add(new SelectBoxOption("高管","高管"));
		list.add(new SelectBoxOption("中管","中管"));
		list.add(new SelectBoxOption("管","管"));
		return list;
	}

	public static List<SelectBoxOption> getWorkerType() {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		list.add(new SelectBoxOption("驾驶员","驾驶员"));
		list.add(new SelectBoxOption("管理员","管理员"));
		list.add(new SelectBoxOption("清洁员","清洁员"));
		return list;
	}

	public static List<SelectBoxOption> getPosition(List<Position> positions) {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		if(positions == null){
			list.add(new SelectBoxOption("人事助理","0"));
			list.add(new SelectBoxOption("培训专员","1"));
			list.add(new SelectBoxOption("战略管理员","2"));
		}else{
			for(Position p:positions){
				list.add(new SelectBoxOption(p.getName(),p.getId()+""));
			}
		}
		return list;
	}

	public static List<SelectBoxOption> getContractType() {
		try{
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		list.add(new SelectBoxOption("正式","正式"));
		list.add(new SelectBoxOption("试用","试用"));
		list.add(new SelectBoxOption("临时工","临时工"));
		return list;
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<SelectBoxOption>();
		}
	}
	
	public static List<SelectBoxOption> getDomicileType(){
		try{
			List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
			list.add(new SelectBoxOption("省内城镇","省内城镇"));
			list.add(new SelectBoxOption("省外农村","省外农村"));
			list.add(new SelectBoxOption("省内农村","省内农村"));
			list.add(new SelectBoxOption("省外城镇","省外城镇"));
			return list;
			}catch(Exception e){
				e.printStackTrace();
				return new ArrayList<SelectBoxOption>();
			}
	}
	
	public static List<SelectBoxOption> getIdcardType(){
		try{
			List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
			list.add(new SelectBoxOption("服务资格证","服务资格证"));
			list.add(new SelectBoxOption("从业资格证","从业资格证"));
			list.add(new SelectBoxOption("驾驶证","驾驶证"));
			return list;
			}catch(Exception e){
				e.printStackTrace();
				return new ArrayList<SelectBoxOption>();
			}
	}
	
	public static List<SelectBoxOption> getCoordinatorType(){
		try{
			List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
			list.add(new SelectBoxOption("升迁","升迁"));
			list.add(new SelectBoxOption("调动","调动"));
			list.add(new SelectBoxOption("离职","离职"));
			list.add(new SelectBoxOption("复职","复职"));
			return list;
			}catch(Exception e){
				e.printStackTrace();
				return new ArrayList<SelectBoxOption>();
			}
	}

	public static List<SelectBoxOption> getSelectBoxFromFixOptions(
			Fixoptions optionListById) {
		if(optionListById == null){
			return new ArrayList<SelectBoxOption>();
		}
		String[] cols = optionListById.getContent().split(",");
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		for(int i=0;i<cols.length;i++){
			list.add(new SelectBoxOption(cols[i],cols[i]));
		}
		return list;
	}

	public static List<SelectBoxOption> getEthnics(List<Ethnic> ethnicList) {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		if(ethnicList == null){
			return list;
		}else{
			for(Ethnic d:ethnicList){
				list.add(new SelectBoxOption(d.getName(),d.getName()));
			}
		}
		return list;
	}

	public static List<SelectBoxOption> getQualification(
			List<Qualification> qualificationList) {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		if(qualificationList == null){
			return list;
		}else{
			for(Qualification d:qualificationList){
				list.add(new SelectBoxOption(d.getName(),d.getName()));
			}
		}
		return list;
	}

	public static List<SelectBoxOption> getWorkerType(
			List<Workertype> workertypeList) {
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		if(workertypeList == null){
			return list;
		}else{
			for(Workertype d:workertypeList){
				list.add(new SelectBoxOption(d.getName(),d.getName()));
			}
		}
		return list;
	}
}
