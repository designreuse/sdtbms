package com.bus.dto.vehicleprofile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Mirror of VehicleLane but with out OneToMany Tag, for faster loading purpose
 * See use hint in VehicleBean.getAllVehicleLaneNames();
 * @author Administrator
 *
 */
@Entity
public class VehicleLaneMirror {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="num")
	private String num;
	
	@Column(name="detail")
	private String detail;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
