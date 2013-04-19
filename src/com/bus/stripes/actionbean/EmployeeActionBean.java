package com.bus.stripes.actionbean;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.services.HRBean;
import com.bus.stripes.selector.EmployeeSelector;
import com.bus.test.data.TestData;
import com.bus.util.ExcelFileSaver;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

@UrlBinding("/actionbean/Employee.action")
public class EmployeeActionBean implements ActionBean, ValidationErrorHandler{
	
	
	private MyActionBeanContext context;
	public MyActionBeanContext getContext() { return context; }
	public void setContext(ActionBeanContext context) { this.context = (MyActionBeanContext)context; }
	
	private FileBean employeeexcel;
	private FileBean checkIds;
	private FileBean drivers;
	private FileBean coordinatorfile;
	private Employee employee;
	private EmployeeSelector employeeselector;
	private Contract contract;
	private List<SelectBoxOption> contracttype = SelectBoxOptions.getContractType();
	private List<SelectBoxOption> domiciletypes = SelectBoxOptions.getDomicileType();
	private List<Employee> employeeList = new ArrayList<Employee>();
	private List<SelectBoxOption> marriage = SelectBoxOptions.getMarriage();
	private List<SelectBoxOption> ethnic = SelectBoxOptions.getEthnics();
	private List<SelectBoxOption> politicalStatus= SelectBoxOptions.getPoliticalStatus();
	private List<SelectBoxOption> qualification= SelectBoxOptions.getQualification();
	private List<SelectBoxOption> department;
	private List<SelectBoxOption> joblevel= SelectBoxOptions.getJobLevel();
	private List<SelectBoxOption> workertype= SelectBoxOptions.getWorkerType();
	private List<SelectBoxOption> position;
	
	
	private List<Employee> employees = new ArrayList<Employee>();
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	
	
	private HRBean bean;
	@SpringBean
	protected void setBean(HRBean bean){
		this.bean = bean;
	}
	protected HRBean getBean(){
		return this.bean;
	}
	
	
	public List<Employee> getEmployeeList() {
		return employeeList;
	}
	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public List<SelectBoxOption> getMarriage() {
		return marriage;
	}
	public List<SelectBoxOption> getEthnic() {
		return ethnic;
	}
	public List<SelectBoxOption> getPoliticalStatus() {
		return politicalStatus;
	}
	public List<SelectBoxOption> getQualification() {
		return qualification;
	}
	public List<SelectBoxOption> getDepartment() {
		return department;
	}
	public List<SelectBoxOption> getJoblevel() {
		return joblevel;
	}
	public List<SelectBoxOption> getWorkertype() {
		return workertype;
	}
	public List<SelectBoxOption> getPosition() {
		return position;
	}
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	public int getPagenum(){
		return pagenum;
	}
	public int getLotsize(){
		return lotsize;
	}
	public void setPagenum(int pagenum){
		this.pagenum = pagenum;
	}
	public void setLotsize(int lotsize){
		this.lotsize = lotsize;
	}
	public Long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(Long totalcount) {
		this.totalcount = totalcount;
	}
	
	private void loadOptionList(){
		this.department = SelectBoxOptions.getDepartment(bean.getAllDepartment());
		this.position= SelectBoxOptions.getPosition(bean.getAllPosition());
	}
	
	
	
	public void initData(){
		loadOptionList();
		if(pagenum <= 0 || lotsize <= 0){
			pagenum = 1;
			lotsize = 20;
		}
		getEmployeesBySelector();
		if(pagenum > totalcount)
			pagenum = Integer.parseInt(totalcount.toString());
		
	}
	private void getEmployeesBySelector() {
		if(employeeselector == null){
			setTotalcount(bean.countEmployees()/lotsize +1);
			setEmployees(bean.getEmployees(pagenum, lotsize));
		}else{
			String statement  = employeeselector.getSelectorStatement();
			System.out.println(statement);
			Map<String, Object> map = bean.getEmployeesBySelector(pagenum,lotsize,statement);
			setTotalcount((((Long)map.get("count"))/lotsize +1));
			setEmployees((List<Employee>)map.get("list"));
		}
	}
	@DefaultHandler
	public Resolution defaultAction(){
		initData();
		employee = TestData.getEmployeeTestData();
		return new ForwardResolution("/index.jsp").addParameter("pagenum", pagenum);
	}
	
	@HandlesEvent(value = "create")
	public Resolution create() {
		employee.setAccount(context.getUser());
		bean.saveEmployee(employee);
        return new StreamingResolution("text;charset=utf-8", new StringReader("新建档案成功"));
    }
	
