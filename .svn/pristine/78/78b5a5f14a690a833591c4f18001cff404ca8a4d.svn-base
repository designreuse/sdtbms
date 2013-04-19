package com.bus.test.data;

import java.util.Date;
import java.util.Random;

import com.bus.dto.Account;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.Hrimage;
import com.bus.dto.Position;

public class TestData {

	static Random r = new Random();
	
	public static Employee getEmployeeTestData(){
		Employee e = new Employee();
		Department d = new Department();
		Position p = new Position();
		Account a = new Account();
		d.setId(1);
		p.setId(1);
		a.setId(1);
		e.setDepartment(d);
		e.setDob(new Date());
		e.setDocumentkey("123123");
		e.setEmail("email@jianxing.com");
		e.setEthnic("1");
		e.setFirstworktime(new Date());
		
		
		e.setFullname("Darren leung" + r.nextInt(1000));
		e.setHomeaddress("asdasdasd address");
		e.setHomenumber("12312313");
		e.setIdentitycode("123123");
		e.setJoblevel("1");
		e.setMarriage("1");
		e.setMobilenumber("12312555");
		e.setPob("here");
		e.setPoliticalstatus("1");
		e.setPosition(p);
		e.setAccount(a);
		e.setPostcode("111111");
		e.setQualification("1");
		e.setRemark("");
		e.setSex("1");
		e.setTimeofjoinrpc(new Date());
		e.setWorkage(23);
		e.setWorkerid("444");
		e.setWorkertype("1");
		e.setStatus("A");
		Hrimage i = new Hrimage();
		i.setId(1);
		e.setHrimage(i);
		return e;
	}
	
	public static Department getDepartmentData(){
		Department d = new Department();
		d.setName("Department "+ r.nextInt(1000));
		d.setRemark("remark "+ r.nextInt(10000));
		return d;
	}

	public static Position getPositionData() {
		Position p = new Position();
		p.setName("Position" + r.nextInt(1000));
		p.setRemark("remark");
		return p;
	}
}
