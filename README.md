# WeatherApp
Gives the current, daily and hourly weather report for current location or selected location. 

This app displays the current weather details, daily weather for 8 days and hourly weather for 48 hours. It requires location permission which has to be enabled by the user when a prompt occurs.

It uses Location manager to get the current location and Geocoder to get the location details of the selected city.

The weather deatils are fetched using Retrofit from weather API. This app is developed using fragments and bottom navigation component to display current, hourly and daily details.

This app uses Model-View-ViewModel(MVVM) design patterns.

If there is no network connection then the previous search data is shown to the user. The data is stored in database using ROOM Jetpack architecture pattern.
