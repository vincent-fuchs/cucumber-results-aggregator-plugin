package com.github.vincent_fuchs.cucumber_results_aggregator_plugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which aggregates Cucumber results from different modules
 * 
 * @goal aggregate
 * 
 * @phase pre-site
 */
public class CucumberResultsAggregator extends AbstractMojo {

	/**
	 * 
	 * @parameter default-value="${project.build.directory}"
	 */
	private File targetDirectory;

	private File rootModuleDirectory=null;

	/**
	 * 
	 * @parameter default-value="/target/cucumber/*.json"
	 */
	private String filePattern;

	/**
	 * @parameter
	 */
	private List<String> modules = new ArrayList<String>();

	/**
	 * @parameter default-value="aggregatedCucumberResults.json"
	 */
	private String outputFileName;

	public void execute() throws MojoExecutionException {
		
		if(getLog().isInfoEnabled()){
			getLog().info("targetDirectory:" + targetDirectory);
			getLog().info("configured modules:");
			for(String module : modules){
				getLog().info("\t- "+module);
			}
		}
		
		rootModuleDirectory = targetDirectory.getParentFile();
			
		List<File> cucumberResultFilesToAggregate = null;

		try {	
			
			ResultFilesFinder resultFilesFinder=new ResultFilesFinder(rootModuleDirectory.getCanonicalPath(), modules, filePattern);
	
			cucumberResultFilesToAggregate=resultFilesFinder.find();

		} catch (IOException e) {
			throw new MojoExecutionException(
					"Issue while searching for cucumber results files", e);
		}

		aggregateAndOutputFile(cucumberResultFilesToAggregate);

	}

	private void aggregateAndOutputFile(
			List<File> cucumberResultFilesToAggregate) {

		
		JsonConcatenator jsonConcatenator=new JsonConcatenator();
	
		for (File cucumberResultFile : cucumberResultFilesToAggregate) {
			 jsonConcatenator.add(cucumberResultFile);
		}

		outputAggregatedFile(jsonConcatenator);
	}

	private void outputAggregatedFile(JsonConcatenator jsonConcatenator) {
		PrintWriter out = null;
		try {
			
			if(targetDirectory!=null){
			out = new PrintWriter(targetDirectory + File.separator	+ outputFileName);
			out.write(jsonConcatenator.getResultAsString());
			}
			else{
				getLog().warn("couldn't write output file, as target directory doesn't exist");
			}
			
		} catch (IOException e) {
			getLog().error("can't write ouput file",e);
		} finally {
			if(out!=null){
				out.close();
			}
		}
	}

}
