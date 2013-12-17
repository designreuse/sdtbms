package com.bus.util;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

import com.bus.dto.Account;
import com.bus.dto.Contract;
import com.bus.dto.Employee;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.Scoretype;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;

public class ScoreExcelFileProcessor extends ExcelFileProcessor {

	public ScoreExcelFileProcessor(FileInputStream fis) {
		super(fis);
	}

	/**
	 * cvs file must have these four columns in order:
	 * reference, description,period,score,exmine,scoreobject,remark,type,
	 * @param scoreBean
	 * @param user
	 * @return
	 */
	public String saveItems(ScoreBean scoreBean, Account user) {
		String str = "";
		while (hasNextLine()) {

			String[] cols = strLine.split("\t");
			if (cols.length < 8) {
				String num = "N.A.";
				if (cols.length > 1)
					num = cols[0];
				str += "第" + index + "行" + "录入失败,编号" + num  + "\n<br/>";
				continue;
			}
			try {
//				String remark = "";
//				for(int i=3;i<cols.length;i++){
//					if(i == cols.length-1)
//						remark += cols[i];
//					else
//						remark += cols[i] + ",";
//				}
				Scoretype st = new Scoretype();
				st.setAccount(user);
				st.setCreatedate(Calendar.getInstance().getTime());
				st.setDescription(cols[1]);
				st.setReference(cols[0]);
				st.setPeriod(cols[2]);
				st.setScore(Float.parseFloat(cols[3]));
				st.setExamine(cols[4]);
				st.setScoreobject(cols[5]);
				st.setRemark(cols[6]);
				if (cols[7].equals(Scoretype.SCORE_TYPE_FIX_STR))
					st.setType(Scoretype.SCORE_TYPE_FIX);
				else if (cols[7].equals(Scoretype.SCORE_TYPE_TEMP_STR))
					st.setType(Scoretype.SCORE_TYPE_TEMP);
				else if (cols[7].equals(Scoretype.SCORE_TYPE_PERFORMENCE_STR))
					st.setType(Scoretype.SCORE_TYPE_PERFORMENCE);
				else if (cols[7].equals(Scoretype.SCORE_TYPE_PROJECT_STR))
					st.setType(Scoretype.SCORE_TYPE_PROJECT);
				else {
					str += "不知名的类型." + "第" + index + "行" + "录入失败,编号"+ cols[0]  + "\n<br/>";
					continue;
				}
				scoreBean.saveScoretype(user, st);
			} catch (Exception e) {
				e.printStackTrace();
				str += "转换出错." + "第" + index + "行" + "录入失败,编号" + cols[0]  + "\n<br/>";
				continue;
			}
		}
		return str;
	}

}
