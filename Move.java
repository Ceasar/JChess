
public class Move implements Comparable<Move>
{   
   private Location start;
   private Location end;
   private Piece piece;
   private double strength;
    
   public Move(Location beggining, Location destination)
   {
       start = beggining;
       end = destination;
   }
   
   public Move(Location beggining, Location destination, Piece p)
   {
       start = beggining;
       end = destination;
       piece = p;
   }
   
   public Location getEnd()
   {
       return end;
   }
    
   public Location getStart()
   {
       return start;
   }
   
   public Piece getPiece()
   {
       return piece;
   }
   
   public double getStrength()
   {
       return strength;
   }
   
   public void setStrength(double a)
   {
       strength = a;
   }
   
   public int compareTo(Move a)
   {
       if (this.getStrength() == a.getStrength())
           return 0;
       else if (this.getStrength() < a.getStrength())
           return 1;
       else if (this.getStrength() > a.getStrength())
           return -1;
       return 0;
   }
   
   public String toString()
   {
       return getPiece() + " " + getEnd();
   }
}
