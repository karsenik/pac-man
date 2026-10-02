# Pac-Man (Java)

A Java implementation of the classic Pac-Man arcade game. Eat every pellet in the maze while avoiding three ghosts, each with its own chasing strategy. Built with object-oriented design and rendered with the [StdDraw](https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html) library.

🎥 [Watch the gameplay video](https://youtu.be/x9bAhjuz7Xk?si=nYIXK8QFNhNnF_og)

## Features

- **BFS ghost AI:** ghosts find the shortest path through the maze with Breadth-First Search.
- **Three ghosts, three behaviors:** Blinky, Pinky and Inky extend a common `Enemy` class and override only their target selection.
- **Smooth movement:** characters slide between tiles using double-precision visual coordinates instead of jumping tile to tile.
- **Input buffering:** a turn requested before an intersection is stored and executed once Pac-Man is centered on a tile.
- **Animation:** Pac-Man's mouth cycles through frames and freezes when he stops. Ghost sprites change with their direction.
- **Game state machine:** start screen, ready, playing, paused, game over and win.

## Controls

| Key | Action |
|---|---|
| Arrow keys | Move Pac-Man |
| `SPACE` | Start the game |
| `P` | Pause / resume |
| `R` | Restart after winning or losing |
| `Q` | Quit |

## Ghosts

All ghosts use the same BFS pathfinder. Only their targets differ.

| Ghost | Target |
|---|---|
| Pinky | Pac-Man directly |
| Blinky | Closest corner |
| Inky | Mix of direct pursuit and random neighboring tiles |

## How It Works

### Pathfinding
`BFSPathFinder.getFullShortestPath()` explores the maze layer by layer with a FIFO queue. Neighbors are checked in a fixed order (up, down, left, right) so movement is deterministic. Once the goal is found, the path is rebuilt from a `parent` array and reversed.

### Collision detection
Collision uses the visual coordinates of Pac-Man and each ghost, not their grid positions. If the distance between their centers is below `COLLISION_THRESHOLD`, the game switches to `LOST`.

### Input buffering
`Player.update()` only changes direction when Pac-Man is grid-aligned. If the requested direction is free of walls, it becomes the current direction. Otherwise he keeps moving in the current one.

### Data structures
- `char[][]` for the maze layout (walls and pellets)
- `boolean[][]` and `Position[][]` for BFS visited and parent tracking
- `Enemy[]` for the ghosts, `ArrayList` for neighbors and paths

## Project Structure

```
├── Main.java          // entry point, loads the map
├── Game.java          // game loop, state machine, input, collision
├── GameRenderer.java
├── Player.java        // Pac-Man movement, input buffer, score
├── Enemy.java         // abstract ghost base class
├── Blinky.java
├── Pinky.java
├── Inky.java
├── BFSPathFinder.java // shortest path search
├── MapData.java       // maze storage and move validation
└── Position.java
```


## Getting Started

### Requirements
- Java JDK 17 or newer (the code uses `switch` expressions)
- `StdDraw.java` (included in the repo / [download here](https://introcs.cs.princeton.edu/java/stdlib/))


### Run

```bash
# compile
javac *.java

# run
java Main
```
