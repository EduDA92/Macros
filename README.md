## Macros:

- Macros tracker app made entirely with kotlin following MVVM arch and using XML for the UI part.

- The app local storage is built with Room and Proto Datastore for the user related data, and retrieves data from API with Retrofit.

- The dependency injection is managed with Hilt in order to inject the required data into the repositories

- The app uses Flow with Coroutines to represent the data into the UI.

- The App also supports Barcode Scanning via Android ML Kit & open food facts API

## Screenshots:
|  User Info Screen  |   Set Units Dialog   | Set User Data DropDown Dialog |
| :----------------------: | :----------------------: | :-----------------------: |
| ![](./screenshots/UserFragment.png) | ![](./screenshots/Units.png) | ![](./screenshots/ExerciseLevel.png)  |

|  Set User Data Dialog  |   User Food List   | User Recipe list |
| :----------------------: | :----------------------: | :-----------------------: |
| ![](./screenshots/setHeight.png) | ![](./screenshots/UserFoodList.png) | ![](./screenshots/UserRecipeList.png)  |

|     Diary Screen     |    Diary Meals Screen   |      MyFoods Screen     |
| :----------------------------: | :----------------------------: | :----------------------------: |
| ![](./screenshots/DiaryFragment.png) | ![](./screenshots/EditMealList.png) | ![](./screenshots/MyFoodListFragment.png) |

|        MyRecipeList Screen       |    Create Food Screen    |    Create Recipe 1   |
| :-----------------------: | :-----------------------: | :-----------------------: |
| ![](./screenshots/MyRecipes.png) | ![](./screenshots/CreateFoodFragment.png) | ![](./screenshots/RecipeNameDescription.png) |

|    Create Recipe 2     |    Create Recipe 3     |     Barcode Scanner       |
| :--------------------------: | :--------------------------: | :--------------------------: |
| ![](./screenshots/AddRecipeIngredients.png) | ![](./screenshots/RecipeAddIngredientFragment.png) | ![](./screenshots/BarcodeScanner.png) |



