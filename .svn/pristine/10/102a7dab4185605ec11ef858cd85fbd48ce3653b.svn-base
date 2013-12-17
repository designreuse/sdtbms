package com.bus.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.bus.dto.Account;
import com.bus.dto.Employee;
import com.bus.dto.score.Scoremember;
import com.bus.dto.score.Scorerecord;
import com.bus.dto.score.ScorerecordRemark;
import com.bus.dto.score.Scoretype;
import com.bus.services.HRBean;
import com.bus.services.ScoreBean;

public class ExcelScoreUpload extends ExcelFile{

	/**
	 * 添加打分记录07版本以上的excel文件用
	 * @param hrBean
	 * @param scoreBean
	 * @param user
	 * @throws Exception
	 */
	public void saveScore2007(HRBean hrBean, ScoreBean scoreBean, Account user) throws Exception{
		XSSFSheet sheet  = wb07.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		saveNormalScore(hrBean,scoreBean,user,rows);
	}

	/**
	 * 添加打分记录03版本的excel文件用
	 * @param hrBean
	 * @param scoreBean
	 * @param user
	 * @throws Exception
	 */
	public void saveScore2003(HRBean hrBean, ScoreBean scoreBean, Account user) throws Exception{
		HSSFSheet sheet = wb03.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		saveNormalScore(hrBean,scoreBean,user,rows);
	}

	/**
	 * 添加驾驶员打分记录Excel2003用
	 * @param hrBean
	 * @param scoreBean
	 * @param user
	 */
	public void saveDriverScore2003(HRBean hrBean, ScoreBean scoreBean,
			Account user) throws Exception{
		int index = getIndexLikeName("车辆稽查月报");
		System.out.println("2003");
		HSSFSheet sheet = wb03.getSheetAt(index);
		Iterator<Row> rows = sheet.rowIterator();
		saveDriverScore(hrBean,scoreBean,user,rows);
	}

	/**
	 * 添加驾驶员打分记录Excel2007用
	 * @param hrBean
	 * @param scoreBean
	 * @param user
	 */
	public void saveDriverScore2007(HRBean hrBean, ScoreBean scoreBean,
			Account user) throws Exception{
		int index = getIndexLikeName("车辆稽查月报");
		XSSFSheet sheet  = wb07.getSheetAt(index);
		Iterator<Row> rows = sheet.rowIterator();
		saveDriverScore(hrBean,scoreBean,user,rows);
	}

	/**
	 * 获取有sheetName字符串的表名的表的Index
	 * @param sheetName
	 * @return
	 */
	private int getIndexLikeName(String sheetName) {
		if(wb07 != null){
			for(int i=0;i<wb07.getNumberOfSheets();i++){
				if(wb07.getSheetName(i).contains(sheetName))
					return i;
			}
		}else{
			for(int i=0;i<wb03.getNumberOfSheets();i++){
				if(wb03.getSheetName(i).contains(sheetName))
					return i;
			}
		}
		return 0;
	}

	/**
	 * 添加普通的打分纪录
	 * @param hrBean
	 * @param scoreBean
	 * @param user
	 * @param rows
	 * @throws Exception
	 */
	private void saveNormalScore(HRBean hrBean, ScoreBean scoreBean,
			Account user, Iterator<Row> rows) throws Exception{
		String err="";
		int indexCount=0;
		List<Scorerecord> records = new ArrayList<Scorerecord>();
		while(rows.hasNext()){
			try{
			Row row = rows.next();
			if(row.getCell(0) == null)
				continue;
			row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
			String firstCell = row.getCell(0).toString();
			if(firstCell.equals("序号"))
				continue;
			if(!HRUtil.isInteger(firstCell))
				continue;
			
			Scorerecord sr = new Scorerecord();
			//打分日期
			if(isExcelDate(row.getCell(1))){
				sr.setScoredate(row.getCell(1).getDateCellValue());
			}else{
				System.out.println("序号"+row.getCell(0).toString()+" 的日期格式不正确，使用今天日期代替");
				sr.setScoredate(new Date());
			}
			//编号
			Scoretype st = new Scoretype();
			st.setReference(row.getCell(2).getStringCellValue().trim());
			sr.setScoretype(st);
			//打分人
			row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
			Scoremember sm = new Scoremember();
			Employee sender = new Employee();
			sender.setWorkerid(row.getCell(4).getStringCellValue().trim());
			sm.setEmployee(sender);
			sr.setSender(sm);
			//受分人
			row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
			Scoremember smR = new Scoremember();
			Employee receiver = new Employee();
			receiver.setWorkerid(row.getCell(6).getStringCellValue().trim());
			smR.setEmployee(receiver);
			sr.setReceiver(smR);
			//设置分值
			if(row.getCell(7)!= null && !row.getCell(7).toString().trim().equals("")){
				Double score = row.getCell(7).getNumericCellValue();
				sr.setScore(score.floatValue());
			}
			//设置注释
			if(row.getCell(9) != null && !row.getCell(9).toString().trim().equals("")){
				ScorerecordRemark sRemark = new ScorerecordRemark();
				row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
				sRemark.setRemark(row.getCell(9).getStringCellValue());
				sr.setRemark(sRemark);
			}
			
			//设置创建日期
			sr.setCreatedate(new Date());
			//设置创建者
			sr.setCreator(user);
			records.add(sr);
			}catch(Exception e){
				err += "行:"+indexCount+","+e.getMessage()+";<br/>";
			}
		}
		System.out.println("完成转换:"+records.size());
		try{
			scoreBean.saveMassScoresFromList(records,user,hrBean);
		}catch(Exception e){
			e.printStackTrace();
			err += e.getMessage()+"<br/>";
		}
		if(!err.equals(""))
			throw new Exception(err);
	}
	
