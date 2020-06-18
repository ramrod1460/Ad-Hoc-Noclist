

# Spring Boot Noclist solution

This is a simple and quick Spring Boot (*SB*) based Noclist solution based on SB CmdLineRunner
This README.md ( *markdown file* ) may be viewed with formatting using the free online tool
found at http://dillinger.io  ( *or any other decent markdown tool* ) ...just paste the README.md file text into the editor pane.

# Requirements

This app assumes that you have  the following tools installed on the system where you intend to compile and run.

 - Java 8 or higher  
 - Docker ( *to run Noclist  server* )
 - Maven 3.6 or higher to build the application
 
##  Build the application

Place the source code in the desired folder.
Ensure that you are in the folder containing the pom.xml file that accompanies the app.

Check the application.properties file in the src/main/resources folder to ensure that the property settings
are as required for your environment... assuming that the requirements in the homework as regards the URL's
have not changed, only the SleepTime may require adjustment. The SleepTime setting is to allow a small defined 
period of time between retries of a failure in communicating with the auth or users API call(s).

Base URI for API calls
**noclist.base** = http://0.0.0.0:8888

Users part of URL for API calls
**noclist.users**=/users

Auth part of URL for API calls
**noclist.auth**=/auth

Header tokens:

**noclist.BadsecAuthentication_token**=Badsec-Authentication-Token

**noclist.XRequestToken**=X-Request-Checksum

Max attempts to API calls
**noclist.MaxAttempts**=3

Sleep time on API retry ...can be 0 
**noclist.SleepTime**=1000

Execute to following command:

    mvn package
    
*The above command will construct a Java jar file for execution. 
The constructed jar file will be in the folder named **target**.

Activate the Noclist server

    docker run --rm -p 8888:8888 adhocteam/noclist

## Execute the application
As required by the instructions for construction of the application 
*( [https://homework.adhoc.team/noclist/](https://homework.adhoc.team/noclist/) )*
the application must only send the useful results to **stdout**. Errors and other logging are to be sent to **stderr**.
Based on the above requirements, there are a few ways to execute the application.

**1.**
Execute the app and preserve the output from **stderr** to filename
**err**.  The **stdout** output will be directed to the console.

java -jar target\NoclistSolution-0.0.1-SNAPSHOT.jar 2> err

**2.**
Execute the app and preserve the output **stderr** to filename
**err**.  The **stdout** output will be directed to filename **norm** on your system.

java -jar target\NoclistSolution-0.0.1-SNAPSHOT.jar > norm 2> err

**3.**
Execute the app and preserve the output **stderr** to filename **err**. **stdout** output will **piped** to application **x**, where application **x** is any application capable of receiving an output stream from the *pipe* symbol. The following example pipes output through the **more** command.

java -jar target\NoclistSolution-0.0.1-SNAPSHOT.jar | more  2> err

