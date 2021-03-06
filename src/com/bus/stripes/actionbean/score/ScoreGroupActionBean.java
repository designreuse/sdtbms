package com.bus.stripes.actionbean.score;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import security.action.Secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import com.bus.dto.Department;
import com.bus.dto.Employee;
import com.bus.dto.score.DepartmentScore;
import com.bus.dto.score.ScoreDivGroup;
import com.bus.dto.score.ScoreGroupMapper;
import com.bus.dto.score.Scoreapprover;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;
import com.bus.util.EmpDepartments;
import com.bus.util.Roles;
import com.google.gson.JsonObject;

@UrlBinding(value="/actionbean/ScoreGroup.action")
public class ScoreGroupActionBean extends CustomActionBean{

	private ScoreBean scoreBean;
	public ScoreBean getScoreBean(){return this.scoreBean;}
	@SpringBean
	public void setScoreBean(ScoreBean bean){this.scoreBean = bean;}
	
	private HRBean hrBean;
	public HRBean getHrBean(){return this.hrBean;}
	@SpringBean
	public void setHrBean(HRBean bean){this.hrBean = bean;}
	
	private EmpDepartments empList;
	private ScoreDivGroup scoreNewGroup;
	
	@DefaultHandler
	public Resolution defaultAction(){
		try{
			empList = new EmpDepartments();
			empList.setDeptId("0");
			empList.setDept("根目录");
			empList = getChildrens(empList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ForwardResolution("/score/scoregroup.jsp");
	}
	
	//获取积分组的所有子组合员工
	private EmpDepartments getChildrens(EmpDepartments e) throws Exception{
		//获取当前父组的子组以及员工，赋值回父组
		EmpDepartments child = scoreBean.getAllChildrenAndEmployeeForScoreGroup(e);
		//如果子组不为null，则每个子组继续循环直到没有子组为止
		if(child.getExtras() != null && child.getExtras().size() > 0){
			List<EmpDepartments> newChildren = new ArrayList<EmpDepartments>();
			for(EmpDepartments ed: child.getExtras()){
				newChildren.add(getChildrens(ed));
			}
			child.setExtras(newChildren);
		}
		return child;
	}
	
	@HandlesEvent(value="addGroup")
	@Secure(roles=Roles.SCORE_GROUP_EDIT)
	public Resolution addGroup(){
		JsonObject json = new JsonObject();
		try{
			String pid = context.getRequest().getParameter("gid");
			if(pid != null && !pid.equals("0")){
				ScoreDivGroup sdg = new ScoreDivGroup();
				sdg.setId(Integer.parseInt(pid));
				scoreNewGroup.setParent(sdg);
			}
			scoreBean.addScoreDivGroup(scoreNewGroup,context.getUser());
			json.addProperty("result", "1");
			json.addProperty("msg", "添加成功");
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "添加失败."+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf-8;",json.toString());
	}

	@HandlesEvent(value="delGroup")
	@Secure(roles = Roles.SCORE_GROUP_EDIT)
	public Resolution delGroup(){
		JsonObject json = new JsonObject();
		try{
			String gid = context.getRequest().getParameter("gid");
			if(gid == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "删除失败.没有提供组Id");
			}else{
				scoreBean.delScoreDivGroup(gid,context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "删除成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "删除成功."+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf-8;",json.toString());
	}
	
	@HandlesEvent(value="joinGroup")
	@Secure(roles = Roles.SCORE_GROUP_EDIT)
	public Resolution joinGroup(){
		JsonObject json = new JsonObject();
		try{
			String workerids = context.getRequest().getParameter("workerids");
			String gid = context.getRequest().getParameter("gid");
			if(workerids == null || gid == null){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有员工资料或组资料");
			}else{
				scoreBean.joinEmployeeToScoreGroup(workerids,gid,context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "添加成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "添加失败。"+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf-8;",json.toString());
	}
	
	@HandlesEvent(value="quitGroup")
	public Resolution quitGroup(){
		JsonObject json = new JsonObject();
		try{
			String ids = context.getRequest().getParameter("ids");
			if(ids == null ){
				json.addProperty("result", "0");
				json.addProperty("msg", "没有员工资料");
			}else{
				scoreBean.quitEmployeeFromScoreGroup(ids,context.getUser());
				json.addProperty("result", "1");
				json.addProperty("msg", "退出组成功");
			}
		}catch(Exception e){
			e.printStackTrace();
			json.addProperty("result", "0");
			json.addProperty("msg", "退出失败。"+e.getMessage());
		}
		return new StreamingResolution("text/charset=utf-8;",json.toString());
	}
	
	@HandlesEvent(value="getScoreMembersFromDepartment")
	@Secure(roles = Roles.SCORE_GROUP_EDIT)
	public Resolution getScoreMembersFromDepartment(){
		try{
			System.out.println("Coverting from departmentscores To Score_group and Score_group_mapper......");
			List<Department> departments = hrBean.getAllActiveDepartmentWithChildren();
			List<DepartmentScore> dScore = scoreBean.getAllDepartmentScores();
			for(Department d:departments){
//				System.out.println("For department:"+d.getName());
				ScoreDivGroup temp = scoreBean.getScoreDivGroupByName(d.getName());
				if(temp != null) continue; 
				
				ScoreDivGroup sdg = new ScoreDivGroup();
				sdg.setLastUpdateDate(Calendar.getInstance().getTime());
				sdg.setName(d.getName());
				
				DepartmentScore ds = scoreBean.getDepartmentScoresByDepartmetnIdORWorkerId(d.getId(), null);
				if(ds == null){
//					System.out.println("Department Score is null");
					sdg.setAvailable(0F);
					sdg.setBasescore(0F);
				}else{
					sdg.setAvailable(ds.getAvailable());
					sdg.setBasescore(ds.getBasescore());
				}
				
				if(d.getParent() != null){
					ScoreDivGroup sdgParent = new ScoreDivGroup();
					sdgParent = scoreBean.getScoreDivGroupByName(d.getParent().getName());
					sdg.setParent(sdgParent);
				}
				scoreBean.addScoreDivGroup(sdg, context.getUser());
			}
			System.out.println("Score groups created. Now mapper employees to groups.");
			List<Employee> employees = hrBean.getAllEmployee("A");
			int count=0;
			for(Employee e:employees){
				if(count%500 == 0){
					System.out.println(count + " DONE!");
				}
				ScoreDivGroup temp = scoreBean.getScoreDivGroupByName(e.getDepartment().getName());
				if(temp != null)
					scoreBean.joinEmployeeToScoreGroup(e.getWorkerid(), temp.getId()+"", context.getUser());
				count++;
			}
			System.out.println(count + " ALL DONE!");
			
			System.out.println("Now moving data from scoreapprover_old table to scoreapprover...........");
			List<Object[]> results = (List<Object[]>) scoreBean.runQuery("SELECT o.id as oid, o.approver as eid, d.name as dname, o.isapprover as approver FROM scoreapprover_old o LEFT JOIN department d ON o.departmentid=d.id ORDER BY o.id");
			for(Object[] objs:results){
				if(objs[2] == null)
					continue;
				String depName = (String) objs[2];
				Integer empId = (Integer) objs[1];
				String isapprover = (String) objs[3];
				Scoreapprover sa = new Scoreapprover();
				Employee e = hrBean.getEmployeeById(empId+"");
				ScoreDivGroup sdg = scoreBean.getScoreDivGroupByName(depName);
				sa.setApprover(e);
				sa.setGroup(sdg);
				sa.setIsapprover(isapprover);
				scoreBean.addApprover(sa, context.getUser());
			}
			System.out.println("Everyting Done...........");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return defaultAction();
	}
	
	public EmpDepartments getEmpList() {
		return empList;
	}

	public void setEmpList(EmpDepartments empList) {
		this.empList = empList;
	}
	public ScoreDivGroup getScoreNewGroup() {
		return scoreNewGroup;
	}
	public void setScoreNewGroup(ScoreDivGroup scoreNewGroup) {
		this.scoreNewGroup = scoreNewGroup;
	}
}
