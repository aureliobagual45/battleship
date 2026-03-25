public class Main
{
    public static void main(String[] args)
    {
        boolean running = true;
        GameUI ui = new GameUI();
        Game game = new Game();

        while (running)
        {
            int input = ui.startGame();

            switch (input)
            {
                case 1 -> game.run(ui);
                case 2 -> ui.toggleDifficulty();
                case 3 -> running = false;
                default -> ui.showInvalidOption();
            }
        }
    }
}