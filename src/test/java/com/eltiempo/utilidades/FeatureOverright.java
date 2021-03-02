package com.eltiempo.utilidades;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class FeatureOverright {
    
	private FeatureOverright() {
    	
    }
    
	private static List<String> fileData;
    private static boolean foundHashTag;
    private static boolean featureData;
    public static void setFeatureData(Boolean dato){featureData = dato;}
    public static void setFoundHashTag(Boolean dato){foundHashTag = dato;}
    public static Boolean getFeatureData() {return featureData;}
    public static Boolean getfoundHashTag() {return foundHashTag;}
    private static final Logger LOGGER = Logger.getLogger(FeatureOverright.class.getName());
    private static StringBuilder cellData = new StringBuilder();
    
	private static List<String> setExcelDataToFeature(File featureFile, String tag)
	            throws InvalidFormatException, IOException {
		 	String tagActual = tag.replace("@", "");
		 	fileData = new ArrayList<>();
	        try (BufferedReader buffReader = new BufferedReader(
	                new InputStreamReader(new BufferedInputStream(new FileInputStream(featureFile)), "UTF-8"))) {
	            String data;
	           
	            while ((data = buffReader.readLine()) != null) {
	                String sheetName = "Sin nombre";
	                String excelFilePath = "Sin ruta";
	               
	                if (data.trim().contains("##@externaldata")) {
	                	excelFilePath = new File("").getAbsolutePath()+'\\'+data.substring(StringUtils.ordinalIndexOf(data, "@", 2)+1, data.lastIndexOf("@"));
	                    sheetName = data.substring(data.lastIndexOf("@")+1, data.length());
	                    setFoundHashTag(true);
	                    sobreEscribirDatoExterno(foundHashTag,tagActual,excelFilePath,sheetName,data);                    
	                }else {
	                	 validarDataTableFeature(data);
	                	 validarDataFeature(data);
	                }
	            }
	        }
	        return fileData; 
	    }
    

	public static void overrideFeatureFiles(String listadoTags) {
    	try {
			String [] listado = listadoTags.split(",");
	    	for(String tagActual :listado) { 
		   		File featureFile = new File(FeatureRepository.getPathFeature(tagActual));
	            if(featureFile.exists()) {
	            	overWriteFeature(featureFile,tagActual);
	            }		           
	       }
    	}catch (Exception e) {
    		LOGGER.info("overrideFeatureFiles, feature file " + e.getMessage());
    	}
    }

    private static void overWriteFeature(File featureFile, String tagActual) {
    	try {
	   		List<String> featureWithExcelData = setExcelDataToFeature(featureFile,tagActual);
            try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(featureFile), "UTF-8"));) {
                for (String string : featureWithExcelData) {
                    writer.write(string);
                    writer.write("\n");                    
                }
                writer.flush();
            }
            
    	}catch (Exception e) {
    		LOGGER.info("overWriteFeature " + e.getMessage());
    	}

		
	}
    /**
     * 
     * @param foundHashTag = tag actual del caso de prueba
     * @param tagActual
     * @param excelFilePath
     * @param sheetName
     * @param data
     */
	private static void sobreEscribirDatoExterno(boolean foundHashTag, String tagActual, String excelFilePath, String sheetName, String data) {
	try {
	    List<Map<String, String>> excelData;	
    	if (foundHashTag && sheetName.equalsIgnoreCase(tagActual)) { 
    			/*recorrer todas las filas del excel*/
    		 	fileData.add(data);
    			excelData = (List<Map<String,String>>) new LectorExcel().getData(excelFilePath, sheetName);
    			for(int numeroFila=0;numeroFila<excelData.size()-1;numeroFila++) { 
    				cellData.setLength(0);
    				if(excelData.get(numeroFila).get("estado").equalsIgnoreCase("A")) {
    					cellData=getDataExternal(excelData,numeroFila,cellData);
        				if(cellData.length()>0) {
        					fileData.add(cellData.substring(2) + "|");
        				}
    				}
    			}
            setFoundHashTag(false);
            setFeatureData(true);              
        }else {
        	fileData.add(data);
        }   		
	}catch(Exception e) {
		LOGGER.info("sobreEscribirDatoExterno " + e.getMessage());
	}		
	}


	/**
	 * 
	 * @param excelData = columna del archivo excel con los datos para ser incluidos
	 * @param numeroFila = posicion o fila actual del excel
	 * @param cellData = objeto de String builder
	 * @return = devolver los datos de de tipo string builder
	 */
	private static StringBuilder getDataExternal(List<Map<String, String>> excelData, int numeroFila, StringBuilder cellData) {
		for (Entry<String, String> mapData : excelData.get(numeroFila).entrySet()) {
			cellData.append("|" + mapData.getValue());
		}
		return cellData;	
		 
	}

	private static void validarDataTableFeature(String data) {
		try {
	        if(data.startsWith("|") || data.endsWith("|")){
	        	 if(!getFeatureData()){
	        		 fileData.add(data);	            		
	        	 }	
	        }else {
	        	 setFeatureData(false);
	        }
		}catch (Exception e) {
			LOGGER.info("validarDataTableFeature " + e.getMessage());
		}	
	}
	private static void validarDataFeature(String data) {
		try {
	        if(!data.startsWith("|") && !data.endsWith("|") && !getFeatureData()){
	        		 fileData.add(data);	            			
	        }
		}catch (Exception e) {
			LOGGER.info("validarDataFeature " + e.getMessage());
			}
		
		
	}
}
