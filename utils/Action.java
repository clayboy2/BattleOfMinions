/*
 * Copyright 2017 austen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package utils;

import actors.AbstractPlaceable;
import actors.Placeable;
import actors.Unit;
import battleofminions.SimpleMode;
import board.Board;
import java.util.ArrayList;
import utils.Storage.LastAttack;

/**
 * This class holds all of the logic for actions that Units can take
 * @author Austen Clay
 */
public class Action {
    
    //Defines a correlation between cardinal directions and the character w,s,a,d
    private static final char NORTH = 'w';
    private static final char EAST = 'd';
    private static final char SOUTH = 's';
    private static final char WEST = 'a';
    
    //Moves a unit in a given direction
    public static int move(Unit u, char direction)
    {
        u.takeTurn();
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
            default:
                return ExitCodes.INVALID_TARGET;
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
    
    //Used for melee attacks
    public static int attack(Unit attacker, char direction)
    {
        int row = attacker.getRow();
        int col = attacker.getCol();
        Placeable[][] gameBoard = Storage.getBoard().getBoard();
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
            default:
                return ExitCodes.INVALID_TARGET;
        }
        return attack(attacker,gameBoard[row][col]);
    }
    
    //This will move all units that have a turn
    public static int moveall(User user,char direction)
    {
        ArrayList<Integer> irregularExitCodes = new ArrayList<>();
        boolean movedUnit = false;
        for (Unit unit : user.getControlGroup())
        {
            if (unit.hasTurn())
            {
                int code = move(unit,direction);
                if (code!=ExitCodes.SUCCESSFUL)
                {
                    irregularExitCodes.add(code);
                }
            }
        }
        if (irregularExitCodes.isEmpty())
        {
            return ExitCodes.SUCCESSFUL;
        }
        else if (irregularExitCodes.size()==1)
        {
            return irregularExitCodes.get(0);
        }
        else
        {
            return ExitCodes.MULTIPLE_ERRORS;
        }
    }
    
    //Standard attack action
    public static int attack(Unit attacker, Placeable defender)
    {
        int exitCode = ExitCodes.SUCCESSFUL;
        attacker.takeTurn();
        int attackRoll = attacker.makeAttack(), defenseRoll = defender.makeDefense(),damageDone=0;
        boolean defenderDied = false;
        String attackerName = attacker.getName();
        String defenderName = defender.getName();
        if (attackRoll>=defenseRoll)
        {
            damageDone = attacker.doDamage();
            defender.takeDamage(damageDone);
        }
        else
        {
            exitCode = ExitCodes.MISSED_TARGET;
        }
        if (defender.isDead())
        {
            defenderDied = true;
            LastAttack.setAttack(attackRoll, defenseRoll, damageDone, attackerName, defenderName, defenderDied);
            Board b = Storage.getBoard();
            b.removeUnit(defender);
            defender.setPosition(-1, -1);
            if (defender.isUnit())
            {
                for (User u : b.getPlayers())
                {
                    if (u.belongsToMe((AbstractPlaceable)defender))
                    {
                        if (u.belongsToMe((AbstractPlaceable)attacker))
                        {
                            SimpleMode.friendlyFireKills.add((Unit)defender);
                        }
                        else
                        {
                            u.getControlGroup().remove((Unit)defender);
                        }
                    }
                }
            }
            exitCode = ExitCodes.TARGET_IS_DEAD;
        }
        LastAttack.setAttack(attackRoll, defenseRoll, damageDone, attackerName, defenderName, defenderDied);
        return exitCode;
    }
    
    //Inner class for all of the exit codes
    public class ExitCodes
    {
        public static final int SUCCESSFUL = 0;
        public static final int ERR_TARGET_IS_OCCUPIED = 1;
        public static final int ERR_TARGET_IS_WALL = 2;
        public static final int TARGET_IS_DEAD = 3;
        public static final int INVALID_TARGET = 4;
        public static final int MULTIPLE_ERRORS = 5;
        public static final int MISSED_TARGET = 6;
        public static final int FRIENDLY_FIRE = 7;
    }
}