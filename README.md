# wa-arm-spring-selector
**Workshop Automation: Workshop Arm: Spring selection service**

This repository contains of a small set of services that work together to provide an interface for selecting stock springs from a spring catalogue that will allow for the gravity balancing of a mechanical arm. This system is founded in earlier work done by Frank Beinersdorf with some minor functional expansions to allow for the spring mass to be included in the system mass and to allow for spring selections that can be used to balance variable payloads.

![General screenshot](media/Screenshot2.PNG)

This project is part of wider work to design a template for a robotic arm to assist solo inventors in a workshop context, however, this tool can be used standalone for balancing any mechanical arm. It is assumed that the mechanical arm can be modelled such that it conforms to the general mechanical model shown below. Note that for a parallogram 4-bar linkage, the connection points for the spring will work on either of the horizontal bars and their respective vertical bars, as visualised by the dotted line in the diagram.

![Generic Mechanical Model](media/model.png)

The tool reflects the mechanical structure shown using the following variables:

| Variable      | Description                                                                                          |
|---------------|------------------------------------------------------------------------------------------------------|
| R             | Distance from the pivot of the arm to the centre of mass of the arm (including the payload)          |
| R<sub>2</sub> | Distance from the pivot of the arm to the arm connection point of the spring(s)                      |
| A             | Distance from the pivot to the vertical connection point of the spring(s)                            |
| MA            | The mechanical advantage of the system, presumably provided by a pulley system                       |
| m<sub>s</sub> | System mass - this is the mass of the mechanical arm being pivoted, excluding the spring if relevant |
| m<sub>p</sub> | Payload mass - the mass of the payload                                                               |
| N             | The number of springs to be used                                                                     |

## Tool Usage

The basic usage flow is as follows:

1. Put in the values for the variables outlined above into the Scenario Setup panel on the left of the tool and press the "Find Springs" button.
2. This will populate the Spring List that occupies the bottom of the tool with all the springs that match the specified scenario. Similarly the same springs will also be populated in the chart.
3. Selecting a spring in either the list or by clicking on it in the chart will bring up the details for that spring in the pop-up panel on the right.
4. There are two tabs on the Spring Details panel. The "Static" tab shows the manufacturing details of the spring, i.e. those unaffected by the scenario the spring is being used in. More interestingly to the designer, the "Scenario" tab allows the designer to determine the connection points (A and R<sub>2</sub>) for the spring(s) in the design.

**Note**: The separation between m<sub>p</sub> and m<sub>s</sub> is only relevant for systems where a variable payload is to be supported. In this case the system mass is assumed to be constant and the payload mass is assumed to vary between zero grams and the specific payload mass.

**Note**: Please note that, as the tooltip states, the checkbox allowing for a system design where the spring is essentially also balancing it's own mass will result in a system that doesn't perfectly balance at all pivot angles. This is because the centre of mass of the spring will change change as it stretches which will therefore also affect the force that needs to be applied to balance the system. This is a non-linear behaviour that cannot be accounted for in a design that uses springs to balance. This may well be an error that you choose to accept in the design of your system however and can probably be adequately compensated for simply by intentionally introducing some friction in to the rotation of the pivot. 

### Variable Payload Design

If the arm is being designed to handle a variable payload then this needs to be taken in to account when selecting the spring(s) to balance it. For a spring-based gravity balancing mechanism there are two options for balancing a variable payload. Either the spring constant of the spring needs to change, or the connection points need to be moved based on the payload. It is impractical to physically swap the spring out during operation so it is assumed that balancing of a variable payload is achieved by moving one or both of the connection points, i.e. dynamically changing the distances A and R<sub>2</sub>.

### Understanding the spring fit

To better understand how the spring fits the mechanical scenario outlined, a chart is available on the Spring Detail -> Scenario tab. Clicking on the chart will provide an enlarged version such as the one shown in the screenshot below.

![Screenshot showing connection point chart](media/Screenshot.PNG)

This chart shows the spring's behaviour as a function of it's connection points with the rectangles showing the bounds for allowed values of A and R<sub>2</sub> and those that the spring could theoretically manage. The intersection of these rectangles, limited by the function shown, gives the range of possible values for A and R<sub>2</sub> that are possible for your system which will correspond to the slider ranges you can move on the Spring Detail -> Scenario tab. The small yellow dot represents the most optimal (meaning the smallest possible maximum spring deflection) connection point possible for the configuration required.

