import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HardAI extends BaseAI
{
    @Override
    protected AttackResult search(Board board)
    {
        int[][] heatMap = buildHeatMap(board);
        /// printHeatMap(heatMap);
        List<Position> bestCells = collectBestCells(board, heatMap);

        if (bestCells.isEmpty())
            return AttackResult.ALREADY_TRIED;

        Position chosen = chooseRandomCell(bestCells);
        AttackResult result = board.attack(chosen.x(), chosen.y());

        if (result == AttackResult.HIT)
            startTargetMode(chosen.x(), chosen.y());

        return result;
    }

    private int[][] buildHeatMap(Board board)
    {
        int size = board.getSize();
        int[][] heatMap = new int[size][size];
        Map<Integer, Integer> shipsLeft = board.getShipCountsBySize();

        for (Map.Entry<Integer, Integer> entry : shipsLeft.entrySet())
        {
            int shipSize = entry.getKey();
            int amount = entry.getValue();

            if (shipSize == 1)
                continue;

            for (int y = 0; y < size; y++)
            {
                for (int x = 0; x < size; x++)
                {
                    if (isValidPlacement(board, x, y, 1, 0, shipSize))
                        addPlacementScore(heatMap, x, y, 1, 0, shipSize);

                    if (isValidPlacement(board, x, y, 0, 1, shipSize))
                        addPlacementScore(heatMap, x, y, 0, 1, shipSize);
                }
            }

        }

        return heatMap;
    }

    private void addPlacementScore(int[][] heatMap, int startX, int startY, int dx, int dy, int shipSize)
    {
        for (int i = 0; i < shipSize; i++)
        {
            int x = startX + dx * i;
            int y = startY + dy * i;

            heatMap[y][x]++;
        }
    }

    private List<Position> collectBestCells(Board board, int[][] heatMap)
    {
        int size = board.getSize();
        int bestScore = -1;
        List<Position> bestCells = new ArrayList<>();

        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                if (!board.isValidAttack(x, y))
                    continue;

                if (heatMap[y][x] > bestScore)
                {
                    bestScore = heatMap[y][x];
                    bestCells.clear();
                    bestCells.add(new Position(x, y));
                }
                else if (heatMap[y][x] == bestScore)
                {
                    bestCells.add(new Position(x, y));
                }
            }
        }

        return bestCells;
    }

    private Position chooseRandomCell(List<Position> cells)
    {
        return cells.get(random.nextInt(cells.size()));
    }

    private void startTargetMode(int x, int y)
    {
        targetMode = true;
        directionKnown = false;
        firstHitX = x;
        firstHitY = y;
        lastHitX = x;
        lastHitY = y;
    }

    private boolean isValidPlacement(Board board, int startX, int startY, int dx, int dy, int shipSize)
    {
        for (int i = 0; i < shipSize; i++)
        {
            int x = startX + dx * i;
            int y = startY + dy * i;

            if (!board.isInside(x, y))
                return false;

            if (board.blocksPlacement(x, y))
                return false;
        }

        return true;
    }

    private void printHeatMap(int[][] heatMap)
    {
        System.out.println("\nHEAT MAP:");

        int size = heatMap.length;

        System.out.print("   ");
        for (int i = 0; i < size; i++)
            System.out.printf("%2c ", (char)('A' + i));
        System.out.println();

        for (int y = 0; y < size; y++)
        {
            System.out.printf("%2d ", y);
            for (int x = 0; x < size; x++)
            {
                System.out.printf("%2d ", heatMap[y][x]);
            }
            System.out.println();
        }
    }
}