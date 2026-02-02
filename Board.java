import java.util.Random;

public class Board
{
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
        Random random = new Random();

        while (true)
        {
            clear();
            boolean failed = false;

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
                {
                    failed = true;
                    break;
                }
            }

            if (!failed) return;
        }
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
            }
            else
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

            for (int ay = -1; ay <= 1; ay++)
                for (int ax = -1; ax <= 1; ax++)
                    if (inside(x + ax, y + ay) && grid[y + ay][x + ax] == '@')
                        return false;
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
            default  -> '·';
        };
    }
}