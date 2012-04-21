import java.util.ArrayList;
import java.awt.*;
import java.util.Collections;

public class AI
{
    Grid<Actor> grid;

    public AI(Grid<Actor> gr)
    {
        grid = gr;
    }
    
    public Move analyze(double initialScore, double alpha, double beta, Piece dummy, boolean isBlacksMove, int depth, Piece firstChecker, Piece secondChecker)
    {
        ArrayList<Move> moves = new ArrayList<Move>();
        if (isBlacksMove == true){
            if(firstChecker != null){
                moves = dummy.getAllPossibleAllyMoves3(firstChecker, secondChecker);
            }
            else
                moves = dummy.getAllPossibleAllyMoves2();
        }
        else
            moves = dummy.getAllPossibleEnemyMoves2();
        
        //SORT
        if (depth > 0)
        {
            for(int i = 0; i < moves.size(); i++)
            {
                Actor tempPiece = null;
                Piece temp = null;
                if (grid.get(moves.get(i).getEnd()) != null)
                    {
                        tempPiece = grid.get(moves.get(i).getEnd());
                        temp = (Piece) tempPiece;
                        if (tempPiece.getColor().equals(Color.BLACK))
                            initialScore -= temp.getValue();
                        else    
                            initialScore += temp.getValue();
                        grid.remove(moves.get(i).getEnd());
                    }
                moves.get(i).getPiece().moveTo(moves.get(i).getEnd());
                
                moves.get(i).setStrength(scorePosition(initialScore, dummy));
                
                moves.get(i).getPiece().moveTo(moves.get(i).getStart());
                if (tempPiece != null)
                    {
                        grid.put(moves.get(i).getEnd(), tempPiece);
                        if (tempPiece.getColor().equals(Color.BLACK))
                            initialScore += temp.getValue();
                        else    
                            initialScore -= temp.getValue();
                    }
            }
            Collections.sort(moves);
            if (!isBlacksMove)
                Collections.reverse(moves);
        }   

        for(int i = 0; i < moves.size(); i++)
        {
                Actor tempPiece = null;
                Piece temp = null;
                if (grid.get(moves.get(i).getEnd()) != null)
                {
                    tempPiece = grid.get(moves.get(i).getEnd());
                    temp = (Piece) tempPiece;
                    if (tempPiece.getColor().equals(Color.BLACK))
                        initialScore -= temp.getValue();
                    else    
                        initialScore += temp.getValue();
                    grid.remove(moves.get(i).getEnd());
                }
                moves.get(i).getPiece().moveTo(moves.get(i).getEnd());
                
                //System.out.println(depth + " " + moves.get(i));
                
                //System.out.println(depth + " " + moves.get(i) + " " + moves.get(i).getStrength() + " a:" + alpha + " b:" + beta + " initial:" + initialScore);
                
                if (i>0)
                    if (isBlacksMove)
                    {
                        if (moves.get(i-1).getStrength() > beta)
                            beta = moves.get(i-1).getStrength();
                    }
                    else
                        if (moves.get(i-1).getStrength() < alpha)
                            alpha = moves.get(i-1).getStrength();
                        
                if (depth > 0 || (temp != null && temp.getValue() <= moves.get(i).getPiece().getValue()))
                    moves.get(i).setStrength(analyze(initialScore, alpha, beta, dummy, !isBlacksMove, depth - 1, firstChecker, secondChecker).getStrength());
                else
                    moves.get(i).setStrength(scorePosition(initialScore, dummy));
                    
                moves.get(i).getPiece().moveTo(moves.get(i).getStart());
                if (tempPiece != null)
                {
                    grid.put(moves.get(i).getEnd(), tempPiece);
                    if (tempPiece.getColor().equals(Color.BLACK))
                        initialScore += temp.getValue();
                    else    
                        initialScore -= temp.getValue();
                }
                
                System.out.println(depth + " " + moves.get(i) + " " + moves.get(i).getStrength());
                //Exit search early
                
                if (isBlacksMove)
                {
                    if (moves.get(i).getStrength() > alpha)
                        break;
                }
                else
                    if (moves.get(i).getStrength() < beta)
                        break;
        }
        Collections.sort(moves);
        if (!isBlacksMove)
            Collections.reverse(moves);
        return moves.get(0);
    }
    
    public double scorePosition(double initialScore, Piece dummy)
    {
        double score = initialScore;
        score += dummy.getAllPossibleAllyMoves2().size() - dummy.getAllPossibleEnemyMoves2().size();
        return score;
    }

    public Move findBestMove(Piece dummy, Piece firstChecker, Piece secondChecker)
    {
        double alpha = 1000;
        double beta = -1000;
        double initialScore = 0;
        
        ArrayList<Piece> allPieces = dummy.getAllPiecesOnBoard();
        for(int j = 0; j < allPieces.size(); j++)
        {
            if (allPieces.get(j).getColor().equals(Color.BLACK))
                initialScore += allPieces.get(j).getValue();
            else    
                initialScore -= allPieces.get(j).getValue();
        }
        
        Move bestMove = analyze(initialScore, alpha, beta, dummy, true, 2, firstChecker, secondChecker);
        System.out.println(bestMove + " " + bestMove.getStrength());
        return bestMove;
    }

}
