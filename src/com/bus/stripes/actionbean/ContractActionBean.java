package com.bus.stripes.actionbean;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import security.action.Secure;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Employee;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.stripes.selector.ContractSelector;
import com.bus.stripes.selector.EmployeeSelector;
import com.bus.util.Roles;
import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

public class ContractActionBean extends CustomActionBean implements Permission{

	
	private HRBean bean;
	private List<Contract> contracts  = new ArrayList<Contract>();
	private Contract contract;
	private List<SelectBoxOption> contracttype;
	
	private EmployeeSelector employeeselector;
	private ContractSelector contractselector;
	
	private int pagenum;
	private int lotsize;
	private Long totalcount;
	
	public List<Contract> getContracts() {
		return contracts;
	}
	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public HRBean getBean() {
		return bean;
	}
	
	@SpringBean
	public void setBean(HRBean bean) {
		this.bean = bean;
	}

	
	private void loadOptionList(){
		this.contracttype = SelectBoxOptions.getSelectBoxFromFixOptions(bean.getOptionListById(5));
	}
	
	private void initData(){
		loadOptionList();
		if(pagenum <= 0 || lotsize <= 0){
			pagenum = 1;
			lotsize = 20;
		}
		setTotalcount(bean.countContracts()/lotsize +1);
		getContractsFromSelector();
		if(pagenum > totalcount)
			pagenum = Integer.parseInt(totalcount.toString());
	}
	
	private void getContractsFromSelector() {
		if(employeeselector == null && contractselector == null)
			setContracts(bean.getContracts(pagenum, lotsize));
		else{
			if(employeeselector != null && employeeselector.getName() != null){
				setContracts(bean.getContractsByName(pagenum, lotsize, employeeselector.getName()));
			}else if(contractselector != null){
				String statement = contractselector.getSelectorStatement();
				try{
//					statement = URLDecoder.decode(statement, "UTF-8");
					setContracts(bean.getContractsBySelector(pagenum,lotsize,statement));
//					System.out.println("Using statement:"+statement);
				}catch(Exception e){
					e.printStackTrace();
					setContracts(bean.getContracts(pagenum, lotsize));
				}
			}else{
				setContracts(bean.getContracts(pagenum, lotsize));
			}
		}
	}
	
	@DefaultHandler
	@Secure(roles=Roles.EMPLOYEE_VIEW_CONTRACT)
	public Resolution defaultAction(){
		initData();
		return new ForwardResolution("/hr/contract.jsp").addParameter("pagenum", pagenum);
	}
	
	@HandlesEvent(value="viewall")
	@Secure(roles=Roles.EMPLOYEE_VIEW_CONTRACT)
	public Resolution viewall(){
		String id = context.getRequest().getParameter("targetId");
		setContracts(bean.getContractsByEmployeeId(Integer.parseInt(id)));
		return new ForwardResolution("/hr/viewcontracts.jsp");
	}
	
	@HandlesEvent(value="edit")
	@Secure(roles=Roles.EMPLOYEE_EDIT_CONTRACT)
	public Resolution edit(){
		bean.updateContract(contract);
		return new StreamingResolution("text;charset=utf-8","合同修改成功");
	}
	
	@HandlesEvent(value="delete")
	@Secure(roles=Roles.EMPLOYEE_RM_CONTRACT)
	public Resolution delete(){
		String id = context.getRequest().getParameter("targetId");
		bean.removeContract(Integer.parseInt(id));
		return new StreamingResolution("text;charset=utf-8","合同已经删除");
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
	
	@HandlesEvent(value="filter")
	public Resolution filter(){
		return defaultAction();
	}
	
	public Long getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(long l) {
		this.totalcount = l;
	}
	public int getLotsize() {
		return lotsize;
	}
	public void setLotsize(int lotsize) {
		this.lotsize = lotsize;
	}
	public int getPagenum() {
		return pagenum;
	}
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
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
	public ContractSelector getContractselector() {
		return contractselector;
	}
	public void setContractselector(ContractSelector contractselector) {
		this.contractselector = contractselector;
	}
}
