package com.ibm.mobilefirst.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class AccessProperties {

	public static Properties configFile = new Properties();
	
	static Logger logger = Logger.getLogger(AccessProperties.class.getName());

	public static void getInstance() {
		
		try {

			 File dir = new File(".");
		        String[] extensions = new String[]{"properties"};
		        Collection<File> files = FileUtils.listFiles(dir, extensions, false);
		        for (File file : files) {
		            try {
		                try (FileReader fileReader = new FileReader(file)) {
		                    configFile.load(fileReader);
		                }
		            } catch (IOException e) {
		                logger.severe("Unable to load properties files : "+file);
		            }

		        }

		} catch (Exception e) {
			logger.info(e.toString());
		}
	}

}
