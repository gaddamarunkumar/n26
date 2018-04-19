
Tech Stack Used *********************************
Build Tool - Maven
JDK - 1.8
Server side Frameworks - Spring Boot, lombok
Testing - JUnit

How to run? *************************************
1) Navigate to working directory(pom.xml, src should be the contents) in cmd/terminal.
2) Run below command to build and compile the application. Maven setup(m2, settings xml),Internet/Nexus connection should be available for successfull build. For a off line build dependencies mentioned in pom xml should be there in m2 local.
	mvn clean install
3) Run the below command to deploy and run the application 
	mvn spring-boot:run
		By default the port will be 8080, if you want to change the port, replace the port number with <port> in below command
			mvn spring-boot:run -Dserver.port=<port>

Import to IDE Eclipse *********************************
1) Open IDE, Import as an maven project
2) Open com.n26.codechallenge.boot.StarterApp, and run it as Java Application. If the build was success in the first step, this should work fine. 

Rest Calls ***********************************
1) Post a transaction
	URL: http://localhost:8080/transactions
	Body:
		{
		"amount": 78.3,
		"timestamp": 1478192204000
		}
2) Get statistics
	URL: http://localhost:8080/statistics
