import java.util.ArrayList;

public class BFSPathFinder {
    public ArrayList<Position> getFullShortestPath(Position start, Position goal, MapData mapData) {

        // If start equals goal, return a list containing only the start position.
        if (start.equals(goal)) {
            ArrayList<Position> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        int rows = mapData.getRows();
        int cols = mapData.getCols();

        // visited array to avoid revisiting tiles
        boolean[][] visited = new boolean[rows][cols];

        // parent array to reconstruct the path
        Position[][] parent = new Position[rows][cols];

        // BFS queue
        ArrayList<Position> queue = new ArrayList<>();

        visited[start.getRow()][start.getCol()] = true;
        queue.add(start);

        // Direction order: UP, DOWN, LEFT, RIGHT
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        boolean found = false;

        while (!queue.isEmpty()) {
            Position current = queue.remove(0);

            for (int i = 0; i < 4; i++) {
                int newRow = current.getRow() + dRow[i];
                int newCol = current.getCol() + dCol[i];

                if (!mapData.isValidMove(newRow, newCol)) continue;
                if (visited[newRow][newCol]) continue;

                visited[newRow][newCol] = true;
                parent[newRow][newCol] = current;
                Position neighbor = new Position(newRow, newCol);
                queue.add(neighbor);

                if (neighbor.equals(goal)) {
                    found = true;
                    break;
                }
            }

            if (found) break;
        }

        if (!found) return null;

        // Reconstruct path from goal to start, then reverse
        ArrayList<Position> path = new ArrayList<>();
        Position current = goal;

        while (current != null && !current.equals(start)) {
            path.add(0, current);
            current = parent[current.getRow()][current.getCol()];
        }
        path.add(0, start);

        return path;
    }
}