import java.util.Random;

public class Game
{
    private Board userBoard;
    private Board computerBoard;
    private final ComputerAI computerAI = new ComputerAI();

    private boolean playerTurn;
    private boolean gameOver;
    private boolean playerWon;

    public void setup(Board user, Board computer)
    {
        this.userBoard = user;
        this.computerBoard = computer;

        playerTurn = true;
        gameOver = false;
        playerWon = false;
    }

    private void setupBoards(GameUI ui)
    {
        userBoard = new Board(10);
        computerBoard = new Board(10);

        int choice = ui.getShipPlacement();

        if (choice == 1)
            userBoard.placeShipsManually(ui);
        else
            userBoard.placeShipsRandom();

        computerBoard.placeShipsRandom();
    }

    public void run(GameUI ui)
    {
        setupBoards(ui);

        setup(userBoard, computerBoard);

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
            int[] pos = ui.getAttackCoordinates(computerBoard.getSize());
            x = pos[0];
            y = pos[1];

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

            checkVictory();

        } while (!gameOver && (result == AttackResult.HIT || result == AttackResult.SUNK));

        playerTurn = true;
    }
}