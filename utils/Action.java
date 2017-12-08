/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import actors.Placeable;
import actors.Unit;
import board.Board;

/**
 *
 * @author ninja
 */
public class Action {
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    
    public static int move(Unit u, char direction)
    {
        Board b = Storage.getBoard();
        int row = u.getRow();
        int col = u.getCol();
        switch(direction)
        {
            case NORTH:
                row--;
                break;
            case EAST:
                col++;
                break;
            case SOUTH:
                row++;
                break;
            case WEST:
                col--;
                break;
        }
        if (b.isEmptySpace(row, col))
        {
            b.placePlaceable(row, col, u);
            b.removeUnit(u);
            u.setPosition(row, col);
            return ExitCodes.SUCCESSFUL;
        }
        else if (b.getBoard()[row][col].isWall())
        {
            return ExitCodes.ERR_TARGET_IS_WALL;
        }
        else 
        {
            return ExitCodes.ERR_TARGET_IS_OCCUPIED;
        }
    }
    
    public int attack(Unit attacker, Placeable defender)
    {
        if (attacker.makeAttack()>=defender.makeDefense())
        {
            defender.takeDamage(attacker.doDamage());
        }
        if (defender.isDead())
        {
            Board b = Storage.getBoard();
            b.removeUnit(defender);
            defender.setPosition(-1, -1);
            if (defender.isUnit())
            {
                for (User u : b.getPlayers())
                {
                    u.unitDeath((Unit)defender);
                }
            }
            return ExitCodes.TARGET_IS_DEAD;
        }
        return ExitCodes.SUCCESSFUL;
    }
    
    public class ExitCodes
    {
        private static final int SUCCESSFUL = 0;
        private static final int ERR_TARGET_IS_OCCUPIED = 1;
        private static final int ERR_TARGET_IS_WALL = 2;
        private static final int TARGET_IS_DEAD = 3;
    }
}
