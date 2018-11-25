# Planets
This applications get data from https://swapi.co . Show a list of planets and detail information when you select one planet.
The app has a search ,  user can search in the api different planets and also pagination when scroll down the 
recyclerview.

The architecture is separate by folders, I added Logic, Models, ServerConnection and Views. 

In the Logic folder we can find a Singelton class that factory the data from server and convert to client model , and also
keep the data in memory , to have access always .

In Models I have the models of data of the app, you will find two very similar… one to parse the Json from server and other 
to print data on the views.

In ServerConnection I only have one Interface class using Retrofit where are the calls of the app (Three ..get planets , get 
more planets with pagination and search)

And in Views I have some Views helpers to do pagination of recyclerview.

The project is generated by Android Studio (Master Detail) , so the activities and the fragment is created by default by 
Android Studio.

In the class PlanetItemListActiviy you will see all the invocations and some logics...I think the code is clear to understand 
and can be read easily. 

There is not control of erros if there are timeouts on server... I only added a function to check if user have network.

There is nothing about test ... I cannot implement it.

If you have any doubt, contact me.