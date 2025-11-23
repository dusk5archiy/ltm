CATALINA_HOME="/c/Program Files/Apache Software Foundation/Tomcat 11.0"
mvn clean package
rm "$CATALINA_HOME/webapps/ROOT.war"
rm -r "$CATALINA_HOME/webapps/ROOT"
cp target/app.war "$CATALINA_HOME/webapps/ROOT.war"
start http://localhost:8080
