import java.util.ArrayList;
import java.awt.*;

public class Knight extends Piece
{
    private ArrayList<Location> validMoves;
    private Color color;
    
    private static final int VALUE = 30;
    
    public Knight(Color col)
    {
        color = col;
        validMoves = getValidMoveLocations();
    }
    
   

    /**
     * Generates the Knight's moveset by looping clockwise. First take two steps in
     * a certain direction to create a baseLocation, and then go to the right and left
     * of the baseLocation to check for possible moves.
     */
    public ArrayList<Location> getValidMoveLocations()
    {
       Grid<Actor> gr = getGrid();
       if( gr == null)
       {
            return null;
       }
       ArrayList<Location> result = new ArrayList<Location>();
       Location possibleLocation1, possibleLocation2;
       for( int i = 0; i < Location.FULL_CIRCLE; i+= Location.RIGHT)
       {
           Location baseLocation = getLocation();
           for (int counter = 0; counter < 2; counter++)
           {
               baseLocation = baseLocation.getAdjacentLocation(i);
           }
           possibleLocation1 = baseLocation.getAdjacentLocation(i + Location.LEFT);
           if( gr.isValid(possibleLocation1) )
           {
                result.add(possibleLocation1);
                if( gr.get(possibleLocation1) instanceof Piece 
                        && gr.get(possibleLocation1).getColor().equals(getColor()))
                        {
                            result.remove(possibleLocation1);
                        }
           }    
           possibleLocation2 = baseLocation.getAdjacentLocation(i + Location.RIGHT); 
           if( gr.isValid(possibleLocation2) )
           {     
                result.add(possibleLocation2);
                if( gr.get(possibleLocation2) instanceof Piece 
                        && gr.get(possibleLocation2).getColor().equals(getColor()))
                        {
                            result.remove(possibleLocation2);
                        }
           }
       }  
       return result;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    /**
    * Helper method in order to check for the legality of certain moves
    * made by a King Piece.
    */
   public ArrayList<Location> getAttackZones()
   {
       Grid<Actor> gr = getGrid();
       if( gr == null)
       {
            return null;
       }
       ArrayList<Location> result = new ArrayList<Location>();
       Location possibleLocation1, possibleLocation2;
       for( int i = 0; i < Location.FULL_CIRCLE; i+= Location.RIGHT)
       {
           Location baseLocation = getLocation();
           for (int counter = 0; counter < 2; counter++)
           {
               baseLocation = baseLocation.getAdjacentLocation(i);
           }
           possibleLocation1 = baseLocation.getAdjacentLocation(i + Location.LEFT);
           if( gr.isValid(possibleLocation1) )
           {
                result.add(possibleLocation1);           
           }    
           possibleLocation2 = baseLocation.getAdjacentLocation(i + Location.RIGHT); 
           if( gr.isValid(possibleLocation2) )
           {     
                result.add(possibleLocation2);
           }
       }  
       return result;
      
   }
   
   public double getValue()
    {
        return VALUE;
    }
   
   public String toString()
    {
        String a = "Knight";
        a += " " + this.getLocation();
        return a;
    }
}
