/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
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
 * @author Cay Horstmann
 */

 



import java.util.ArrayList;
import java.awt.*;

/**
 * An <code>ActorWorld</code> is occupied by actors. <br />
 * This class is not tested on the AP CS A and AB exams.
 */

public class ActorWorld extends World<Actor>
{
    /**
     * Constructs an actor world with a default grid.
     */
    public ActorWorld()
    {
    }

    /**
     * Constructs an actor world with a given grid.
     * @param grid the grid for this world.
     */
    public ActorWorld(Grid<Actor> grid)
    {
        super(grid);
    }

    public void show()
    {
        if (getMessage() == null)
        {
            String defaultMessage = "Click on a piece to move it."; 
            setMessage(defaultMessage);
        }
        super.show();
    }

    public void step()
    {
        Grid<Actor> gr = getGrid();
        ArrayList<Actor> actors = new ArrayList<Actor>();
        for (Location loc : gr.getOccupiedLocations())
            actors.add(gr.get(loc));

        for (Actor a : actors)
        {
            // only act if another actor hasn't removed a
            if (a.getGrid() == gr)
                a.act();
        }
    }

    /**
     * Adds an actor to this world at a given location.
     * @param loc the location at which to add the actor
     * @param occupant the actor to add
     */
    public void add(Location loc, Actor occupant)
    {
        occupant.putSelfInGrid(getGrid(), loc);
    }

    /**
     * Adds an occupant at a random location.
     * @param occupant the occupant to add
     */
    public void add(Actor occupant)
    {
        Location loc = getRandomEmptyLocation();
        if (loc != null)
            add(loc, occupant);
    }

    /**
     * Removes an actor from this world.
     * @param loc the location from which to remove an actor
     * @return the removed actor, or null if there was no actor at the given
     * location.
     */
    public Actor remove(Location loc)
    {
        Actor occupant = getGrid().get(loc);
        if (occupant == null)
            return null;
        occupant.removeSelfFromGrid();
        return occupant;
    }
    
    public void setUpBoard()
    {     
         for( int i = 0; i < getGrid().getNumCols(); i++ )
         {
             Pawn whitePawn = new Pawn(Color.WHITE);
             add(new Location(6, i), whitePawn);
             Pawn blackPawn = new Pawn(Color.BLACK);    
             add(new Location(1, i), blackPawn);
         }
         int currentRow = 0;
         //Using loop variable as a counter for color.
         for( int colorRGB = 0; colorRGB <= 255; colorRGB += 255)
         {
             Color currentColor = new Color(colorRGB, colorRGB, colorRGB);
             ArrayList<Piece> setUp = new ArrayList<Piece>();
             setUp.add(new Rook(currentColor));                
             setUp.add(new Knight(currentColor));           
             setUp.add(new Bishop(currentColor));
             setUp.add(new Queen(currentColor));
             setUp.add(new King(currentColor));
             setUp.add(new Bishop(currentColor));
             setUp.add(new Knight(currentColor));
             setUp.add(new Rook(currentColor));
             for( int col = 0; col < getGrid().getNumCols(); col++)
             {
                add(new Location(currentRow, col), setUp.get(col));    
             }
             currentRow = 7;
         }
        
    }
}