import java.util.ArrayList;
import java.awt.*;

public abstract class Piece extends Actor
{

    private static Color currentTurn = Color.WHITE; 
    private static boolean enPassant = false;
    private static Location enPassantSquare = null;
    
    protected static boolean blkCanCastleQ = true;
    protected static boolean blkCanCastleK = true;
    protected static boolean whtCanCastleQ = true;
    protected static boolean whtCanCastleK = true;
    
    public static final Location BLK_QUEEN_CASTLE_SQUARE = new Location(0, 2);
    public static final Location BLK_KING_CASTLE_SQUARE = new Location(0, 6);
    public static final Location WHT_QUEEN_CASTLE_SQUARE = new Location(7, 2);
    public static final Location WHT_KING_CASTLE_SQUARE = new Location(7, 6);
    
    /**
     * Generates an ArrayList containing all the valid move Locations for this piece
     */
    public abstract ArrayList<Location> getValidMoveLocations();  
    public abstract Color getColor();
    
    /**
     * Helper method to check the legality of King Moves.
     */
    public abstract ArrayList<Location> getAttackZones();
    
//     public void endTurn()
//     {
//         if(currentTurn.equals(Color.WHITE))
//         {
//             currentTurn = Color.BLACK;
//         }
//         else
//         {
//             currentTurn = Color.WHITE;
//         }
//     }
      
    public Color getCurrentTurn()
    {
        return currentTurn;
    }
    
    public ArrayList<Piece> getOpposingPiecesOnBoard()
    {
        Grid<Actor> gr = getGrid();
        ArrayList<Piece> result = new ArrayList<Piece>();
        ArrayList<Location> occupiedLoc = gr.getOccupiedLocations();
        for( int i = 0; i < occupiedLoc.size(); i++)
        {
            Piece pce = (Piece) gr.get(occupiedLoc.get(i));
            if( !(pce.getColor().equals(getColor())) )
            {
                result.add(pce);
            }
        }
        return result;
    }
    
    public ArrayList<Piece> getPiecesOnBoard()
    {
        Grid<Actor> gr = getGrid();
        ArrayList<Piece> result = new ArrayList<Piece>();
        ArrayList<Location> occupiedLoc = gr.getOccupiedLocations();
        for( int i = 0; i < occupiedLoc.size(); i++)
        {
            Piece pce = (Piece) gr.get(occupiedLoc.get(i));
            if( pce.getColor().equals(getColor()) )
            {
                result.add(pce);
            }
        }
        return result;
    }
          
    public ArrayList<Piece> getAllPiecesOnBoard()
    {
        Grid<Actor> gr = getGrid();
        ArrayList<Piece> result = new ArrayList<Piece>();
        ArrayList<Location> occupiedLoc = gr.getOccupiedLocations();
        for( int i = 0; i < occupiedLoc.size(); i++)
        {
            Piece pce = (Piece) gr.get(occupiedLoc.get(i));
            result.add(pce);     
        }
        return result;  
    }
    
    /**
     * This list does not contain the move locations of the King.
     */
    public ArrayList<Location> getAllPossibleAllyMoves()
    {
        Grid<Actor> gr = getGrid();
        ArrayList<Location> result = new ArrayList<Location>();
        ArrayList<Piece> allPiece = new ArrayList<Piece>();
        allPiece = getPiecesOnBoard();
        King tempKing = (King) gr.get(getKingLocation()); 
        allPiece.remove(allPiece.indexOf(tempKing));
        for( int i = 0; i < allPiece.size(); i++)
        {
            Piece tempPiece = allPiece.get(i);
            ArrayList<Location> locs = new ArrayList<Location>();
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
                    locs = tempPiece.getValidMoveLocations();
//                 }
//             }
            for ( int j = 0; j < locs.size(); j++)
            {
                if( !result.contains(locs.get(j)) )
                {
                    result.add(locs.get(j));
                }
            }
        }  
        return result;
    }
    
     public ArrayList<Move> getAllPossibleAllyMoves2()
    {
        Grid<Actor> gr = getGrid();
        Location start = new Location();
        ArrayList<Move> result = new ArrayList<Move>();
        ArrayList<Piece> allPiece = new ArrayList<Piece>();
        allPiece = getPiecesOnBoard();
        for( int i = 0; i < allPiece.size(); i++)
        {
            Piece tempPiece = allPiece.get(i);
            start = tempPiece.getLocation();
            ArrayList<Location> locs = new ArrayList<Location>();
            locs = tempPiece.getValidMoveLocations();
            
            for ( int j = 0; j < locs.size(); j++)
            {
                Move tempMove = new Move(start, locs.get(j), tempPiece);
                result.add(tempMove);
            }
        }  
        return result;
    }
    
