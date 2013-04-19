package com.bus.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.Idmanagement;
import com.bus.dto.Position;
import com.bus.dto.Promoandtransfer;
import com.bus.services.HRBean;

public class ExcelFileSaver {
	
	private InputStream inputStream = null;
	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;
	private Iterator rows = null;
	
	private DataInputStream datais=null;
	private BufferedReader bf=null;
	private String strLine;
	private int index = 0;
	
	public ExcelFileSaver(FileInputStream fis){
		try{
			datais = new DataInputStream(fis);
			bf = new BufferedReader(new InputStreamReader(datais));
			this.index = 0;
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public boolean hasNextLine(){
		try {
			strLine = bf.readLine();
			this.index++;
			if(strLine == null && index > 10){
				datais.close();
				return false;
			}else{
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Map<String,Object> getEmployeeObjects(){
		Map<String, Object> map = new HashMap<String,Object>();
		String[] cols = strLine.split(",");
		if(!isValidColForEmployeeSheet(cols)){
			if(cols.length > 0){
				String str = "Column in Row " + this.index + "  starting with :"+cols[0]+ " is not a valid column";
				map.put("error", str);
				return map;
			}else{
				String str = "Column in Row " + this.index + " is empty";
				map.put("error", str);
				return map;
			}
		}
		try{
			Employee e = getEmployeeFromRow(cols);
			Contract c = getContactFromRow(cols);
			map.put("employee", e);
			map.put("contract", c);
			return map;
		}catch(Exception e){
			e.printStackTrace();
			String str = "Error -- Column in Row " + this.index + " starting with :"+cols[0]+ " is not a valid column";
			map.put("error", str);
			return map;
		}
	}
	
	/**
	 * For resign employees
	 * @param bean
	 * @return
	 */
	public String resignedEmployees(HRBean bean, Account act){
//		addList(bean);
//		return "";
		String str="";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(!isValidColForEmployeeSheet(cols)){
				//Ignores empty starting rows
				if(cols.length>24){
					boolean digit = true;
					if(cols[2].length() < 1) digit = false;
					for(int i=0; i<cols[2].length();i++){
						if(!Character.isDigit(cols[2].trim().charAt(i))){
							digit = false;
							break;
						}
					}
					if(!digit){
//						str += " Column in Row " + this.index + "  starting with :"+cols[0]+ " is not a valid column<br/>\n";
						continue;
					}
				}else{
//					if(cols.length < 1)
//						str+="Column is empty at index:"+index;
//					else
//						str += " Column in Row " + this.index + "  starting with :"+cols[0]+ " is not a valid column<br/>\n";
					continue;
				}
			}
			try{
				Employee e = getEmployeeFromRow(cols);
				e.setStatus("E");
				e.setAccount(act);
				Contract c = getContactFromRow(cols);
				c.setStatus("E");
				if(!bean.employeeExist(e.getFullname(), e.getDocumentkey()));
					bean.createEmployeeAndContract(e, c);
			}catch(Exception e){
				e.printStackTrace();
				str += "error occur for name:" + cols[1]+"\n";
				break;
			}
		}
		return str;
	}
	
	/**
	 * For driver license
	 * @param bean
	 * @return
	 */
	public String getDriverIds(HRBean bean){
		String str="\n";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 15){
				continue;
			}else if(index == 1){
				continue;
			}
			try {
				String name = cols[0].trim();
				String workerid = cols[1];
				Date bod = HRUtil.parseDate(cols[4], "yyyy.MM.dd");
				Employee employee = getEmployee(bean,name,workerid,bod);
				if(employee != null){
					Idmanagement driverLicense = new Idmanagement();
					driverLicense.setEmployee(employee);
					driverLicense.setType("驾驶证");
					driverLicense.setNumber(cols[7]);
					driverLicense.setRemark("准驾车型:"+cols[8]+";");
					if(!cols[9].trim().equals("") && !cols[10].trim().equals("")){
						driverLicense.setValidfrom(HRUtil.parseDate(cols[9], "yyyy.MM.dd"));
						driverLicense.setExpiredate(HRUtil.parseDate(cols[10], "yyyy.MM.dd"));
					}
					
					Idmanagement serviceLicense = new Idmanagement();
					serviceLicense.setEmployee(employee);
					serviceLicense.setType("从业资格证");
					serviceLicense.setRemark(cols[12]);
					serviceLicense.setNumber(cols[11]);
					if(!cols[13].equals(""))
						serviceLicense.setValidfrom(HRUtil.parseDate(cols[13], "yyyy.MM.dd"));
					
					Idmanagement service = null;
					if(!cols[14].equals("")){
						service = new Idmanagement();
						service.setEmployee(employee);
						service.setType("服务资格证");
						service.setNumber(cols[14]);
					}
					bean.saveIdsFromFile(driverLicense, serviceLicense,service);
				}else{
					str += "证件添加失败:" + cols[0]+" 不存在或者相同名字出现!!请自行添加.<br/>\n";
				}
			} catch (Exception e) {
				str += "证件添加失败:" + cols[0]+"数据转换出错.请自行添加."+ e.getMessage() +"<br/>\n";
			}
		}
		return str;
	}
	
	public String getCoordinations(HRBean bean, Account creator){
		String str="";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 10){
				continue;
			}else if(index == 1){
				continue;
			}
			try {
				String name = cols[1].trim();
				String workerid = cols[2];
				Employee employee = getEmployee(bean,name,workerid,null);
				if(employee != null){
					Promoandtransfer coordinator = new Promoandtransfer();
					coordinator.setEmployee(employee);
					Department department = bean.getDepartmentByName(cols[3].trim());
					if(department == null){
						department = bean.saveDepartment(new Department(cols[3].trim()));
					}
					coordinator.setPredepartment(department);

					Position position = bean.getPositionByName(cols[4].trim());
					if(position == null){
						position = bean.savePosition(new Position(cols[4].trim()));
					}
					coordinator.setPreposition(position);
					
					department = bean.getDepartmentByName(cols[6].trim());
					if(department == null){
						department = bean.saveDepartment(new Department(cols[6].trim()));
					}
					coordinator.setCurdepartment(department);
					
					position = bean.getPositionByName(cols[7].trim());
					if(position == null){
						position = bean.savePosition(new Position(cols[7].trim()));
					}
					coordinator.setCurposition(position);
					
					if(!cols[8].equals(""))
						coordinator.setMovedate(HRUtil.parseDate(cols[8], "yyyy/MM/dd"));
					if(!cols[9].equals(""))
						coordinator.setActivedate(HRUtil.parseDate(cols[9], "yyyy/MM/dd"));
					coordinator.setCreator(creator);
					coordinator.setType("调动");
					bean.saveCoordination(coordinator);
				}else{
					str += "调动添加失败:" + cols[1]+" 不存在或者相同名字出现!!请自行添加.<br/>\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
				str += "调动添加失败:" + cols[1]+" 数据转换出错.请自行添加. "+ e.getMessage() +"<br/>\n";
			}
		}
		return str;
	}
	
	/**
	 * TO find the employee
	 * @param bean
	 * @param name
	 * @param workerid
	 * @param bod
	 * @return
	 */
	private Employee getEmployee(HRBean bean, String name, String workerid, Date bod) {
		Employee e = null;
		if(bod != null)
			e = bean.getEmployeeByNameAndBod(name, bod);
		if(e != null){
			return e;
		}else{
			while(workerid.length() < 4){
				workerid = "0"+workerid;
			}
			e = bean.getEmployeeByNameAndWorkerid(name, workerid);
			if(e!=null)
				return e;
			List<Employee> em = bean.getEmployeeByName(name);
			if(em != null && em.size() == 1){
				e = em.get(0);
				return e;
			}
		}
		return null;
	}

	public Employee getEmployeeFromRow(String[] cols) throws Exception{
		Employee e = new Employee();
		if(cols[0].trim().equals(""))
			e.setDocumentkey(cols[2].trim());
		else
			e.setDocumentkey(cols[0].trim());
		e.setFullname(cols[1].trim());
		e.setWorkerid(cols[2].trim());
		e.setFirstworktime(HRUtil.parseDate(cols[3], "yyyy-MM-dd"));
		
		e.setIdentitycode(cols[4].trim());
		e.setEthnic(cols[5].trim());
		e.setMarriage(cols[6].trim());
		e.setDob(HRUtil.parseDate(cols[7], "yyyy/MM/dd"));
		e.setSex(cols[8].trim());
		
		Department d = new Department();
		d.setName(cols[11].trim());
		e.setDepartment(d);
		
		Position p = new Position();
		p.setName(cols[12].trim());
		e.setPosition(p);
		
		e.setPlacebelong(cols[13].trim());
		e.setPob(cols[14].trim());
		e.setHomeaddress(cols[15].trim());
		e.setMobilenumber(cols[16].trim());
		e.setDomiciletype(cols[17].trim());
		e.setQualification(cols[18].trim());
		e.setPoliticalstatus(cols[21].trim());
		
		if(!cols[22].trim().equals("") && Character.isDigit(cols[22].charAt(0))){
			e.setTimeofjoinrpc(HRUtil.parseDate(cols[22], "yyyy-MM-dd"));
		}
		
		e.setWorkertype(cols[23].trim());
		if(cols[24] != null && !cols[24].trim().equals(""))
			e.setArmy("是");
		else{
			e.setArmy("否");
		}
		e.setJoblevel(cols[25].trim());
		
		return e;
	}
	
	public Contract getContactFromRow(String[] cols) throws Exception{
		Contract c = new Contract();
		if(cols[26] == null || cols[26].equals("") || cols[26].equals("#N/A")
				|| cols[27] == null || cols[27].equals("") || cols[27].equals("#N/A"))
			return c;
		c.setType("正式");
		c.setStartdate(HRUtil.parseDate(cols[26], "yyyy/MM/dd"));
		c.setEnddate(HRUtil.parseDate(cols[27], "yyyy/MM/dd"));
		c.setCreatedate(c.getStartdate());
		return c;
	}
	
	/**
	 * Only use to extract departments and positions
	 * @param bean
	 */
	public void addList(HRBean bean){
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 20 || cols[0].trim().equals("") || !Character.isDigit(cols[2].charAt(0))){
				continue;
			}
			String department = cols[11];
			String position = cols[12];
			
			Department d = bean.getDepartmentByName(department);
			if(d == null){
				bean.saveDepartment(new Department(department));
				System.out.println("Department Added:"+department);
			}
			Position p = bean.getPositionByName(position);
			if(p == null){
				bean.savePosition(new Position(position));
				System.out.println("Position Added:"+position);
			}
		}
	}
	
	public String checkInsertedIds(HRBean bean){
		String non="";
		while(hasNextLine()){
			String id = strLine.trim();
			boolean exist  = bean.isEmployeeWorkerIdExist(id);
			if(!exist){
				non +=id+"\n";
			}
		}
		return non;
	}
	
	public boolean isValidColForEmployeeSheet(String[] cols){
		if(cols.length < 1){
			return false;
		}
		if(cols[0].length() < 4){
			return false;
		}
		if(cols[0].trim().equals("")){
			return false;
		}
		String str = cols[0].trim();
		for(int i=0; i<str.length();i++){
			if(!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
	}
	
	public int getIndex(){
		return this.index;
	}
}
