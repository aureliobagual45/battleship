import java.util.Random;
import java.util.Scanner;

public class Main
{
    static Scanner scanner = new Scanner(System.in);

    static boolean running = true, gameover = false;
    static String difficulty = "EASY";
    static char[][] grid_user, grid_computer;
    static final int MAX_TRIES = 50;

    public static void main(String[] args)
    {
        grid_user = createGrid(10, 10, '·');
        grid_computer = createGrid(10, 10, '·');

        int input;

        while (running)
        {
            System.out.println("+-------------+");
            System.out.println("| BATTLE SHIP |");
            System.out.println("+-------------+");

            System.out.println("1 - Play");
            System.out.println("2 - Difficulty: " + difficulty);
            System.out.println("3 - Exit");

            input = scanner.nextInt();
            scanner.nextLine();

            switch (input)
            {
                case 1 -> play();

                case 2 -> switchDifficulty();

                case 3 -> running = false;
            }
        }

        scanner.close();
    }

    static void play()
    {
        int[] user_ship = {4, 3, 2, 1};
        int[] pc_ship = {4, 3, 2, 1};
        int input = 0;

        while (true)
        {
            System.out.println("======================");
            System.out.println("Do you want to place:");
            System.out.println("1 - Manually");
            System.out.println("2 - Randomly");

            input = scanner.nextInt();

            if (input == 1 || input == 2)
            {
                break;
            } else
            {
                System.out.println("Invalid value!");
            }
        }

        if (input == 1)
        {
            while (user_ship[0] > 0 || user_ship[1] > 0 || user_ship[2] > 0 || user_ship[3] > 0)
            {
                boolean valid_choice = false;

                System.out.println("======================");
                printGrid(grid_user);
                System.out.println("======================");

                while (!valid_choice)
                {
                    System.out.println("Choose ships placements:");
                    System.out.println("Ships:");
                    System.out.printf("1 - @ x%d | ", user_ship[0]);
                    System.out.printf("2 - @@ x%d | ", user_ship[1]);
                    System.out.printf("3 - @@@ x%d | ", user_ship[2]);
                    System.out.printf("4 - @@@@ x%d\n", user_ship[3]);

                    input = scanner.nextInt();

                    if (input < 1 || input > 4)
                    {
                        System.out.println("Invalid value!");
                        continue;
                    }

                    for (int i = 0; i < 4; i++)
                    {
                        if (input - 1 == i)
                        {
                            if (user_ship[i] <= 0)
                            {
                                System.out.printf("All ships %d have already been placed\n", i + 1);
                            } else
                            {
                                valid_choice = true;
                            }
                        }
                    }
                }

                if (input == 1)
                {
                    user_ship[0]--;

                    placeShipUser(input);
                } else if (input == 2)
                {
                    user_ship[1]--;

                    placeShipUser(input);
                } else if (input == 3)
                {
                    user_ship[2]--;

                    placeShipUser(input);
                } else if (input == 4)
                {
                    user_ship[3]--;

                    placeShipUser(input);
                }

            }
        } else
        {
            while (true)
            {
                grid_user = createGrid(10, 10, '·');

                printGrid(grid_user);

                if (generateShips(grid_user))
                {
                    printGrid(grid_user);
                    break;
                }
            }
        }

        while (true)
        {
            grid_computer = createGrid(10, 10, '·');

            printGrid(grid_computer);

            if (generateShips(grid_computer))
            {
                printGrid(grid_computer);
                break;
            }
        }

        while (!gameover)
        {
            System.out.println("Your turn: ");
            System.out.print("Attack X: ");
            input = scanner.nextInt();
            System.out.print("Attack Y: ");
            input = scanner.nextInt();


        }
    }

    static boolean generateShips(char[][] grid)
    {
        if (!placeShipOfSize(grid, 4, 1)) return false;

        if (!placeShipOfSize(grid, 3, 2)) return false;

        if (!placeShipOfSize(grid, 2, 3)) return false;

        if (!placeShipOfSize(grid, 1, 4)) return false;

        return true;
    }

