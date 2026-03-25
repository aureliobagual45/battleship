import java.util.Random;

public abstract class BaseAI implements ComputerAI
{
    protected final Random random = new Random();
    protected boolean targetMode = false;
    protected boolean directionKnown = false;

    protected int firstHitX;
    protected int firstHitY;

    protected int lastHitX;
    protected int lastHitY;

    protected int directionX;
    protected int directionY;

    protected static final int[][] DIRECTIONS = {
            {0, -1},
            {0, 1},
            {1, 0},
            {-1, 0}
    };

    @Override
    public AttackResult attack(Board board)
    {
        AttackResult result;

        if (!targetMode)
        {
            result = search(board);
        } else if (!directionKnown)
        {
            result = probeDirection(board);
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

    protected abstract AttackResult search(Board board);

    protected AttackResult probeDirection(Board board)
    {
        boolean[] tried = new boolean[4];
        int attempts = 0;

        while (attempts < 4)
        {
            int directionIndex = random.nextInt(4);

            if (tried[directionIndex])
                continue;

            tried[directionIndex] = true;
            attempts++;

            int dx = DIRECTIONS[directionIndex][0];
            int dy = DIRECTIONS[directionIndex][1];

            int newX = firstHitX + dx;
            int newY = firstHitY + dy;

            if (!board.isInside(newX, newY))
                continue;

            AttackResult result = board.attack(newX, newY);

            if (result == AttackResult.ALREADY_TRIED)
                continue;

            if (result == AttackResult.HIT)
            {
                directionKnown = true;
                directionX = dx;
                directionY = dy;
                lastHitX = newX;
                lastHitY = newY;

                chooseInitialSide();
            }

            return result;
        }

        targetMode = false;

        return AttackResult.MISS;
    }

    protected AttackResult followDirection(Board board)
    {
        int attempts = 0;

        while (attempts < 2)
        {
            int newX = lastHitX + directionX;
            int newY = lastHitY + directionY;

            if (!board.isInside(newX, newY))
            {
                switchSide(board);
                attempts++;
                continue;
            }

            AttackResult result = board.attack(newX, newY);

            if (result == AttackResult.ALREADY_TRIED)
            {
                switchSide(board);
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
                switchSide(board);
                return result;
            }

            return result;
        }

        targetMode = false;
        directionKnown = false;
        return AttackResult.MISS;
    }

    protected void switchSide(Board board)
    {
        directionX *= -1;
        directionY *= -1;

        lastHitX = firstHitX;
        lastHitY = firstHitY;

        while (true)
        {
            int nextX = lastHitX + directionX;
            int nextY = lastHitY + directionY;

            if (!board.isInside(nextX, nextY))
                break;

            if (board.getCell(nextX, nextY) != Cell.HIT)
                break;

            lastHitX = nextX;
            lastHitY = nextY;
        }
    }

    protected void chooseInitialSide()
    {
        if (random.nextBoolean())
        {
            directionX *= -1;
            directionY *= -1;
            lastHitX = firstHitX;
            lastHitY = firstHitY;
        }
    }

    protected void resetState()
    {
        targetMode = false;
        directionKnown = false;

        firstHitX = 0;
        firstHitY = 0;
        lastHitX = 0;
        lastHitY = 0;
        directionX = 0;
        directionY = 0;
    }
}