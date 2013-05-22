package com.bus.stripes.actionbean;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.bus.dto.Account;
import com.bus.dto.Department;
import com.bus.dto.Position;
import com.bus.services.CustomActionBean;
import com.bus.services.HRBean;
import com.bus.test.data.TestData;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

@UrlBinding("/actionbean/Position.action")
public class PositionActionBean  extends CustomActionBean implements Permission{

	private Position position;
	private HRBean bean;
	
	private List<Position> positions = new ArrayList<Position>();
	
	
	@SpringBean
	protected void setBean(HRBean bean){
		this.bean = bean;
	}
	protected HRBean getBean(){
		return this.bean;
	}
	
	public Position getPosition(){
		return this.position;
	}
	public void setPosition(Position position){
		this.position = position;
	}
	
	public List<Position> getPositions() {
		return positions;
	}
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
	
	public void initData(){
		setPositions(bean.getAllPosition());
	}
	
	@DefaultHandler
	public Resolution defaultAction(){
		initData();
//		position = TestData.getPositionData();
		return new ForwardResolution("/hr/position.jsp");
	}
	
	@HandlesEvent(value="create")
	public Resolution create(){
		try{
			bean.savePosition(position);
			return new StreamingResolution("text;charset=utf-8", new StringReader("创建成功"));
		}catch(Exception e){
			return new StreamingResolution("text;charset=utf-8", new StringReader("创建失败 "));
		}
	}

	@HandlesEvent(value="delete")
	public Resolution delete(){
		Position d = new Position();
		String targetId = context.getRequest().getParameter("targetId");
		d.setId(Integer.parseInt(targetId));
		if(d.getId() == null){
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "Delete Fail").addParameter("description", "This position may have already assigned to some employees or there is server error during deletion. Please contact administrator for further operation.");
		}else if(bean.deletePosition(d)){
			return defaultAction();
		}else{
			return new ForwardResolution("/actionbean/Error.action").addParameter("error", "Delete Fail").addParameter("description", "This position may have already assigned to some employees or there is server error during deletion. Please contact administrator for further operation.");
		}
	}

}
