import java.util.Map;
import java.util.Scanner;

public class GameUI
{
    private final Scanner scanner = new Scanner(System.in);

    private String difficulty = "EASY";

    public int startGame()
    {
        System.out.println("+-------------+");
        System.out.println("| BATTLESHIP  |");
        System.out.println("+-------------+");
        System.out.println("1 - Play");
        System.out.println("2 - Difficulty: " + difficulty);
        System.out.println("3 - Exit");

        int input = scanner.nextInt();
        scanner.nextLine();

        return input;
    }

    public int getShipPlacement()
    {
        System.out.println("\nShip placement:");
        System.out.println("1 - Manual");
        System.out.println("2 - Random");

        int choice;
        do
        {
            choice = scanner.nextInt();
        } while (choice != 1 && choice != 2);

        return choice;
    }

    public Ship getShipFromInput(int size)
    {
        int dx = 0, dy = 0;

        char dir;

        System.out.println("\nPlace a ship:");

        do
        {
            System.out.print("Direction (H/V): ");
            dir = scanner.next().toUpperCase().charAt(0);
        } while (dir != 'H' && dir != 'V');

        dx = (dir == 'H') ? 1 : 0;
        dy = (dir == 'V') ? 1 : 0;

        System.out.print("X: ");
        int x = scanner.nextInt();

        System.out.print("Y: ");
        int y = scanner.nextInt();

        return new Ship(x, y, dx, dy);
    }

    public void printShipsLeft(Board board)
    {
        Map<Integer, Integer> ships = board.getShipsCountBySize();

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
        System.out.println("\nComputer is playing...");
        printShipsLeft(user);
        user.print();
    }

    public int[] getAttackCoordinates()
    {
        System.out.println("\nSend your attack:");
        System.out.print("X: ");
        int x = scanner.nextInt();
        System.out.print("Y: ");
        int y = scanner.nextInt();
        return new int[]{x, y};
    }

    public void showPlayerAttackResult(int x, int y, AttackResult result)
    {
        System.out.println("\nYou fired at (" + x + ", " + y + ")");

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

            default:
                break;
        }
    }

    public void showInvalidAttack()
    {
        System.out.println("Invalid attack. Try again.");
    }

    public void toggleDifficulty()
    {
        difficulty = difficulty.equals("EASY") ? "HARD" : "EASY";
    }

    public void showGameResult(boolean playerWon)
    {
        if (playerWon)
            System.out.println("\nYou Won!\n");
        else
            System.out.println("\nYou Lost!\n");
    }
}