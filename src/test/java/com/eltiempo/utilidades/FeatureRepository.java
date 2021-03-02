package com.eltiempo.utilidades;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FeatureRepository {
	  private FeatureRepository() {
		    throw new IllegalStateException("error al obtener dato");
		  }
	  
	 public static String getPathFeature(String tag) throws IOException {
		 FileInputStream fis = null;
		 fis = new FileInputStream("tagRepository.conf");		 
		 Properties prop = null; 
	     prop = new Properties();
	     prop.load(fis);
	     return prop.getProperty(tag);
	}
	 
}
