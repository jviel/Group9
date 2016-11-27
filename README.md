# CA Management System

## CA Logins
**Manager Fibonacci**: 11235813

**Operator Prime**: 23571113

**Provider**: any from 100000001 to 100000015

## Building the Test Database
Run `DBTest.java` from the tests folder. It generates a db in your project dir called `testDB.db`. Rename that file to `database.db` and Terminal will then recognize it as the main db.  

## Building the Makefile and Run Scripts
The Python 3 script `createMakefile.py` builds `runTerminal.sh`, `runTests.sh`, and `Makefile`. The script asks you for the locations of project dependencies. Find the _absolute path_ for the JARs used by the project and enter them when prompted. Alternatively, you can create a file `locations.txt` and pipe it to the script:

```
python3 createMakefile.py < locations.txt
```

An example `locations.txt` for a Linux system looks like:

```
U
/home/mike/Downloads/sqlite-jdbc-3.15.1.jar
/usr/lib/jvm/java-7-openjdk-amd64/lib/tools.jar
/usr/share/java/junit4.jar
/usr/share/java/hamcrest-core-1.3.jar
```

The option `U` is replaced with a `W` for a Windows system, though this will likely require Cygwin or Bash for Windows to work. 

The `hamcrest-core` dependency can be downloaded [here](http://search.maven.org/#search|gav|1|g:%22org.hamcrest%22%20AND%20a:%22hamcrest-core%22). The other libraries are on your system somewhere. Your IDE may be able to help you find them.

Run `make` to compile the project.

Change the permissions as needed on `runTerminal.sh` and `runTests.sh` and they will be ready to run the Terminal or the JUnit tests respectively.

## Building a Release
Follow the instructions for building the `Makefile`, then run `make product`. This will produce a zip file with a compiled version of CA. 

To run CA, extract the zip file and run `java -jar CA.jar`.

## Adding JDBC 

Go to [here](https://bitbucket.org/xerial/sqlite-jdbc/downloads) and select a jar. The requirements document specifies Version 3.14.2.1, but later versions also work. Note that 

### Running with JDBC

If you're running from the command line, you need to include the jar in your `classpath`. If you don't feel like changing your `.bashrc` to export the path to the `classpath`, the SQLite Tester repo has a [Linux / Mac script](https://github.com/mbottini/SQLite-Tester/blob/master/run.sh) and a [Powershell script](https://github.com/mbottini/SQLite-Tester/blob/master/run.ps1). Note that both of these scripts run the 3.14.2.1 version; if you download a later version, you need to change that. You'll also need to change the `mainApp` to whatever program you are running. If you're running in something like Eclipse, you can add this to your classpath in the [Run Configuration](http://i.imgur.com/1NdjFnm.png), as shown by this screenshot. Click on Group9 and then select "Add External JAR" about midway down the right.

Someone who is using IntelliJ or another pleb ID should be able to do something similar. Please post a screenshot of your chosen snowflake IDE doing the needful.

**To set up SQLite database once you have JDBC jar above: for Intellij, go to `Project Structure -> Libraries -> hit '+' -> point it to jdbc jar'` then recompile  
   

## .gitattributes file deals with line endings  
create a file in your local repo called .gitattributes with the following:
```
text = auto  # for Windows
text = input # for macOS 

*.java  text
*.class text
*.txt   text
*.csv   text
*.ps1   text
*.sh    text
*.md    text

Makefile text
makefile text

*.docx binary
*.pdf  binary
```

##pull requests
1. Make a branch or switch to existing
2. Edit code
3. Commit locally (with meaningful message!)
4. `$ git pull origin master` (origin is name of GitHub repo)  
5. `$ git push origin <branch name>` (origin is name of GitHub repo)  
6. On github go to Pull-Requests tab and click New Pull Request
7. Set 'base' to master and 'compare' to your branch
8. Add a comment
9. Hit 'Create pull request'
10. Merge request will then be visible in Pull Request tab, you can make comments
