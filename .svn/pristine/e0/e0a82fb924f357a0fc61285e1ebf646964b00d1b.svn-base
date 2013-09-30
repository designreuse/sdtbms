package com.bus.util.importfile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.bus.dto.Account;
import com.bus.dto.Employee;
import com.bus.dto.vehicleprofile.VehicleTeam;
import com.bus.services.HRBean;

public class EmployeeImportFile extends ImportFile{
	
	public EmployeeImportFile(FileInputStream fis){
		super(fis);
	}
	
	/**
	 * File must start with: 
	 * 
	 * workerid,name,team,extrastuff
	 * workerid,name,team,extrastuff
	 * 
	 * @param bean
	 * @param creator
	 * @return
	 */
	public String saveDriverTeams(HRBean bean, Account creator){
		String str = "";
		while(hasNextLine()){
			try{
				index++;
				String cols[] = strLine.split(",",-1);
				if(cols.length < 3){
					str += "Index "+ index + " length too short."+strLine+".<br/>";
					continue;
				}
				Employee e = bean.getEmployeeByWorkerId(cols[0]);
				if(e == null){
					List<Employee> el = bean.getEmployeeByName(cols[1], false);
					if(el.size() != 1){
						str += "Index "+ index + " Employee not found."+strLine+".<br/>";
						continue;
					}else{
						e = el.get(0);
					}
				}
				VehicleTeam team = bean.getDriverTeamByName(cols[2]);
				if(team == null){
					str += "Index " + index + " No Such Team Found."+strLine+".<br/>";
					continue;
				}
				e.setTeam(team);
				bean.updateEmployeeOriginal(e);
			}catch(Exception e){
				e.printStackTrace();
				str += e.getMessage() + "<br/>";
			}
		}
		return str;
	}
}
