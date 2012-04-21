import java.util.ArrayList;
import java.awt.*;

public class Queen extends Piece
{
    private ArrayList<Location> validMoves;
    private Color color;
    
    private static final int VALUE = 90;
    
    public Queen(Color col)
    {
        color = col;
        validMoves = getValidMoveLocations();
    }
    
//     public Queen(Color col, Location loc)
//     {
//         setColor(col);
//         moveTo(loc);
//         validMoves = getValidMoveLocations();
//     }

   public ArrayList<Location> getValidMoveLocations()
   {
       Grid<Actor> gr = getGrid();
       if( gr == null)
       {
           return null;
        }
       ArrayList<Location> result = new ArrayList<Location>();
       for (int i = 0; i < Location.FULL_CIRCLE; i += Location.HALF_RIGHT)
       {
           Location possibleLocation = getLocation();          
           for( int j = 0; j < gr.getNumCols(); j++)
           {
               possibleLocation = possibleLocation.getAdjacentLocation(i); 
               if( gr.isValid(possibleLocation))
               {
                    if( gr.get(possibleLocation) instanceof Piece)
                    {
                        if(!(gr.get(possibleLocation).getColor().equals(getColor())))
                        {
                            result.add(possibleLocation);
                        }
                        j+=gr.getNumCols();
                    }
                    else
                    {
                        result.add(possibleLocation);                       
                    }
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
       for (int i = 0; i < Location.FULL_CIRCLE; i += Location.HALF_RIGHT)
       {
           Location possibleLocation = getLocation();          
           for( int j = 0; j < gr.getNumCols(); j++)
           {
               possibleLocation = possibleLocation.getAdjacentLocation(i); 
               if( gr.isValid(possibleLocation))
               {
                    if( (gr.get(possibleLocation) instanceof Piece) && !( gr.get(possibleLocation) instanceof King) )
                    {
//                         if(!(gr.get(possibleLocation).getColor().equals(getColor())))
//                         {
                            result.add(possibleLocation);
//                         }
                        j+=gr.getNumCols();
                    }
                    else
                    {
                        result.add(possibleLocation);                       
                    }
               }
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
        String a = "Queen";
        a += " " + this.getLocation();
        return a;
    }
}
