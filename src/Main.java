import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            MapData mapData = loadMapData("data/map.txt");

            Player player = new Player(mapData.getPlayerStart());

            Enemy[] enemies = new Enemy[]{
                    new Pinky(mapData.getPinkyStart()),
                    new Inky(mapData.getInkyStart()),
                    new Blinky(mapData.getBlinkyStart())
            };

            Game game = new Game(player, enemies, mapData);

            GameRenderer renderer = new GameRenderer(mapData, game);
            renderer.setupDraw();

            // Game loop
            while (true) {
                // Handle keyboard input
                if (StdDraw.hasNextKeyTyped()) {
                    char key = StdDraw.nextKeyTyped();
                    game.handleInput(key);
                }

                // Handle arrow keys for direction
                if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_UP)) {
                    game.handleArrowKey(Game.Direction.UP);
                } else if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_DOWN)) {
                    game.handleArrowKey(Game.Direction.DOWN);
                } else if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_LEFT)) {
                    game.handleArrowKey(Game.Direction.LEFT);
                } else if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_RIGHT)) {
                    game.handleArrowKey(Game.Direction.RIGHT);
                }

                // Update game state
                game.update();

                // Draw current frame
                renderer.tickAnimation();
                renderer.drawGame();

                // ~60 FPS
                StdDraw.pause(16);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Map file could not be loaded.");
            e.printStackTrace();
        }
    }

    // Method for loading map data from a file. You should not need to modify this method.
    private static MapData loadMapData(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));

        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        scanner.nextLine();

        String[] playerLine = scanner.nextLine().split(" ");
        Position playerStart = new Position(
                Integer.parseInt(playerLine[1]),
                Integer.parseInt(playerLine[2])
        );

        String[] directEnemyLine = scanner.nextLine().split(" ");
        Position directEnemyStart = new Position(
                Integer.parseInt(directEnemyLine[1]),
                Integer.parseInt(directEnemyLine[2])
        );

        String[] randomChaseEnemyLine = scanner.nextLine().split(" ");
        Position randomChaseEnemyStart = new Position(
                Integer.parseInt(randomChaseEnemyLine[1]),
                Integer.parseInt(randomChaseEnemyLine[2])
        );

        String[] closestCornerEnemyLine = scanner.nextLine().split(" ");
        Position closestCornerEnemyStart = new Position(
                Integer.parseInt(closestCornerEnemyLine[1]),
                Integer.parseInt(closestCornerEnemyLine[2])
        );

        String[] cornerHeader = scanner.nextLine().split(" ");
        int cornerCount = Integer.parseInt(cornerHeader[1]);

        Position[] corners = new Position[cornerCount];
        for (int i = 0; i < cornerCount; i++) {
            int r = scanner.nextInt();
            int c = scanner.nextInt();
            scanner.nextLine();
            corners[i] = new Position(r, c);
        }

        char[][] map = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < cols; j++) {
                map[i][j] = line.charAt(j);
            }
        }

        scanner.close();

        return new MapData(
                map,
                playerStart,
                directEnemyStart,
                randomChaseEnemyStart,
                closestCornerEnemyStart,
                corners
        );
    }
}