# Countries
Hng9 Stage3 task. A responsive app consuming [Countries API](https://restcountries.com/v3/all) to display list of countries with their details.

# Features
The application:
- Displays a list of countries
- Show details of the selected country
- Support Dark and light modes
# Setup App Development Environment and how to run

   1. Download and install Android Studio if not installed
   2. Download / clone this repository to a folder on your computer
   3. Start Android Studio, open your source code folder and verify that Gradle build is going to be successful using Build.
   4. If the build is successful, you can run the app by doing the following: click Run -> Run 'app'.
   5. Connect your phone or create a new virtual device following on screen instruction
   6. Enjoy the experience🎉

# Tech stack
Countries app is build using:
- [Kotlin](https://developer.android.com/kotlin) -modern statically typed programming language used to develop android applications. 
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android’s recommended, modern toolkit for building native UI. It simplifies and accelerates UI development on Android.

# Libraries in use
- [Jetpack](https://developer.android.com/jetpack)
    -   [Datastore](https://developer.android.com/topic/libraries/architecture/datastore) -  data storage solution that allows you to store key-value pairs. Used in this app to store and retrive light and dark mode values
    -   [Compose](https://developer.android.com/jetpack/compose) -  A modern declarative way to build android ui
    -   [Navigation Components](https://developer.android.com/jetpack/compose/navigation) - Android Jetpack's component for implementing navigation for consistent and predictable user experience in android app

- [Retrofit](https://square.github.io/retrofit) - Type-safe HTTP Client for consuming RESTful API web services in android
- [Coil](https://github.com/coil-kt/coil) - Kotlin-first image loading library for Android which uses Kotlin Coroutines behind the hood

# Links 
Apk Link: [https://drive.google.com/file/d/1cPUhi05xghv1annxOcaveuqoWrJxMhuH/view?usp=share_link](https://drive.google.com/file/d/1cPUhi05xghv1annxOcaveuqoWrJxMhuH/view?usp=share_link)

Appetize.io Link: [https://appetize.io/app/5nv2n7uewgtnzbsc7g6gac3wme](https://appetize.io/app/5nv2n7uewgtnzbsc7g6gac3wme)

# Development Challenges
- User Interface design - App responsive experience was a challenge. Used [Jetpack Compose](https://developer.android.com/jetpack/compose) to come up with responsive UI
# Future feature implementation
- Multilanguage support
- Filter by subregion, currency

