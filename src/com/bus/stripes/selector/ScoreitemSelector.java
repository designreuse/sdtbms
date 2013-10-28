package com.bus.stripes.selector;

public class ScoreitemSelector implements BMSSelector{

	private String reference;
	private Integer type = -1;
	private String itemWords;
	private Integer itemlist;
	
	@Override
	public String getSelectorStatement() {
		int set = 0;
		String query = "";
		if(itemlist != null){
			query = "SELECT distinct q FROM Scoretype q, Scoresheetmapper m,Scoresheets s WHERE (m.sheet.id="+itemlist+" OR (s.id="+ itemlist+ " AND m.sheet.id=s.parent.id)) AND q.id=m.type.id ";
			set++;
		}else
			query = "SELECT q FROM Scoretype q  WHERE ";
		if(reference != null){
			if(set > 0)
				query += " AND";
			query += " q.reference LIKE '%"+reference+"%'";
			set++;
		}
		if(type != -1){
			if(set > 0)
				query += " AND";
			query += " q.type="+type;
			set++;
		}
		if(itemWords != null){
			if(set > 0)
				query += " AND";
			query += " q.description LIKE '%"+ itemWords +"%'";
		}
		query += " ORDER BY q.reference";
		return query;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getItemWords() {
		return itemWords;
	}

	public void setItemWords(String itemWords) {
		this.itemWords = itemWords;
	}

	public Integer getItemlist() {
		return itemlist;
	}

	public void setItemlist(Integer itemlist) {
		this.itemlist = itemlist;
	}
	
}
