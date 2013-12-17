package com.bus.util.dtoconverter;

import java.util.Date;

import com.bus.dto.Account;
import com.bus.dto.score.Scoretype;

public class ScoreConvertor {

	public static Scoretype convertToScoreType(Integer id, String reference,String description,Float score,Integer type,String period, String examine, String scoreObject, Integer creator, String remark, Date createdate){
		Scoretype st = new Scoretype();
		st.setId(id);
		st.setReference(reference);
		st.setDescription(description);
		st.setPeriod(period);
		st.setExamine(examine);
		st.setScoreobject(scoreObject);
		st.setScore(score);
		st.setRemark(remark);
		st.setType(type);
		if(creator != null){
			Account a = new Account();
			a.setId(creator);
			st.setAccount(a);
		}
		st.setCreatedate(createdate);
		return st;
	}
}
