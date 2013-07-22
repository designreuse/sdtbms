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
			if(strLine == null){
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
	
	public String saveEmployees(HRBean bean, Account act){
		String str="";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 2){
				str += strLine +"<br/>\n";
				continue;
			}
			if(cols.length < 15){
				str += "E-"+cols[1] +"<br/>\n";
			}
			if(null != bean.employeeExist(cols[1].trim(),null)){
				str += "H-"+cols[1]+"<br/>\n";
				continue;
			}
			try{
				Employee e = getEmployeeFromRow(cols);
				e.setStatus("A");
				e.setAccount(act);
				Contract c = getContactFromRow(cols);
				c.setStatus("A");
				e = bean.createEmployeeAndContract(e, c);
				
			}catch(Exception e){
				String firstline = e.getStackTrace()[0].toString();
				str += "N-" + cols[1]+ " Err:" + e.getMessage() + "<br/>\n"+firstline+"<br/>\n";
				continue;
			}
		}
		return str;
	}
	
	/**
	 * For resign employees
	 * @param bean
	 * @return
	 */
	public String resignedEmployees(HRBean bean, Account act){
		String str="";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 2){
				str += strLine +"<br/>\n";
				continue;
			}
			if(cols.length < 15){
				str += "E-"+cols[1] +"<br/>\n";
			}
			if(null != bean.employeeExist(cols[1].trim(),null)){
				str += "H-"+cols[1]+"<br/>\n";
				continue;
			}
			try{
				Employee e = getEmployeeFromRow(cols);
				e.setStatus("E");
				e.setAccount(act);
				Contract c = getContactFromRow(cols);
				c.setStatus("E");
				e = bean.createEmployeeAndContract(e, c);
				
				if(cols.length > 30  && !cols[30].trim().equals("")){
					Promoandtransfer coor = new Promoandtransfer();
					coor.setType("离职");
					coor.setEmployee(e);
					coor.setMovedate(HRUtil.parseDate(cols[30], "yyyy/MM/dd"));
					coor.setActivedate(HRUtil.parseDate(cols[30], "yyyy/MM/dd"));
					if(cols.length > 31){
						coor.setRemark(cols[31]);
					}
					bean.saveCoordination(coor);
				}else{
					str += "NLEAVE- Info: 离职调动没创建成功。<br/>\n";
				}
			}catch(Exception e){
				String firstline = e.getStackTrace()[0].toString();
				str += "N-" + cols[1]+ " Err:" + e.getMessage() + ".Trace:"+firstline+"<br/>\n";
				continue;
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
		
		String str="<br/>\n";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			try {
			if(cols.length < 8){
				str += strLine + "<br/>\n";
				continue;
			}
			String workerid = "";
			if(cols[1].trim().length() < 6){
				workerid = cols[1].trim();
				while(workerid.length() < 5){
					workerid = "0" + workerid;
				}
				workerid = "1" + workerid;
			}
			Employee e = null;
			e = bean.employeeExist(cols[0].trim(),null);
			if(null == e){
				str += "NH-"+cols[0] + "-"+workerid+ "<br/>\n";
				continue;
			}else{
				if(bean.confirmDuplicated(e)){
					e = bean.employeeExist(cols[0].trim(),workerid);
					if (e == null){
						str += "NH*-"+cols[0] + "-"+workerid+ "<br/>\n";
						continue;
					}
				}
			}
			Idmanagement driverLicense = null;
			Idmanagement serviceLicense = null;
			Idmanagement service = null;
			
			if(cols.length > 7){
				driverLicense = new Idmanagement();
				driverLicense.setEmployee(e);
				driverLicense.setType("驾驶证");
				driverLicense.setNumber(cols[7]);
				if(cols.length > 8)
					driverLicense.setRemark(cols[8]+";");
				if(cols.length > 9 && !cols[9].trim().equals(""))
					driverLicense.setValidfrom(HRUtil.parseDate(cols[9], "yyyy.MM.dd"));
				if(cols.length > 10 && !cols[10].trim().equals(""))
					driverLicense.setExpiredate(HRUtil.parseDate(cols[10], "yyyy.MM.dd"));
			}
			
			if(cols.length > 11){
				serviceLicense = new Idmanagement();
				serviceLicense.setEmployee(e);
				serviceLicense.setType("从业资格证");
				serviceLicense.setNumber(cols[11]);
				if(cols.length > 12)
					serviceLicense.setRemark(cols[12]);
				if(cols.length > 13 && !cols[13].equals("")){
					serviceLicense.setExpiredate(HRUtil.parseDate(cols[13], "yyyy.MM.dd"));
				}
			}
				
			if(cols.length > 14){
				service = new Idmanagement();
				service.setEmployee(e);
				service.setType("服务资格证");
				service.setNumber(cols[14].trim());
			}
			
			bean.saveIdsFromFile(driverLicense, serviceLicense,service);
			
			} catch (Exception e) {
				str += "N-"+cols[0]+"-"+e.getMessage()+"<br/>\n";
			}
		}
		return str;
	}
	
	public void addDepartmentsAndPositionsFromCoordination(HRBean bean) throws Exception{
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 7){
				continue;
			}
			String department1 = cols[2];
			String position1 = cols[3];
			String department2 = cols[5];
			String position2 = cols[6];
			
			addDepartment(bean,department1);
			addDepartment(bean,department2);
			addPosition(bean, position1);
			addPosition(bean, position2);
		}
	}
	
	public Position addPosition(HRBean bean, String name) throws Exception{
		try{
			return bean.getPositionByName(name);
		}catch(Exception e){
			bean.savePosition(new Position(name));
			System.out.println("Position Added:"+name);
			return null;
		}
	}
	
	public Department addDepartment(HRBean bean, String name) throws Exception{
		try{
			return bean.getDepartmentByName(name);
		}catch(Exception e){
			bean.saveDepartment(new Department(name));
			System.out.println("Department Added:"+name);
			return null;
		}
	}
	
	public String getCoordinations(HRBean bean, Account creator){
		String str="";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			try {
				if(cols.length < 7){
					str+= "E-"+strLine+"<br/>\n";
					continue;
				}
				String workerid = cols[1].trim();
				if(cols[1].trim().length() < 6){
					while(workerid.length() < 5){
						workerid = "0" + workerid;
					}
					workerid = "1" + workerid;
				}
				Employee employee = bean.getEmployeeByNameAndWorkerid(cols[0].trim(),workerid);
				if(employee == null){
					str += "NH-"+cols[0]+"<br/>\n";
					continue;
				}
				
				Promoandtransfer coordinator = new Promoandtransfer();
				coordinator.setEmployee(employee);
				if(!cols[2].trim().equals("")){
					Department department = bean.getDepartmentByName(cols[2].trim());
					coordinator.setPredepartment(department);
				}
				
				if(!cols[3].trim().equals("")){
					Position position = bean.getPositionByName(cols[3].trim());
					coordinator.setPreposition(position);
				}
				
				if(!cols[5].trim().equals("")){
					Department department = bean.getDepartmentByName(cols[5].trim());
					coordinator.setCurdepartment(department);
				}
				
				if(!cols[6].trim().equals("")){
					Position position = bean.getPositionByName(cols[6].trim());
					coordinator.setCurposition(position);
				}
				
				
				if(cols.length > 7 && !cols[7].equals(""))
					coordinator.setMovedate(HRUtil.parseDate(cols[7], "yyyy/MM/dd"));
				if(cols.length > 8 && !cols[8].equals(""))
					coordinator.setActivedate(HRUtil.parseDate(cols[8], "yyyy/MM/dd"));
				coordinator.setCreator(creator);
				coordinator.setType("调动");
				bean.saveCoordination(coordinator);
			} catch (Exception e) {
				e.printStackTrace();
				str += "N-"+cols[0]+e.getMessage()+"<br/>\n";
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
			List<Employee> em = bean.getEmployeeByName(name, true);
			if(em != null && em.size() == 1){
				e = em.get(0);
				return e;
			}
		}
		return null;
	}

	public Employee getEmployeeFromRow(String[] cols) throws Exception{
		try{
		Employee e = new Employee();
		if(cols[0].trim().equals(""))
			e.setDocumentkey("O-"+cols[2].trim());
		else
			e.setDocumentkey(cols[0].trim());
		e.setFullname(cols[1].trim());
		e.setWorkerid(cols[2].trim());
		if(!cols[3].trim().equals(""))
			e.setFirstworktime(HRUtil.parseDate(cols[3], "yyyy-MM-dd"));
		
		e.setIdentitycode(cols[4].trim());
		e.setEthnic(cols[5].trim());
		e.setMarriage(cols[6].trim());
		if(!cols[7].trim().equals(""))
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
		if(cols.length > 15)
			e.setHomeaddress(cols[15].trim());
		if(cols.length > 16)
			e.setMobilenumber(cols[16].trim());
		if(cols.length > 17)
			e.setDomiciletype(cols[17].trim());
		if(cols.length > 18)
			e.setQualification(cols[18].trim());
		if(cols.length > 19)
			e.setColleage(cols[19].trim());
		if(cols.length > 20)	
			e.setMajor(cols[20].trim());
		if(cols.length > 21)
			e.setPoliticalstatus(cols[21].trim());
		
		if(cols.length>22 && !cols[22].trim().equals(""))
			e.setTimeofjoinrpc(HRUtil.parseDate(cols[22], "yyyy-MM-dd"));
		
		if(cols.length > 23)
			e.setWorkertype(cols[23].trim());
		
		//特殊身份
		if(cols.length > 24 && !cols[24].trim().equals("")){
			if(cols[24].trim().equals("干部"))
				e.setArmy("国家干部");
			else
				e.setArmy(cols[24].trim());
		}else{
			e.setArmy("无");
		}
		
		if(cols.length > 25){
			if(cols[25].trim().equals(""))
				e.setJoblevel("非管");
			else
				e.setJoblevel(cols[25].trim());
		}
		
		if(cols.length > 28 && !cols[28].trim().equals(""))
			e.setTransfertime(HRUtil.parseDate(cols[28], "yyyy-MM-dd"));
		
		return e;
		}catch(Exception err){
			throw new Exception("员工资料转换 -"+err.getMessage());
		}
	}
	
	public Contract getContactFromRow(String[] cols) throws Exception{
		try{
		Contract c = new Contract();
		c.setType("正式");
		c.setCreatedate(c.getStartdate());
		if(cols.length > 26 && !cols[26].trim().equals("") && !cols[26].trim().equals("#N/A")){
			c.setStartdate(HRUtil.parseDate(cols[26], "yyyy/MM/dd"));
		}
		if(cols.length > 27 && !cols[27].trim().equals("") && !cols[27].trim().equals("#N/A")){
			c.setEnddate(HRUtil.parseDate(cols[27], "yyyy/MM/dd"));
		}
		if(c.getStartdate() == null && c.getEnddate() == null)
			return c;
		return c;
		}catch(Exception err){
			
			throw new Exception("员工合同转换出错 -"+err.getMessage());
		}
	}
	
	/**
	 * Only use to extract departments and positions
	 * @param bean
	 * @throws Exception 
	 */
	public void addList(HRBean bean) throws Exception{
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 13){
				continue;
			}
			String department = cols[11].trim();
			String position = cols[12].trim();
			
			Department d = null;
			try{
				d = bean.getDepartmentByName(department);
			}catch(Exception e){
				bean.saveDepartment(new Department(department));
				System.out.println("Department Added:"+department);
			}
			
			Position p = null;
			try{
				p = bean.getPositionByName(position);
			}catch(Exception e){
				bean.savePosition(new Position(position));
				System.out.println("Position Added:"+position);
			}
		}
	}
	
	public void addDepartmentsFromDrivers(HRBean bean) throws Exception{
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 14 || cols[0].trim().equals("") || !Character.isDigit(cols[1].charAt(0))){
				continue;
			}
			String department = cols[2].trim();
			
			Department d = bean.getDepartmentByName(department);
			if(d == null){
				bean.saveDepartment(new Department(department));
				System.out.println("Department Added:"+department);
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

	//离职调动
	public String resignedEmployeesCoordination(HRBean bean, Account user) {
		String str="<br/>\n";
		while(hasNextLine()){
			String[] cols = strLine.split(",");
			if(cols.length < 4){
				str += "E-"+strLine+"<br/>\n";
				continue;
			}
			try {
				String name = cols[1].trim();
				String workerid = cols[2].trim();
				if(cols[2].trim().length() < 6){
					while(workerid.length() < 5){
						workerid = "0" + workerid;
					}
					workerid = "1" + workerid;
				}
				Employee  e = bean.getEmployeeByNameAndWorkerid(name, null);
				if(e == null){
					str += "NH-"+cols[1]+"<br/>\n";
					continue;
				}
				Promoandtransfer coor = new Promoandtransfer();
				coor.setType("离职");
				coor.setEmployee(e);
				if(cols.length > 3 && !cols[3].trim().equals("")){
					coor.setMovedate(HRUtil.parseDate(cols[3], "yyyy/MM/dd"));
					coor.setActivedate(HRUtil.parseDate(cols[3], "yyyy/MM/dd"));
				}
				if(cols.length > 4 && !cols[4].trim().equals(""))
					coor.setRemark(cols[4]);
				if(bean.isCoordinatorExistForResign(coor) == null)
					bean.saveCoordination(coor);
			} catch (Exception e) {
				str += "N-"+cols[1]+"<br/>\n";
				continue;
			}
		}
		return str;
	}
}
