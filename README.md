JHalma
======
Vipul Kohli and Andrew Socha CSE 4345 Fall 2014 Team Project

<a href="http://lyle.smu.edu/~coyle/halmagame/halma1.0/canvas.html#halma">Click here for halma rules</a>

Java GridWorld Halma that connects with 2 Web AI Players

-Victory conditions:
	
	Win = 9-square destination area filled
	
	Tie = Both teams' destination areas filled on same move

Collision Penalties:

-Accidental head-on collisions when both teams move to the same square on the same time:
	
	Enemy Damage = 5
	
	Home Damage = 5

-Intentional collisions onto already occupied squares will result in:
	
	Enemy Damage = 5

	Home Damage unchanged

-Danger Zone:
	
	Home  Damage = 5 if full health

	Enemy Damage unchanged unless collision
