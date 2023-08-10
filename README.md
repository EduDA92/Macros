# Macros

Macros tracker app made entirely with kotlin following MVVM arch and using XML for the UI part.

The app local storage is built with Room and Proto Datastore for the user related data, and retrieves data from API with Retrofit.

The dependency injection is managed with Hilt in order to inject the required data into the repositories

The app uses Flow with Coroutines to represent the data into the UI.

The App also supports Barcode Scanning via Android ML Kit & open food facts API

# Screenshots
 ![MainScreen](/screenshots/MainScreens.png?raw=true "MainScreen")
 ![FoodCreationScreen](/screenshots/FoodCreationScreens.png?raw=true "FoodCreationScreens")


