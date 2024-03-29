# Implementation of John Conway's Game of Life in Java with Javax Swing GUI

1. [Description](#description)
2. [Features](#features)
3. [Installation](#installation)

## Description

This is a simple implementation of [John Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) in Java with a GUI made with Javax Swing. The Game of Life is a cellular automaton devised by the British mathematician John Horton Conway in 1970. Conway once called it a zero-player game, as its evolution is determined by its initial state, requiring no further input. One interacts with the Game of Life by creating an initial configuration and observing how it evolves.

The rules of the Game of Life are simple.: The game is played on an infinite two-dimensional grid of square cells, each of which is in one of two possible states, live or dead. Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:
   - Any live cell with fewer than two live neighbours dies, as if by underpopulation.
   - Any live cell with two or three live neighbours lives on to the next generation.
   - Any live cell with more than three live neighbours dies, as if by overpopulation.
   - Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

As the Game of Life is [turing complete](https://en.wikipedia.org/wiki/Turing_completeness) (see also [here](https://conwaylife.com/wiki/Turing_machine) and compare to another turing complete cellular automaton [Rule 110](https://en.wikipedia.org/wiki/Rule_110)), it can simulate any algorithm or computation that can be performed by a computer. 
Especially interesting are the so called [Spaceships](https://conwaylife.com/wiki/Spaceship) which are patterns that move across the board. The [Gosper Glider Gun](https://conwaylife.com/wiki/Gosper_glider_gun) is a pattern that creates infinitely many [Gliders](https://conwaylife.com/wiki/Glider) (small spaceships) in a loop.

Other common patterns are [Oscillators](https://conwaylife.com/wiki/Oscillator) like the [Toad](https://conwaylife.com/wiki/Toad) or the [Pulsar](https://conwaylife.com/wiki/Pulsar), which are pattern that oscillates between two or more states, and [Still Lifes](https://conwaylife.com/wiki/Still_life), which are static patterns like the [Block](https://conwaylife.com/wiki/Block) or the [Boat](https://conwaylife.com/wiki/Boat) that remain the same in every generation.

## Features
The user can:
- start and stop the game.
- clear the board.
- change the speed of the game.
- change the size of the cells.
- change the size of the board by resizing the window.
- interact with the game while it is running, by holding the mouse button and dragging the cursor over the cells to make them alive or dead.
- import famous patterns from the menu. (e.g. Glider, Gosper Glider Gun, etc.)

## Installation

1. Clone the repository with `git clone git@github.com:sirrenberg/Game-of-Life.git`
2. Change to the directory with `cd Game-of-Life/src/`
3. Compile the program with `javac gol/GameOfLifeMain.java`
4. Execute the program with `java gol.GameOfLifeMain`


