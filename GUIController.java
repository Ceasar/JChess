/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2002-2006 College Entrance Examination Board 
 * (http://www.collegeboard.com).
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Julie Zelenski
 * @author Cay Horstmann
 */

 

import java.awt.*;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;

/**
 * The GUIController controls the behavior in a WorldFrame. <br />
 * This code is not tested on the AP CS A and AB exams. It contains GUI
 * implementation details that are not intended to be understood by AP CS
 * students.
 */

public class GUIController<T>
{
    public static final int INDEFINITE = 0, FIXED_STEPS = 1, PROMPT_STEPS = 2;

    private static final int MIN_DELAY_MSECS = 10, MAX_DELAY_MSECS = 1000;
    private static final int INITIAL_DELAY = MIN_DELAY_MSECS
            + (MAX_DELAY_MSECS - MIN_DELAY_MSECS) / 2;

    private Timer timer;
    private JButton stepButton, runButton, stopButton;
    private JComponent controlPanel;
    private GridPanel display;
    private WorldFrame<T> parentFrame;
    private int numStepsToRun, numStepsSoFar;
    private ResourceBundle resources;
    private DisplayMap displayMap;
    private boolean running;
    private Set<Class> occupantClasses;
    
    private JPopupMenu promotionMenu;
    private boolean promotionMenuIsUp = false;
    private boolean justPromotedPawn = false;
    private boolean justMoved;
    private Piece currentPiece;
    private Piece currentChecker;
    private Piece secondChecker;
    private ArrayList<Location> currentMoves;
    private static boolean gameInSession = true;
    private AI ai;


    /**
     * Creates a new controller tied to the specified display and gui
     * frame.
     * @param parent the frame for the world window
     * @param disp the panel that displays the grid
     * @param displayMap the map for occupant displays
     * @param res the resource bundle for message display
     */
    public GUIController(WorldFrame<T> parent, GridPanel disp,
            DisplayMap displayMap, ResourceBundle res)
    {
        currentPiece = null;
        resources = res;
        display = disp;
        parentFrame = parent;
        this.displayMap = displayMap;
        makeControls();

        occupantClasses = new TreeSet<Class>(new Comparator<Class>()
        {
            public int compare(Class a, Class b)
            {
                return a.getName().compareTo(b.getName());
            }
        });

        World<T> world = parentFrame.getWorld();
        Grid<T> gr = world.getGrid();
        /**
         * Uncomment this when you're done.
         */
         ai = new AI((Grid<Actor>) gr);
        
        for (Location loc : gr.getOccupiedLocations())
            addOccupant(gr.get(loc));
        for (String name : world.getOccupantClasses())
            try
            {
                occupantClasses.add(Class.forName(name));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        timer = new Timer(INITIAL_DELAY, new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                step();
            }
        });

        display.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent evt)
            {
                Grid<T> gr = parentFrame.getWorld().getGrid();
                Location loc = display.locationForPoint(evt.getPoint());
                if (loc != null && gr.isValid(loc) && !isRunning())
                {
                    display.setCurrentLocation(loc);
                    locationClicked();
                }
            }
        });
        stop();
    }

    public void step() {}

    private void addOccupant(T occupant)
    {
        Class cl = occupant.getClass();
        do
        {
            if ((cl.getModifiers() & Modifier.ABSTRACT) == 0)
                occupantClasses.add(cl);
            cl = cl.getSuperclass();
        }
        while (cl != Object.class);
    }

    public void run() {}

    public void stop(){}

    public boolean isRunning()
    {
        return running;
    }

    private void makeControls() {}

    /**
     * Returns the panel containing the controls.
     * @return the control panel
     */
    public JComponent controlPanel()
    {
        return controlPanel;
    }

    /**
     * Callback on mousePressed when editing a grid.
     */
    private void locationClicked()
    {
        World<T> world = parentFrame.getWorld();
        Location loc = display.getCurrentLocation();
        if (loc != null && !world.locationClicked(loc) && !promotionMenuIsUp)
            editLocation();
            else
            {
                Point p = display.pointForLocation(display.getCurrentLocation());
                promotionMenu.show(display, p.x, p.y);
                currentPiece = MenuMaker.getPromotedPiece();
                if( currentPiece != null && !(currentPiece instanceof Pawn) )
                {
                    promotionMenuIsUp = false;
                    currentMoves = currentPiece.getValidMoveLocations();
                    if(isInCheck(true))
                    {
                        displayCheck();
                        if(checkMate())
                        {
                             displayCheckMate();
                             gameInSession = false;
                        }     
                    }
                    setCurrentStateNull();
                }
            }
        parentFrame.repaint();
    }

    /**
     * Edits the contents of the current location, by displaying the constructor
     * or method menu.
     * COMMENTED SOME OUT FOR FUTURE IMPLEMENTATION
     */
    public void editLocation()
    {
        justMoved = false;
        World<T> world = parentFrame.getWorld();
        Move lastMoved = null;
        T tempPiece = null;
        Location loc = display.getCurrentLocation();
        if(gameInSession){
       
        if (loc != null)
        {
            T occupant = world.getGrid().get(loc);
            if (occupant == null)
            {
                if( !(currentPiece == null) && currentMoves.contains(loc) )
                {
                    Location locOfPiece = currentPiece.getLocation();
                    setCastlingConditions();
                    boolean pawnOnStartingSquare = false;
                    if( (currentPiece instanceof Pawn) && ( locOfPiece.getRow() == 1
                        || locOfPiece.getRow() == 6 ) )
                      {
                          pawnOnStartingSquare = true;
                      }              
                    lastMoved = new Move(currentPiece.getLocation(), loc, currentPiece);
                    currentPiece.moveTo(loc);
                    if( pawnOnStartingSquare && currentPiece.checkForEnPassant() )           
                        currentPiece.enableEnPassant();
                    else
                        currentPiece.disableEnPassant();
                    if(currentPiece instanceof Pawn)
                    {
                        Pawn temp = (Pawn) currentPiece;
                        if( (currentPiece instanceof Pawn) && currentPiece.getLocation().getAdjacentLocation(temp.getPawnDirection() + Location.HALF_CIRCLE).equals(currentPiece.getEnPassantSquare()) )
                        {
                            world.getGrid().remove(currentPiece.getEnPassantSquare());
                        }
                    } 
                    checkCastlingConditions();
                    setCastlingConditions();
                    if(pawnIsPromotable())
                    {
                        promotePawn();
                    }
                    if(isInCheck(true))
                    {
                        displayCheck();
                        if(checkMate())
                        {
                             displayCheckMate();
                             gameInSession = false;
                        }   
                    }
               
                    justMoved = true;
                }         
                setCurrentStateNull();               
            }
            else
            {   
                if( !(currentPiece == null) && currentMoves.contains(loc) && (world.getGrid().get(loc) instanceof Piece))
                {
                    //Need to check condition before and after the piece moves.
                    setCastlingConditions();
                    lastMoved = new Move(currentPiece.getLocation(), loc, currentPiece);
                    tempPiece = world.getGrid().get(lastMoved.getEnd());
                    currentPiece.moveTo(loc);      
                    setCastlingConditions();

                    if(pawnIsPromotable()) 
                    {
                        promotePawn();
                    }
                    if(currentPiece != null && isInCheck(true) )
                    {
                        displayCheck();
                        if(checkMate())
                        {
                            displayCheckMate();
                            gameInSession = false;
                        }
                    }
                    justMoved = true;
                    
                }
                else
                {                          
                    currentPiece = (Piece) occupant;     
                    currentMoves = currentPiece.getValidMoveLocations();          
                    if ( !(currentPiece.getColor().equals(currentPiece.getCurrentTurn())))
                    {
                         currentPiece = null;
                    }
                    justMoved = false;
                }
            }
        }
        if( currentPiece == null && lastMoved != null)
        {
        
            currentPiece = lastMoved.getPiece();
        
            if( isInCheck(false) )
            {
                
                currentPiece = (Piece) world.getGrid().get(lastMoved.getEnd());
                currentPiece.moveTo(lastMoved.getStart());
                if( tempPiece != null)
                {
                    world.add(lastMoved.getEnd(),tempPiece);
                    tempPiece = null;
                }
                displayIllegalMove();

                //Quick fix
                currentPiece.disableEnPassant();
                justMoved = false;
                setCurrentStateNull();   
            }  
         
             
            
        }
        
    }
    parentFrame.repaint();    
    if(justMoved)
        {
            isInCheck(true); //Sets up the firstChecker and secondChecker
            if(currentChecker != null)
                System.out.print(currentChecker + " ");
            if(secondChecker != null)
                System.out.print(secondChecker);
            Grid<T> gra = parentFrame.getWorld().getGrid();
            Piece tempPce = (Piece) gra.get(gra.getOccupiedLocations().get(0));
            //Piece king = (Piece) gra.get(tempPce.getColoredKingLocation(Color.BLACK));
           
            Move compMove = ai.findBestMove(tempPce, currentChecker, secondChecker);
            Piece pceToBeMoved = (Piece) gra.get(compMove.getStart());
             
            pceToBeMoved.moveTo(compMove.getEnd());
            display.setCurrentLocation(compMove.getEnd());
          
            setCurrentStateNull();   
        }
        
    
    }

    /**
     * Edits the contents of the current location, by displaying the constructor
     * or method menu.
     */
    public void deleteLocation()
    {
        World<T> world = parentFrame.getWorld();
        Location loc = display.getCurrentLocation();
        if (loc != null)
        {
            world.remove(loc);
            parentFrame.repaint();
        }
    }
    
    private void setCurrentStateNull()
    {
        currentPiece = null;
        currentMoves = null;
    }
    
    private void setCastlingConditions()
    {
        if(currentPiece instanceof King && !(Piece.getCastlingSquares().contains(display.getCurrentLocation())) )
        {
            currentPiece.disableCastling();
        }
        
        if(currentPiece instanceof Rook)
        {
            Location rookLoc = currentPiece.getLocation();
            if( rookLoc.getCol() == 0)
            {
                currentPiece.disableCastleQueen();
            }
            if( rookLoc.getCol() == 7)
            {
                currentPiece.disableCastleKing();
            }
        }
    }
    
    private void checkCastlingConditions()
    {
        if( currentPiece instanceof King)
        {   
            Location loc = display.getCurrentLocation();
            if( Piece.whtCanCastleK )
            {
                if( loc.equals(Piece.WHT_KING_CASTLE_SQUARE) )
                {
                    currentPiece.getGrid().get(new Location(7,7)).moveTo(new Location(7,5));
                    currentPiece.disableCastling();
                }
            }
            if( Piece.whtCanCastleQ )
            {
                
                if( loc.equals(Piece.WHT_QUEEN_CASTLE_SQUARE) )
                {
                    currentPiece.getGrid().get(new Location(7,0)).moveTo(new Location(7,3));
                    currentPiece.disableCastling();
                }
            }
            if( Piece.blkCanCastleK )
            {
                if( loc.equals(Piece.BLK_KING_CASTLE_SQUARE) )
                {
                    currentPiece.getGrid().get(new Location(0,7)).moveTo(new Location(0,5));
                    currentPiece.disableCastling();
                }
            }
            if( Piece.blkCanCastleQ )
            {
                if( loc.equals(Piece.BLK_QUEEN_CASTLE_SQUARE) )
                {
                    currentPiece.getGrid().get(new Location(0,0)).moveTo(new Location(0,3));
                    currentPiece.disableCastling();
                }
            }
        }
    }
    
   public boolean pawnIsPromotable()
   {
        if( !(currentPiece instanceof Pawn) )
        {
            return false;
        }
        if( currentPiece.getLocation().equals(new Location(0, currentPiece.getLocation().getCol())) 
                || currentPiece.getLocation().equals(new Location(7, currentPiece.getLocation().getCol())) )
                return true;
        else
                return false;
   }
   
   public void promotePawn()
   {
       MenuMaker<T> maker = new MenuMaker<T>(parentFrame, resources, displayMap);
       promotionMenu = maker.makePromotionMenu(currentPiece.getLocation());  
       Point p = display.pointForLocation(display.getCurrentLocation());
       promotionMenu.show(display, p.x, p.y); 
       promotionMenuIsUp = true;
   }
   
   /**
    * Checks if the current player is giving check to the opposing player.
    */
   public boolean isInCheck(boolean whiteTurn)
   {
       boolean result = false;
       currentChecker = null;
       secondChecker = null;
       ArrayList<Piece> allPiece = new ArrayList<Piece>();
       if( whiteTurn )
            allPiece = currentPiece.getPiecesOnBoard();
       else
            allPiece = currentPiece.getOpposingPiecesOnBoard();
       for( int i = 0; i < allPiece.size(); i++)
       {
           Piece tempPiece = allPiece.get(i);
           ArrayList<Location> captureLocs = new ArrayList<Location>();
           if( tempPiece instanceof Pawn)
           {
               Pawn tempPawn = (Pawn) tempPiece;
               captureLocs = tempPawn.getPawnCaptureSquares();
           }
           else
           {
               if( tempPiece instanceof King)
               {
                   captureLocs = tempPiece.getValidKingMoves();
               }
               else
               {
                   captureLocs = tempPiece.getValidMoveLocations();
               }
           }
           Location kingLoc = null;
           if( whiteTurn)
                kingLoc = currentPiece.getOpposingKingLocation();
           else
                kingLoc = currentPiece.getKingLocation();
           if( captureLocs.contains(kingLoc) )
           {
               if(currentChecker == null)               
                    currentChecker = tempPiece;
               else
                    secondChecker = tempPiece;
               result = true;
           }
       }  
       
       return result;
   }
   
   
   /**
    * This method is called to check for CheckMate.
    * It is called with the Offense, NOT the defense.
    */
   public boolean checkMate()
   {
       Grid<T> gr = parentFrame.getWorld().getGrid();
       King opposingKing = (King) gr.get(currentPiece.getOpposingKingLocation());
       ArrayList<Location> opposingKingAvailableMoves = opposingKing.getValidMoveLocations();
       ArrayList<Piece> opposingKingDefenders = currentPiece.getOpposingPiecesOnBoard();
       //removes the King from the list of defenders.
       opposingKingDefenders.remove(opposingKingDefenders.indexOf(opposingKing));
       ArrayList<Location> defenderSquares = opposingKing.getAllPossibleAllyMoves();
       if( opposingKingAvailableMoves.isEmpty() )
       {
            //Checks for a double check
            if( !(currentChecker == null) && !(secondChecker == null) )
            {
                return true;
            }
            else
            {
                //Checks to see if the piece giving check is capturable.
                if( defenderSquares.contains(currentChecker.getLocation()) )
                {
                    //works
                    return false;
                }
                else
                {
                    if( (currentPiece instanceof Knight) || (currentPiece instanceof Pawn))
                    {
                        return true;
                    }
                    else
                    {
                        //Checks to see if the piece giving check can be blocked.
                        ArrayList<Location> pathOfAttack = new ArrayList<Location>();                    
                        Location locOfAttack = currentChecker.getLocation();
                        int directionOfAttack = locOfAttack.getDirectionToward(opposingKing.getLocation());
                        while( !(gr.get(locOfAttack) instanceof King) )
                        {
                            pathOfAttack.add(locOfAttack);
                            locOfAttack = locOfAttack.getAdjacentLocation(directionOfAttack);
                        }
                        for( int i = 0; i < pathOfAttack.size(); i++)
                        {
                            if( defenderSquares.contains(pathOfAttack.get(i)) )
                            {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
       }
       return false;
   }
   
   private void displayCheck()
   {
       JPopupMenu check = new JPopupMenu();
       check.add("Check!");
       Point p = display.pointForLocation(display.getCurrentLocation());
       check.show(display, p.x, p.y);
   }
   
   private void displayCheckMate()
   {
       JPopupMenu check = new JPopupMenu();
       check.add("CheckMate!");
       Point p = display.pointForLocation(display.getCurrentLocation());
       check.show(display, p.x, p.y);
   }
   
   public static boolean gameIsInSession()
   {
       return gameInSession;
   }
   
   private void displayIllegalMove()
   {
       JPopupMenu illegal = new JPopupMenu();
       illegal.add("Illegal Move");
       Point p = display.pointForLocation(display.getCurrentLocation());
       illegal.show(display, p.x, p.y);
    }
}
