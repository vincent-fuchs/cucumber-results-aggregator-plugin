package com.github.vincent_fuchs.cucumber_results_aggregator_plugin;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ResultFilesFinderTest {

	private Properties properties;

	private ResultFilesFinder resultFilesFinder;

	private String rootDirectory;

	private String pattern = "/target/cucumber/*.json";

	@Before
	public void setUp() throws IOException {

		properties = load("/cucumberResultsAggregatorPlugin.properties");

		rootDirectory = properties.getProperty("rootDirectory")
				+ File.separator + "rootDir";
		List<String> modules = new ArrayList<String>();

		modules.add("module1");
		modules.add("module2");

		resultFilesFinder = new ResultFilesFinder(rootDirectory, modules,
				pattern);
	}

	@Test
	public void shouldReturnExpectedNumberOfFiles() throws IOException {

		List<File> filesToAggregate = resultFilesFinder.find();

		assertThat(filesToAggregate).hasSize(4);

	}

	@Test
	public void shouldReturnFilesinExpectedOrder() throws IOException {

		List<File> filesToAggregate = resultFilesFinder.find();

		List<String> actualFileNames = toListOfString(filesToAggregate);

		List<String> expectedFileNames = new ArrayList<String>();
		expectedFileNames.add(getFullPathToExpectedFile("module1",
				"module1.json"));
		expectedFileNames.add(getFullPathToExpectedFile("module1",
				"module1a.json"));
		expectedFileNames.add(getFullPathToExpectedFile("module2",
				"1-module2.json"));
		expectedFileNames.add(getFullPathToExpectedFile("module2",
				"2-module2.json"));

		assertThat(actualFileNames)
				.containsExactlyElementsOf(expectedFileNames);

	}

	private String getFullPathToExpectedFile(String module, String fileName) {
		return rootDirectory + File.separator + module + File.separator
				+ "target" + File.separator + "cucumber" + File.separator
				+ fileName;
	}

	@Test
	public void shouldReturnFilesinExpectedOrder_withModule2First()
			throws IOException {

		List<String> modules = new ArrayList<String>();

		modules.add("module2");
		modules.add("module1");

		resultFilesFinder = new ResultFilesFinder(rootDirectory, modules,
				pattern);

		List<File> filesToAggregate = resultFilesFinder.find();

		List<String> actualFileNames = toListOfString(filesToAggregate);

		List<String> expectedFileNames = new ArrayList<String>();
		expectedFileNames.add(getFullPathToExpectedFile("module2",
				"1-module2.json"));
		expectedFileNames.add(getFullPathToExpectedFile("module2",
				"2-module2.json"));
		expectedFileNames.add(getFullPathToExpectedFile("module1",
				"module1.json"));
		expectedFileNames.add(getFullPathToExpectedFile("module1",
				"module1a.json"));

		assertThat(actualFileNames)
				.containsExactlyElementsOf(expectedFileNames);

	}

	private List<String> toListOfString(List<File> files) throws IOException {
		List<String> filesAsString = new ArrayList<String>();

		for (File file : files) {
			filesAsString.add(file.getCanonicalPath());
		}

		return filesAsString;

	}

	private Properties load(String resourcePath) throws IOException {
		java.io.InputStream in = ResultFilesFinderTest.class.getResourceAsStream(resourcePath);
		try {
			Properties properties = new Properties();
			properties.load(new InputStreamReader(in, "UTF8"));
			return properties;
		} finally {
			in.close();
		}
	}

}
