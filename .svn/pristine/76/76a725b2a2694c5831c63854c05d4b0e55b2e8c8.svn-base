package com.bus.controller;

import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bus.dto.Employee;
 
@Controller
@RequestMapping("/hr")
public class HrController {
 
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public @ResponseBody Employee getMovie(@PathVariable String name) {
		ApplicationContext appContext = 
		    	  new ClassPathXmlApplicationContext("config/BeanLocations.xml");
//		 
//		    	EmployeeDAOO stockBo = (EmployeeDAOO)appContext.getBean("employeeDaoo",EmployeeDAOOImpl.class);
		 
		    	/** insert **/
		    	Employee stock = new Employee();
		    	Random r = new Random();
		    	
		    	stock.setEmail("asdasd");
//		    	stockBo.save(stock);
//		model.addAttribute("movie", name);
//		return stock.toString();
		return stock;
 
	}
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody String getDefaultMovie(ModelMap model) {
		ApplicationContext appContext = 
		    	  new ClassPathXmlApplicationContext("config/BeanLocations.xml");
		 
//		    	EmployeeDAOO stockBo = (EmployeeDAOO)appContext.getBean("employeeDaoo",EmployeeDAOOImpl.class);
		 
		/** select **/
//    	Employee stock2 = (Employee) stockBo.findById(5);
//		return stock2.toString();
		    	return "success";
 
	}
 
}
