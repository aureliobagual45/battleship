BATTLESHIP

This is a console-based implementation of the classic Battleship game.

The game allows the player to play against a computer with different difficulty levels and includes both manual and random ship placement.

FEATURES

Player vs Computer gameplay
Two difficulty levels (easy and hard)
Manual or random ship placement
Turn-based system with repeat turns on hit
Ship tracking and win/lose detection
Coordinate-based input (e.g. A0, B5)
Console UI with game state visualization

AI

The game includes two types of AI:

Easy AI: random attacks
Hard AI: uses a probability-based heatmap to choose better positions

The AI also switches to a "target mode" after hitting a ship, trying to follow its direction until it is sunk

GAME LOGIC

The board is represented as a grid with different cell states (empty, ship, hit, miss)
Ships are placed with validation to avoid overlap
Attacks return different results (hit, miss, sunk, already tried)
The game ends when all ships of one side are destroyed

STRUCTURE

Main.java: entry point and main loop
Game.java: controls game flow and turns
Board.java: handles grid, ship placement and attack logic
GameUI.java: input/output and user interaction
AI classes: computer behavior (easy and hard)

HOW TO RUN

Compile all files:

javac *.java

Run:

java Main

WHAT I LEARNED

Structuring a larger project with multiple classes
Separating logic, UI and control flow
Working with enums, records and object-oriented design
Implementing game loops and state management
Designing simple AI behavior

NOTES

This project was made to practice object-oriented programming and game logic.

The focus was on structure and functionality rather than graphics.
