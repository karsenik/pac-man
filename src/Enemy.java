import java.util.ArrayList;

public abstract class Enemy {
    protected static final double VISUAL_SPEED = 0.067;

    protected Position pos;
    protected Game.Direction direction;
    protected BFSPathFinder finder;

    protected double visualRow;
    protected double visualCol;

    public Enemy(Position pos) {
        this.pos = pos;
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
        this.finder = new BFSPathFinder();
    }

    public Enemy(Position pos, BFSPathFinder finder) {
        this.pos = pos;
        this.finder = finder;
        this.direction = Game.Direction.NONE;
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
    }

    public double getVisualRow() {
        return visualRow;
    }

    public double getVisualCol() {
        return visualCol;
    }

    public void setVisualRow(double visualRow) {
        this.visualRow = visualRow;
    }

    public void setVisualCol(double visualCol) {
        this.visualCol = visualCol;
    }

    public Game.Direction getDirection() {
        return direction;
    }

    public void setDirection(Game.Direction direction) {
        this.direction = direction;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
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

    public void snapVisualToLogical() {
        this.visualRow = pos.getRow();
        this.visualCol = pos.getCol();
    }

    // Resets the enemy to its starting position.
    public void reset(Position startPos) {
        this.pos = startPos;
        this.direction = Game.Direction.NONE;
        this.visualRow = startPos.getRow();
        this.visualCol = startPos.getCol();
    }

    // Calculates the movement direction from one position to another.
    protected Game.Direction getDirectionFromPositions(Position from, Position to) {
        int dRow = to.getRow() - from.getRow();
        int dCol = to.getCol() - from.getCol();

        if (dRow == -1 && dCol == 0) return Game.Direction.UP;
        if (dRow == 1  && dCol == 0) return Game.Direction.DOWN;
        if (dRow == 0  && dCol == -1) return Game.Direction.LEFT;
        if (dRow == 0  && dCol == 1) return Game.Direction.RIGHT;

        return Game.Direction.NONE;
    }

    // Moves the enemy one step toward the target determined by selectTarget().
    // Only executes when the enemy is grid-aligned.
    public void move(Player player, MapData mapData) {
        // Update visual position every tick
        if (!isGridAligned()) {
            updateVisualPosition();
            return;
        }

        snapVisualToLogical();

        Position target = selectTarget(player, mapData);

        if (target == null) return;

        // Run BFS to find shortest path
        ArrayList<Position> path = finder.getFullShortestPath(pos, target, mapData);

        if (path == null || path.size() < 2) return;

        // Take only the first step of the path
        Position nextPos = path.get(1);
        direction = getDirectionFromPositions(pos, nextPos);
        pos = nextPos;

        updateVisualPosition();
    }

    public abstract Position selectTarget(Player player, MapData mapData);
}