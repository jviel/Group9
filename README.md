# **do not push directly to origin/master!**  

## Test Database instructions
Run `DBTest.java` from the tests folder. It generates a db in your project dir called `testDB.db`. Rename that file to `database.db` and Terminal will then recognize it as the main db.  

# Adding JDBC

Go to [here](https://bitbucket.org/xerial/sqlite-jdbc/downloads) and select a jar. The requirements document specifies Version 3.14.2.1, but later versions also work.

## Running with JDBC

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

#pull requests  
1. make a branch or switch to existing
2. edit code
3. commit locally (with meaningful message!)
4. `$ git push origin <branch name>` (origin is name of GitHub repo)  
5. on github go to Pull-Requests tab and click New Pull Request
6. set 'base' to master and 'compare' to your branch
7. add a comment
8. hit 'Create pull request'
9. Merge request will then be visible in Pull Request tab, you can make comments
