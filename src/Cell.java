public enum Cell
{
    EMPTY,
    SHIP,
    HIT,
    MISS,
    BLOCKED;

    public char toChar()
    {
        return switch (this)
        {
            case SHIP -> '@';
            case HIT -> 'X';
            case MISS -> 'O';
            case BLOCKED -> '*';
            case EMPTY -> '·';
        };
    }
}
