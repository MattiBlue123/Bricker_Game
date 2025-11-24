**# Bricker_Game**
An Object-Oriented implementation of an Arkanoid-style arcade game featuring dynamic brick strategies, power-ups, and design patterns. Built with the DanoGameLab engine.

Bricker is a breakout-style arcade game developed as an Object-Oriented Programming exercise. The project is built using the DanoGameLab game engine (v1.1.0). The game challenges players to break bricks using a ball and paddle while preventing the ball from falling off the screen.

**Features**
Core Gameplay:
  Classic paddle movement and ball physics.
  Numeric and graphic life counters.
  Win/Loss conditions based on brick clearance or life depletion.

Advanced Brick Strategies:
  Approximately 50% of the bricks possess special behaviors triggered upon collision:

Extra Balls (Pucks):
  Spawns two additional, smaller balls that move at random angles.

Extra Paddle:
  Generates a temporary secondary paddle in the center of the screen that mimics the main paddle's movement.
  
Exploding Bricks:
  Destroys the target brick and all surrounding neighbor bricks (excluding diagonals).

Life Recovery:
  Drops a falling heart object; catching it with the main paddle restores a life (up to a max of 4).

Double Strategy:
  A composite behavior that combines two other effects. It can stack up to a maximum of 3 special behaviors on a single brick.

Architecture & Design:
  This project focuses on clean Object-Oriented Design principles, specifically the Open-Closed Principle. Key architectural choices include:
  Strategy Pattern: Used to manage diverse brick collision behaviors via the CollisionStrategy interface.

Decorator Pattern:
  Implemented to handle the "Double Strategy," allowing behaviors to wrap around one another.

Inheritance & Composition:
  utilized to create game objects (Ball, Paddle, Brick) that interact with the GameObject class provided by the engine.

Project Structure:
  The source code is organized into the following packages:
    bricker.main: Contains the BrickerGameManager and entry point.
    bricker.gameobjects: Contains entity classes like Ball, Paddle, and Brick.
    bricker.brick_strategies: Contains the logic for special collision behaviors.


**Tech Stack**
Language: Java
  Engine: DanoGameLab API
