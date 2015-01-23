package com.github.vincent_fuchs.cucumber_results_aggregator_plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class JsonConcatenator {

	JsonArray combinedJsonResults = new JsonArray();
	
	 private Logger log = LoggerFactory.getLogger(JsonConcatenator.class);
	
	public void add(File jsonFile) {
		
		 JsonArray fileAsJsonArray = getJsonFromFile(jsonFile);

		 for(int i=0 ; i<fileAsJsonArray.size();i++){
			 combinedJsonResults.add(fileAsJsonArray.get(i));
		 }		
		
	}

	public String getResultAsString() {
		
		
			return combinedJsonResults.toString();
		
		
				
	}
	
	
	private JsonArray getJsonFromFile(File cucumberResultFile) {

		JsonParser jsonParser = new JsonParser();
		JsonArray result = null;

		FileReader cucumberResultFileReader;
		try {
		
			cucumberResultFileReader = new FileReader(cucumberResultFile);
			result = (JsonArray)jsonParser.parse(cucumberResultFileReader);
		
		} catch (FileNotFoundException e) {
			log.warn("can't find input file to JSON-ify",e);
		} 
	
		return result;

	}

}
