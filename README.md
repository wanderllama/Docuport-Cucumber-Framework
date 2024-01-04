TODO throughout the project that would need to be updated for a specific application  

run from CLI using:

* mvn test
  * this will cause the downloadDriver.sh script to run before build and download WebdDiver
  * change pom.xml property to pick WebDriver (edge, chrome, firefox)
  * change driver.path property in pom.xml to define your own WebDriver and script will not download
  * will run based of testNG.xml (all scenarios)

* mvn -Ddataproviderthreadcount=<thread count<l>> -DmaxRetryCount=<retry count<l>> -Dcucumber.filter.tags="(@<tag
  name<l>>
  or @<another tag name<l>>) and not @wip and not @defect and not @closed" clean verify
  * <thread count<l>>=number of threads for parallel execution
  * <retry count<l>>=number of times to retry a failed scenario
  * <tag name<l>>=scenario tag you wish to include in test execution, Multiple tags can be added (@<tag name<l>>
    or @<another tag name<l>>

* bash -c "source mvnFlags.sh; start -h" (requires assigning value to variables line 2-8)
  * will show you flag options and arguments to trigger a maven execution
  * ex:  bash -c "source mvnFlags.sh; start -t 2 -r 1 -env dev -m test -tag @jw"
    * will trigger parrallel execution with 2 threads, will retry failed test once, testing will occur in dev env, the scenarios with @jw tag will run using mvn test
