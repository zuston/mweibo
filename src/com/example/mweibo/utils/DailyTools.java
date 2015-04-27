package com.example.mweibo.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;





import java.io.Serializable;
import java.util.Map;

import android.content.Context;

public class DailyTools implements Serializable{
	public void write(Context context,String filename,String response) throws IOException{
		FileOutputStream fos;
		try {
			//设置为不append，直接刷新一次重置一次
			fos = context.openFileOutput(filename,Context.MODE_PRIVATE);
			fos.write(response.getBytes());
	    	fos.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
    	
	}
	public String read(String filename,Context context) throws IOException{
    	try {
    		FileInputStream inStream=context.openFileInput(filename);
            StringBuffer sb=new StringBuffer("");
            byte[] buffer=new byte[1024];
            int length=0;
            while((length=inStream.read(buffer))!=-1)   {
                sb.append(new String(buffer,0,length));
            }
            inStream.close();
            return sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	 private Map<String,Object> map;
	 public Map<String, Object> getMap() {
	        return map;
	    }
	 public void setMap(Map<String, Object> map) {
	        this.map = map;
			
	    }
}
