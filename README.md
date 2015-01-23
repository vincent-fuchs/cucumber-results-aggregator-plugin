# cucumber-results-aggregator-plugin

This Maven plugin is a workaround to a problem I am facing regularly : in a multi-module project, if we have Cucumber tests spread in various modules, we can't publish a single aggregated view in Jenkins.

The goal of this Maven plugin is to aggregate Cucumber results from a configured list of subModules, so that it can be processed by Cucumber reporting plugin, as it was all coming from a single module.

### Plugin configuration

Assuming you have a structure like this :

myProject
   |-->module1
   |-->module2
   |-->module3
   -pom.xml

If you have Cucumber results in 2 modules (module1 and module3), you can configure the plugin like this :

```
<plugin>
  <groupId>com.github.vincent-fuchs</groupId>
  <artifactId>cucumber-results-aggregator-plugin</artifactId>
  <version>1.0</version>
  <executions>
    <execution>
      <id>execution</id>
      <phase>integration-test</phase>
      <goals>
        <goal>aggregate</goal>
      </goals>
      <configuration>
        <modules>
          <module>module1</module>
          <module>module3</module>
        </modules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

It will :
- look into the 2 modules for /target/cucumber/*.json files (this can be overridden in configuration with filePattern attribute)
- generate an aggregatedCucumberResults.json file in target directory of main project (myProject). It will be sorted following the order of configured modules. Then for each module, it will follow the alphabetical order    
 
You can then configure maven-cucumber-reporting plugin to read this file and generate the reports.