    public ArrayList<Location> getAllPossibleEnemyMoves()
    {
        ArrayList<Location> result = new ArrayList<Location>();
        ArrayList<Piece> allPiece = new ArrayList<Piece>();
        allPiece = getOpposingPiecesOnBoard();
        for( int i = 0; i < allPiece.size(); i++)
        {
            Piece tempPiece = allPiece.get(i);
            ArrayList<Location> locs = new ArrayList<Location>();
            if( tempPiece instanceof Pawn)
            {
                Pawn tempPawn = (Pawn) tempPiece;
                locs = tempPawn.getPawnCaptureSquares();
            }
            else
            {
                if( tempPiece instanceof King)
                {
                    locs = tempPiece.getValidKingMoves();
                }
                else
                {
                    locs = tempPiece.getValidMoveLocations();
                }
            }
            for ( int j = 0; j < locs.size(); j++)
            {
                if( !result.contains(locs.get(j)) )
                {
                    result.add(locs.get(j));
                }
            }
        }  
        return result;
    }
    
    public ArrayList<Move> getAllPossibleEnemyMoves2()
    {
        ArrayList<Move> result = new ArrayList<Move>();
        Location start = new Location();
        ArrayList<Piece> allPiece = new ArrayList<Piece>();
        allPiece = getOpposingPiecesOnBoard();
        for( int i = 0; i < allPiece.size(); i++)
        {
            Piece tempPiece = allPiece.get(i);
            start = tempPiece.getLocation();
            ArrayList<Location> locs = new ArrayList<Location>();
            locs = tempPiece.getValidMoveLocations();
            for ( int j = 0; j < locs.size(); j++)
            {
                Move tempMove = new Move(start, locs.get(j), tempPiece);
                result.add(tempMove);
            }
        }  
        return result;
    }
    
    
    /**
     * Contains all possible moves that will get the AI out of check.
     */
   public ArrayList<Move> getAllPossibleAllyMoves3(Piece firstChecker, Piece secondChecker){
       Grid<Actor> gr = getGrid();
       Piece king = (Piece) gr.get(getKingLocation());
       ArrayList<Move> result = new ArrayList<Move>();
       //Add king's move into result
       ArrayList<Location> kingsMove = king.getValidMoveLocations();
       Location locOfChecker = firstChecker.getLocation();
       Location start = getKingLocation();
       for( int i = 0; i < kingsMove.size(); i++){
            Move tempMove = new Move(start, kingsMove.get(i), king);
            result.add(tempMove);
       }
       //if double check.
       if(secondChecker != null)
            return result;
       else{
            ArrayList<Move> getDefendedSquares = getAllPossibleAllyMoves2();
            if(firstChecker instanceof Knight || firstChecker instanceof Pawn){
              for(int i = 0; i < getDefendedSquares.size(); i++){
                 Move tempMove = getDefendedSquares.get(i);
                 if(firstChecker instanceof Knight || firstChecker instanceof Pawn){
                    if(tempMove.getEnd().equals(locOfChecker))
                        result.add(tempMove); 
                 }  
              }
              return result;
            }
            else{
                ArrayList<Location> pathOfAttack = new ArrayList<Location>();                    
                Location locOfAttack = firstChecker.getLocation();
                Piece opposingKing = (Piece) gr.get(getColoredKingLocation(Color.BLACK));
                int directionOfAttack = locOfAttack.getDirectionToward(opposingKing.getLocation());
                while( !(gr.get(locOfAttack) instanceof King) )
                {
                    pathOfAttack.add(locOfAttack);
                    locOfAttack = locOfAttack.getAdjacentLocation(directionOfAttack);
                }
                for(int i = 0; i < getDefendedSquares.size(); i++){
                    Move tempMove = getDefendedSquares.get(i);
                    for(int j = 0; j < pathOfAttack.size(); j++){
                        if(tempMove.getEnd().equals(pathOfAttack.get(j)))
                            result.add(tempMove);
                    }
                }
           }
       }
       return result;
   }
    
   
    /**
     * Get the Location of the King with the same Color
     * as the Piece calling the method.
     */
    public Location getKingLocation()
    {
        Grid<Actor> gr = getGrid();
        Location result = null;
        ArrayList<Location> occupiedLoc = gr.getOccupiedLocations();
        for( int i = 0; i < occupiedLoc.size(); i++)
        {
            Piece pce = (Piece) gr.get(occupiedLoc.get(i));
            if( pce.getColor().equals(getColor()) && pce instanceof King)
            {
                result = pce.getLocation();
            }
        }
        return result;
    }
    
