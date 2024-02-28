# Website Status Checker
The Website Status Checker is a Java program that allows users to check the status of websites by providing their URLs. It provides information about the server's response code, checks for custom error handling, and identifies server and client errors.

## Features
Check the HTTP response code for a provided website URL.
Identify if the server is handling custom error pages.
Handle different HTTP response codes including 200, 404, 400 series, and 500 series errors.
Inform users about the server's status and any errors encountered during the connection.

## Usage 
execute `./run_healthmonitor.sh` in your terminal. This command will build a Dockerfile and start the application within a Docker container.

The application needs user inputs where we should provide domain addresses to check if they are running. 

Simply enter the domain addresses when prompted, and the application will perform the checks accordingly.
