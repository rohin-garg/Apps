# Hack Swiftly
Android app



## CovidAlert

By Rohin Garg

#### Topic: Healthcare


### What the app accomplishes:

By using crowdsourcing, this mobile-based full-stack application can be used to efficiently and proactively alert users if they have been exposed to someone who tested positive for Covid-19. 
When any user tests positive for covid-19, the app determines which of its users have been in the same location as the infected user in the last 14 days prior to showing symptoms. This would help identify people who may have been infected, but are not showing symptoms yet, expediting the testing process and flattening the curve.

To achieve this, the consent-driven app maintains a database of the locations of all its users. For any user with covid-19 diagnosis, it determines all the locations of that user in the last 14 days. Then it determines which other users had been in those same locations at the same time as the diagnosed user, and notifies them that they may have been exposed, so that they could get themselves tested as well as take proper precautions. 



### Application Features

Logs your location wherever you go
Allows you to use contact tracing to prevent coronavirus before it happens
Protects privacy by only detecting registered users’ locations
Connection to a SQL Database to seamlessly and securely manage all users’ data, through a central server
Map integration



### Technologies Used
1. Android
2. Location data
3. SQL Database
4. Flask/Python Server


![Technology Diagram](https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/CovidAlertDiagram.png)



### Installation:
1. An android phone OR
2. Android Studio with a virtual device installed

### Usage:
1. Run the server (Click [here](https://flask.palletsprojects.com/en/1.1.x/installation/) and [here](https://flask.palletsprojects.com/en/1.1.x/quickstart/) for more information)
   - Create a virtual environment on your laptop
   - Run “pip install _______” in your command line for every module listed below
     - sqlite3
     - flask
   - Run the server
   - Make sure the .db files and the python file are located in the same folder
2. Run the app
   - Open Android Studio and open the folder with this submission in the app
   - Click [here](https://developer.android.com/training/basics/firstapp/running-app) for more information on how to run the app
   - Browse the app by creating a new user, and navigating using the onscreen buttons
   
<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200417-232220.png" height="300" width="150">

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200417-232259.png" height="300" width="150">

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200417-232319.png" height="300" width="150">

<img src="https://github.com/rohin-garg/Apps/blob/master/CoronavirusMapApp/Screenshot_20200417-232411.png" height="300" width="150">
