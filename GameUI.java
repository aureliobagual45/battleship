import java.util.Scanner;

public class GameUI
{
    private final Scanner scanner = new Scanner(System.in);

    public String difficulty = "EASY";

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
        System.out.println("Ship placement:");
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

        if (size > 1)
        {
            char dir;
            do
            {
                System.out.print("Direction (H/V): ");
                dir = scanner.next().toUpperCase().charAt(0);
            } while (dir != 'H' && dir != 'V');

            dx = (dir == 'H') ? 1 : 0;
            dy = (dir == 'V') ? 1 : 0;
        }

        System.out.print("X: ");
        int x = scanner.nextInt();

        System.out.print("Y: ");
        int y = scanner.nextInt();

        return new Ship(x, y, dx, dy);
    }

    public void printShipPlacement(Board user, Board computer)
    {
        System.out.println("\nYour board:");
        user.print();

        System.out.println("\nComputer board (hidden):");
        computer.printHidden();
    }
}