    static boolean placeShipOfSize(char[][] grid, int length, int amount)
    {
        Random random = new Random();
        for (int i = 0; i < amount; i++)
        {
            boolean placed = false;
            int tries = 0;

            while (!placed && tries < MAX_TRIES)
            {
                tries++;

                ShipPosition pos = new ShipPosition();

                pos.y = random.nextInt(0, 10);
                pos.x = random.nextInt(0, 10);

                if (length > 1)
                {
                    if (random.nextBoolean())
                    {
                        pos.direction = 'H';
                        pos.dir_x = length - 1;
                        pos.dir_y = 0;
                    } else
                    {
                        pos.direction = 'V';
                        pos.dir_x = 0;
                        pos.dir_y = length - 1;
                    }
                }

                if (checkIfCanPlace(length, pos, grid))
                {
                    place(pos, grid);
                    placed = true;
                }
            }
            if (!placed)
            {
                return false;
            }
        }
        return true;
    }

    static void placeShipUser(int input)
    {
        while (true)
        {
            ShipPosition pos = new ShipPosition();

            getPosition(input, pos);

            if (!checkIfCanPlace(input, pos, grid_user))
            {
                System.out.println("Invalid position!");
                continue;
            }

            place(pos, grid_user);

            System.out.println("Ship is in position");
            break;
        }
    }

    static void getPosition(int input, ShipPosition pos)
    {
        if (input != 1)
        {
            while (true)
            {
                System.out.print("Enter the direction (H - Horizontal | V - Vertical): ");
                pos.direction = scanner.next().toUpperCase().charAt(0);
                scanner.nextLine();

                if (pos.direction == 'H' || pos.direction == 'V')
                {
                    if (pos.direction == 'H')
                    {
                        pos.dir_x = (input - 1);
                        pos.dir_y = 0;
                    } else
                    {
                        pos.dir_x = 0;
                        pos.dir_y = (input - 1);
                    }
                    break;
                } else
                {
                    System.out.println("Invalid input!");
                }
            }
        }

        while (true)
        {
            System.out.print("Place the X axis (0 - 9): ");
            pos.x = scanner.nextInt();

            if (pos.x < 0 || pos.x > 9)
            {
                System.out.println("Invalid value!");
            } else
            {
                break;
            }
        }

        while (true)
        {
            System.out.print("Place the Y axis (0 - 9): ");
            pos.y = scanner.nextInt();

            if (pos.y < 0 || pos.y > 9)
            {
                System.out.println("Invalid value!");
            } else
            {
                break;
            }
        }
    }

    static boolean checkIfCanPlace(int input, ShipPosition pos, char[][] grid)
    {
        for (int i = 0; i < input; i++)
        {
            int y_ship = pos.y + (pos.direction == 'V' ? i : 0);
            int x_ship = pos.x + (pos.direction == 'H' ? i : 0);

            if (x_ship < 0 || x_ship > 9 || y_ship < 0 || y_ship > 9)
            {
                return false;
            }

            if (grid[y_ship][x_ship] == '@' || grid[y_ship][x_ship] == '*')
            {
                return false;
            }
        }
        return true;
    }

    static void place(ShipPosition pos, char[][] grid)
    {
        for (int i = -1; i < 2 + pos.dir_y; i++)
        {
            for (int j = -1; j < 2 + pos.dir_x; j++)
            {
                if (pos.y + i >= 0 && pos.y + i < 10 && pos.x + j >= 0 && pos.x + j < 10)
                {
                    grid[pos.y + i][pos.x + j] = '*';
                }
            }
        }
        for (int i = 0; i <= pos.dir_y; i++)
        {
            for (int j = 0; j <= pos.dir_x; j++)
            {
                grid[pos.y + i][pos.x + j] = '@';
            }
        }
    }

    static void printGrid(char[][] grid)
    {
        for (int i = -1; i < grid[0].length; i++)
        {
            if (i < 0)
            {
                System.out.print(" ");
            } else
            {
                System.out.print(" " + i);
            }
        }
        System.out.println();
        for (int i = 0; i < grid.length; i++)
        {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++)
            {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    static char[][] createGrid(int rows, int cols, char fill)
    {
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                grid[i][j] = fill;
            }
        }
        return grid;
    }

    static void switchDifficulty()
    {
        if (difficulty.equals("EASY"))
        {
            difficulty = "HARD";
        } else
        {
            difficulty = "EASY";
        }
    }
}