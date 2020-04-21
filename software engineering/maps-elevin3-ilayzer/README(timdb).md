## 

#### Known Bugs

There are no known bugs.

#### Design details

Our within the timdb packages is organized into the following subpackages:
###actor
- Actor.java — is an implementation of the Vertex<Edge> interface (described below) and stores things like an actor’s id, name, edges, and films it’s been in.
- ActorEdge.java — is an implementation of Edge<Vertex> interface (described below) and contains an edge’s source, destination, edge weight, and movie id.
- ActorGraph.java — is an implementation of the Graph<Vertex, Edge> interface (described below) and contains access to the Database class (described below) and allows us to make calls to the Database methods so we can populate the edges of the Graph’s vertices.
- Film.java — is an implementation of the Personal interface (described below) and is used to store information about films such as ID, name, and the set of actors in the film. It is mostly used for when we are reconstructing the path and want to create hyperlinks efficiently in our GUI.
commands
- Hollywood.java contains access to the ActorGraph, which contains access to the Database class (our proxy) and contains two implementations of the ICommand interface
Connect
Executes the connect command. Does the relevant parsing for actors entered in, error handling, and uses an instance of Dijkstra (see below) to find the shortest path between the two actors entered in. Generates informative error messages when the input is malformed in any way
MDB
Executes the mdb command. Does relevant parsing and calls the db graph’s database’s connectDb() method in order to establish a connection with the user specified database. Generates informative error messages when the database is either not found or malformed.
graph
- Dijkstra.java — implements the GraphAlgorithm interface and contains the eponymous implementation of getting the shortest path between two vertices.
- Edge.java — an interface that specifies the contract for any class that wants to implement an edge, including methods like getSource(), getDestination() and getWeight()
- Graph.java — an interface that the methods any implementation of a Graph needs to implement, specifically getEdges(Vertex) and getSize().
- GraphAlgorithm.java — an interface that specifies any graph algorithm that implements it needs to implement a getShortestPath(Vertex1, Vertex2) method.
- Vertex.java — an interface that specifies the methods any implementation of a vertex needs to have, including getId(), getEdges() and getEdge(Vertex target)
handlers
Contains three handler classes for the relevant endpoints our GUI can reach.
###userIO
- ErrorMessages.java — a utility class that produces informative error messages for our program
- Prompt.java — a utility class that produces a message upon successful connection to the database
###Database.java
- Is a proxy to the database, separating the connection from other classes to the database itself. Contains our cache and other methods that query the database for information as our dijkstra implementation is running. 
TimdbMain.java
Sets up the commands hashmap so that the REPL can run the relevant commands for our system
#### Runtime/Space optimizations

Our main optimization, which is not beyond the minimum requirements, is the caching of Nodes, with each node storing a set of edges to other nodes. This will help us on any queries after the first query because we will have lots of nodes and their edges already cached.

#### Building and running the project

To build the project, use the command “mvn package”.
To run the project, enter “./run” or to host a spark server to run the gui, enter “./run --gui”.
Once in the REPL, to connect to a database, enter “mdb <path/to/database>”.
To get the path between two actors, enter “connect “Actor 1 Name” “Actor 2 Name”.
The GUI is pretty self-explanatory. You can enter a connection query in the form and the shortest path will be displayed. You can click any of the actor or film links to get to that objects personal page.
Note: You must connect the database with “mdb <path/to/database>” in the command line REPL before you use the GUI!

#### Running tests

You can run the tests with “mvn test” or “mvn build”. To conform with our system and unit tests,
all data files must exist in the data/timdb/ folder. 

#### Tests

We created a suite of system and unit tests, covering both 
correct and incorrect inputs. We also created a new SimpleGraphClass in order to test our
dijkstra's on small, manageable graphs.
We excluded any malformed databases from this repo
that we used for testing (except for improperFormat.db) due to Github's space constraints and in respect of the general principle of not committing
large data files to a remote repo. Specifically, we created an empty database and attempted to load it in, generating the
error "Db specified at 'file' was not formatted correctly. Try again."


#### Design Questions

How could you modify your project, so other developers can easily add new graph search algorithms without having to worry about other constraints of the project (e.g. structure of the database, first initial -> last initial)?

We are pretty close to this already, though I think we still have to change some things. Currently, we have a GraphAlgorithm interface which Dijkstra implements, so it would be a matter of augmenting the code in our Hollywood class so that it calls the shortestPath() method (which is contained in the interface) of some object that implements the GraphAlgorithm interface rather than simply calling the shortestPath() method of a Dijkstra object.

How could you improve your code to make it able to accept multiple types of files? For example, what if you wanted your program to be able to accept both a SQL database or a number of CSV files containing the same data?

We would have an interface (DataLayer or something) that any class that interfaces with the data layer would implement. For our purposes, this would include methods like getEdges(Actor), getActor(id), getFilm(id), or any other method that is actually getting and returning data. In any class that currently calls these methods from the Database class, we would instead have call the methods from a DataLayer object (an object that implements the DataLayer interface). In this way, it doesn’t matter to the rest of our code how the data layer is formatted.

What would you need to change if movies now had an associated year of release and the chain of movies had to go in chronological order?

In Dijkstra’s, we can maintain a map “yearMap” that is basically the same as the parentsMap (or find some way to integrate it into the same map) but the new map would map a vertex to the year of the movie that got us to that vertex. So if we traveled along an edge E to vertex A and the film of Edge E was released in 1965, we would put in our map (key: A, value: 1965). Then, when we are looping through the edges in Dijkstra’s (the main for loop in getShortestPath()), we can ignore edge’s whose movie’s year of release is chronologically before the year stored in yearMap for the current node. So if we are looping through the neighbors of vertex A, we can ignore any edge for movies released before 1965).


#### Checkstyle Appeals

N/A

#### Partner division of labor

We did almost all of our work on this project sitting side by side. While it wasn’t completely pair-programmed because we had often separated tasks and were working independently, we spent near equivalent amounts of time on the project.
