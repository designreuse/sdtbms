package com.bus.stripes.selector;

public class ScoreitemSelector implements BMSSelector{

	private String reference;
	private Integer type = -1;
	
	@Override
	public String getSelectorStatement() {
		int set = 0;
		String query = "SELECT q FROM Scoretype q  WHERE";
		if(reference != null){
			query += " reference='"+reference+"'";
			set++;
		}
		if(type != -1){
			if(set > 0)
				query += " AND";
			query += " type="+type;
		}
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
	
}
