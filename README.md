# Movie Explorer

### Used Technologies

- Built in Java, with some libraries used to handle specific tasks:
- Glide (com.github.bumptech.glide)
Glide allows us to set image for ImageViews from internet

- Material (com.google.android.material)
Googles material library contains new components such as view pagers
- Android Jetpack Navigation
Android Jetpack Navigation allows us to define relationships between activities and fragments. Navigation UI is using for navigating between activities and fragments automatically. I used Navigation ui for navigating between fragments

- Grid Layout
Grid Layout is using to place components in grid view

### Project Structure
Project files are collected under directories called java and res directories.

java directory contains the source code for the project.
res directory contains defines about views, values, styles etc.

#### Activities
Project consists of three activities. Each activity is responsible for different tasks.
- Main Activity: responsible for listing movies. It has two fragments.
- Movie Details Activity: Displays movie details (rating, genre, actors, runtime, poster)
- Share Activity: Displays a popup to share the movie (poster and title) to other people

#### Adapters
Adapters are used to define what contents will show on recycler views and view pagers

#### Async Tasks
Async tasks - tasks that should run on background as asynchronous
- MovieFetcher is one of them. It is responsible for fetching movie data from API.

#### Fragments
Fragments are other little activities inside of activities. Each fragment is also responsible for different things. 
Here in this project, we have two fragments for two jobs:
- Explore Fragment: Main page for movie listing. All movies are listed here
- Favorites: Lists the movies that user liked

#### Models

Models represent the data that we fetch from the API. Here are two types of data:
##### Movie:
- movie id
- movie name
- movie description
- movie poster uri
- categories 
- imdb rate
- duration in minutes
- popularity
- release date
- casts

##### Cast
- name
- surname
- photo uri

#### Repositories

Repositories are part of an abstraction layer of data fetching. All data is fetched from repositories. I defined one abstract repository, and two implementations of it.

#### Res directory

Res directory, also known as resource directory stores the definition of how layout should be, what images we have, the theme, what strings twe have and so on.

- color: Color directory contains the custom color definitions
- drawable: Drawable directory contains complex backgrounds, image assets and vector assets
- font: Font directory contains custom font definitions
- layout: Layout directory contains layouts. Layouts are template views that we see on our application.
- menu: Menu directory contains our menu configurations. Menus are part of navigations. Bottom part of application decides which navigation components to show from here.
- mipmap: Also contains icons and images for project assets
- navigation: Android jetpack navigation provides us new resource directory called navigation. Here, we can define our relationships between activities and fragments. So that navigation ui can automatically handle navigation actions
- values: Values directory contains various definitions like colors, strings that used in application, styles, themes and etc.
