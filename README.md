# wa-arm-spring-selector
Workshop Automation: Workshop Arm: Spring selection service

This project is Gradle driven and consists of several sub-projects:
* core - Core: The spring selection application. This is a command line app that contains the core logic.
* ws - Web Service: The RESTful web service wrapper around core
* wa - Wep App: A GUI web front end for interacting with the web service
 
Executing `gradle tasks` in the root folder of the main project or any of the sub-projects will provide a relevant lists of tasks that can be executed against that projeoct, e.g. gradle run or gradle build.

## IDEs
### Eclipse
Despite the Gradle Eclipse plugin being applied in the build file, Eclipse integration works best if you *DON'T* use the command line eclipse generation tools in Gradle. Simply check out the project from github and then use the Eclipse->File->Import... function and import an existing Gradle project. The Eclipse project structure should be correctly generated at that point.

Please note that, at the time of writing, Gradle test and Eclipse debug capability are not yet integrated in the Eclipse Gradle buildship plugin, though you can manually set up a remote debug target to a gradle test execution instance in Eclipse.

## References
* Jersey: https://jersey.github.io/documentation/latest/media.html#json.moxy
* HSQLDB Text Tables: http://hsqldb.org/doc/guide/texttables-chapt.html
* D3.JS / C3.JS / REST Example: https://github.com/Automattic/grasshopper
* Using Gradle with Javascript: https://dzone.com/articles/javascript-webapps-gradle
* Gradle + WebPack: https://objectpartners.com/2016/04/22/using-webpack-with-gradle/
* Gradle Javascript dependency resolution: https://docs.gradle.org/current/userguide/declaring_dependencies.html
* Angular + D3.js: https://jaxenter.com/d3-js-busy-angular-developer-146873.html
* Angular + D3.js: https://medium.com/@balramchavan/integrating-d3js-with-angular-5-0-848ed45a8e19
* Angular tutorial: https://angular.io/tutorial