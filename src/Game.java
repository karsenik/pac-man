public class Game {

    public enum GameState {
        START_SCREEN,
        READY,
        PLAYING,
        PAUSED,
        WON,
        LOST,
    }

    public enum Direction {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1), NONE(0, 0);

        private final int dRow;
        private final int dCol;

        Direction(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }

        public int getDRow() { return dRow; }
        public int getDCol() { return dCol; }
    }

    private static final double COLLISION_THRESHOLD = 0.75;
    private static final long READY_DURATION_MS = 1000;

    private final Player player;
    private final Enemy[] enemies;
    private GameState gameState;
    private final MapData mapData;

    private long readyStartTime;
    private int totalPellets;

    public Game(Player player, Enemy[] enemies, MapData mapData) {
        this.player = player;
        this.enemies = enemies;
        this.mapData = mapData;
        this.gameState = GameState.START_SCREEN;
        this.totalPellets = countPellets();
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    // Counts total pellets on the current map state.
    private int countPellets() {
        int count = 0;
        for (int r = 0; r < mapData.getRows(); r++) {
            for (int c = 0; c < mapData.getCols(); c++) {
                if (mapData.hasPellet(r, c)) count++;
            }
        }
        return count;
    }

    // Resets all entities and map to initial state, returns to START_SCREEN.
    public void reset() {
        mapData.resetMap();
        player.reset(mapData.getPlayerStart());
        enemies[0].reset(mapData.getPinkyStart());
        enemies[1].reset(mapData.getInkyStart());
        enemies[2].reset(mapData.getBlinkyStart());
        totalPellets = countPellets();
        gameState = GameState.START_SCREEN;
    }

    // Handles keyboard input and routes to correct handler based on game state.
    public void handleInput(char key) {
        // Q quits from any state
        if (key == 'q' || key == 'Q') {
            System.exit(0);
        }

        switch (gameState) {
            case START_SCREEN:
                if (key == ' ') {
                    gameState = GameState.READY;
                    readyStartTime = System.currentTimeMillis();
                }
                break;

            case PLAYING:
                if (key == 'p' || key == 'P') {
                    gameState = GameState.PAUSED;
                } else {
                    handleDirectionInput(key);
                }
                break;

            case PAUSED:
                if (key == 'p' || key == 'P') {
                    gameState = GameState.PLAYING;
                }
                break;

            case LOST:
            case WON:
                if (key == 'r' || key == 'R') {
                    reset();
                }
                break;

            default:
                break;
        }
    }

    // Also handle arrow key input passed as special codes from Main.
    public void handleArrowKey(Direction dir) {
        if (gameState == GameState.PLAYING) {
            player.setRequestedDirection(dir);
        }
    }

    // Maps character input to direction requests.
    private void handleDirectionInput(char key) {
        switch (key) {
            case 'w': player.setRequestedDirection(Direction.UP);    break;
            case 's': player.setRequestedDirection(Direction.DOWN);  break;
            case 'a': player.setRequestedDirection(Direction.LEFT);  break;
            case 'd': player.setRequestedDirection(Direction.RIGHT); break;
        }
    }

    // Main update method — called every game tick.
    public void update() {
        switch (gameState) {
            case READY:
                if (System.currentTimeMillis() - readyStartTime >= READY_DURATION_MS) {
                    gameState = GameState.PLAYING;
                }
                break;

            case PLAYING:
                updatePlaying();
                break;

            default:
                break;
        }
    }

    private void updatePlaying() {
        player.update(mapData);

        // Update enemies
        for (Enemy enemy : enemies) {
            enemy.move(player, mapData);
        }

        // Check collision
        if (isCollision()) {
            gameState = GameState.LOST;
            return;
        }

        // Check win condition
        if (allPelletsCollected()) {
            gameState = GameState.WON;
        }
    }

    // Checks if Pac-Man's visual position overlaps with any enemy's visual position.
    private boolean isCollision() {
        double px = player.getVisualCol();
        double py = player.getVisualRow();

        for (Enemy enemy : enemies) {
            double ex = enemy.getVisualCol();
            double ey = enemy.getVisualRow();

            double distance = Math.sqrt((px - ex) * (px - ex) + (py - ey) * (py - ey));
            if (distance < COLLISION_THRESHOLD) {
                return true;
            }
        }
        return false;
    }

    // Checks if all pellets have been collected.
    private boolean allPelletsCollected() {
        for (int r = 0; r < mapData.getRows(); r++) {
            for (int c = 0; c < mapData.getCols(); c++) {
                if (mapData.hasPellet(r, c)) return false;
            }
        }
        return true;
    }
}