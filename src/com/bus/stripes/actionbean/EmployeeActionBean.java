package com.bus.stripes.actionbean;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.selector.EmployeeSelector;
import com.bus.test.data.TestData;
import com.bus.util.ExcelFileSaver;
import com.bus.util.ExcelFileWriter;
import com.bus.util.Roles;
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
public class EmployeeActionBean extends CustomActionBean implements ValidationErrorHandler{
	
	private FileBean employeeexcel;
	private FileBean checkIds;
	private FileBean drivers;
	private FileBean coordinatorfile;
	private Employee employee;
	private EmployeeSelector employeeselector;
	private Contract contract;
	private List<SelectBoxOption> contracttype;
	private List<SelectBoxOption> domiciletypes;
	private List<Employee> employeeList = new ArrayList<Employee>();
	private List<SelectBoxOption> marriage;
	private List<SelectBoxOption> ethnic;
	private List<SelectBoxOption> politicalStatus;
	private List<SelectBoxOption> qualification;
	private List<SelectBoxOption> department;
	private List<SelectBoxOption> joblevel;
	private List<SelectBoxOption> workertype;
	private List<SelectBoxOption> position;
	
	
	private List<Employee> employees = new ArrayList<Employee>();
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	private Long recordsTotal;
	
	private Date resigndate;
	
	
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
		this.domiciletypes = SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(2));
		this.marriage = SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(4));
		this.ethnic = SelectBoxOptions.getEthnics(bean.getEthnicList());
		this.politicalStatus= SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(1));
		this.qualification= SelectBoxOptions.getQualification(bean.getQualificationList());
		this.joblevel= SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(3));
		this.workertype= SelectBoxOptions.getWorkerType(bean.getWorkertypeList());
		this.contracttype = SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(5));
	}
	
	
	
	public void initData(){
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
			setRecordsTotal(bean.countEmployees());
			setEmployees(bean.getEmployees(pagenum, lotsize));
		}else{
			String statement  = employeeselector.getSelectorStatement();
//			System.out.println(statement);
			Map<String, Object> map = bean.getEmployeesBySelector(pagenum,lotsize,statement);
			setRecordsTotal(((Long)map.get("count")));
			setEmployees((List<Employee>)map.get("list"));
		}
		setTotalcount(getRecordsTotal()/lotsize +1);
	}
	
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYEE_VIEW)
	public Resolution defaultAction(){
		loadOptionList();
		initData();
//		employee = TestData.getEmployeeTestData();
		return new ForwardResolution("/hr/employee.jsp").addParameter("pagenum", pagenum);
	}
	
	@HandlesEvent(value = "create")
	@Secure(roles=Roles.EMPLOYEE_EDIT)
	public Resolution create() {
		employee.setAccount(context.getUser());
		bean.saveEmployee(employee);
        return new StreamingResolution("text;charset=utf-8", new StringReader("新建档案成功"));
    }
	
	@HandlesEvent(value="delete")
	@Secure(roles=Roles.EMPLOYEE_RM)
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
	@Secure(roles=Roles.EMPLOYEE_VIEW_DETAIL)
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
	@Secure(roles=Roles.EMPLOYEE_EDIT)
	public Resolution edit(){
		bean.updateEmployee(employee);
		return new StreamingResolution("text;charset=utf-8", "修改成功");
	}
	
	@HandlesEvent(value="createcontract")
	@Secure(roles=Roles.EMPLOYEE_ADD_CONTRACT)
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
	@Secure(roles=Roles.EMPLOYEE_DATAFILE_UPLOAD)
	public Resolution employeefileupload(){
//		String info = "";
		try{
			if(employeeexcel != null){
				//departments and positions
				System.out.println("Content-type is :"+employeeexcel.getContentType());
				ExcelFileSaver saverAddList  = new ExcelFileSaver(((FileInputStream)employeeexcel.getInputStream()));
				saverAddList.addList(bean);
				
				ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)employeeexcel.getInputStream()));
				String str = saver.saveEmployees(bean,context.getUser());
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:在职员工资料导入</span>")
							.addParameter("description", "这些编号的员工没有被上传:<br/>\n" + str);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return defaultAction();
	}
	
	@HandlesEvent(value="employeeresignfileupload")
	@Secure(roles=Roles.EMPLOYEE_DATAFILE_UPLOAD)
	public Resolution employeeresignfileupload(){
		try{
			if(employeeexcel != null){
				//departments and positions
				ExcelFileSaver saverAddList  = new ExcelFileSaver(((FileInputStream)employeeexcel.getInputStream()));
				saverAddList.addList(bean);
				
				ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)employeeexcel.getInputStream()));
				String str = saver.resignedEmployees(bean,context.getUser());
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:离职员工资料导入</span>")
							.addParameter("description", "这些编号的员工没有被上传:<br/>\n" + str);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="resignemployeecoordination")
	@Secure(roles=Roles.EMPLOYEE_DATAFILE_UPLOAD)
	public Resolution resignemployeecoordination(){
		try{
			if(coordinatorfile != null){
				ExcelFileSaver saver  = new ExcelFileSaver(((FileInputStream)coordinatorfile.getInputStream()));
				String str = saver.resignedEmployeesCoordination(bean,context.getUser());
				if(!str.equals("")){
					return new ForwardResolution("/actionbean/Error.action").addParameter("error", "<span style='color:red;'>出错:员工编号不存在</span>")
							.addParameter("description", "这些编号的员工离职没有上传:" + str);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	@HandlesEvent(value="checkids")
	@Secure(roles=Roles.EMPLOYEE_IDCHECK_FILE_UPLOAD)
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
	@Secure(roles=Roles.EMPLOYEE_DATAFILE_UPLOAD)
	public Resolution driverlisences(){
		try{
			if(drivers != null){
				ExcelFileSaver saverDepartments  = new ExcelFileSaver(((FileInputStream)drivers.getInputStream()));
				saverDepartments.addDepartmentsFromDrivers(bean);
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
	
	@HandlesEvent(value = "coordinatorfileupload")
	@Secure(roles=Roles.EMPLOYEE_COOR_FILE_UPLOAD)
	public Resolution coordinatorfileupload(){
		try{
			if(coordinatorfile != null){
				ExcelFileSaver saverCoor = new ExcelFileSaver(((FileInputStream)coordinatorfile.getInputStream()));
				saverCoor.addDepartmentsAndPositionsFromCoordination(bean);
				
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
	@Secure(roles=Roles.EMPLOYEE_IDCARDS_VIEW)
	public Resolution idcards(){
		String targetId = context.getRequest().getParameter("targetId");
		return new ForwardResolution("/actionbean/IDCards.action").addParameter("targetId", targetId);
	}
	
	@HandlesEvent(value="resign")
	@Secure(roles=Roles.EMPLOYEE_RESIGN)
	public Resolution resign(){
		String targetId = context.getRequest().getParameter("targetId");
		String remark  = context.getRequest().getParameter("remark");
		if(resigndate == null)
			return defaultAction();
		else{
			bean.resignEmployee(targetId, resigndate, remark);
			return defaultAction();
		}
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
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long long1) {
		this.recordsTotal = long1;
	}
	public Date getResigndate() {
		return resigndate;
	}
	public void setResigndate(Date resigndate) {
		this.resigndate = resigndate;
	}
	
	
	@HandlesEvent(value="downloadcurrentform")
	@Secure(roles=Roles.EMPLOYEE_DATA_DOWNLOAD)
	public Resolution downloadCurrentForm(){
		ExcelFileWriter writer = new ExcelFileWriter();
		String statement = "";
		if(employeeselector != null)
			statement = employeeselector.getSelectorStatement();
		else
			statement = "SELECT q FROM Employee q WHERE status='A'";
		System.out.println(statement);
		String content = writer.writeSelectedEmployees(bean,statement);
		StreamingResolution sr = new StreamingResolution("text/csv",content);
		sr.setAttachment(true);
		sr.setCharacterEncoding("UTF-8");
		sr.setFilename("selectedEmployees.csv");
		return sr;
	}
	
	@HandlesEvent(value="getnamebyid")
	public Resolution getnamebyid(){
		String workerId = context.getRequest().getParameter("workerid");
//		System.out.println(workerId);
		Employee e = bean.getEmployeeByWorkerId(workerId);
		return new StreamingResolution("text;charset=utf-8", e.getFullname());
	}
	
	@HandlesEvent(value="getnewworkerid")
	public Resolution getnewworkerid(){
		List<String> workerids = bean.getCurrentDocumentIds();
		int documentid = 1;
		boolean next = true;
		while(next && documentid < 10000){
			String did = ""+documentid;
			while(did.length()<4)
				did = "0"+did;
			boolean exist = false;
			for(int i=0;i<workerids.size();i++){
				if(did.equals(workerids.get(i))){
					exist = true;
					break;
				}
				else
					continue;
			}
			if(exist){
				documentid++;
				continue;
			}
			if(bean.isEmployeeWorkerIdExist("10"+did)){
				documentid++;
				continue;
			}else{
				next =false;
			}
		}
		String did = ""+documentid;
		while(did.length()<4)
			did = "0"+did;
		return new StreamingResolution("text;charset=utf-8", did);
	}
	
	@HandlesEvent(value="checkworkerid")
	public Resolution checkworkerid(){
		String workerId = context.getRequest().getParameter("workerid");
//		System.out.println(workerId);
		boolean exist = bean.isEmployeeWorkerIdExist(workerId);
		if(exist)
			return new StreamingResolution("text/html;charset=utf-8", "1");
		else
			return new StreamingResolution("text/html;charset=utf-8", "0");
	}

}
