import java.util.Map;
import java.util.Scanner;

public class GameUI
{
    private final Scanner scanner = new Scanner(System.in);

    private Difficulty difficulty = Difficulty.EASY;

    /// INPUT

    public int startGame()
    {
        System.out.println("+-------------+");
        System.out.println("| BATTLESHIP  |");
        System.out.println("+-------------+");
        System.out.println("1 - Play");
        System.out.println("2 - Difficulty: " + difficulty);
        System.out.println("3 - Exit");

        while (true)
        {
            if (scanner.hasNextInt())
            {
                int input = scanner.nextInt();
                scanner.nextLine();

                if (input >= 1 && input <= 3)
                    return input;
            }
            else
            {
                scanner.nextLine();
            }

            showInvalidOption();
        }
    }

    public int getShipPlacement()
    {
        System.out.println("\nShip placement:");
        System.out.println("1 - Manual");
        System.out.println("2 - Random");

        while (true)
        {
            if (scanner.hasNextInt())
            {
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1 || choice == 2)
                    return choice;
            }
            else
            {
                scanner.nextLine();
            }

            showInvalidOption();
        }
    }

    public ShipPosition getShipFromInput(int size)
    {
        System.out.println("\nPlace a ship:");

        char dir;
        while (true)
        {
            System.out.print("Direction (H/V): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.length() == 1 && (input.charAt(0) == 'H' || input.charAt(0) == 'V'))
            {
                dir = input.charAt(0);
                break;
            }

            showInvalidOption();
        }

        int dx = (dir == 'H') ? 1 : 0;
        int dy = (dir == 'V') ? 1 : 0;

        Position pos = readPosition("Starting position:", size);

        return new ShipPosition(pos.x(), pos.y(), dx, dy);
    }

    private Position readPosition(String prompt, int size)
    {
        String hint = "Use A0 to " + (char) ('A' + size - 1) + (size - 1);

        while (true)
        {
            System.out.println(prompt);

            String input = scanner.nextLine().trim().toUpperCase();
            Position pos = parsePosition(input, size);

            if (pos != null)
                return pos;

            System.out.println("Invalid coordinates! " + hint);
        }
    }

    private Position parsePosition(String input, int size)
    {
        if (input.length() < 2)
            return null;

        char letter = input.charAt(0);
        String numberPart = input.substring(1);

        if (!Character.isLetter(letter))
            return null;

        for (char c : numberPart.toCharArray())
        {
            if (!Character.isDigit(c))
                return null;
        }

        int x = letter - 'A';
        int y;

        try
        {
            y = Integer.parseInt(numberPart);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        if (x < 0 || x >= size || y < 0 || y >= size)
            return null;

        return new Position(x, y);
    }

    public Position getAttackCoordinates(int size)
    {
        return readPosition("\nSend your attack:", size);
    }

    /// OUTPUT

    public void printShipsLeft(Board board)
    {
        Map<Integer, Integer> ships = board.getShipCountsBySize();

        System.out.print("Ships left: ");

        int[] sizes = {4, 3, 2, 1};

        for (int i = 0; i < sizes.length; i++)
        {
            int size = sizes[i];
            int amount = ships.getOrDefault(size, 0);

            System.out.print("X".repeat(size) + " = " + amount);

            if (i < sizes.length - 1)
                System.out.print(" | ");
        }

        System.out.println();
    }

    public void printComputerBoard(Board computer)
    {
        System.out.println("\nComputer board (hidden):");
        printShipsLeft(computer);
        computer.printHidden();
    }

    public void printUserBoard(Board user)
    {
        showComputerTurn();
        printShipsLeft(user);
        user.print();
    }

    public void showPlayerAttackResult(int x, int y, AttackResult result)
    {
        System.out.println("\nYou fired at " + (char)('A' + x) + y);

        switch (result)
        {
            case HIT:
                System.out.println("HIT!");
                System.out.println("You play again!");
                break;

            case SUNK:
                System.out.println("You've SUNK a ship!");
                System.out.println("You play again!");
                break;

            case MISS:
                System.out.println("You MISSED!");
                break;
        }
    }

    public void showComputerAttackResult(AttackResult result)
    {
        switch (result)
        {
            case HIT:
                System.out.println("Computer HIT your ship!");
                System.out.println("Computer plays again...");
                break;

            case SUNK:
                System.out.println("Computer SUNK one of your ships!");
                System.out.println("Computer plays again...");
                break;

            case MISS:
                System.out.println("Computer MISSED!");
                break;
        }
    }

    public void showGameResult(boolean playerWon)
    {
        if (playerWon)
            System.out.println("\nYou Won!\n");
        else
            System.out.println("\nYou Lost!\n");
    }

    public void showInvalidAttack()
    {
        System.out.println("Invalid attack. Try again.");
    }

    public void showInvalidPosition()
    {
        System.out.println("Invalid position. Try again.");
    }

    public void showComputerTurn()
    {
        System.out.println("\nComputer is playing...");
    }

    public void showInvalidOption()
    {
        System.out.println("Invalid option. Try again.");
    }

    /// HELPER

    public void toggleDifficulty()
    {
        difficulty = (difficulty == Difficulty.EASY) ? Difficulty.HARD : Difficulty.EASY;
    }

    public Difficulty getDifficulty()
    {
        return difficulty;
    }
}