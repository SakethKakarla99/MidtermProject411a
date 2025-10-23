# MidtermProject411a 
Simple multi-screen Android Application built using Jetpack Compose. The goal of this project is to demonstrate understanding of state management, ViewModel usage, and screen navigation using the hardcoded data into one working app.
This app provides a simple Recipe Book interface where users can browse recipes, view details (the ingredient and steps) and mark recipes as favorites. All data is stored locally in memory using ViewModel and updates are reflected instantly in the UI using mutableStateListOf

__Core Features__

Home Screen(Recipes List)
-> Displays a list of harcoded reciples titles and a small description
-> Each recipe row includes a favorite toggle button
-> Clicking a recipe navigates to its Details Screen
-> Clicking Favorites button in the header will navigate to the Favorites Screen

(<img width="450" height="591" alt="HomeScreenApp" src="https://github.com/user-attachments/assets/66cc07f7-ab06-4049-847a-6eb257185479" />)

Details Screen
-> Shows the full recipe details.
-> Users can mark and unmark as favorite using the button
-> includes a back button to return to the previous screen

<img width="439" height="512" alt="image" src="https://github.com/user-attachments/assets/f99b56c7-3611-41f5-8cea-722b3baeb525" />

Favorites Screen
-> Displays only the recipe the user has marked as favorites
-> Users can remove the favorites directly from this screen
-> Includes a Back button to return to Home. 

<img width="441" height="551" alt="image" src="https://github.com/user-attachments/assets/9084061e-5108-4852-8d04-6f797650fb1d" />

Demo:
https://www.youtube.com/watch?v=f900xjbCWB4










