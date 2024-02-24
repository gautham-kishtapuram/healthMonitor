# healthMonitor

This program checks if websites are healthy. For example, when we trigger `youtube.com`, it should respond back with a success message (200 response). But if we try something like `youtube.com/&` (which is incorrect), it should show an error message (like 400 error).

The program also looks for any extra spaces in the website addresses for extra validation.

Additionally, I was trying to add a feature, where it has to tell, if a website is using a custom handling 404 responses and Since it seems a little overwhelming to read HTML responses.However, this part isn't finished yet and needs to be addressed.

## Usage 
execute `./run_healthmonitor.sh` in your terminal. This command will build a Dockerfile and start the application within a Docker container.

The application needs user inputs where we should provide domain addresses to check if they are running. 

Simply enter the domain addresses when prompted, and the application will perform the checks accordingly.
