package com.github.vincent_fuchs.cucumber_results_aggregator_plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultFilesFinder {

	private String rootDirectory;
	private List<String> modules;
	private String pattern;
	
	 private Logger log = LoggerFactory.getLogger(ResultFilesFinder.class);
		

	
	public ResultFilesFinder(String rootDirectory, List<String> modules,
			String pattern) {
		
		this.rootDirectory=rootDirectory;
		this.modules=modules;
		this.pattern=pattern;
		
	}


	public List<File> find() throws IOException {
		
		List<File> matchingFiles = new ArrayList<File>();

		log.info("root directory : "+rootDirectory);
		
		if(modules.isEmpty()){
			log.warn("no modules configured, so not doing anything");
		}
		
		for (String module : modules) {
			
			File childModule = new File(rootDirectory + File.separator + module);

			log.info("looking for Cucumber results in :" + childModule);
			
			if(!childModule.exists()){
				log.warn("\tskipping, as directory doesn't exist");
				break;
			}
			
			
			DirectoryScanner scanner= new DirectoryScanner();
			scanner.setIncludes(new String[]{pattern});
			scanner.setBasedir(childModule);
			scanner.setCaseSensitive(false);
			scanner.scan();
			String[] foundFiles = scanner.getIncludedFiles();

			if(foundFiles.length==0){
				log.warn("no json result file found for module "+module+" in "+rootDirectory+File.separator+module+File.separator+pattern);
			}

			List<File> matchingFilesForThisModule=new ArrayList<File>();
			
			for(String matchingFile : foundFiles){
				
				File jsonCucumberFile = new File(rootDirectory + File.separator + module + File.separator + matchingFile);
				
				if (jsonCucumberFile.exists()) {
					log.info("\t found "+jsonCucumberFile.getCanonicalPath());
					matchingFilesForThisModule.add(jsonCucumberFile);
				} 
				
			}
			
			Collections.sort(matchingFilesForThisModule);
			matchingFiles.addAll(matchingFilesForThisModule);
		
		}
		
		return matchingFiles;
		
	}

}