	/**
	 * 保存驾驶员的打分纪录
	 * @param hrBean
	 * @param scoreBean
	 * @param user
	 * @param rows
	 * @throws Exception
	 */
	private void saveDriverScore(HRBean hrBean, ScoreBean scoreBean,
			Account user, Iterator<Row> rows) throws Exception{
		List<Scorerecord> records = new ArrayList<Scorerecord>();
		String err="";
		int indexCount = 0;
		while(rows.hasNext()){
			try{
			Row row = rows.next();
			indexCount++;
			//如果第一个Cell不是整数序号，直接跳过
			if(row.getCell(0) == null){
				continue;
			}
			row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
			String firstCell = row.getCell(0).toString();
			if(firstCell.equals("序号"))
				continue;
			if(!HRUtil.isInteger(firstCell))
				continue;
			
			Scorerecord sr = new Scorerecord();
			//打分日期,选最后的日期
			String date = row.getCell(3).getStringCellValue();
			String dateSplit[] = date.split("/",-1);
			Date scoreDate = HRUtil.parseDate(dateSplit[dateSplit.length-1],"yyyyMMdd-hh:mm");
			sr.setScoredate(scoreDate);
			
			//编号
			String ref = row.getCell(12).getStringCellValue();
			if(ref.trim().equals(""))
				throw new Exception("条例编号不能为空");
			Scoretype st = new Scoretype();
			st.setReference(ref);
			sr.setScoretype(st);
			
			//打分人
			Scoremember sm = new Scoremember();
			Employee sender = new Employee();
			sender.setWorkerid(user.getEmployee());
			sm.setEmployee(sender);
			sr.setSender(sm);
			
			//受分人
			row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
			if(row.getCell(8).getStringCellValue() == null || row.getCell(8).getStringCellValue().equals("")){
				continue;
			}
			Scoremember smR = new Scoremember();
			Employee receiver = new Employee();
			receiver.setWorkerid(row.getCell(8).getStringCellValue().trim());
			smR.setEmployee(receiver);
			sr.setReceiver(smR);
			
			//设置分值
			if(row.getCell(14)!= null && !row.getCell(14).toString().trim().equals("")){
				Double score = row.getCell(14).getNumericCellValue();
				sr.setScore(score.floatValue());
			}else{
				sr.setScore(0F);
			}
			
			//设置注释
			row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
			String remark = row.getCell(9).getStringCellValue();
			ScorerecordRemark srRemark = new ScorerecordRemark();
			srRemark.setRemark(remark);
			sr.setRemark(srRemark);
			
			records.add(sr);
			}catch(Exception e){
				err += "行:"+indexCount+","+e.getMessage()+";<br/>";
			}
		}
		System.out.println("完成转换:"+records.size());
		try{
			if(!err.equals(""))
				throw new Exception(err);
			scoreBean.saveMassScoresFromList(records,user,hrBean);
		}catch(Exception e){
			e.printStackTrace();
			err += e.getMessage()+"<br/>";
		}
		if(!err.equals(""))
			throw new Exception(err);
	}

	/**
	 * 从excel文件读取积分条例，适用于*.xls
	 * @param scoreBean
	 * @param user
	 */
	public void uploadScoreitems2003(ScoreBean scoreBean, Account user) throws Exception{
		String err = "";
		for(int i=0;i<wb03.getNumberOfSheets();i++){
			try{
				HSSFSheet sheet = wb03.getSheetAt(i);
				Iterator<Row> rows = sheet.rowIterator();
				String sheetName = sheet.getSheetName();
				Map<String,Scoretype> sheetTypes = getTypeFromSheet(rows);
				Map<String,Scoretype> serverTypes = scoreBean.getScoretypesBySheetName(sheetName);
				Map<String,Scoretype> updatedTypes = checkScoreTypesUpdate(sheetTypes,serverTypes,user);
				scoreBean.saveUpdatedScoretypes(updatedTypes, user,sheetName);
			}catch(Exception e){
				e.printStackTrace();
				err += e.getMessage();
			}
		}
		if(!err.equals(""))
			throw new Exception(err);
	}

