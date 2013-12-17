package com.bus.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 当用到Excel文件时提供相应的帮助函数
 * @author Administrator
 *
 */
public class ExcelFile {

	protected XSSFWorkbook wb07 = null;
	protected HSSFWorkbook wb03 = null;
	protected FormulaEvaluator evaluator = null;
	
	public void init(FileInputStream fis, boolean isExcelFile2007){
		try{
			if(isExcelFile2007)
				wb07 = new XSSFWorkbook(fis);
			else
				wb03 = new HSSFWorkbook(fis);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查文件名是否为.xls 或.xlsx
	 * @param file
	 * @return
	 */
	public boolean isExcelFile(String file){
		if(file.contains(".xls") || file.contains(".xlsx"))
			return true;
		else
			return false;
	}
	
	/**
	 * 检查文件是否为2003文件xls
	 * @param file
	 * @return
	 */
	public boolean isExcelFile2003(String file){
		if(file.contains(".xls"))
			return true;
		else
			return false;
	}
	
	/**
	 * 检查文件是否为2007以上的版本xlsx
	 * @param file
	 * @return
	 */
	public boolean isExcelFile2007(String file){
		if(file.contains("xlsx"))
			return true;
		else
			return false;
	}
	
	/**
	 * 检查Cell是否为日期类型
	 * @param cell
	 * @return
	 */
	protected boolean isExcelDate(Cell cell){
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
	
	public XSSFWorkbook getWb07() {
		return wb07;
	}
	public void setWb07(XSSFWorkbook wb07) {
		this.wb07 = wb07;
	}
	public HSSFWorkbook getWb03() {
		return wb03;
	}
	public void setWb03(HSSFWorkbook wb03) {
		this.wb03 = wb03;
	}
	public FormulaEvaluator getEvaluator() {
		return evaluator;
	}
	public void setEvaluator(FormulaEvaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	
	
}
