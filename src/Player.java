public class Player {
    private static final double VISUAL_SPEED = 0.10;

    private Position pos;
    private int score;

    private Game.Direction currentDirection;
    private Game.Direction requestedDirection;

    private double visualRow;
    private double visualCol;

    private boolean moving;

    public Player(Position pos) {
        this.currentDirection = Game.Direction.NONE;
        this.requestedDirection = Game.Direction.NONE;
        this.pos = pos;
        this.score = 0;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
        this.moving = false;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Game.Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Game.Direction dir) {
        this.currentDirection = dir;
    }

    public Game.Direction getRequestedDirection() {
        return requestedDirection;
    }

    public void setRequestedDirection(Game.Direction dir) {
        this.requestedDirection = dir;
    }

    public double getVisualRow() {
        return visualRow;
    }

    public double getVisualCol() {
        return visualCol;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Returns true if visual position has caught up to the logical grid position.
    public boolean isGridAligned() {
        return Math.abs(visualRow - pos.getRow()) < 0.01 &&
                Math.abs(visualCol - pos.getCol()) < 0.01;
    }

    // Moves the visual position toward the logical grid position.
    public void updateVisualPosition() {
        if (visualRow < pos.getRow()) {
            visualRow = Math.min(visualRow + VISUAL_SPEED, pos.getRow());
        } else if (visualRow > pos.getRow()) {
            visualRow = Math.max(visualRow - VISUAL_SPEED, pos.getRow());
        }

        if (visualCol < pos.getCol()) {
            visualCol = Math.min(visualCol + VISUAL_SPEED, pos.getCol());
        } else if (visualCol > pos.getCol()) {
            visualCol = Math.max(visualCol - VISUAL_SPEED, pos.getCol());
        }
    }

    // Sets visual position directly to the logical grid position.
    public void snapVisualToLogical() {
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
    }

    // Resets the player to a given starting position and clears all state.
    public void reset(Position startPos) {
        this.pos = startPos;
        this.score = 0;
        this.currentDirection = Game.Direction.NONE;
        this.requestedDirection = Game.Direction.NONE;
        this.visualRow = startPos.getRow();
        this.visualCol = startPos.getCol();
        this.moving = false;
    }

    // Called every game tick when the game is in PLAYING state.
    // Handles input buffering, movement, and pellet collection.
    public void update(MapData mapData) {
        // Only attempt a new logical move when visually aligned with the grid
        if (!isGridAligned()) {
            updateVisualPosition();
            return;
        }

        snapVisualToLogical();

        int row = pos.getRow();
        int col = pos.getCol();

        // Try to switch to the requested direction if valid
        if (requestedDirection != Game.Direction.NONE) {
            int newRow = row + requestedDirection.getDRow();
            int newCol = col + requestedDirection.getDCol();
            if (mapData.isValidMove(newRow, newCol)) {
                currentDirection = requestedDirection;
            }
        }

        // Try to move in the current direction
        if (currentDirection != Game.Direction.NONE) {
            int newRow = row + currentDirection.getDRow();
            int newCol = col + currentDirection.getDCol();

            if (mapData.isValidMove(newRow, newCol)) {
                pos = new Position(newRow, newCol);
                moving = true;

                // Collect pellet if present
                if (mapData.hasPellet(newRow, newCol)) {
                    mapData.removePellet(newRow, newCol);
                    score += 10;
                }
            } else {
                // Hit a wall — stop moving
                moving = false;
                currentDirection = Game.Direction.NONE;
            }
        } else {
            moving = false;
        }

        updateVisualPosition();
    }
}









