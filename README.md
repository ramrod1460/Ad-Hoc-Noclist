# Spring Boot Noclist solution

This is a simple and quick Spring Boot (*SB*) based Noclist solution based on SB CmdLineRunner


# Requirements

This app assumes that you have  the following tools installed on the system where you intend to compile and run.

 - Java 8 or higher  
 - Docker ( *to run Noclist  server* )
 - Maven 3.x or higher to build the application
 
##  Build the application

Place the source code in the desired folder.
Ensure that you are in the folder containing the pom.xml file that accompanies the app.
Execute to following command:

    mvn package
    
*The above command will construct a Java jar file for execution. The constructed jar file will be in the folder named **target**.*

Activate the Noclist server

    docker run --rm -p 8888:8888 adhocteam/noclist

## Execute the application
As required by the instructions for construction of the application 
*( [https://homework.adhoc.team/noclist/](https://homework.adhoc.team/noclist/) )*
the application must only send the useful results to **stdout**. Errors and other logging are to be sent to **stderr**.
Based on the above requirements, there are a few ways to execute the application.

1. Execute the app and preserve the output from **stderr** to filename **err**. 
**stdout** output will be directed to the console.

    java -jar target\NoclistSolution-0.0.1-SNAPSHOT.jar 2> err

2.  Execute the app and preserve the output **stderr** to filename **err**. 
**stdout** output will be directed to filename **norm**.

    java -jar target\NoclistSolution-0.0.1-SNAPSHOT.jar > norm 2> err
    
3.  Execute the app and preserve the output **stderr** to filename **err**. **stdout** output will **piped** to application **x**, where application **x** is any application capable of receiving an output stream from the *pipe* symbol. The following example pipes output through the **more** command.

    java -jar target\NoclistSolution-0.0.1-SNAPSHOT.jar | more  2> err