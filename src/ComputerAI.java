import java.util.Random;

public class ComputerAI
{
    Random random = new Random();
    boolean huntingMode = false;
    boolean directionDefined = false;

    private int firstHitX;
    private int firstHitY;

    private int lastHitX;
    private int lastHitY;

    private int directionX;
    private int directionY;

    private int directionIndex = 0;

    private final int[][] directions = {
            {0, -1},
            {0, 1},
            {1, 0},
            {-1, 0}
    };

    public AttackResult attack(Board board)
    {
        AttackResult result;

        if (!huntingMode)
        {
            result = hunt(board);
        } else if (!directionDefined)
        {
            result = searchDirection(board);
        } else
        {
            result = followDirection(board);
        }

        if (result == AttackResult.SUNK)
        {
            resetState();
        }

        return result;
    }

    private AttackResult hunt(Board board)
    {
        int x = 0, y = 0;
        AttackResult result = null;

        do
        {
            x = random.nextInt(10);
            y = random.nextInt(10);
            result = board.attack(x, y);
        }
        while (result == AttackResult.ALREADY_TRIED);

        if (result == AttackResult.HIT)
        {
            huntingMode = true;
            firstHitX = x;
            firstHitY = y;
            lastHitX = x;
            lastHitY = y;
            directionIndex = 0;
        }

        return result;
    }

    private AttackResult searchDirection(Board board)
    {
        boolean[] tried = new boolean[4];
        int attempts = 0;

        while (attempts < 4)
        {
            directionIndex = random.nextInt(4);

            if (tried[directionIndex])
                continue;

            tried[directionIndex] = true;
            attempts++;

            int dx = directions[directionIndex][0];
            int dy = directions[directionIndex][1];

            int newX = firstHitX + dx;
            int newY = firstHitY + dy;

            if (!isValid(newX, newY))
                continue;

            AttackResult result = board.attack(newX, newY);

            if (result == AttackResult.ALREADY_TRIED)
                continue;

            if (result == AttackResult.HIT)
            {
                directionDefined = true;
                directionX = dx;
                directionY = dy;
                lastHitX = newX;
                lastHitY = newY;
            }

            return result;
        }

        huntingMode = false;

        return AttackResult.MISS;
    }

    private AttackResult followDirection(Board board)
    {
        int attempts = 0;
        while (attempts < 2)
        {
            int newX = lastHitX + directionX;
            int newY = lastHitY + directionY;

            if (!isValid(newX, newY))
            {
                chooseRandomSide();
                attempts++;
                continue;
            }

            AttackResult result = board.attack(newX, newY);

            if (result == AttackResult.ALREADY_TRIED)
            {
                chooseRandomSide();
                attempts++;
                continue;
            }

            if (result == AttackResult.HIT)
            {
                lastHitX = newX;
                lastHitY = newY;
                return result;
            }

            if (result == AttackResult.MISS)
            {
                chooseRandomSide();
                return result;
            }

            return result;
        }

        huntingMode = false;
        directionDefined = false;
        return AttackResult.MISS;
    }

    private void chooseRandomSide()
    {
        if (random.nextBoolean())
        {
            directionX *= -1;
            directionY *= -1;
        }

        lastHitX = firstHitX;
        lastHitY = firstHitY;
    }

    private boolean isValid(int x, int y)
    {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    private void resetState()
    {
        huntingMode = false;
        directionDefined = false;
        directionIndex = 0;
    }
}