parser
======

Translator for yail files to java.

For instruction on how to update the parser please visit https://sites.google.com/site/usfinnovate/app-inventor/codegeneration

Once the parser has been updated, you will need to create a runnable jar, with appinventor project as the main. And call it yail-to-java. And place it in the appinventor/libs/yava/ folder. Then it's just a matter of updating the buildserver. 

See appinventor readme for full details including how to update buildserver at https://github.com/appinventortojava/appinventor

Mostly used files:
parser.jj (java CC parser)
AppInventorProject.java (contains the main of the parser)  

NOTE: If you would like to contribute to this project please email David Wolber at wolberd@usfca.edu 
