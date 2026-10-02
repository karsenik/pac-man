public class Blinky extends Enemy {

    public Blinky(Position pos) {
        super(pos);
    }

    @Override
    public Position selectTarget(Player player, MapData mapData) {
        Position[] corners = mapData.getCorners();
        Position playerPos = player.getPos();

        Position closestCorner = null;
        double minDistance = Double.MAX_VALUE;

        for (Position corner : corners) {
            double distance = euclideanDistance(playerPos, corner);
            if (distance < minDistance) {
                minDistance = distance;
                closestCorner = corner;
            }
        }

        return closestCorner;
    }

    private double euclideanDistance(Position a, Position b) {
        int dRow = a.getRow() - b.getRow();
        int dCol = a.getCol() - b.getCol();
        return Math.sqrt(dRow * dRow + dCol * dCol);
    }
}
