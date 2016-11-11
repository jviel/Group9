# **do not push directly to origin/master!**  

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
