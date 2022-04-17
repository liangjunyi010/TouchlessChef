# Touchless Chef

>[50.001](https://istd.sutd.edu.sg/undergraduate/courses/50001-information-systems-programming) 1D Project
>
>A POC Recipe Android Application using Google Mediapipe Hands Tracking model for smart touchless scrolling.
>
>Deliverables:
>- Download and install our `.apk` [***HERE***](https://drive.google.com/file/d/13otJ7cr-M_WNgoN18USStgj5GLp90yvS/view?usp=sharing) to try it out
>- [Slides](https://drive.google.com/file/d/1CY5GiMf-hH6lPLB7P39XUdJx_tyvs2Lg/view)
>- [Poster](https://drive.google.com/file/d/1Y4r8ZRK22YiWPUSEJxjkPF3D8pMjsIxF/view?usp=sharing)
>- [Video](https://drive.google.com/file/d/1eMMJQq2ctQO_Nt-fLtD9gfOnbFEruVBF/view?usp=sharing)

## A. Acknowledgement:
- Tran Nguyen Bao Long [@TNBL](https://github.com/TNBL265): Project Lead
    - define software architecture
    - documentation
- Li Xueqing [@cnmnrll](https://github.com/cnmnrll): Project Design
    - set basic `Recipe`, `Ingredients` and `Instructions` models [`model`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/model)
    - documentation
- Melodie Chew En Qi [@melmelchew](https://github.com/melmelchew): Marketing + Frontend
    - `RecyclerView` and `NavigationView`
    - [`adapter`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/adapter/recipe) for `RecyclerView`
- Melvin Lim Boon Han [@melvinlimbh](https://github.com/melvinlimbh): Marketing + Frontend
    - Fragments for different cuisines [`home`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/fragment/home)
    - Fragments for creating recipes [RecipeCreate](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/fragment/recipe)
    - all resources and layouts [`res`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/res)
- Han Jin [@dlmszhhh](https://github.com/dlmszhhh): Backend Acitivity
    - basic recipe activities [`acitivity`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/activity)
- Wang Zhouran [@wzrwzr23](https://github.com/wzrwzr23): Backend Database
    - setup `SQLite` database and corresponding Data Access Objects [`dao`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/dao) and [`adapter`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/adapter/DatabaseAdapter.java)
- Liang Junyi [@LiangJunyi-010](https://github.com/LiangJunyi-010) Backend HandTracking
    - implement HandTracking using Google Mediapipe [**Hands model**](https://google.github.io/mediapipe/solutions/hands.html) for touchless scrolling in [`ViewRecipeActivity`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/activity/recipe/ViewRecipeActivity.java)

## B. Description
>Overview of Design Pattern used:
>- **Adapter Design Pattern** (for SQLite database and RecyclerView)
>- **Singleton Design Pattern** (for SQLite database)
>- **Observer Design Pattern** (for `CreateRecipeActivity`)
>- **Factory Design Pattern** (for Cuisine Fragment)
>- **Template Method Design Pattern** (for Create Recipe Fragment)

### 0. [Model](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/model)
- Utilize [`Parcelable`](https://developer.android.com/reference/android/os/Parcelable) interface:
    - speed up data transfer using Java Serializable
    - easily pass data as `Bundle` object through Activity
- `Recipe`:
    - `id`: primary key for SQLite
    - `name`: name of recipe
    - `description`: short description of recipe
    - `category`: category/cuisine (Vietnamese/Chinese)
    - `mealType`: type of meals (Breakfast, Dinner,...)
    - `time`: estimated to prepare the dish
    - `imagePath`: local URI path to Recipe image
    - `ingredients`: list of ingredients
    - `instructions`: list of instructions
- `Instruction`:
    - `id`: primary key for SQLite
    - `recipeID`: foreign key
    - `content`: instruction body
- `Ingredient`
    - `id`: primary key for SQLite
    - `recipeID`: foreign key
    - `name`: ingredient name
      ![](https://i.imgur.com/YOL7Ouj.png)

### 1. Database
- We use local database SQLite and utilize abstract class [`SQLiteOpenHelper`](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper) to create and manage our database in [`SQLiteDbCRUD`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/dao/SQLiteDbCRUD.java)
<p float="left">
  <img src="https://i.imgur.com/Uo2dQiF.png" alt="drawing" width="310" height=""/>
  <img src="https://i.imgur.com/uvgY26Y.png" alt="drawing" width="300" height=""/></p>

- Adapter in [`DatabaseAdapter`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/adapter/DatabaseAdapter.java)
    - using **Adapter Design Pattern** as main API for Recipe CRUD operations
    - using **Singleton Design Pattern** to create a single instance of our database to be shared within all activities
- Data Access Objects ([DAO](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/dao)):
    - provide an API for our app to set/get data from SQLite database
    - map SQLite `Config` with corresponding model attributes
    - `Config` is a nested class improving encapsulation since it is only used within `dao` package for CRUD operations
      ![](https://i.imgur.com/MLwsWfS.png)


### 2. Activity
- Both `MainActivity` and `ViewRecipeActivity` inherits a `Toolbar` (explain in section B5a) from abstract class [`MenuActivity`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/activity/MenuActivity.java)
- [`MainActivity`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/activity/home/MainActivity.java)
    - implements interface `BaseFragment.FragmentListener` that define blueprint for methods to:
        - display recipe: `onShowRecipe` which navigates to `ViewRecipeActivity` (an **Explicit Intent**)
        - delete recipe: `onDeleteRecipe`
    - consists of 3 main views:
        - `ImageView` show background image for selected Cuisines
        - `DrawerLayout` for `NavigationView` (explain in section B5b)
        - `NestedScrollView` for displaying of recipes for different Cuisine Fragments (explain in section B4a)
          ![](https://i.imgur.com/gntdG6b.png)
- [`ViewRecipeActivity`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/activity/recipe/ViewRecipeActivity.java):
    - display chosen recipe from the data passed as parcel from `MainActivity`
    - where **HandsTracking** feature (explain in section B3) is located
- [`CreateRecipeActivity`](https://github.com/TNBL265/TouchlessChef/blob/main/hands/src/main/java/app/touchlessChef/activity/recipe/CreateRecipeActivity.java)
    - implements 3 Recipe Create Fragment listener, each corresponding to 1 step in creating the recipe (explain in section B4b)
    - using **Observer Design Pattern**, changing Fragment when reaching subsequent step in creating Recipe
      ![](https://i.imgur.com/JQQMFrf.png)
### 3. Hand Tracking
- This feature is implemented in `ViewRecipeActivity`
    - 2 main Buttons `Start Tracking` and `Stop Tracking` to enable/disable the feature, which open up a `glSurfaceView` in the `RelativeLayout at the bottom of the page`
    - `glSurfaceView` is an instance of `SolutionGlSurfaceView<HandsResult>` provided by Mediapipe Library, similar to [OpenGL](https://en.wikipedia.org/wiki/Java_OpenGL) that allows writing words and drawing on webcam output.
    - we detect whether the index finger is pointing up or down and from there, post a `Runnable` to our `ScrollView` (that is displaying the Ingredients and Instructions) to perform a smooth scroll correspoding to the gesture.
      <img src="https://i.imgur.com/QHqElik.png" alt="drawing" width="300" height=""/>

### 4. Fragment
#### a. Cuisine Fragment under [`fragment/home`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/fragment/home)
![](https://i.imgur.com/gf6GY9z.png)
- Both `ChineseFragment` and `VietnameseFragment` extends from abstract class `BaseFragment`
  - using **Factory Design Pattern** (`BaseFragment` is the factory, `ChineseFragment` and `VietnameseFragment` are the products) allow us to easily scale to multiple cuisines in the future
  - the only abstract method is `getFragmentLayout` that each product should define to generate its own correct fragment
  - both using `RecyclerView` to display its recipes (explain in section B5c)

#### b. Recipe Create Fragment under [`fragment/recipe`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/fragment/recipe)
![](https://i.imgur.com/f075fmk.png)
- 3 fragments correspond to 3 steps in creating a new Recipe:
    - `RecipeCreateImageFragment`: (1st step) to add image, description, choose meal type and time
    - `RecipeCreateIngredientFragment`: (2nd step) to add ingredients
    - `RecipeCreateInstructionFragment`: (3rd step) to add instructions
<p float="left">
  <img src="https://i.imgur.com/0KrLSpd.png" alt="drawing" width="200" height=""/>
  <img src="https://i.imgur.com/lMJkrus.png" alt="drawing" width="200" height=""/>
  <img src="https://i.imgur.com/CzsnCpM.png" alt="drawing" width="200" height=""/></p>

- All 3 Create Recipe Fragments extends the abstract class `NavigableFragment`:
    - blueprint for method `onNext` that define which fragment to navigate to after completing each creating new recipe step
    - using **Template Method Design Pattern** with `NavigableFragment` as the template
- `RecipeCreateImageFragment` can access the local storage to upload new image for recipe (an **Implicit Intent**)

### 5. View and Layout
#### a. Toolbar
- Both `MainActivity` and `ViewRecipeActivity` needs to define its own Toolbar according to the blueprint defined in the abstract class `MenuActivity`
- `MainActivity` Toolbar:
    - includes `NavigationView` (explain in section B5b below)
    - includes menu to add a "New recipe"
- `ViewRecipeActivity` Toolbar:
    - support **Edit** and **Delete** recipe functionalities
<p float="left">
  <img src="https://i.imgur.com/78fN0JK.png" alt="drawing" width="300" height=""/>
  <img src="https://i.imgur.com/XCXGNQS.png" alt="drawing" width="300" height=""/></p>

#### b. Navigation View and DrawerLayout
- make use of a `switch` case to switch to a different cuisine inside `MainActivity`
- once a new cuisine is selected, the corresponding Fragment (`ChineseFragment` or `VietnameseFragment`) will be added into `NestedScrollView` to show recipe `CardView`
<p float="left">
  <img src="https://i.imgur.com/H9uYXXW.png" alt="drawing" width="300" height=""/>
  <img src="https://i.imgur.com/3xTjW44.png" alt="drawing" width="300" height=""/></p>

#### c. RecyclerView
- We user `RecyclerView` at 2 places:
    - to display recipes in the Cuisine Fragment (`BaseFragment`)
    - to display the added ingredients and instructions when creating new recipe in Recipe Create Fragment
- To implement `RecyclerView` we need to implement the corresponding adapter as shown in [`adapter/recipe`](https://github.com/TNBL265/TouchlessChef/tree/main/hands/src/main/java/app/touchlessChef/adapter/recipe) (**Adapter Design Pattern**)
    - the 3 main functions in each adapter to override are:
        - `onCreateViewHolder` that inflate a new view in the parent layout to allow for display of listed items
        - `onBindViewHolder` that binds the data to be displayed in as listed items
        - `getItemCount` that return the size of all the items to be displayed
    - in the corresponding Fragment, before we set the above adapter, we also need to set a `LinearLayoutManager` for the `RecyclerView` to reside in
    - code examples:
```java=
private RecyclerView instructionRecyclerView;
instructionRecyclerView.setHasFixedSize(true);
instructionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
instructionRecyclerView.setAdapter(instructionAdapter);
```

## C. Appendix
### 0. Reference:
- We made use of a some very useful Youtube tutorials + Github repos to build a normal Cooking Recipe app before adding modifications and our hand tracking feature:
    - [Coding with Evan](https://www.youtube.com/watch?v=6-891CSz6v0)
    - [Muhammad Shahid Niazi](https://www.youtube.com/watch?v=Pe5dlibIMG8&list=PLPIUB9tHYMdvnKejGY-ggulrkgj6cBb4S)
    - [aza0092/Cooking-Recipe-Android-App](https://github.com/aza0092/Cooking-Recipe-Android-App)

### 1. User Story Development
- BDD: Behaviour-Driven Design
  ![](https://i.imgur.com/LXZ1ZLb.png)
  ![](https://i.imgur.com/LaUQ5xZ.png)
- SMART User Stories: **Specific, Measurable, Achievable, Relevant, and Time- boxed**
  ![](https://i.imgur.com/FD7Mssf.jpg)
  ![](https://i.imgur.com/9fyqIFf.jpg)
  ![](https://i.imgur.com/modevqd.png)