## Project Usage

This project is Gradle driven and consists of several sub-projects:
* **data** - Data: Spring databases and source files
* **core** - Core: The spring selection application. This is where the core logic lives and can be executed as a command line app.
* **ws** - Web Service: The RESTful web service wrapper around core
* **wa** - Wep App: A GUI web front end for interacting with the web service

Executing `gradle tasks` in the root folder of the main project or any of the sub-projects will provide a relevant lists of tasks that can be executed against that projeoct, e.g. gradle run or gradle build.

### Getting Started / Dependencies
You'll need the following installed to get this project fully up and running on your machine:

* Java 1.8+ : http://www.oracle.com/technetwork/java/javase/downloads/index.html
* Gradle 4+ : https://gradle.org/install/
* Node.js and NPM: https://nodejs.org/en/download/
* Angular CLI: To install this, execute `npm install -g @angular/cli`

The `data` project uses bash scripts and relies on `sed`, `awk` and `grep` as well as a handful of other standard bash shell lanugage commands. This therefore means that it can only be run on a Linux or Linux emulating environment such as Cygwin on Windows. To date this project has been entirely developed on a Windows machine using a MinGW 32-bit terminal (https://osdn.net/projects/mingw/releases/) and therefore it has been proven to work correctly in that environment. While it should theoretically work fine in a native Linux build this hasn't been tested. The `data` sub-project is NOT Windows native compatible at this time.

Once you've got the dependencies above installed, the following steps should get the web app up and running:

1. Change to the `data` sub-directory and run `gradle publish`. This will compile the datasets together to form the spring database and then publish that to a locally created Maven repository for the other projects to access. This is used by the Core application if it's run independently and the Web Service.
2. Change to the `core` sub-directory and run `gradle publish`. This will compile the core libraries and publish them to a local Maven repository for the Web Service to access.
3. Change to the `ws` sub-directory and run `gradle run`. This will compile and execute the web service which will start an embedded web server for the web app to connect to. You'll need to leave this running while you use the web app.
4. In a new terminal, change to the `wa` sub-directory and run `npm i` to download and install all javascript dependencies. This will take a while (and not say much on the command link so will look like it's not doing anything!) but you only ever have to do it once. Once that's complete run `ng serve --open`. This will start the Web App in a local Node.js instance and then open a web browser page pointing at it.

That's it! You should have a fully functional app running in your browser at this point.

**Note:** While most of the projects use Gradle for dependency management, the Web App project (wa sub-folder) uses NPM via Angular to manage dependencies. This will result in the creation of a local `node_modules` folder. Please see the Web App section below for details on it's dependencies.

### Core
This is the core logic and database executable as a command line application. It is also used as an integrated dependency of the web service. It is dependent on the data project and will compile and this include this as required. To run the application:

* Change to the `core` directory
* Execute `gradle run --args=''`. This will force the app to tell you what command line parameters are required to use it.

**Note**: Running `gradle run` with no arguments will execute the app with some default command line arguments. These arguments can be found in the `gradle.build` file under the core sub-project directory.

Core does not need to be run as a stand-alone application to be able to use the Web App.

### Web Service
This is the RESTful web service that allows for remote connection and use of the Core app and database.

* Change to the `ws` directory
* Execute `gradle run`. This will start up the web service at http://localhost:8080/.

The Web Service must be running for the Web App to work.

### Web App
This is the graphical front end interface to the web service.

At present this sub-project is not integrated into the gradle build.

Note: To run the web app (wa sub-project) you will need the following pre-requisites installed:
* Node.js and NPM: https://nodejs.org/en/download/
* Angular CLI: To install this, execute `npm install -g @angular/cli`

Make sure the web service is already running on the local machine as this app will connect to it.

* Change to the `wa` directory
* Run `npm i` to download all javascript dependencies. This will take a while the first time and you don't need to do it again.
* Run `ng serve --open`. This will start the web app running on a local Node web server instance and open the web app in your system's default web browser.

### IDEs
#### Eclipse
Despite the Gradle Eclipse plugin being applied in the build file, Eclipse integration works best if you *DON'T* use the command line eclipse generation tools in Gradle. Simply check out the project from github and then use the Eclipse->File->Import... function and import an existing Gradle project. The Eclipse project structure should be correctly generated at that point.

Please note that, at the time of writing, Gradle test and Eclipse debug capability are not yet integrated in the Eclipse Gradle buildship plugin, though you can manually set up a remote debug target to a gradle test execution instance in Eclipse.

#### Visual Studio Code
VS Code was used for the creation and modification of the Web App. Simply opening the project folder in VS Code should be sufficient for VS Code to function. I use the following extensions for this project which youmaywish to consider:
* Angular 6 Snippets
* Markdown All In One
* Java Extension Pack

## Spring Manufacturers
A web search of "local" New Zealand spring manufacturers was undertaken and those that had easier to parse stock catalogues have their springs listed in this app. In addition to these, Gutekunst and Knoerzer had previously been compiled by Frank Beinersdorf and these are also included in the Spring database.

Legend: R = Researched, L = Listed

| Company | URL | Status |
|---------|-----|--------|
| Century Springs | http://centurysprings.co.nz/ | L |
| Bearing & Engineering Supplies Ltd. | https://www.bearingandengineering.co.nz/ | R |
| National Springs & Wire Products NZ Ltd. | http://www.natspring.co.nz/ | R |
| Spring Specialists Limited | http://www.springspecialists.co.nz/ | R |
| Gutekunst Federn | https://www.federnshop.com/en/ | L |
| Federntechnik Knoenzer | https://www.knoerzer.eu/ | L |
| UK Spring Supplies | http://ukspringsupplies.co.uk/ | R |
| Extex Stock Springs | http://www.entexstocksprings.co.uk/index.php?route=common/home | R |
| Flexo Springs | http://www.flexosprings.com/stock-springs/extension-springs | R |
| Lee Springs | https://www.leespring.com/uk_index.asp | R |
| ABSSAC | https://www.abssac.co.uk/ | R |

### Century Springs
The spring data was copied from their PDF catalogues available online. Once scraped, it became evident that the catalogues had a small number of data errors in addition to a significant number of problems being introduced as a result of scraping the data from PDF in the first place. The original scraped source pages can be found in the `data/centurysprings` directory. 

Cleanup scripts were created to process the raw pages and generate a single CSV output. This process can be re-executed by running the `clean.sh` script (`-h` as an argument to get usage instructions). Please note that this requires `sed` and `awk` to be available on the command line. This was tested and proven to work on a MinGW terminal on Windows. No other environment has been verified at this stage. This process has now been integrated in to Gradle so using standard Gradle tasks will execute this for you.

### Gutekunst Federn
The spring data was collated from their catalogue by Frank Beinersdorf and the final curated CSV can be found in the `data/gutekunst` project.

### Knoerzer
Spring data from Knoerzer was already included in the curated spring data collected by Frank Beinersdorf and can therefore be found in the `data/gutekunst` project.

## References
* Gradle multi-project builds: https://docs.gradle.org/current/userguide/multi_project_builds.html
* Jersey JSON / Moxy: https://jersey.github.io/documentation/latest/media.html#json.moxy
* HSQLDB Text Tables: http://hsqldb.org/doc/guide/texttables-chapt.html
* D3.JS / C3.JS / REST Example: https://github.com/Automattic/grasshopper
* Angular tutorial: https://angular.io/tutorial
* Angular event distribution: https://angularfirebase.com/lessons/sharing-data-between-angular-components-four-methods/
* Angular + C3.js: https://stackoverflow.com/questions/46250941/how-add-c3-charts-to-angular-2-project
* Angular observables: https://angular.io/guide/observables
* Angular material icons: https://material.io/tools/icons/?style=baseline
* Angular material theming: https://medium.com/@tomastrajan/the-complete-guide-to-angular-material-themes-4d165a9d24d1
* D3.js colour functions: https://github.com/d3/d3-scale-chromatic/blob/master/README.md#interpolateWarm
* D3.js curve explorer: http://bl.ocks.org/d3indepth/b6d4845973089bc1012dec1674d3aff8
* D3.js V5 line chart Example: https://bl.ocks.org/gordlea/27370d1eea8464b04538e6d8ced39e89
* Angular Progress spinner example: https://stackoverflow.com/questions/45512285/angular-httpclient-show-spinner-progress-indicator-while-waiting-for-service-t