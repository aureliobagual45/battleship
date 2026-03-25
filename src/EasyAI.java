public class EasyAI extends BaseAI
{
    @Override
    protected AttackResult search(Board board)
    {
        int size = board.getSize();

        while (true)
        {
            int x = random.nextInt(size);
            int y = random.nextInt(size);

            AttackResult result = board.attack(x, y);

            if (result == AttackResult.ALREADY_TRIED)
                continue;

            if (result == AttackResult.HIT)
            {
                targetMode = true;
                firstHitX = x;
                firstHitY = y;
                lastHitX = x;
                lastHitY = y;
            }

            return result;
        }
    }
}