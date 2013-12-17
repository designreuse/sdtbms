package com.bus.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.bus.dto.Account;
import com.bus.dto.vehicleprofile.VehicleAccident;
import com.bus.dto.vehicleprofile.VehicleBasicInfo;
import com.bus.dto.vehicleprofile.VehicleChange;
import com.bus.dto.vehicleprofile.VehicleLevel;
import com.bus.dto.vehicleprofile.VehiclePartsRepair;
import com.bus.dto.vehicleprofile.VehicleProfile;
import com.bus.dto.vehicleprofile.VehicleRepairRecord;
import com.bus.dto.vehicleprofile.VehicleTechnicalDetail;
import com.bus.dto.vehicleprofile.VehicleUseRecord;
import com.bus.services.VehicleBean;

public class VehicleProfileExcelFile {

	private XSSFWorkbook wb = null;
	private FormulaEvaluator evaluator = null;
	
	public VehicleProfileExcelFile(FileInputStream fis){
		try{
			wb = new XSSFWorkbook(fis);
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * 从Excel2007文件保存车辆档案
	 * @param vBean
	 * @param user
	 * @throws Exception
	 */
	public void saveVehicleProfileFromExcel(VehicleBean vBean,Account user) throws Exception{
		evaluator = wb.getCreationHelper().createFormulaEvaluator();
		
		//第一页基础资料
		VehicleProfile vp = getVehicleProfileFromSheet(wb.getSheetAt(0),user,"长途");
		VehicleProfile vpTemp =  vBean.getVehicleProfileByVid(vp.getVid());
		if(vpTemp == null)
			vp = vBean.saveVehicle(vp, user);
		else
			vp = vBean.saveVehicle(vpTemp, user);
		
		VehicleBasicInfo vbi = vBean.getVehicleBasicInfoByVehicleId(vp.getId());
		if(vbi == null)
			vbi = getVehicleBasicInfoFromSheet(wb.getSheetAt(0),vp, new VehicleBasicInfo());
		else
			vbi = getVehicleBasicInfoFromSheet(wb.getSheetAt(0),vp, vbi);
		vbi.setCreator(user);
		vBean.saveVehicleBasicInfo(vbi, user);
		
		//第二页技术档案
		VehicleTechnicalDetail vtd = vBean.getVehicleTechnicalDetailByVehicleId(vp.getId());
		if(vtd == null)
			vtd = getVehicleTechnicalDetailFromSheet(wb.getSheetAt(1),vp,new VehicleTechnicalDetail());
		else
			vtd = getVehicleTechnicalDetailFromSheet(wb.getSheetAt(1),vp,vtd);
		vtd.setCreator(user);
		vBean.saveVehicleTechDetail(vtd, user);
		
		//第三页 车辆维修登记表
		List<VehicleRepairRecord> vrrs = getVehicleRepairRecords(wb.getSheetAt(2),vp,user);
		vBean.saveVehicleRepairRecords(vrrs);
		
		//第四页 车辆总部件换成
		List<VehiclePartsRepair> vparts = getVehiclePartsRepairs(wb.getSheetAt(3),vp,user);
		vBean.saveVehiclePartsRepairs(vparts);
		
		//第五页 车辆等级评定登记表
		List<VehicleLevel> vls = getVehicleLevel(wb.getSheetAt(4),vp,user);
		vBean.saveVehicleLevels(vls);
		
		//第六页 车辆变更登记表
		List<VehicleChange> vcs = getVehicleChanges(wb.getSheetAt(5),vp,user);
		vBean.saveVehicleChanges(vcs);
		
		//第七页 车辆使用记录表
		List<VehicleUseRecord> vurs = getVehicleUseRecords(wb.getSheetAt(6),vp,user);
		vBean.saveVehicleUseRecords(vurs);
		
		//第八页 车辆交通事故记录
		List<VehicleAccident> vats = getVehicleAccidents(wb.getSheetAt(7),vp,user);
		vBean.saveVehicleAccidents(vats);
		
		System.out.println("Saved "+ vp.getVid());
	}

	private List<VehicleChange> getVehicleChanges(XSSFSheet sheet,
			VehicleProfile vp, Account user) throws Exception{
		int rowNumber = 0;
		Iterator<Row> rows = sheet.rowIterator();
		List<VehicleChange> list = new ArrayList<VehicleChange>();
		while(rows.hasNext()){
			Row row = null;
			row = rows.next();
			if(rowNumber++ < 2)
				continue;
			if(row.getLastCellNum() < 3)
				continue;
			if(!isExcelDate(row.getCell(0))){
				continue;
			}
			VehicleChange vpr = new VehicleChange();
			vpr.setDate(row.getCell(0).getDateCellValue());
			vpr.setDescription(row.getCell(2).getStringCellValue());
			vpr.setCreator(user);
			vpr.setReason(row.getCell(1).getStringCellValue());
			vpr.setRegistrant(row.getCell(3).getStringCellValue());
			vpr.setVid(vp);
			list.add(vpr);
		}
		return list;
	}

	private List<VehiclePartsRepair> getVehiclePartsRepairs(XSSFSheet sheet,
			VehicleProfile vp, Account user) throws Exception{
		int rowNumber = 0;
		Iterator<Row> rows = sheet.rowIterator();
		List<VehiclePartsRepair> list = new ArrayList<VehiclePartsRepair>();
		while(rows.hasNext()){
			Row row = null;
			row = rows.next();
			if(rowNumber++ < 3)
				continue;
			if(row.getLastCellNum() < 3)
				continue;
			if(!isExcelDate(row.getCell(0))){
				continue;
			}
			String isempty = row.getCell(0).toString().trim();
			if(isempty.equals("")){
				continue;
			}
			VehiclePartsRepair vpr = new VehiclePartsRepair();
			vpr.setChangedate(row.getCell(0).getDateCellValue());
			vpr.setCompany(row.getCell(2).getStringCellValue());
			vpr.setCreator(user);
			vpr.setDescription(row.getCell(1).getStringCellValue());
			vpr.setRegistrant(row.getCell(3).getStringCellValue());
			vpr.setVid(vp);
			list.add(vpr);
		}
		return list;
	}

	private List<VehicleAccident> getVehicleAccidents(XSSFSheet sheet,
			VehicleProfile vp, Account user) throws Exception{
		int rowNumber = 0;
		Iterator<Row> rows = sheet.rowIterator();
		List<VehicleAccident> list = new ArrayList<VehicleAccident>();
		while(rows.hasNext()){
			Row row = null;
			row = rows.next();
			if(rowNumber++ < 3)
				continue;
			if(row.getLastCellNum() < 6)
				continue;
			if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING ){
				System.out.println("first value is String:"+row.getCell(0).toString());
				continue;
			}else if(!DateUtil.isCellDateFormatted(row.getCell(0))){
				Date d = null;
				try{
					d = row.getCell(0).getDateCellValue();
				}catch(Exception e){
					d= null;
				}
				if(d == null){
					System.out.println("VUR Row "+rowNumber+" first value not date:"+row.getCell(0).toString());
					continue;
				}
			}
			
			VehicleAccident va = new VehicleAccident();
			va.setAtype(row.getCell(2).getStringCellValue());
			if(row.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC)
				va.setCost(row.getCell(5).getNumericCellValue());
			va.setCreator(user);
			va.setDate(row.getCell(0).getDateCellValue());
			va.setDescription(row.getCell(4).getStringCellValue());
			va.setPlace(row.getCell(1).getStringCellValue());
			va.setRegistrant(row.getCell(6).getStringCellValue());
			va.setResponsibility(row.getCell(3).getStringCellValue());
			va.setVid(vp);
			list.add(va);
		}
		return list;
 	}

	private List<VehicleUseRecord> getVehicleUseRecords(XSSFSheet sheet,VehicleProfile vp, Account user) throws Exception{
		int rowNumber = 0;
		Iterator<Row> rows = sheet.rowIterator();
		List<VehicleUseRecord> list = new ArrayList<VehicleUseRecord>();
		while(rows.hasNext()){
			Row row = null;
			row = rows.next();
			if(rowNumber++ < 3)
				continue;
			if(row.getLastCellNum() < 9)
				continue;
			if(row.getCell(0) == null){
				continue;
			}
			if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING ){
				System.out.println("first value is String:"+row.getCell(0).toString());
				continue;
			}else if(!DateUtil.isCellDateFormatted(row.getCell(0))){
				Date d = null;
				try{
					d = row.getCell(0).getDateCellValue();
				}catch(Exception e){
					d= null;
				}
				if(d == null){
					System.out.println("VUR Row "+rowNumber+" first value not date:"+row.getCell(0).toString());
					continue;
				}
			}
			VehicleUseRecord vur = new VehicleUseRecord();
			vur.setCompany(row.getCell(7).getStringCellValue());
			vur.setCreator(user);
			vur.setDate(row.getCell(0).getDateCellValue());
			if(row.getCell(6) != null && row.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setExceed(row.getCell(6).getNumericCellValue());
			if(row.getCell(3) != null && row.getCell(3).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setFuel(row.getCell(3).getNumericCellValue());
			if(row.getCell(2) != null && row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setIntervalmileage(row.getCell(2).getNumericCellValue());
			
			evaluator.evaluateInCell(row.getCell(1)); //Calculate cell 1 's value
//			Cell cell = evaluator.evaluateInCell(row.getCell(1)); //Calculate cell 1 's value
//			row.getCell(1).setCellValue(cell.getNumericCellValue());
			if(row.getCell(1) != null && row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setMileage(row.getCell(1).getNumericCellValue());
			
			vur.setRegistrant(row.getCell(8).getStringCellValue().trim());
			
			if(row.getCell(5) != null && row.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setRemine(row.getCell(5).getNumericCellValue());
			if(row.getCell(4) != null && row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setSetprice(row.getCell(4).getNumericCellValue());
			vur.setVid(vp);
			
			list.add(vur);
		}
		return list;
	}

	private List<VehicleLevel> getVehicleLevel(XSSFSheet sheet,
			VehicleProfile vp, Account user) throws Exception{
		int rowNumber = 0;
		Iterator<Row> rows = sheet.rowIterator();
		List<VehicleLevel> list = new ArrayList<VehicleLevel>();
		while(rows.hasNext()){
			Row row = null;
			row = rows.next();
			if(rowNumber++ < 2)
				continue;
			if(row.getLastCellNum() < 6)
				continue;
			if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING || !DateUtil.isCellDateFormatted(row.getCell(0)))
				continue;
			VehicleLevel vlevel = new VehicleLevel();
			vlevel.setDate(row.getCell(0).getDateCellValue());
			vlevel.setCarlevel(row.getCell(4).getStringCellValue());
			vlevel.setCompany(row.getCell(5).getStringCellValue());
			vlevel.setCreator(user);
			vlevel.setDescription(row.getCell(2).getStringCellValue());
			if(row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
				row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			vlevel.setDistance(row.getCell(1).getStringCellValue());
			vlevel.setRegistrant(row.getCell(6).getStringCellValue());
			vlevel.setTechlevel(row.getCell(3).getStringCellValue());
			vlevel.setVid(vp);
			list.add(vlevel);
		}
		return list;
	}

	private List<VehicleRepairRecord> getVehicleRepairRecords(XSSFSheet sheet, VehicleProfile vp,Account user) throws Exception{
		int rowNumber = 0; //start at 3
		Iterator<Row> rows = sheet.rowIterator();
		List<VehicleRepairRecord> list = new ArrayList<VehicleRepairRecord>();
		while(rows.hasNext()){
			Row row = null;
			if(rowNumber++ < 3){
				row = rows.next();
				continue;
			}
			row = rows.next();
			if(row.getLastCellNum() < 5){
				continue;
			}
			if(row.getCell(0) == null){
				continue;
			}
			String isempty = row.getCell(0).toString().trim();
			if(isempty.equals("")){
				continue;
			}
			if(row.getCell(0).getCellType() != Cell.CELL_TYPE_STRING && DateUtil.isCellDateFormatted(row.getCell(0))){
				System.out.println("cell is date "+row.getCell(0).toString());
			}else{
				System.out.println("cell isn't date"+row.getCell(0).toString());
				continue;
			}
			VehicleRepairRecord vrr = new VehicleRepairRecord();
			vrr.setCreator(user);
			vrr.setDescription(row.getCell(2).getStringCellValue());
			vrr.setRcompany(row.getCell(3).getStringCellValue());
			vrr.setRdate(row.getCell(0).getDateCellValue());
			vrr.setRegistrant(row.getCell(4).getStringCellValue());
			vrr.setRtype(row.getCell(1).getStringCellValue());
			vrr.setVid(vp);
			list.add(vrr);
		}
		return list;
	}

	private VehicleTechnicalDetail getVehicleTechnicalDetailFromSheet(
			XSSFSheet sheet, VehicleProfile vp, VehicleTechnicalDetail vtd) throws Exception{
		vtd.setVid(vp);
		vtd.setVtype(sheet.getRow(1).getCell(1).getStringCellValue());
		vtd.setFactorycode(sheet.getRow(1).getCell(3).getStringCellValue());
		
		String factorydateAndMadeIn = sheet.getRow(1).getCell(5).getStringCellValue();
		String[] fami = factorydateAndMadeIn.split("\\/", -1);
		if(fami.length>0){
			if(fami[0].length() > 4){
				vtd.setFactorydate(HRUtil.parseDate(fami[0], "yyyy-MM-dd"));
			}
			if(fami.length == 2)
				vtd.setMadein(fami[1]);
			else{
				String madein = getTickValue(factorydateAndMadeIn)[0];
				if(madein == null || madein.trim().equals(""))
					madein="国产";
				vtd.setMadein(madein);
			}
		}
		
		vtd.setVincode(sheet.getRow(2).getCell(1).getStringCellValue());
		vtd.setBasecode(sheet.getRow(2).getCell(3).getStringCellValue());
		vtd.setVlevel(sheet.getRow(2).getCell(5).getStringCellValue());
		
		String[] size = getSize(sheet.getRow(3).getCell(1).getStringCellValue());
		vtd.setVlength(size[0]);
		vtd.setVwidth(size[1]);
		vtd.setVheight(size[2]);
		vtd.setWeight(sheet.getRow(3).getCell(3).getStringCellValue());
//		vtd.setSitarrange(sheet.getRow(3).getCell(5).getStringCellValue());
		vtd.setSitarrange("2+2");
		
		sheet.getRow(4).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
		vtd.setSits(sheet.getRow(4).getCell(1).getStringCellValue());
		vtd.setDragweight(sheet.getRow(4).getCell(3).getStringCellValue());
		vtd.setVaxis(sheet.getRow(4).getCell(5).getStringCellValue());
		
		sheet.getRow(5).getCell(3).setCellType(Cell.CELL_TYPE_STRING);
		vtd.setEnginecode(sheet.getRow(5).getCell(3).getStringCellValue());
		vtd.setEnginemodel(sheet.getRow(5).getCell(1).getStringCellValue());
		vtd.setFueltype(sheet.getRow(5).getCell(5).getStringCellValue());
		
		vtd.setEnginehorse(sheet.getRow(6).getCell(3).getStringCellValue());
		vtd.setEnginepower(sheet.getRow(6).getCell(1).getStringCellValue());
		
		String release = getTickValue(sheet.getRow(6).getCell(5).getStringCellValue())[0];
		if(release == null || release.trim().equals("")){
			release = "国Ⅲ";
		}
		vtd.setReleasestandard(release);
		
		vtd.setDrivermode(sheet.getRow(7).getCell(1).getStringCellValue());
		vtd.setTyre(sheet.getRow(7).getCell(3).getStringCellValue());
//		vtd.setFrontlight(sheet.getRow(7).getCell(5).getStringCellValue());
		vtd.setFrontlight("四灯制");
		
		vtd.setVariatormode(getTickValue(sheet.getRow(8).getCell(1).getStringCellValue())[0]);
		vtd.setRetarder(getTickValue(sheet.getRow(8).getCell(3).getStringCellValue())[0]);
		vtd.setTurner(getTickValue(sheet.getRow(8).getCell(5).getStringCellValue())[0]);
		
		String breakStr = sheet.getRow(9).getCell(1).getStringCellValue();
		String breakmode = breakStr.substring(0,breakStr.indexOf("前轮"));
		String backBreak = breakStr.substring(breakStr.indexOf("后轮：")+3,breakStr.length());
		
		vtd.setBreakmode(getTickValue(breakmode)[0]);
		vtd.setFrontbreak("气囊");
		vtd.setBackbreakList(Arrays.asList(getTickValue(backBreak)));
		
		vtd.setHangmodefront("非独立,气囊");
		vtd.setHangmodeback("非独立,气囊");
		vtd.setOtherList(Arrays.asList(getTickValue(sheet.getRow(11).getCell(1).getStringCellValue())));
		return vtd;
	}


	private String[] getSize(String input) throws Exception{
		String[] ar = input.split("：",-1);
		if(ar.length == 4){
			String[] size = new String[3];
			size[0] = HRUtil.removeNoneNumber(ar[1]);
			size[1] = HRUtil.removeNoneNumber(ar[2]);
			size[2] = HRUtil.removeNoneNumber(ar[3]);
			return size;
		}else{
			ar = input.split("\\*",-1);
			if(ar.length == 3){
				String[] size = new String[3];
				size[0] = HRUtil.removeNoneNumber(ar[0]);
				size[1] = HRUtil.removeNoneNumber(ar[1]);
				size[2] = HRUtil.removeNoneNumber(ar[2]);
				return size;
			}
			System.out.println("parse size fail:"+input);
			return new String[]{"","",""};
		}
	}


	private String[] getTickValue(String input) {
		char tick = '√';
		int count = StringUtils.countOccurrencesOf(input, "√");
		if(count == 0)
			return new String[]{""};
		int lastIndex = -1;
		int firstIndex = -1;
		List<String> list = new ArrayList<String>();
		for (int i=input.length()-1; i >= 0 ; i--)
	    {
	        if (input.charAt(i) == tick){
	             for(int j=i-1;j>=0;j--){
	            	 if(lastIndex == -1 && input.charAt(j) != ' '){
	            		 lastIndex = j;
	            	 }else if(lastIndex != -1 && (input.charAt(j) == ' ' || input.charAt(j) == '/' || j==0)){
	            		 if(j== 0)
	            			 firstIndex = j;
	            		 else
	            			 firstIndex = j+1;
	            	 }
	            	 if(lastIndex != -1 && firstIndex != -1){
	            		 list.add(input.substring(firstIndex,lastIndex+1).trim());
	            		 firstIndex = -1;
	            		 lastIndex = -1;
	            		 i = j;
	            		 break;
	            	 }
	             }
	        }
	    }
		if(list.size() < 1)
			list.add("");
		return list.toArray(new String[list.size()]);
	}


	/**
	 * 获取车辆档案基本资料，Excel的第一页
	 * @param sheet
	 * @param vp
	 * @param vehicleBasicInfo
	 * @return
	 * @throws Exception
	 */
	private VehicleBasicInfo getVehicleBasicInfoFromSheet(XSSFSheet sheet,
			VehicleProfile vp, VehicleBasicInfo vbi) throws Exception{
		vbi.setVid(vp);
		vbi.setCompanytype(sheet.getRow(9).getCell(1).getStringCellValue());
		vbi.setMaintenancemile(sheet.getRow(16).getCell(1).getStringCellValue());
		vbi.setMaster(sheet.getRow(7).getCell(1).getStringCellValue());
		vbi.setOperatelevel(sheet.getRow(11).getCell(1).getStringCellValue());
		vbi.setOperatemode(sheet.getRow(10).getCell(1).getStringCellValue());
		vbi.setOperatenumber(sheet.getRow(8).getCell(1).getStringCellValue());
		vbi.setOperaterange(sheet.getRow(12).getCell(1).getStringCellValue());
		vbi.setOperateroute(sheet.getRow(14).getCell(1).getStringCellValue());
		sheet.getRow(13).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
		String number = sheet.getRow(13).getCell(1).getStringCellValue();
		while(number.length() < 9){
			number = "0"+number;
		}
		vbi.setTransportnumber(number);
		vbi.setSource(sheet.getRow(15).getCell(1).getStringCellValue());
		return vbi;
	}


	/**
	 * 获取车辆档案的初始值档案,Excel第一页
	 * @param sheetAt
	 * @return
	 */
	private VehicleProfile getVehicleProfileFromSheet(XSSFSheet sheet,Account user,String subCompany) throws Exception{
		VehicleProfile vp = new VehicleProfile();
		vp.setVid(sheet.getRow(2).getCell(1).getStringCellValue());
		vp.setVcolor(sheet.getRow(2).getCell(2).getStringCellValue());
		vp.setRegisterDate(sheet.getRow(2).getCell(3).getDateCellValue());
		vp.setSubcompany(subCompany);
		vp.setCreator(user);
		vp.setStatus(VehicleProfile.statusA);
		return vp;
	}
	
	private boolean isExcelDate(Cell cell){
		if(cell.getCellType() == Cell.CELL_TYPE_STRING ){
			return false;
		}else if(!DateUtil.isCellDateFormatted(cell)){
			Date d = null;
			try{
				d = cell.getDateCellValue();
			}catch(Exception e){
				d= null;
			}
			if(d == null){
				return false;
			}
		}
		return true;
	}

	//从excel2007文件保存车辆档案
	public void importVehicles(VehicleBean vBean, Account user) throws Exception{
		evaluator = wb.getCreationHelper().createFormulaEvaluator();
		XSSFSheet sheet = wb.getSheetAt(1);
		Iterator<Row> rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表1"))
				saveVehicleProfilesBasicInfo(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehicleBasicProfile success!");
		
		//第三页车辆档案技术
		sheet = wb.getSheetAt(2);
		rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表2"))
				saveVehicleTechnicalDetail(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehicleTechnicalDetail success!");
		
		//第四页车辆维修记录
		sheet  = wb.getSheetAt(3);
		rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表3"))
				saveVehicleRepairRecords(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehicleRepairRecord success!");
		
		//第五页车辆主要部件更换
		sheet  = wb.getSheetAt(4);
		rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表4"))
				saveVehiclePartsRepairs(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehiclePartsRepair success!");
		
		//第六页车辆等级评定
		sheet  = wb.getSheetAt(5);
		rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表5"))
				saveVehicleLevels(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehicleLevel success!");
		
		//第七页 车辆更换表
		sheet  = wb.getSheetAt(6);
		rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表6"))
				saveVehicleChanges(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehicleChange success!");
		
		//第八页 车辆使用记录
		sheet  = wb.getSheetAt(7);
		rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表7"))
				saveVehicleUseRecords(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehicleUseRecord success!");
		
		//第九页 车辆事故记录
		sheet  = wb.getSheetAt(8);
		rows = sheet.rowIterator();
		while(rows.hasNext()){
			Row row = rows.next();
			if(row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING &&
					row.getCell(0).getStringCellValue().trim().equals("表8"))
				saveVehicleAccidents(vBean,user,sheet,row.getRowNum());
		}
		System.out.println("Save VehicleAccident success!");
	}

	private void saveVehicleAccidents(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception{
		String vid = sheet.getRow(rowNum).getCell(6).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(6).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist."+vid);
			return;
		}
		
		int rowCount = 1;
		List<VehicleAccident> list = new ArrayList<VehicleAccident>();
		String firstCell = null;
		while(sheet.getRow(rowNum+rowCount) != null){
			if(sheet.getRow(rowNum+rowCount).getCell(0) == null){
				rowCount++;
				continue;
			}
			firstCell = sheet.getRow(rowNum+rowCount).getCell(0).toString().trim();
			if(rowCount < 3){
				rowCount++;
				continue;
			}
			if(firstCell.equals("")){
				rowCount++;
				continue;
			}else if(firstCell.equals("表8")){
				break;
			}
			if(!isExcelDate(sheet.getRow(rowNum+rowCount).getCell(0))){
				rowCount++;
				continue;
			}
			Row row = sheet.getRow(rowNum+rowCount);
			VehicleAccident va = new VehicleAccident();
			va.setAtype(row.getCell(2).getStringCellValue());
			if(row.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC)
				va.setCost(row.getCell(5).getNumericCellValue());
			va.setCreator(user);
			va.setDate(row.getCell(0).getDateCellValue());
			va.setDescription(row.getCell(4).getStringCellValue());
			va.setPlace(row.getCell(1).getStringCellValue());
			va.setRegistrant(row.getCell(6).getStringCellValue());
			va.setResponsibility(row.getCell(3).getStringCellValue());
			va.setVid(vp);
			list.add(va);
			rowCount++;
		}
		vBean.saveVehicleAccidents(list);
	}

	private void saveVehicleUseRecords(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception{
		String vid = sheet.getRow(rowNum).getCell(8).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(8).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist."+vid);
			return;
		}
		
		int rowCount = 1;
		List<VehicleUseRecord> list = new ArrayList<VehicleUseRecord>();
		String firstCell = null;
		while(sheet.getRow(rowNum+rowCount) != null){
			if(sheet.getRow(rowNum+rowCount).getCell(0) == null){
				rowCount++;
				continue;
			}
			firstCell = sheet.getRow(rowNum+rowCount).getCell(0).toString().trim();
			if(rowCount < 4){
				rowCount++;
				continue;
			}
			if(firstCell.equals("")){
				rowCount++;
				continue;
			}else if(firstCell.equals("表7")){
				break;
			}
			if(!isExcelDate(sheet.getRow(rowNum+rowCount).getCell(0))){
				rowCount++;
				continue;
			}
			Row row = sheet.getRow(rowNum+rowCount);
			VehicleUseRecord vur = new VehicleUseRecord();
			vur.setCompany(row.getCell(7).getStringCellValue());
			vur.setCreator(user);
			vur.setDate(row.getCell(0).getDateCellValue());
			if(row.getCell(6) != null && row.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setExceed(row.getCell(6).getNumericCellValue());
			if(row.getCell(3) != null && row.getCell(3).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setFuel(row.getCell(3).getNumericCellValue());
			if(row.getCell(2) != null && row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setIntervalmileage(row.getCell(2).getNumericCellValue());
			
			evaluator.evaluateInCell(row.getCell(1)); //Calculate cell 1 's value
			if(row.getCell(1) != null && row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setMileage(row.getCell(1).getNumericCellValue());
			
			vur.setRegistrant(row.getCell(8).getStringCellValue().trim());
			
			if(row.getCell(5) != null && row.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setRemine(row.getCell(5).getNumericCellValue());
			if(row.getCell(4) != null && row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC)
				vur.setSetprice(row.getCell(4).getNumericCellValue());
			vur.setVid(vp);
			list.add(vur);
			rowCount++;
		}
		vBean.saveVehicleUseRecords(list);
	}

	private void saveVehicleChanges(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception{
		String vid = sheet.getRow(rowNum).getCell(3).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(3).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist."+vid);
			return;
		}
		
		int rowCount = 1;
		List<VehicleChange> list = new ArrayList<VehicleChange>();
		String firstCell = null;
		while(sheet.getRow(rowNum+rowCount) != null){
			if(sheet.getRow(rowNum+rowCount).getCell(0) == null){
				rowCount++;
				continue;
			}
			firstCell = sheet.getRow(rowNum+rowCount).getCell(0).toString().trim();
			if(rowCount < 3){
				rowCount++;
				continue;
			}
			if(firstCell.equals("")){
				rowCount++;
				continue;
			}else if(firstCell.equals("表6")){
				break;
			}
			if(!isExcelDate(sheet.getRow(rowNum+rowCount).getCell(0))){
				rowCount++;
				continue;
			}
			Row row = sheet.getRow(rowNum+rowCount);
			VehicleChange vpr = new VehicleChange();
			vpr.setDate(row.getCell(0).getDateCellValue());
			vpr.setDescription(row.getCell(2).getStringCellValue());
			vpr.setCreator(user);
			vpr.setReason(row.getCell(1).getStringCellValue());
			vpr.setRegistrant(row.getCell(3).getStringCellValue());
			vpr.setVid(vp);
			list.add(vpr);
			rowCount++;
		}
		vBean.saveVehicleChanges(list);
	}

	private void saveVehicleLevels(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception {
		String vid = sheet.getRow(rowNum).getCell(6).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(6).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist."+vid);
			return;
		}
		
		int rowCount = 1;
		List<VehicleLevel> list = new ArrayList<VehicleLevel>();
		String firstCell = null;
		while(sheet.getRow(rowNum+rowCount) != null){
			if(sheet.getRow(rowNum+rowCount).getCell(0) == null){
				rowCount++;
				continue;
			}
			firstCell = sheet.getRow(rowNum+rowCount).getCell(0).toString().trim();
			if(rowCount < 3){
				rowCount++;
				continue;
			}
			if(firstCell.equals("")){
				rowCount++;
				continue;
			}else if(firstCell.equals("表5")){
				break;
			}
			if(!isExcelDate(sheet.getRow(rowNum+rowCount).getCell(0))){
				rowCount++;
				continue;
			}
			
			VehicleLevel vlevel = new VehicleLevel();
			Row row = sheet.getRow(rowNum+rowCount);
			vlevel.setDate(row.getCell(0).getDateCellValue());
			vlevel.setCarlevel(row.getCell(4).getStringCellValue());
			vlevel.setCompany(row.getCell(5).getStringCellValue());
			vlevel.setCreator(user);
			vlevel.setDescription(row.getCell(2).getStringCellValue());
			if(row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC)
				row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			vlevel.setDistance(row.getCell(1).getStringCellValue());
			vlevel.setRegistrant(row.getCell(6).getStringCellValue());
			vlevel.setTechlevel(row.getCell(3).getStringCellValue());
			vlevel.setVid(vp);
			list.add(vlevel);
			rowCount++;
		}
		vBean.saveVehicleLevels(list);
	}

	private void saveVehiclePartsRepairs(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception{
		String vid = sheet.getRow(rowNum).getCell(3).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(3).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist."+vid);
			return;
		}
		
		int rowCount = 1;
		List<VehiclePartsRepair> list = new ArrayList<VehiclePartsRepair>();
		String firstCell = null;
		while(sheet.getRow(rowNum+rowCount) != null){
			if(sheet.getRow(rowNum+rowCount).getCell(0) == null){
				rowCount++;
				continue;
			}
			firstCell = sheet.getRow(rowNum+rowCount).getCell(0).toString().trim();
			if(rowCount < 3){
				rowCount++;
				continue;
			}
			if(firstCell.equals("")){
				rowCount++;
				continue;
			}else if(firstCell.equals("表4")){
				break;
			}
			if(!isExcelDate(sheet.getRow(rowNum+rowCount).getCell(0))){
				rowCount++;
				continue;
			}
			
			VehiclePartsRepair vpr = new VehiclePartsRepair();
			vpr.setChangedate(sheet.getRow(rowNum+rowCount).getCell(0).getDateCellValue());
			vpr.setCompany(sheet.getRow(rowNum+rowCount).getCell(2).getStringCellValue());
			vpr.setCreator(user);
			vpr.setDescription(sheet.getRow(rowNum+rowCount).getCell(1).getStringCellValue());
			vpr.setRegistrant(sheet.getRow(rowNum+rowCount).getCell(3).getStringCellValue());
			vpr.setVid(vp);
			list.add(vpr);
			rowCount++;
		}
		vBean.saveVehiclePartsRepairs(list);
	}

	private void saveVehicleRepairRecords(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception{
		String vid = sheet.getRow(rowNum).getCell(4).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(4).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist."+vid);
			return;
		}
		
		int rowCount = 1;
		List<VehicleRepairRecord> list = new ArrayList<VehicleRepairRecord>();
		String firstCell = null;
		while(sheet.getRow(rowNum+rowCount) != null){
			if(sheet.getRow(rowNum+rowCount).getCell(0) == null){
				rowCount++;
				continue;
			}
			firstCell = sheet.getRow(rowNum+rowCount).getCell(0).toString().trim();
//			System.out.println("Row 1"+firstCell);
			if(rowCount < 3){
				rowCount++;
				continue;
			}
			if(firstCell.equals("")){
				rowCount++;
				continue;
			}else if(firstCell.equals("表3")){
				break;
			}
			if(!isExcelDate(sheet.getRow(rowNum+rowCount).getCell(0))){
				rowCount++;
				continue;
			}
			
			VehicleRepairRecord vrr = new VehicleRepairRecord();
			vrr.setCreator(user);
			vrr.setDescription(sheet.getRow(rowNum+rowCount).getCell(2).getStringCellValue());
			vrr.setRcompany(sheet.getRow(rowNum+rowCount).getCell(3).getStringCellValue());
			vrr.setRdate(sheet.getRow(rowNum+rowCount).getCell(0).getDateCellValue());
			vrr.setRegistrant(sheet.getRow(rowNum+rowCount).getCell(4).getStringCellValue());
			vrr.setRtype(sheet.getRow(rowNum+rowCount).getCell(1).getStringCellValue());
			vrr.setVid(vp);
			list.add(vrr);
			rowCount++;
		}
		vBean.saveVehicleRepairRecords(list);
	}

	private void saveVehicleTechnicalDetail(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception{
		String vid = sheet.getRow(rowNum).getCell(5).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(5).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist."+vid);
			return;
		}
		
		VehicleTechnicalDetail vtd = vBean.getVehicleTechnicalDetailByVehicleId(vp.getId());
		if(vtd == null){
			vtd = new VehicleTechnicalDetail();
		}
		rowNum++;
		vtd.setVtype(sheet.getRow(rowNum+1).getCell(1).getStringCellValue());
		if(vtd.getVtype().length()>7) System.out.println("VID:"+vp.getVid()+" vtype:"+vtd.getVtype());
		vtd.setFactorycode(sheet.getRow(rowNum+1).getCell(3).getStringCellValue());
		
		String factorydateAndMadeIn = sheet.getRow(rowNum+1).getCell(5).getStringCellValue();
		String[] fami = factorydateAndMadeIn.split("\\/", -1);
		if(fami.length>0){
			if(fami[0].length() > 4){
				vtd.setFactorydate(HRUtil.parseDate(fami[0], "yyyy-MM-dd"));
			}
			if(fami.length == 2)
				vtd.setMadein(fami[1]);
			else{
				String madein = getTickValue(factorydateAndMadeIn)[0];
				if(madein == null || madein.trim().equals(""))
					madein="国产";
				vtd.setMadein(madein);
				if(vtd.getMadein().length()>7) System.out.println("VID:"+vp.getVid()+" madeIn:"+vtd.getMadein());
			}
		}
		
		vtd.setVincode(sheet.getRow(rowNum+2).getCell(1).getStringCellValue().trim());
		vtd.setBasecode(sheet.getRow(rowNum+2).getCell(3).getStringCellValue().trim());
		
		vtd.setVlevel(sheet.getRow(rowNum+2).getCell(5).getStringCellValue().trim());
		if(vtd.getVlevel().length()>7) System.out.println("VID:"+vp.getVid()+" Vlevel:"+vtd.getVlevel());

		String[] size = getSize(sheet.getRow(rowNum+3).getCell(1).getStringCellValue().trim());
		vtd.setVlength(size[0]);
		vtd.setVwidth(size[1]);
		vtd.setVheight(size[2]);
		sheet.getRow(rowNum+3).getCell(3).setCellType(Cell.CELL_TYPE_STRING);
		vtd.setWeight(sheet.getRow(rowNum+3).getCell(3).toString());
//		vtd.setSitarrange(sheet.getRow(3).getCell(5).getStringCellValue());
		vtd.setSitarrange("2+2");
		
		sheet.getRow(rowNum+4).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
		vtd.setSits(sheet.getRow(rowNum+4).getCell(1).getStringCellValue().trim());
		vtd.setDragweight(sheet.getRow(rowNum+4).getCell(3).getStringCellValue().trim());
		try{
			vtd.setVaxis(sheet.getRow(rowNum+4).getCell(5).getStringCellValue().trim());
		}catch(Exception e){
			vtd.setVaxis("2/1");
		}
		if(vtd.getVaxis().length()>7) System.out.println("VID:"+vp.getVid()+" axis:"+vtd.getVaxis());
		
		sheet.getRow(rowNum+5).getCell(3).setCellType(Cell.CELL_TYPE_STRING);
		vtd.setEnginecode(sheet.getRow(rowNum+5).getCell(3).getStringCellValue().trim());
		vtd.setEnginemodel(sheet.getRow(rowNum+5).getCell(1).getStringCellValue().trim());
		vtd.setFueltype(sheet.getRow(rowNum+5).getCell(5).getStringCellValue().trim());
		
		vtd.setEnginehorse(sheet.getRow(rowNum+6).getCell(3).getStringCellValue().trim());
		vtd.setEnginepower(sheet.getRow(rowNum+6).getCell(1).getStringCellValue().trim());
		
		String release = getTickValue(sheet.getRow(rowNum+6).getCell(5).getStringCellValue())[0];
		if(release == null || release.trim().equals("")){
			release = "国Ⅲ";
		}
		vtd.setReleasestandard(release);
		if(vtd.getReleasestandard().length()>7) System.out.println("VID:"+vp.getVid()+" releaseStandard:"+vtd.getReleasestandard());
		
		vtd.setDrivermode(sheet.getRow(rowNum+7).getCell(1).getStringCellValue().trim());
		if(vtd.getDrivermode().length()>7) System.out.println("VID:"+vp.getVid()+" driverMode:"+vtd.getDrivermode());
		
		vtd.setTyre(sheet.getRow(rowNum+7).getCell(3).getStringCellValue().trim());
//		vtd.setFrontlight(sheet.getRow(7).getCell(5).getStringCellValue());
		vtd.setFrontlight("四灯制");
		
		vtd.setVariatormode(getTickValue(sheet.getRow(rowNum+8).getCell(1).getStringCellValue())[0].trim());
		if(vtd.getVariatormode().length()>7) System.out.println("VID:"+vp.getVid()+" variatorMode:"+vtd.getVariatormode());
		
		vtd.setRetarder(getTickValue(sheet.getRow(rowNum+8).getCell(3).getStringCellValue())[0].trim());
		vtd.setTurner(getTickValue(sheet.getRow(rowNum+8).getCell(5).getStringCellValue())[0].trim());
		if(vtd.getTurner().length()>7) System.out.println("VID:"+vp.getVid()+" turner:"+vtd.getTurner());
		
		String breakStr = sheet.getRow(rowNum+9).getCell(1).getStringCellValue().trim();
		String breakmode = breakStr.substring(0,breakStr.indexOf("前轮"));
		String frontBreak = breakStr.substring(breakStr.indexOf("前轮：")+3,breakStr.indexOf("后轮："));
		String backBreak = breakStr.substring(breakStr.indexOf("后轮：")+3,breakStr.length());
		
		vtd.setBreakmode(getTickValue(breakmode)[0].trim());
		if(vtd.getBreakmode().length()>7) System.out.println("VID:"+vp.getVid()+" breakMode:"+vtd.getBreakmode());
		
		vtd.setFrontbreakList(Arrays.asList(getTickValue(frontBreak)));
		vtd.setBackbreakList(Arrays.asList(getTickValue(backBreak)));
		
		String hangmodeStr = sheet.getRow(rowNum+10).getCell(1).getStringCellValue().trim();
		String fronthang = hangmodeStr.substring(hangmodeStr.indexOf("前轮：")+3,hangmodeStr.indexOf("后轮："));
		String backhang = hangmodeStr.substring(hangmodeStr.indexOf("后轮：")+3,hangmodeStr.length());
		vtd.setHangmodefrontList((Arrays.asList(getTickValue(fronthang))));
		vtd.setHangmodebackList((Arrays.asList(getTickValue(backhang))));
		vtd.setOtherList(Arrays.asList(getTickValue(sheet.getRow(rowNum+11).getCell(1).getStringCellValue())));
		vtd.setVid(vp);
		vtd.setCreator(user);
		vBean.saveVehicleTechDetail(vtd, user);
	}

	private void saveVehicleProfilesBasicInfo(VehicleBean vBean, Account user,
			XSSFSheet sheet, int rowNum) throws Exception{
		String vid = sheet.getRow(rowNum).getCell(5).getStringCellValue();
		String selfId = sheet.getRow(rowNum+1).getCell(5).getStringCellValue();
		vid = HRUtil.removeNoneNumber(vid);
		try{
			selfId = selfId.split("：",-1)[1].trim();
		}catch(Exception e){
			selfId = selfId.split(":",-1)[1].trim();
		}
		
		VehicleProfile vp = vBean.getVehicleProfileLikeVid(vid, selfId);
		if(vp == null){
			System.out.print("vp not exist.");
		}
		System.out.println("Vid:"+vid + " SelfId:"+selfId);
		
		vp.setSelfid(selfId);
		vp.setVcolor(sheet.getRow(rowNum+3).getCell(2).getStringCellValue());
		vp.setRegisterDate(sheet.getRow(rowNum+3).getCell(3).getDateCellValue());
		vp.setSubcompany("公交二");
		vp.setCreator(user);
		vp.setStatus(VehicleProfile.statusA);
		vp = vBean.saveVehicle(vp, user);
	
		
		VehicleBasicInfo vbi = vBean.getVehicleBasicInfoByVehicleId(vp.getId());
		if(vbi == null)
			vbi = new VehicleBasicInfo();
		vbi.setCompanytype(sheet.getRow(rowNum + 10).getCell(1).getStringCellValue());
		vbi.setMaintenancemile(sheet.getRow(rowNum + 17).getCell(1).getStringCellValue());
		vbi.setMaster(sheet.getRow(rowNum + 8).getCell(1).getStringCellValue());
		vbi.setOperatelevel(sheet.getRow(rowNum + 12).getCell(1).getStringCellValue());
		vbi.setOperatemode(sheet.getRow(rowNum + 11).getCell(1).getStringCellValue());
		
		sheet.getRow(rowNum + 9).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
		vbi.setOperatenumber(sheet.getRow(rowNum + 9).getCell(1).toString());
		vbi.setOperaterange(sheet.getRow(rowNum + 13).getCell(1).getStringCellValue());
		vbi.setOperateroute(sheet.getRow(rowNum + 15).getCell(1).getStringCellValue());
		sheet.getRow(rowNum + 14).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
		String number = sheet.getRow(rowNum + 14).getCell(1).toString();
		vbi.setTransportnumber(number);
		vbi.setSource(sheet.getRow(rowNum + 16).getCell(1).getStringCellValue());
		
		vbi.setCreator(user);
		vbi.setVid(vp);
		vBean.saveVehicleBasicInfo(vbi, user);
	}
}