	@HandlesEvent(value="delete")
	public Resolution delete(){
		Employee d = new Employee();
		String targetId = context.getRequest().getParameter("targetId");
		d.setId(Integer.parseInt(targetId));
		if(d.getId() == null){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "Delete Fail").addParameter("description", "This employee may have already assigned to some user function tables or there is server error during deletion. Please contact administrator for further operation.");
		}else if(bean.deleteEmployee(d)){
			return defaultAction();
		}else{
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "Delete Fail").addParameter("description", "This employee may have already assigned to some user function tables or there is server error during deletion. Please contact administrator for further operation.");
		}
	}
	
	@HandlesEvent(value="detail")
	public Resolution detail(){
		loadOptionList();
		String id = context.getRequest().getParameter("targetId");
		this.employee = bean.getEmployeeById(id);
		if(this.employee == null){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "Get information for employee fail").addParameter("description", "Getting information for employee id " +id+" fail, maybe don't have enough permission, please contact the administrator.");
		}else{
			return new ForwardResolution("/hr/employeedetail.jsp");
		}
	}
	
	@HandlesEvent(value="edit")
	public Resolution edit(){
		bean.updateEmployee(employee);
		return new StreamingResolution("text;charset=utf-8", "修改成功");
	}
	
	@HandlesEvent(value="createcontract")
	public Resolution createcontract(){
		String id = context.getRequest().getParameter("targetId");
		Employee e = new Employee();
		e.setId(Integer.parseInt(id));
		contract.setEmployee(e);
		bean.saveContract(contract);
		return new StreamingResolution("text;charset=utf-8", "新建合同成功" );
	}
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	@HandlesEvent(value="prevpage")
	public Resolution prevpage(){
		pagenum--;
		return defaultAction();
	}
	
	@HandlesEvent(value="nextpage")
	public Resolution nextpage(){
		pagenum++;
		return defaultAction();
	}
	
	@HandlesEvent(value="employeefileupload")
	public Resolution employeefileupload(){
//		String info = "";
		try{
			if(employeeexcel == null){
				return defaultAction();
			}
			ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)employeeexcel.getInputStream()));
//			saver.addList(bean); //only use for extracting departments and postions to database
			while(saver.hasNextLine()){
				Map<String,Object> map = saver.getEmployeeObjects();
				String err = (String) map.get("error");
				if(err != null){
					continue;
				}
				Employee e  = (Employee) map.get("employee");
				if(bean.employeeExist(e.getFullname(), e.getDocumentkey())){
					continue;
				}
				e.setAccount(context.getUser());
				Contract c = (Contract) map.get("contract");
				bean.createEmployeeAndContract(e,c);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return defaultAction();
	}
	
	@HandlesEvent(value="employeeresignfileupload")
	public Resolution employeeresignfileupload(){
		try{
			if(employeeexcel != null){
				ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)employeeexcel.getInputStream()));
				String str = saver.resignedEmployees(bean,context.getUser());
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:员工编号不存在</span>")
							.addParameter("description", "这些编号的员工没有被上传:" + str);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="checkids")
	public Resolution checkids(){
		try{
			if(checkIds != null){
				ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)checkIds.getInputStream()));
				String str = saver.checkInsertedIds(bean);
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:员工编号不存在</span>")
							.addParameter("description", "这些编号的员工没有被上传:<br/>\n" + str);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="driverlisences")
	public Resolution driverlisences(){
		try{
			if(drivers != null){
				ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)drivers.getInputStream()));
				String str = saver.getDriverIds(bean);
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:证件没添加成功</span>")
							.addParameter("description", "这些编号的员工没有被上传:<br/>\n" + str);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value = "coordinatorfile")
	public Resolution coordinatorfile(){
		try{
			if(coordinatorfile != null){
				ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)coordinatorfile.getInputStream()));
				String str = saver.getCoordinations(bean,context.getUser());
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:调动没添加成功</span>")
							.addParameter("description", "这些编号的员工没有被上传:<br/>\n" + str);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="idcards")
	public Resolution idcards(){
		String targetId = context.getRequest().getParameter("targetId");
		return new ForwardResolution("/actionbean/IDCards.action").addParameter("targetId", targetId);
	}
	
	@ValidationMethod(on="create")
	public void avoidCreateNullEmployee(ValidationErrors errors){
		if(this.employee.getIdentitycode() == null || this.employee.getIdentitycode().equals("")){
			errors.add("employee.identitycode", new SimpleError("必须输入身份证"));
		}
	}
	
	@Override
	public Resolution handleValidationErrors(ValidationErrors errors)
			throws Exception {
		StringBuilder message = new StringBuilder();

	    for (List<ValidationError> fieldErrors : errors.values()) {
	        for (ValidationError error : fieldErrors) {
	            message.append("<div class=\"error\">");
	            message.append(error.getMessage(getContext().getLocale()));
	            message.append("</div>");
	        }
	    }
	    return new StreamingResolution("text;charset=utf-8", new StringReader(message.toString()));
	}
	public Contract getContract() {
		return contract;
	}
	public void setContract(Contract contract) {
		this.contract = contract;
	}
	public List<SelectBoxOption> getContracttype() {
		return contracttype;
	}
	public void setContracttype(List<SelectBoxOption> contracttype) {
		this.contracttype = contracttype;
	}
	public EmployeeSelector getEmployeeselector() {
		return employeeselector;
	}
	public void setEmployeeselector(EmployeeSelector employeeselector) {
		this.employeeselector = employeeselector;
	}
	public FileBean getEmployeeexcel() {
		return employeeexcel;
	}
	public void setEmployeeexcel(FileBean employeeexcel) {
		this.employeeexcel = employeeexcel;
	}
	public List<SelectBoxOption> getDomiciletypes() {
		return domiciletypes;
	}
	public void setDomiciletypes(List<SelectBoxOption> domiciletypes) {
		this.domiciletypes = domiciletypes;
	}
	public FileBean getCheckIds() {
		return checkIds;
	}
	public void setCheckIds(FileBean checkIds) {
		this.checkIds = checkIds;
	}
	public FileBean getDrivers() {
		return drivers;
	}
	public void setDrivers(FileBean drivers) {
		this.drivers = drivers;
	}
	public FileBean getCoordinatorfile() {
		return coordinatorfile;
	}
	public void setCoordinatorfile(FileBean coordinatorfile) {
		this.coordinatorfile = coordinatorfile;
	}
	
	
	
	
}