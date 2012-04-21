import java.util.ArrayList;
import java.awt.*;

public class King extends Piece
{
    private ArrayList<Location> validMoves;
    private Color color;
    
    private static final int VALUE = 1000000;
      
    public King(Color col)
    {
        color = col;
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
         for( int i = 0; i < Location.FULL_CIRCLE; i += Location.HALF_RIGHT)
         {
             Location possibleLocation = getLocation();
             possibleLocation = possibleLocation.getAdjacentLocation(i); 
             if( gr.isValid(possibleLocation))
             {
                 result.add(possibleLocation);
                 if( gr.get(possibleLocation) instanceof Piece 
                        && gr.get(possibleLocation).getColor().equals(getColor()))
                        {
                            result.remove(possibleLocation);
                        }                     
             }
         }   
         ArrayList<Location> allValidEnemyMoves = getAllAttackZones();
         for ( int i = 0; i < result.size(); i++)
         {
             if( allValidEnemyMoves.contains(result.get(i)) )
             {
                 result.remove(result.get(i)); 
                 i--;
             }
         }
         ArrayList<Location> castleLocs = getCastleLocations(); //If Any
         if( !(castleLocs == null))
         {
            for(int i = 0; i < castleLocs.size(); i++)
            {
                 result.add(castleLocs.get(i));
            }
         }
         return result;
    }
    

    
//     public ArrayList<Location> getAllPossibleMoves()
//     {
//         Grid<Actor> gr = getGrid();
//         ArrayList<Location> result = new ArrayList<Location>();
//         ArrayList<Piece> allPiece = new ArrayList<Piece>();
//         allPiece = getOpposingPiecesOnBoard(getColor());
//         for( int i = 0; i < allPiece.size(); i++)
//         {
//             Piece tempPiece = allPiece.get(i);
//             ArrayList<Location> locs = new ArrayList<Location>();
//             if( tempPiece instanceof Pawn)
//             {
//                 Pawn tempPawn = (Pawn) tempPiece;
//                 locs = tempPawn.getPawnCaptureSquares();
//             }
//             else
//             {
//                 if( tempPiece instanceof King)
//                 {
//                     locs = tempPiece.getValidKingMoves();
//                 }
//                 else
//                 {
//                     locs = tempPiece.getValidMoveLocations();
//                 }
//             }
//             for ( int j = 0; j < locs.size(); j++)
//             {
//                 if( !result.contains(locs.get(j)) )
//                 {
//                     result.add(locs.get(j));
//                 }
//             }
//         }  
//         return result;
//     }
    
    private ArrayList<Location> getCastleLocations()
    {
        ArrayList<Location> result = new ArrayList<Location>();
        Grid<Actor> gr = getGrid();
        if( gr == null)
        {
            return null;
        }
        ArrayList<Location> allValidEnemyMoves = getAllPossibleEnemyMoves();
        if(getColor().equals(Color.WHITE))
        {
            if( !allValidEnemyMoves.contains(getLocation()) )
            {
                if(whtCanCastleK)
                {
                    boolean checker = true;
                    for(int i = getLocation().getCol() + 1; i < gr.getNumCols() - 1; i++)
                    {
                        Location loc = new Location(getLocation().getRow(), i);
                        if( allValidEnemyMoves.contains(loc) || gr.get(loc) instanceof Piece )
                        checker = false;
                    }
                    if( checker ) result.add(WHT_KING_CASTLE_SQUARE);
                }
                if(whtCanCastleQ)
                {
                    boolean checker = true;
                    for(int i = getLocation().getCol() - 1; i > 1; i--)
                    {
                        Location loc = new Location(getLocation().getRow(), i);
                        if( allValidEnemyMoves.contains(loc) || gr.get(loc) instanceof Piece )
                        checker = false;
                    }
                    if( checker ) result.add(WHT_QUEEN_CASTLE_SQUARE);   
                }
            }
        }
        else
        {  
            if( !allValidEnemyMoves.contains(getLocation()) )
            {
                if(blkCanCastleK)
                {
                    boolean checker = true;
                    for(int i = getLocation().getCol() + 1; i < gr.getNumCols() - 1; i++)
                    {
                        Location loc = new Location(getLocation().getRow(), i);
                        if( allValidEnemyMoves.contains(loc) || gr.get(loc) instanceof Piece )
                        checker = false;
                    }
                    if( checker ) result.add(BLK_KING_CASTLE_SQUARE);
                }
                if(blkCanCastleQ)
                {
                    boolean checker = true;
                    for(int i = getLocation().getCol() - 1; i > 1; i--)
                    {
                        Location loc = new Location(getLocation().getRow(), i);
                        if( allValidEnemyMoves.contains(loc) || gr.get(loc) instanceof Piece )
                        checker = false;
                    }
                    if( checker ) result.add(BLK_QUEEN_CASTLE_SQUARE);   
                }
            }
        }
        return result;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public ArrayList<Location> getAttackZones()
    {
         Grid<Actor> gr = getGrid();
         if( gr == null)
         {
             return null;
         }
         ArrayList<Location> result = new ArrayList<Location>();
         for( int i = 0; i < Location.FULL_CIRCLE; i += Location.HALF_RIGHT)
         {
             Location possibleLocation = getLocation();
             possibleLocation = possibleLocation.getAdjacentLocation(i); 
             if( gr.isValid(possibleLocation))
             {
                 result.add(possibleLocation);
                 if( gr.get(possibleLocation) instanceof Piece 
                        && gr.get(possibleLocation).getColor().equals(getColor()))
                        {
                            result.remove(possibleLocation);
                        }                     
             }
         }       
         return result;
    }
    
    public String toString()
    {
        String a = "King";
        a += " " + this.getLocation();
        return a;
    }
}
