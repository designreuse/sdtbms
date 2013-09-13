package com.bus.util.importfile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ImportFile {

	public DataInputStream datais=null;
	public BufferedReader bf=null;
	public String strLine = "";
	public int index = 0;
	
	public ImportFile(FileInputStream fis){
		try{
			datais = new DataInputStream(fis);
			bf = new BufferedReader(new InputStreamReader(datais));
			strLine = "";
			this.index = 0;
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public boolean hasNextLine(){
		try {
			if(strLine == null)
				return false;
			strLine = bf.readLine();
			this.index++;
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
