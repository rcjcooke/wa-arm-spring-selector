# wa-arm-spring-selector
Workshop Automation: Workshop Arm: Spring selection service

## Project usage
Project is Gradle driven and consists of several sub-projects:
* core - The spring selection application. This is a command line app that contains the core logic.
* ws - The RESTful web service wrapper around core

Executing `gradle tasks` in the root folder of the main project or any of the sub-projects will provide a relevant lists of tasks that can be executed against that projeoct, e.g. gradle run or gradle build.
