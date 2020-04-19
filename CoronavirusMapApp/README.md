# Hack Swiftly
Android app



## CovidAlert

By Rohin Garg

#### Topic: Healthcare


### What the app accomplishes

By using crowdsourcing, this mobile-based full-stack application can be used to efficiently and proactively alert users if they have been exposed to someone who tested positive for Covid-19. 
When any user tests positive for covid-19, the app determines which of its users have been in the same location as the infected user in the last 14 days prior to showing symptoms. This would help identify people who may have been infected, but are not showing symptoms yet, expediting the testing process and flattening the curve.

To achieve this, the consent-driven app maintains a database of the locations of all its users. For any user with covid-19 diagnosis, it determines all the locations of that user in the last 14 days. Then it determines which other users had been in those same locations at the same time as the diagnosed user, and notifies them that they may have been exposed, so that they could get themselves tested as well as take proper precautions. 



### Application Overview

1. Logs your location wherever you go.
2. Allows you to use contact tracing to alert potential coronavirus exposures as early as possible.
3. Protects privacy by only detecting registered users’ locations.
4. Connection to a SQL Database to seamlessly and securely manage all users’ data, through a central server.
5. Map integration.

### Core Features

#### History

Displays users' past locations, for as many days as they choose to view.

#### Near Me

Allows users to view infections within a certain radius of their preffered location, which can be configured in settings.

#### Report Infection

Allows users to report positive test resuls for COVID-19. This is used to alert other users have been in the same location as the person who tested positive.

#### Search

Allows you to look at infections at any area. This can be used to determine if a specific location is safe to go to.

#### Circle

Allows users to build a circle with their friends and family. They can find out if any of their loved ones has tested positive or could have been exposed.

#### Resources

Provides additional information and resources about precautions and other activies that people should partake in during this outbreak.

#### Settings

Allows users to manage their personal information, including account details and preffered locations

### Technologies Used
1. Android
2. Location data
3. SQL Database
4. Flask/Python Server


![Technology Diagram](https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/CovidAlertDiagram.png)



### Installation
1. An android phone OR
2. Android Studio with a virtual device installed

### Usage
1. Run the server (Click [here](https://flask.palletsprojects.com/en/1.1.x/installation/) and [here](https://flask.palletsprojects.com/en/1.1.x/quickstart/) for more information)
   - Activate the virtual environment when navigating inside the folder containing the server (python) files
   - Run “pip install _______” in your command line for every module listed below while staying in the same directory
     - sqlite3
     - flask
   - Before running the server, make sure your firewall isn't blocking the server
   - Run the server
   - Make sure the .db files and the python file are located in the same folder
2. Run the app
   - Open Android Studio and open the folder with this submission in the app
   - Click [here](https://developer.android.com/training/basics/firstapp/running-app) for more information on how to run the app
   - Browse the app by creating a new user, and navigating using the onscreen buttons

### Screenshots of the app

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200418-191253.png" height="300" width="150"> - Sign In

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200418-191441.png" height="300" width="150"> - Sign Up

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200418-191230.png" height="300" width="150"> - Example of Alert

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200418-191206.png" height="300" width="150"> - Displaying Current Location

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200418-191117.png" height="300" width="150"> - Notifying Others of Infection Through App

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200418-191136.png" height="300" width="150"> - Entering Date of Infection
