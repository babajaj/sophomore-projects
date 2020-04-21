# README

## edu.brown.cs.srockhil.tIMDb

    Design Details:
    
Repl: takes commandline input and splits it uses whitespace. For tIMDb the valid commands are mdb and connect
    mdb: created in main. When mdb command is run it takes a filename for a sql database, and mdb object
         gives this sql database to a new Database object. 
             Database: querys on this sql database, returns output from querys
    connect: created in main. When connect command is run with a starting actor and 
    target actor, it creates a new ActorGraph and gives it a root node(starting actor). It then creates a dijkstra 
    object which takes in this graph and searches it. It then recursively prints out the path from starting actor
    to target actor and returns it (for the gui). (or it prints/returns the error message)
        dijkstra: The dijkstra object searches the graph (while the graph expands itself) until it
                      has found the targetnode. Each time the algorithm gets to a new node, it
                       updates (or doesnt) the nodes"previous" field and tells the graph to expand itself 
                       on that noden (only adding nodes if the name constraint
                       passes and the node doesnt already exist). Once it gets to the target node it returns it.
                       Dijkstra's type T is the kind of graphNode that it will be searching on. 
         actorGraph: the actor graph contains all of the nodes in a hashmap as it expands itself by telling
                     the database to query. Its type represents the kind of nodes that will be in it.
               actorNode: representations of actors, contain their names and ids, as well as their previous
               actor and the movie that connect them to their previous actor. It contains a hashmap with
               all of its edges and the movies that the edges represent
           
note*  our edge class is simply an object that contains a weight and it cannot be made more 
specific. The graph object
contains a hashmap that maps edges to two nodes (it stores pairs of neighbors). given an edge and a node, 
one can get the neighbor. dijkstra makes use of that functionality. In addition to this, the Actornodes 
themselves contain a hashmap of their edges that map to movies. This information is added to each Actornode
when the ActorGraph is expanding. 
The nodes keep track of their previous edge and thus their previous movie,
and the edges do not have to store any of that specific information.
                       
        
    
    Runtime/Space optimizations:
In the database we have implemented caches (hashmaps) which contain the results from querying.
This way when there is a query on the same element, an sql query doesnt need to be made.
The information can be gotten from the cache. the hashmaps clear themselves
 after reaching 100000 elements. 
    
    Unit tests:
to run the unit tests, simply input mvn test in the command line before running
 the program. The tests cover bad repl input, incorrect arguments, actor connections when there is a connection,
 actor connects when there is no connection, as well as stars tests just to
  show that everything works the same.
    
    How to run system tests:
./cs32-test tests/student/timdb/* -t 15
runs system tests with 15 second limit 
(./ replaced by "python" on windows)
        
    How to build/run the program from the command line:
mvn package: builds program
Then to run the program, type "./run" (add --gui for the gui). Then, as the program is
  running, any of the executable commands can be run:
  - mdb
  - connect
  - stars
  - neighbors
  - radius
  
I will only address mdb and connect, as the others should not have been
 changed for this project in any way. To load a database, the command from
  the command line is "mdb <filename>". To connect two actors in the database
  ,the command is "connect <actor 1 name> <actor 2 name>" where the actor
   names are surrounded by quotes. Any incorrect input should be correctly
   handled and addressed by
    the repl, and will prompt the user to input something differently.  

    
    Checkstyle appeals:
None
    
    Partner Division of labor:
Sarah: 
- Dijkstra
- graphs, nodes, edges
- Majority of gui

Eyal: 
- Database
- mdb
- testing
- commenting

Both:
- high level design
- css
- gui design
- debugging, cleaning, editing each others' code
- everything else


    Known bugs:
No known bugs.


    Design Questions:
    
1) our program uses a specific Connect class executable to check the project
    constraints and perform a graph search algorithm on the graph. To make it
     possible to use a new graph search algorithm, a developer would simply need
      to add their own algorithm class which would be able to "search" for a
       node and build the path. Our program uses a specific graph that expands on itself,
        and only adds nodes to the database if they fit the name constraint. 
        The developercould instantiate an instance of Graph that does not use this name constraint.
         it simply needs to be able to xpand and get a nodes neighbor given an edge 
         (and set a nodes previous node).


2) There would need to be an interface that handles different types of data
, such as Database. This interface would need to have a method query to query
 the datastructure. A csv version would simply need to query the file given
  the query value and the thing to query from. 
  
 3) we would have to map each edge(movie) to the year they were released. Instead of looping through all
 of the actors edges, the algorithm would search through edges that represent movies that were
 released after the year that the nodes "previous edge" (movie) was released.
