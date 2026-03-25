public class Game
{
    private Board userBoard;
    private Board computerBoard;
    private ComputerAI computerAI;

    private boolean playerTurn;
    private boolean gameOver;
    private boolean playerWon;

    private static final int BOARD_SIZE = 10;

    public void resetState()
    {
        playerTurn = true;
        gameOver = false;
        playerWon = false;
    }

    private void initializeBoards(GameUI ui)
    {
        userBoard = new Board(BOARD_SIZE);
        computerBoard = new Board(BOARD_SIZE);

        int choice = ui.getShipPlacement();

        if (choice == 1)
            userBoard.placeShipsManually(ui);
        else
            userBoard.placeShipsRandom();

        computerBoard.placeShipsRandom();
    }

    public void run(GameUI ui)
    {
        initializeBoards(ui);

        switch (ui.getDifficulty())
        {
            case EASY -> computerAI = new EasyAI();
            case HARD -> computerAI = new HardAI();
        }

        resetState();

        while (!gameOver)
        {
            if (playerTurn)
                playerTurn(ui);
            else
                computerTurn(ui);
        }

        ui.printUserBoard(userBoard);
        ui.printComputerBoard(computerBoard);

        ui.showGameResult(playerWon);
    }

    private void checkVictory()
    {
        if (userBoard.allShipsSunk())
        {
            gameOver = true;
            playerWon = false;
        } else if (computerBoard.allShipsSunk())
        {
            gameOver = true;
            playerWon = true;
        }
    }

    private void playerTurn(GameUI ui)
    {
        int x, y;
        boolean valid;

        ui.printComputerBoard(computerBoard);

        do
        {
            Position pos = ui.getAttackCoordinates(computerBoard.getSize());
            x = pos.x();
            y = pos.y();

            valid = computerBoard.isValidAttack(x, y);

            if (!valid)
                ui.showInvalidAttack();

        } while (!valid);

        AttackResult result = computerBoard.attack(x, y);

        ui.showPlayerAttackResult(x, y, result);

        if (result == AttackResult.MISS)
            playerTurn = false;

        checkVictory();
    }

    private void computerTurn(GameUI ui)
    {
        AttackResult result;

        do
        {
            result = computerAI.attack(userBoard);

            ui.printUserBoard(userBoard);

            ui.showComputerAttackResult(result);

            checkVictory();

        } while (!gameOver && (result == AttackResult.HIT || result == AttackResult.SUNK));

        if (!gameOver)
            playerTurn = true;
    }
}