    /**
     * Get the location of the King with the opposite Color 
     * as the Piece calling the method.
     */
    public Location getOpposingKingLocation()
    {
        Grid<Actor> gr = getGrid();
        Location result = null;
        ArrayList<Location> occupiedLoc = gr.getOccupiedLocations();
        for( int i = 0; i < occupiedLoc.size(); i++)
        {
            Piece pce = (Piece) gr.get(occupiedLoc.get(i));
            if( !(pce.getColor().equals(getColor())) && pce instanceof King)
            {
                result = pce.getLocation();
            }
        }
        return result;
    }
    
    /**
     * Get the location of the King with a specified color
     */
    public Location getColoredKingLocation(Color col){
        Grid<Actor> gr = getGrid();
        Location result = null;
        ArrayList<Location> occupiedLoc = gr.getOccupiedLocations();
        for( int i = 0; i < occupiedLoc.size(); i++)
        {
            Piece pce = (Piece) gr.get(occupiedLoc.get(i));
            if( pce.getColor().equals(col) && pce instanceof King)
            {
                result = pce.getLocation();
            }
        }
        return result;
        
        }
    
    /**
     * Helper method, used to avoid a recursive definition.
     * This method returns all the valid king moves BEFORE checking
     * for legality. The getValidMoveLocations method in the King class
     * returns all the valid king moves AFTER checking for legality.
     */
    public ArrayList<Location> getValidKingMoves()
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
       
    public void enableEnPassant()
    {
        enPassant = true;
    }
    
    public void disableEnPassant()
    {
        enPassantSquare = null;
        enPassant = false;
    }
     public boolean getEnPassant()
    {
        return enPassant;
    }
    
    public Location getEnPassantSquare()
    {
        return enPassantSquare;
    }
    
    public boolean checkForEnPassant()
    {
        boolean result = false;
        Location loc = getLocation();
        if( getColor().equals(Color.WHITE) 
                && loc.equals(new Location(4, loc.getCol())) )
                    {
                        result = true;
                        enPassantSquare = loc;
                    }
        if( getColor().equals(Color.BLACK) 
                && loc.equals(new Location(3, loc.getCol())) )
                    {
                        result = true;
                        enPassantSquare = loc;
                    }
        if(result == true)      
            enPassantSquare = loc;
        else
            enPassantSquare = null;  
        return result;
    }
    
   
    
    public void disableCastleQueen()
    {
        if( getColor().equals(Color.WHITE) )
        {
            whtCanCastleQ = false;
        }
        if( getColor().equals(Color.BLACK) )
        {
            blkCanCastleQ = false;
        }
    }
    
    public void disableCastleKing()
    {
        if( getColor().equals(Color.WHITE) )
        {
            whtCanCastleK = false;
        }
        if( getColor().equals(Color.BLACK) )
        {
            blkCanCastleK = false;
        }
    }
    
    public void disableCastling()
    {
        if( getColor().equals(Color.WHITE) )
        {
            whtCanCastleQ = false;
            whtCanCastleK = false;
        }
        if( getColor().equals(Color.BLACK) )
        {
            blkCanCastleQ = false;
            blkCanCastleK = false;
        }       
    }
 
    public boolean blkCanCastle()
    {
        return (blkCanCastleK || blkCanCastleQ);
    }
    
    public boolean whtCanCastle()
    {
        return (whtCanCastleQ || whtCanCastleK);
    }
    
    public static ArrayList<Location> getCastlingSquares()
    {
        ArrayList<Location> result = new ArrayList<Location>();
        result.add(WHT_KING_CASTLE_SQUARE);
        result.add(WHT_QUEEN_CASTLE_SQUARE);
        result.add(BLK_KING_CASTLE_SQUARE);
        result.add(BLK_QUEEN_CASTLE_SQUARE);
        return result;
    }
    
    /**
     * Helper method inorder to check for the legality of certain
     * King moves. The code is the same as getAllPossibleEnenyMoves, except
     * with a few modification to fix some intent errors.
     */
    protected ArrayList<Location> getAllAttackZones()
    {
        Grid<Actor> gr = getGrid();
        ArrayList<Location> result = new ArrayList<Location>();
        ArrayList<Piece> allPiece = new ArrayList<Piece>();
        allPiece = getOpposingPiecesOnBoard();
        for( int i = 0; i < allPiece.size(); i++)
        {
            Piece tempPiece = allPiece.get(i);
            ArrayList<Location> locs = new ArrayList<Location>();
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
                    locs = tempPiece.getAttackZones();
//                 }
//             }
            for ( int j = 0; j < locs.size(); j++)
            {
                if( !result.contains(locs.get(j)) )
                {
                    result.add(locs.get(j));
                }
            }
        }  
        return result;
    }
    
    public double getValue(){return 0;}
}
