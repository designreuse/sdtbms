package com.bus.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExcelFileProcessor {

	
	protected DataInputStream datais=null;
	protected BufferedReader bf=null;
	protected String strLine;
	protected int index = 0;
	
	public ExcelFileProcessor(){
		
	}
	
	public ExcelFileProcessor(FileInputStream fis){
		try{
			datais = new DataInputStream(fis);
			bf = new BufferedReader(new InputStreamReader(datais));
			index = 0;
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public boolean hasNextLine(){
		try {
			strLine = bf.readLine();
			index++;
			if(strLine == null){
				datais.close();
				return false;
			}else{
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
