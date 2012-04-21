import java.util.ArrayList;
import java.awt.*;

public class Pawn extends Piece
{
    private int direction;
    private Color color;
    
    private static final double VALUE = 10;
    
    private ArrayList<Location> validMoves;
    private ArrayList<Location> captureZones;

    public Pawn(Color col)
    {
        color = col;
        if( getColor().equals(Color.WHITE))     
         { direction = Location.NORTH; }  
         else
         { direction = Location.SOUTH; }
        validMoves = getValidMoveLocations();
    }

    public ArrayList<Location> getValidMoveLocations()
    {
       Grid<Actor> gr = getGrid();
       if( gr == null)
       {
           return null;
       }
       ArrayList<Location> result = new ArrayList<Location>();
       Location possibleLocation = getLocation();
       possibleLocation = possibleLocation.getAdjacentLocation(direction); 
       if( gr.isValid(possibleLocation))
       {
            result.add(possibleLocation);
            if( gr.get(possibleLocation) instanceof Piece )
            {
                 result.remove(possibleLocation);
            }
       }
       if( !(result.isEmpty()) )
       {
            if( (direction == Location.NORTH && getLocation().getRow() == 6)
                  || (direction == Location.SOUTH && getLocation().getRow() == 1))
            {
                possibleLocation = possibleLocation.getAdjacentLocation(direction);
                if( gr.isValid(possibleLocation))
                {
                    result.add(possibleLocation);
                    if( gr.get(possibleLocation) instanceof Piece )
                    {
                        result.remove(possibleLocation);
                    }
                } 
            }
       }
       int[] captureDirections = new int[2];
       captureDirections[0] = direction + 45;
       captureDirections[1] = direction + 315;
       for( int i = 0; i < captureDirections.length; i++)
       {
           possibleLocation = getLocation().getAdjacentLocation(captureDirections[i]);
           if( gr.isValid(possibleLocation))
           {
               if( (gr.get(possibleLocation) instanceof Piece) && !(gr.get(possibleLocation).getColor().equals(getColor())))
               result.add(possibleLocation);
           }
       }
//        if( getLocation().getAdjacentLocation(Location.RIGHT).equals(getEnPassantSquare()) 
//             || getLocation().getAdjacentLocation(Location.LEFT).equals(getEnPassantSquare()) )
//             {
//                 if( getEnPassant() && !(gr.get(getEnPassantSquare()).getColor().equals(getColor())) )
//                 {
//                     Location enPassantLocation = null;   
//                     int newDir = 0;
//                     if( getColor().equals(Color.WHITE))
//                     {
//                         newDir = Location.HALF_RIGHT;
//                     }
//                     else
//                     {
//                         newDir = Location.HALF_LEFT;
//                     }
//                     if ( getLocation().getDirectionToward(getEnPassantSquare()) == Location.RIGHT)
//                         enPassantLocation = getLocation().getAdjacentLocation(direction + newDir);
//                     else
//                         enPassantLocation = getLocation().getAdjacentLocation(direction - newDir);
//                         
//                     result.add(enPassantLocation);
//                 }          
//             }       
       return result;
     }
     
     public ArrayList<Location> getPawnCaptureSquares()
     {
         Grid<Actor> gr = getGrid();
         if( gr == null)
         {
            return null;
         }
         ArrayList<Location> result = new ArrayList<Location>();
         Location possibleLocation = getLocation();
         result.add(possibleLocation.getAdjacentLocation(direction + Location.HALF_RIGHT));
         result.add(possibleLocation.getAdjacentLocation(direction + Location.HALF_LEFT));
         return result;
     }
     
    public Color getColor()
    {
        return color;
    }
    
    public int getPawnDirection()
    {
        return direction;
    }
    
    public ArrayList<Location> getAttackZones()
    {
        Grid<Actor> gr = getGrid();
         if( gr == null)
         {
            return null;
         }
         ArrayList<Location> result = new ArrayList<Location>();
         Location possibleLocation = getLocation();
         result.add(possibleLocation.getAdjacentLocation(direction + Location.HALF_RIGHT));
         result.add(possibleLocation.getAdjacentLocation(direction + Location.HALF_LEFT));
         return result;
    }
    
    public double getValue()
    {
        return VALUE;
    }    
    
    public String toString()
    {
        String a = "Pawn";
        a += " " + this.getLocation();
        return a;
    }
}