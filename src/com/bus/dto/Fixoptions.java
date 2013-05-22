package com.bus.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.bus.util.SelectBoxOption;
import com.bus.util.SelectBoxOptions;


@Entity
@XmlRootElement
@Table(name="fixoptions")
public class Fixoptions  implements Serializable {

	private Integer id;
	private String name;
	private String content;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="name",nullable=false,unique=true,length=128)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Transient
	public List<SelectBoxOption> getOptionList(){
		if(content == null)
			return new ArrayList<SelectBoxOption>();
		String[] cols = content.split(",");
		List<SelectBoxOption> list = new ArrayList<SelectBoxOption>();
		for(int i=0;i<cols.length;i++){
			list.add(new SelectBoxOption(cols[i],cols[i]));
		}
		return list;
	}
}
