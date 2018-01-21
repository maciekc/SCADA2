# Web application for monitoring an industrial process.
The main goal of this project was to create a web application to visualize the current state of the selected industrial process. 

The frontend part of this application has been written in Angular framework using Typescript language. The source code is in the ANGULAR/SCADA-app directory. 

HTTP server has been written in JAVA, using for this purpose the following libraries: Akka-http, Akka-actors, Jdbi and RXJava. The source code is in the SERVER directory. 

All values which are displayed in the application are logged in MySQL database. A backup of this database is available in the BAZA_DANYCH directory.
