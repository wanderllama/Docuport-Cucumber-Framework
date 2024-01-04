TODO throughout the project that would need to be updated for a specific application  

run from CLI using:

* mvn test
  * this will cause the downloadDriver.sh script to run before build and download WebdDiver
  * change pom.xml property to pick WebDriver (edge, chrome, firefox)
  * change driver.path property in pom.xml to define your own WebDriver and script will not download
  * will run based of testNG.xml (all scenarios)

* mvn -Ddataproviderthreadcount=<thread count> -DmaxRetryCount=<retry count> -Dcucumber.filter.tags="(@<tag name>
  or @<another tag name>) and not @wip and not @defect and not @closed" clean verify
  * <thread count>=number of threads for parallel execution
  * <retry count>=number of times to retry a failed scenario
  * <tag name>=scenario tag you wish to include in test execution. Multiple can be added (@login or @downloads)