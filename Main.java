public class Main
{
    static GameUI gameUI = new GameUI();

    static boolean running = true;

    static Board userBoard;
    static Board computerBoard;

    public static void main(String[] args)
    {
        while (running)
        {
            int input = gameUI.startGame();

            switch (input)
            {
                case 1 -> play(gameUI);
                case 2 -> switchDifficulty();
                case 3 -> running = false;
            }
        }
    }

    static void play(GameUI gameUI)
    {
        userBoard = new Board(10);
        computerBoard = new Board(10);

        int choice = gameUI.getShipPlacement();

        if (choice == 1)
        {
            userBoard.placeShipsManually(gameUI);
        } else
        {
            userBoard.placeShipsRandom();
        }

        computerBoard.placeShipsRandom();

        gameUI.printShipPlacement(userBoard, computerBoard);
    }

    static void switchDifficulty()
    {
        gameUI.difficulty = gameUI.difficulty.equals("EASY") ? "HARD" : "EASY";
    }
}