	/**
	 * 检查scoretype的变化，excel表上传的为准，不在表的把status设为0当做删除。不在数据库的表示新增加的
	 * @param sheetTypes
	 * @param serverTypes
	 * @return
	 */
	private Map<String,Scoretype> checkScoreTypesUpdate(Map<String,Scoretype> sheetTypes,
			Map<String,Scoretype> serverTypes, Account user) throws Exception{
		Map<String,Scoretype> sts = new HashMap<String,Scoretype>();
		for(Scoretype st:serverTypes.values()){
			if(sheetTypes.get(st.getReference()) == null){
				serverTypes.get(st.getReference()).setStatus(Scoretype.DELETED);
				sts.put(st.getReference(), serverTypes.get(st.getReference()));
			}else{
				Scoretype record = serverTypes.get(st.getReference());
				Scoretype update = sheetTypes.get(st.getReference());
				record.setReference(update.getReference());
				record.setDescription(update.getDescription());
				record.setPeriod(update.getPeriod());
				record.setScore(update.getScore());
				record.setExamine(update.getExamine());
				record.setScoreobject(update.getScoreobject());
				record.setRemark(update.getRemark());
				record.setCreatedate(Calendar.getInstance().getTime());
				record.setStatus(Scoretype.ACTIVE);
				sts.put(st.getReference(), record);
			}
		}
		
		for(Scoretype st:sheetTypes.values()){
			if(serverTypes.get(st.getReference()) == null){
				st.setAccount(user);
				st.setCreatedate(Calendar.getInstance().getTime());
				st.setStatus(Scoretype.ACTIVE);
				sts.put(st.getReference(),st);
			}
		}
		return sts;
	}

	/**
	 * 从excelSheet获取积分条例
	 * @param rows
	 * @return
	 */
	private Map<String,Scoretype> getTypeFromSheet(Iterator<Row> rows) throws Exception{
		Map<String,Scoretype> scoreTypes = new HashMap<String,Scoretype>();
		String err ="";
		int indexCount = 0;
		boolean start = false;
		while(rows.hasNext()){
			try{
				Row row = rows.next();
				indexCount++;
				//如果第一个Cell不是条例编码，直接跳过
				if(row.getCell(0) == null){
					continue;
				}
				row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				String firstCell = row.getCell(0).toString();
				if(!start && firstCell.equals("条例编码")){
					start = true;
					continue;
				}else if(!start){
					continue;
				}
				if(!firstCell.contains("-"))
					continue;
				//开始获取条例
				System.out.println("Parsing "+row.getCell(0).getStringCellValue());
				Scoretype st = new Scoretype();
				st.setReference(row.getCell(0).getStringCellValue());
				st.setDescription(row.getCell(1).getStringCellValue());
				st.setPeriod(row.getCell(2).getStringCellValue());
				st.setScore(new Float(row.getCell(3).getNumericCellValue()));
				st.setExamine(row.getCell(4).getStringCellValue());
				st.setScoreobject(row.getCell(5).getStringCellValue());
				st.setRemark(row.getCell(6).getStringCellValue());
				if(row.getCell(7) == null)
					st.setType(Scoretype.SCORE_TYPE_TEMP);
				else{
					String str = row.getCell(7).getStringCellValue();
					if(str.equals(Scoretype.SCORE_TYPE_FIX_STR))
						st.setType(Scoretype.SCORE_TYPE_FIX);
					else if(str.equals(Scoretype.SCORE_TYPE_PERFORMENCE_STR))
						st.setType(Scoretype.SCORE_TYPE_PERFORMENCE);
					else if(str.equals(Scoretype.SCORE_TYPE_PROJECT_STR))
						st.setType(Scoretype.SCORE_TYPE_PROJECT);
					else if(str.equals(Scoretype.SCORE_TYPE_TEMP_STR))
						st.setType(Scoretype.SCORE_TYPE_TEMP);
					else
						st.setType(Scoretype.SCORE_TYPE_TEMP);
				}
				if(st.getReference() != null && !st.getReference().trim().equals("") && st.getScore() != null)
						scoreTypes.put(st.getReference(),st);
				else
					throw new Exception("条例编号"+st.getReference()+"有错误");
			}catch(Exception e){
				err += "第"+ indexCount +"行出错," + e.getMessage()+ ".<br/>";
			}
		}
		System.out.println(scoreTypes.keySet().size() + " found");
		if(!err.equals(""))
			throw new Exception(err);
		else
			return scoreTypes;
	}

	/**
	 * 从excel文件读取积分条例,使用于*.xlsx
	 * @param scoreBean
	 * @param user
	 */
	public void uploadScoreitems2007(ScoreBean scoreBean, Account user) throws Exception {
		String err = "";
		for(int i=0;i<wb07.getNumberOfSheets();i++){
			try{
				XSSFSheet sheet = wb07.getSheetAt(i);
				Iterator<Row> rows = sheet.rowIterator();
				String sheetName = sheet.getSheetName();
				Map<String,Scoretype> sheetTypes = getTypeFromSheet(rows);
				Map<String,Scoretype> serverTypes = scoreBean.getScoretypesBySheetName(sheetName);
				Map<String,Scoretype> updatedTypes = checkScoreTypesUpdate(sheetTypes,serverTypes,user);
				scoreBean.saveUpdatedScoretypes(updatedTypes, user,sheetName);
			}catch(Exception e){
				e.printStackTrace();
				err += e.getMessage();
			}
		}
		if(!err.equals(""))
			throw new Exception(err);
	}
	
}
