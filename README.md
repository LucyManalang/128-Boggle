[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/GqrHw_cP)

# Boggle!
For our course project, we created a digital version of Parker Brother's game, "Boggle". Boggle is a game where players shake a board filled with dice that land on random letters, creating a 4x4 board of letters. The players then have a set amount of time to find all words they can on the board by connecting letters on the adjacent dice (including diagonally) while never using the same dice twice in a word. In multiplayer games, players are scored based on how many unique 3 or more letter words they got and are awarded more points for longer words.

# Goals:
For this project, we implemented the classic word game Boggle. In this game, the player shakes a board where dice roll and land on random letters. The players then have a set amount of time to write down all words that they find on the board by connecting letters on the adjacent dice (diagonally counts also) while never using the same dice twice per word. In multiplayer games, players are scored based on how many unique 3 or more letter words they got and are awarded more points for longer words. In our implementation of Boggle, the user is able to:
- Start a new game with a randomized boggle board, or input a seed number to get a specific boggle board.
- Drag to select dice on the board and make words, which are checked for their validity.
- See their score at the end of the game based on the words they found.

# Implementation:
We implemented this project by first creating a dice class that inherits the GraphicsGroup class. An instance of this class represents one of the 16 dice on the boggle board. Although "dice" is the plural version of the word, having a class called "Die" could potentially be surprising for people reviewing the code. A Dice object contains several attributes, including: its current face, its possible faces, its position on the board, up to 8 dice that are adjacent to it, and whether or not it is currently selected by the player. Because of our use of inheritance, each dice object works as a graphics group which has an outline, label, and hitbox which is used for mouse listeners.

We then created a Board class that represents a 2D array of dice objects. We used a 2D array to be able to easily visually represent the game as a board of dice, like how the game is in real life. The board class has 3 different main methods: a method to randomize the board, a method to set all adjacency lists of all the dice, and a method to solve the board. The solve method creates a HashSet of all possible words a player can find on the current board. The solve method uses the Dictionary class (A class that uses our file reader to create a HashSet of all words in the dictionary we use), DictionaryTree class (A class that reuses the methods in PrefixTree in HW3 except for the getWordsForPrefix method), and the TreeNode class (the same TreeNode class that was provided in HW3). The board class also contains a seed feature which allows the user to control the pseudo-random randomizeBoard() function to allow for the board to be randomized in the same way on different devices when the same seed is inputted. This allows for an offline multiplayer where players will need to compare scores after their games are over.

The Boggle class is used to tie everything together. When called, it initializes an instance of the Board class and sets up all visuals in the canvas window. The Boggle class also contains all event handlers, such as mouse listeners and buttons. The Boggle Class also utilizes the Timer class and the Score class. The Timer class handles the timer and the Score class calculates the players score at the end of the game. 


# Data Structure Analysis:
We implemented many different data structures from this class into our final project but have chosen to focus on a few of the more significant data structures in this report. We will be focussing on 2D Arrays and trees in our project but also want to note that we implemented many other data structures that we discussed in class such as HashSets, HashMaps and Deques.

We chose to implement our board with a 2D Array. We needed to represent each of the 16 dice as well as each of the different faces the dice could represent. This data structure allowed us to randomize which die to put in which place on the board and randomize which face of the die was shown. If we use m to represent each die and n represent each face of a die then our 2D Array has a space complexity of O(mn). Our 2D Array is used to randomize each die and face into a spot in the Boggle board. This randomization has a time complexity of O(m * n) with both n and m being the lengths of each Array in the board 2D Array which are both initialized as 4. This is because the board 2D Array is initialized with length of 4 in each Array. The function goes through each die in the board Array and then inside of that for loop goes through each face of the die that was specified in the first for loop which creates our time complexity of O(42).

We considered using 16 different arrays for each dice and the specific combinations of letters that could be represented on them but we thought this was more complicated because it would have to be done manually. Instead we created a text file with the specific letters and dice configurations and read the text file to assign each die with their corresponding letters. Additionally it would be harder to keep track of every individual die since there were 16 different lists so instead we decided on a 2D Array so that we could easily use the id numbers to assign the randomized dice to their respective graphics object on the user interface using a time complexity of O(n2) with n being the length of board.

Additionally we chose to create our dictionary tree as a Tree. Our dictionary tree consists of letters that are represented as nodes. Words are added to the tree letter by letter and marked when they are the end of a word. In this way our dictionary tree contains all of the words in the dictionary. We use this dictionary tree to search for valid words by iterating through the tree to find corresponding letters to the word being searched. This creates a worst case time complexity of O(n) with n being the height of the tree. This is because the worst case scenario is searching for the longest word in the dictionary meaning the function would iterate through every level of the tree until it reached the bottom most terminating node which would be at depth n. 

The decision to use a tree was heavily influenced by HW 3 the auto complete tree. The dictionary tree was exactly what we needed in terms of checking valid words and we knew that a tree would be a good way to solve this problem as it was the one presented to us in class. The other option we considered was looping through our dictionary HashSet to check if it contained a word. However the dictionary HashSet is much longer than the longest word in the dictionary so the worst case time complexity of the tree would be less than that of the HashSet. 

The Dictionary class’s main purpose was to read the dictionary file and create a HashSet out of all the words in the file. Since the dictionary we used is 80,000 words, the use of a data structure with constant lookup time is crucial. We specifically chose a HashSet for the Dictionary class because it has O(1) lookups. Tangentially, the solve method also uses a HashSet to avoid having to deal with the edge cases that may be associated with duplicate words in the possibleWords collection.



# Tests:
We only created tests for the board class because we determined all methods in all other classes were either trivial, required non-repeatable user input, or had a visual output that would verify if the method was working at an acceptable level without the need of testing.

# Bugs:
Unfortunately, due to time constraints, we have found some bugs that we were unable to fix in time. 
- The game may crash at the end of the timer due to a ConcurrentModificationException.
- - How to replicate: Actively draw on the boggle board after the timer reaches zero, but before the end game score is drawn. May occur without user interaction.
- - Plan to fix it: Find a way to prevent changes to the graphics, or stop them from being drawn concurrently at that moment. May require re-implementing the timer using Java's timer class or another method.
- Lag due to canvas.draw() being called often as the game progresses.
- - How to replicate: Play the game normally, or draw a lot on the canvas as the game is playing. Some lag will be caused, and can be seen in the line being drawn on the canvas.
- - Plan to fix it: This bug is caused by the implementation of canvas.draw(). We could try to find another way of updating the canvas, but canvas.draw() is often called behind the scenes in Kilt-Graphics where we can’t change it. 
This bug was greatly improved by changes to the timer implementation and does not have a major effect on the application anymore.
