Nonfunctional requirements:
-The Java GridWorld UI and API shall be used by the game engine to display the game board.
-The game timer shall be based on GridWorld steps.
-Messages shall be displayed in the GridWorld message field.
-The game engine shall support AI players written in PHP or python.
-The game engine shall use HTTP POST to send JSON data to the AIs as follows:
{
 "boardSize":18  
 "home":
       {
        "color":"red",
        "pieces":[{"row":0,"column":0,"damage":0},{"row":1,"column":1,"damage":1}],
        "destinations":[{"row":0,"column":0},{"row":1,"column":1}]
         },
    "enemy":
         {
          "color":"blue",
          "pieces":[{"row":0,"column":0,"damage":0},{"row":1,"column":1,"damage":1}],
          "destinations":[{"row":0,"column":0},{"row":1,"column":1}]
          }
}
-The AIs' responses shall also be JSON formatted:
{
  "from":[{"row":0,"column":0,"damage":0},{"row":1,"column":1,"damage":1},{"row":2,"column":2,"damage":2}],
  "to":[{"row":0,"column":0,"damage":0},{"row":1,"column":1,"damage":1},{"row":2,"column":2,"damage":2}]
}

Functional requirements:
-The game engine shall send each player's Web AI data on both teams' pieces' locations, destinations, and damaged pieces, as well as the board's size.
-The game engine shall receive data from each AI on its next move, including where it is moving from and to.
-4 pieces of each team shall move if possible if outside the destination area.
	-It is the AIs responsibility to check if a move is possible.
	-The game engine shall not check for move possiblilities.
-Using a timer, the game engine shall repeat a cycle of sending the teams their data, receiving information on each team's next move, and performing the moves.
-The game engine shall make 1 move for each team whenever the timer completes.
	-If either team has not submitted a move, the game engine shall not enact either move.
-The game engine shall ensure all submitted moves are valid by the rules of Halma.
	-If either team submitted an invalid move, the game engine shall not enact either move, and the UI shall display an error.
-Collisions shall result in both colliding pieces to become "damaged" and unable to jump for the next 5 turns.
-Repeat collisions will result in the "damage" count being reset to 5.
-The damage count will decrement upon each successful move with a lower limit of 0.
-Upon a player's victory, the UI shall declare "halmate" and prevent any additional moves.
-Messages displayed by the UI shall include the most recent moves made, a list of any "damaged" pieces, the number of turns elapsed, any errors that have occurred, and if "halmate" has occurred.
-Each team shall have different colored pieces.
