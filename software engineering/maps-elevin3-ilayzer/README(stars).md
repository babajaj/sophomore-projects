# README

## Stars
There are no known bugs. 


####The program is designed as follows:

Within the edu.brown.cs.ngoodma3 package, there are four sub packages.
1. ####commands:
-  Within the commands package is an ICommand interface, which specifies the 
general structure of a command that can be used and executed by the REPL. In it
prescribes that each command class that implements this interface must 
implement an execute() method. 
- Additionally, there is a KDTree class, which requires that 
its type is a generic that extends class Node (see star package).
Within this class are a few getter methods, a method for recursively creating
a tree from a list, and a printing method that recursively 
prints the supplied tree in pre-order fashion.
- Lastly is a Universe class, which contains three subclasses, all of which 
implement the ICommand interface:
    - Neighbors: contains an implementation of the K-nearest-neighbors algorithm
    and a corresponding naive implementation for testing purposes. 
    Makes use of a treeset to maintain a sorted tree of the nearest neighbors.
    - Stars: parses a CSV file, producing a list of Star objects from which a KDTree is created. 
    - Radius: contains an implementation of the radius search algorithm and a 
    naive implementation for testing purposes. Makes use of a treeset to maintain 
    a sorted tree of the nearest neighbors.
- the Universe class has some shared fields, such as the current list of stars, that allow the commands to share 
various data structures across the state of the program. Additionally, there are
some parsing methods that are shared among the subclasses. 
3. ####Main:
- Within the Main package are Main.java and StarMain.java. 
    - Main.java: Implements the Spark server, initializes a StarMain instance and runs the REPL
    - StarMain.java: Sets up all the shared dependencies necessary for the REPL to be run
    and a Universe to be run and instantiated, respectively.  
3. ###star: 
- Within the star package are
    - Node.java: an abstract class that contains a data field, 
    used for coordinates in the stars implementation, 
    - Star.java: extends the Node class, containing more subfields such as 
    name, ID, and distance from a target star, which is used for the search algorithms
    - SortByDim.java: extends Comparator so that we can compare two stars by dimension
    - SortByDist.java: extends Comparator so that we can compare two stars by distance 
    from a specified target Star
    - SortByDistDescending.java: extends Comparator so that we can compare two stars by distance
    from target in descending order. 
4. ###userIO
- Within the userIO package are
    - CSVParser.java: takes in a CSV file and produces a list of list of
    strings, which is passed into the Stars class and parsed more specifically for each
    command when it is executed.
    - ErrorMessages.java: produces error messages to the command line 
    and the GUI.
    - IncorrectFormatException.java: an exception the CSV parser throws when a CSV file
    is being parsed. Caught by the Stars class.
    - Prompt.java: produces a message to the terminal when a CSV file has been
    read in successfully.
    - REPL.java: takes in a String, ICommand hashmap so that it can recognize
    valid commands directly from string input and execute the command objects 
    directly.  
    
####Optimizations:
 I maintain a sorted Treeset so that, in addition
 to its other sorting and memory advantages, the 
farthest neighbor can always be retrieved in constant time.

###Tests:
To run my tests, run 'mvn package' in the command line from the root of the stars
folder. To run my system tests, run ./cs32-test src/test/student/stars/* from 
the root directory. To run the TA system tests, run ./cs32-test src/test/ta/stars/*



###Run program
Once you build the program, you can run it by calling ./run
from the command line. If you would like to use the gui, do 
./run --gui <port>

###Answers to design questions
1. To support more commands, one would simply implement the ICommand 
interface, and add additional String, ICommand pairs to the hashmap 
that is passed into the repl. 
2. One problem I would face if I wanted to use my KDTree to find
points that are close to each other on Earth's surface is that the KDTree class
requires the type to be a generic type that extends Node. Unless specified as such, which
might be limiting in other ways design-wise, points on earth's surface don't necessarily
have the same information/structure as a Node might. Additionally, using euclidean distance
for points on the earth's surface might not be the most representative way of finding locations
on earth -- ie other distance metrics might be more useful
3. I would need to add more methods
in order to make my KDTree compatible with the Collections 
interface. Some of them would be:
    - add()
    - contains()
    - size()
    - isEmpty()


