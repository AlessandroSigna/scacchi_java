 
public class Coord
{

    private int x;
    private int y;

    public Coord(int x, int y)
    {
        setPosition(x, y);
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public boolean equals(Coord b)
    {
        if(b.getX() == x && b.getY() == y)
            return true;
        return false;
    }
}