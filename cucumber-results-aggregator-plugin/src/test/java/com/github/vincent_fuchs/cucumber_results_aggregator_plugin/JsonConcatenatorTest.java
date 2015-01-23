package com.github.vincent_fuchs.cucumber_results_aggregator_plugin;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import net.sf.json.util.JSONUtils;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class JsonConcatenatorTest {

	JsonConcatenator jsonConcatenator=new JsonConcatenator();
	
	@Before
	public void setUp(){
		String file=getClass().getResource("/cucumber_single.json").getFile();

		File firstJsonFile=new File(file);
		assertThat(firstJsonFile).exists();
		
		jsonConcatenator.add(firstJsonFile);
		
		File secondJsonFile=new File(file);
		
		jsonConcatenator.add(secondJsonFile);
	}
	
	
	@Test
	public void shouldConcatenate2validJsonDocIntoValidJson(){
		
		
		String resultingString=jsonConcatenator.getResultAsString();
		
		assertThat(resultingString).isNotNull();
		assertThat(JSONUtils.mayBeJSON(resultingString));	
		
	}
	
	@Test
	public void shouldConcatenate2validJsonDocIntoJsonArray(){
				
		String resultingString=jsonConcatenator.getResultAsString();
			
		JsonElement parsedResultingString=new JsonParser().parse(resultingString);
		
		assertThat(parsedResultingString.isJsonArray()).isTrue();
		
		//both input file contain one feature each, so size should be 2
		assertThat((JsonArray)parsedResultingString).hasSize(2);
		
	}
	
}
