<h1 align="center">CV APPLICATION AUTOMATION TOOL</h1>

<h1>Demo</h1>

[![Watch the video](https://img.youtube.com/vi/EEEKk7FEWAo/hqdefault.jpg)](https://youtu.be/EEEKk7FEWAo)

<h3>About the tool:</h3>

This tool is made specifically for the portal **DEV.BG**, a job listings marketplace tailored to IT specialists in Bulgaria.

<h3>Setup before first use</h3>

Configurations before compiling:

1. For editing the frequency which the application checks for new jobs, look at " src/main/java/com/dimitrovsolutions/EntryPoint.java ". Default : 1 hour (if you do this step after compiling binaries you'll need to recompile if using jars)

Compiling sources.

1. Install latest Maven - set Home and Path in evrioment variables
2. Install JDK v16 or above - set Home and Path in enviroment variables
3. Go to root folder where pom is located "[Drive]:\dev-bg-application-bot" and open Terminal
4. mvn compile
5. mvn install

For compiled binaries or running sources in IDE pre-requisites

1. Registrer account at [dev.bg](https://dev.bg/).
2. Uploade a CV in files section [https://dev.bg/my-files](https://dev.bg/my-files/) which the tool will use to send via future applications.
3. Run the application once so a directory and cookie.txt get created, close the application after a few seconds and restart again.
      Fill cookie.txt with the following three rolls
    1. Cookie type
    2. Cookie value
    3.  path, usually just "/"

