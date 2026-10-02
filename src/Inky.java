import java.util.ArrayList;
import java.util.Random;

public class Inky extends Enemy {

    private final Random random = new Random();

    public Inky(Position pos) {
        super(pos);
    }

    // Inky (Random Chase Enemy):
    // 60% chance: targets Pac-Man's current position.
    // 40% chance: targets a random valid neighboring tile.
    @Override
    public Position selectTarget(Player player, MapData mapData) {
        if (random.nextDouble() < 0.60) {
            // 60% - chase Pac-Man directly
            return player.getPos();
        } else {
            // 40% - pick a random valid neighbor (UP, DOWN, LEFT, RIGHT order)
            int[] dRow = {-1, 1, 0, 0};
            int[] dCol = {0, 0, -1, 1};

            ArrayList<Position> validNeighbors = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                int newRow = pos.getRow() + dRow[i];
                int newCol = pos.getCol() + dCol[i];
                if (mapData.isValidMove(newRow, newCol)) {
                    validNeighbors.add(new Position(newRow, newCol));
                }
            }

            if (validNeighbors.isEmpty()) {
                return player.getPos();
            }

            return validNeighbors.get(random.nextInt(validNeighbors.size()));
        }
    }
}
