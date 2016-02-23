package com.uipl.fitforyou.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Converter {
	public static StringBuilder inputStreamToString(InputStream is) {
		   String rLine = "";
		   StringBuilder answer = new StringBuilder();
		   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		 
		   try {
		    while ((rLine = rd.readLine()) != null) {
		     answer.append(rLine);
		    }
		   }
		   catch (IOException e) {
		     e.printStackTrace();
		    //Toast.makeText(context,"Error..." + e.toString(), Toast.LENGTH_LONG).show();
		    System.out.println("Converter@Error..." + e.toString());
		   }
		   return answer;

    }
	
	
}
