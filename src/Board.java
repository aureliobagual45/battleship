import java.util.*;

public class Board
{
    private List<Integer> shipsAlive;
    private final int size;
    private final char[][] grid;
    private static final int MAX_TRIES = 50;
    private static final int[] SHIPS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    public Board(int size)
    {
        this.size = size;
        this.grid = new char[size][size];
        clear();
    }

    public void clear()
    {
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                grid[y][x] = '·';
    }

    /// PLACEMENT

    public void placeShipsManually(GameUI gameUI)
    {
        for (int shipSize : SHIPS)
        {
            while (true)
            {
                Ship pos = gameUI.getShipFromInput(shipSize);

                if (canPlace(shipSize, pos))
                {
                    place(shipSize, pos);
                    print();
                    break;
                }
                System.out.println("Invalid position, try again.");
            }
        }
    }

    public void placeShipsRandom()
    {
        shipsAlive = new ArrayList<>();

        for (int size : SHIPS)
            shipsAlive.add(size);

        Random random = new Random();

        while (!tryPlaceAllShips(random))
        {
            clear();
        }
    }

    private boolean tryPlaceAllShips(Random random)
    {
        for (int shipSize : SHIPS)
        {
            boolean placed = false;

            for (int tries = 0; tries < MAX_TRIES; tries++)
            {
                Ship pos = randomShip(random, shipSize);

                if (canPlace(shipSize, pos))
                {
                    place(shipSize, pos);
                    placed = true;
                    break;
                }
            }

            if (!placed)
                return false;
        }

        return true;
    }

    /// LOGIC

    private Ship randomShip(Random r, int size)
    {
        int x = r.nextInt(this.size);
        int y = r.nextInt(this.size);

        int dx = 0, dy = 0;

        if (size > 1)
        {
            if (r.nextBoolean())
            {
                dx = 1;
                dy = 0;
            } else
            {
                dx = 0;
                dy = 1;
            }
        }

        return new Ship(x, y, dx, dy);
    }

    private boolean canPlace(int size, Ship pos)
    {
        for (int i = 0; i < size; i++)
        {
            int x = pos.x + pos.dx * i;
            int y = pos.y + pos.dy * i;

            if (!inside(x, y)) return false;

            if (grid[y][x] != '·') return false;
        }
        return true;
    }

    private void place(int size, Ship pos)
    {
        for (int i = 0; i < size; i++)
        {
            int cx = pos.x + pos.dx * i;
            int cy = pos.y + pos.dy * i;

            for (int ay = -1; ay <= 1; ay++)
            {
                for (int ax = -1; ax <= 1; ax++)
                {
                    int x = cx + ax;
                    int y = cy + ay;

                    if (inside(x, y))
                        grid[y][x] = '*';
                }
            }
        }

        for (int i = 0; i < size; i++)
        {
            int x = pos.x + pos.dx * i;
            int y = pos.y + pos.dy * i;
            grid[y][x] = '@';
        }
    }

    private boolean inside(int x, int y)
    {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    /// TURNS

    public AttackResult attack(int x, int y)
    {
        if (!inside(x, y))
            return AttackResult.ALREADY_TRIED;

        return switch (grid[y][x])
        {
            case '@' ->
            {
                grid[y][x] = 'X';

                if (!hasAlivePart(x, y))
                {
                    sink(x, y);
                    yield AttackResult.SUNK;
                }

                yield AttackResult.HIT;
            }
            case '·', '*' ->
            {
                grid[y][x] = 'O';
                yield AttackResult.MISS;
            }
            default -> AttackResult.ALREADY_TRIED;
        };
    }

    public boolean isValidAttack(int x, int y)
    {
        if (!inside(x, y))
            return false;

        char cell = grid[y][x];
        return cell != 'X' && cell != 'O';
    }

    private void sink(int x, int y)
    {
        int size = 1;

        markAround(x, y);

        for (int dx = -1; dx <= 1; dx += 2)
        {
            int cx = x + dx;
            while (inside(cx, y) && grid[y][cx] == 'X')
            {
                markAround(cx, y);
                cx += dx;
                size++;
            }
        }

        for (int dy = -1; dy <= 1; dy += 2)
        {
            int cy = y + dy;
            while (inside(x, cy) && grid[cy][x] == 'X')
            {
                markAround(x, cy);
                cy += dy;
                size++;
            }
        }

        shipsAlive.remove(Integer.valueOf(size));
    }

    private void markAround(int x, int y)
    {
        for (int ay = -1; ay <= 1; ay++)
            for (int ax = -1; ax <= 1; ax++)
            {
                int nx = x + ax;
                int ny = y + ay;

                if (inside(nx, ny) && grid[ny][nx] == '*')
                    grid[ny][nx] = 'O';
            }
    }

    private boolean hasAlivePart(int x, int y)
    {
        for (int dx = -1; dx <= 1; dx += 2)
        {
            int cx = x + dx;
            while (inside(cx, y))
            {
                if (grid[y][cx] == '@') return true;
                if (grid[y][cx] != 'X') break;
                cx += dx;
            }
        }

        for (int dy = -1; dy <= 1; dy += 2)
        {
            int cy = y + dy;
            while (inside(x, cy))
            {
                if (grid[cy][x] == '@') return true;
                if (grid[cy][x] != 'X') break;
                cy += dy;
            }
        }

        return false;
    }

    public boolean allShipsSunk()
    {
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                if (grid[j][i] == '@')
                    return false;
            }
        }

        return true;
    }

    public Map<Integer, Integer> getShipsCountBySize()
    {
        Map<Integer, Integer> count = new HashMap<>();

        for (int size : shipsAlive)
            count.put(size, count.getOrDefault(size, 0) + 1);

        return count;
    }

    /// PRINT

    public void print()
    {
        System.out.print("  ");

        for (int i = 0; i < size; i++)
            System.out.print(i + " ");

        System.out.println();

        for (int y = 0; y < size; y++)
        {
            System.out.print(y + " ");
            for (int x = 0; x < size; x++)
                System.out.print(grid[y][x] + " ");
            System.out.println();
        }
    }

    public void printHidden()
    {
        System.out.print("  ");

        for (int i = 0; i < size; i++)
            System.out.print(i + " ");

        System.out.println();

        for (int y = 0; y < size; y++)
        {
            System.out.print(y + " ");

            for (int x = 0; x < size; x++)
                System.out.print(visibleCell(x, y) + " ");

            System.out.println();
        }
    }

    private char visibleCell(int x, int y)
    {
        return switch (grid[y][x])
        {
            case 'X' -> 'X';
            case 'O' -> 'O';
            default -> '·';
        };
    }
}