# wa-arm-spring-selector
Workshop Automation: Workshop Arm: Spring selection service

This project is Gradle driven and consists of several sub-projects:
* core - The spring selection application. This is a command line app that contains the core logic.
* ws - The RESTful web service wrapper around core

Executing `gradle tasks` in the root folder of the main project or any of the sub-projects will provide a relevant lists of tasks that can be executed against that projeoct, e.g. gradle run or gradle build.

## IDEs
Eclipse integration works best if you DON'T use the command line eclipse generation tools in Gradle. Simply check out the project from github and then use the Eclipse->Import function and import an existing Gradle project. The Eclipse project structure should be correctly generated at that point.

Please note that at the time of writing Gradle test and Eclipse debug capability are not yet integrated in the Eclipse Gradle buildship plugin though you can manually set up a remote debug target to a gradle test execution instance in Eclipse.

## References
* Jersey: https://jersey.github.io/documentation/latest/media.html#json.moxy
* HSQLDB Text Tables: http://hsqldb.org/doc/guide/texttables-chapt.html