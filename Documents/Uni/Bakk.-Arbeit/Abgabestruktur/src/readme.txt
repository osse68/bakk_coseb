Running the tests:
-Downlaod Eclipse IDE 3.7.2 or later: https://eclipse.org/downloads/
-Install the ADT (Android Development Tools) Eclipse Plugin. Follow the instructions on http://developer.android.com/sdk/installing/installing-adt.html
-Import the Projects from source folder into workspace:
	- ShinobiQuickStart needs the Projects shinobicharts-android-library and 	appcompat_v7 as libraries (Right-Click ShinobiQuickStart->Properties->Anroid->	Library->Add). If not shown in the dialog right-click the project and check is Libary 	({Project}->Properties->Anroid->Library->is Library)
-To Run a Test right-click on one of the the following projects: ShinobiQuickStart, ProcessingTest, My First Project. Select Run As->Android Application. If a android phone in usb debugging mode is detected the app runs on the phone. If no phone detected create a virtual device using the shown dialog or Window->Android Virtual Device Manager.
	-To change between afreechart, androidplot and achartengine goto 	MainActivity.java in My first Project and comment/uncommet libraries.

	