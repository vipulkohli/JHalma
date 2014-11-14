JHalma
======
Vipul Kohli and Andrew Socha CSE 4345 Fall 2014 Team Project

<a href="http://lyle.smu.edu/~coyle/halmagame/halma1.0/canvas.html#halma">Click here for halma rules</a>

Java GridWorld Halma that connects with 2 Web AI Players

Collision Penalties:

-Intentional collisions onto already occupied squares will result in:
	
	Damage = 1 for enemy piece
	
	Chance of Damage = 5 for home piece

-An accidental head-on collision is when both teams move to the same square on the same tuen:
	
	Damage = 5 for both the home and enemy piece
