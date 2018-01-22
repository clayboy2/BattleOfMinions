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
import actors.AbstractPlaceableUnit;
import actors.EmptySpace;
import actors.Peasant;
import actors.Placeable;
import actors.Unit;
import actors.Warrior;
import actors.Wizard;
import actors.wizardmagic.Summonable;
import battleofminions.SimpleMode;
import board.Board;
import java.util.ArrayList;
import utils.Storage.LastAttack;

/**
 * This class holds all of the logic for actions that Units can take
 *
 * @author Austen Clay
 */
public class Action {

    //Defines a correlation between cardinal directions and the character w,s,a,d
    public static final char NORTH = 'w';
    public static final char EAST = 'd';
    public static final char SOUTH = 's';
    public static final char WEST = 'a';

    //Moves a unit in a given direction
    public static int move(Unit u, char direction) {
        if (u == null) {
            System.out.println("Unit moving is null. Skipping");
            return ExitCodes.INVALID_TARGET;
        }
        u.takeTurn();
        Board b = Storage.getBoard();
        int row = u.getRow();
        int col = u.getCol();
        switch (direction) {
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
        if (b.isEmptySpace(row, col)) {
            b.movePlaceable(u, row, col);
            return ExitCodes.SUCCESSFUL;
        } else if (b.getBoard()[row][col].isWall()) {
            return ExitCodes.ERR_TARGET_IS_WALL;
        } else {
            return ExitCodes.ERR_TARGET_IS_OCCUPIED;
        }
    }

    //Used for melee attacks
    public static int attack(Unit attacker, char direction) {
        int row = attacker.getRow();
        int col = attacker.getCol();
        Placeable[][] gameBoard = Storage.getBoard().getBoard();
        switch (direction) {
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
        return attack(attacker, gameBoard[row][col]);
    }

    //This will move all units that have a turn
    public static int moveall(User user, char direction) {
        ArrayList<Integer> irregularExitCodes = new ArrayList<>();
        boolean movedUnit = false;
        for (Unit unit : user.getControlGroup()) {
            if (unit.hasTurn()) {
                int code = move(unit, direction);
                if (code != ExitCodes.SUCCESSFUL) {
                    irregularExitCodes.add(code);
                }
            }
        }
        if (irregularExitCodes.isEmpty()) {
            return ExitCodes.SUCCESSFUL;
        } else if (irregularExitCodes.size() == 1) {
            return irregularExitCodes.get(0);
        } else {
            return ExitCodes.MULTIPLE_ERRORS;
        }
    }

    //Standard attack action
    public static int attack(Unit attacker, Placeable defender) {
        int exitCode = ExitCodes.SUCCESSFUL;
        attacker.takeTurn();
        int attackRoll = attacker.makeAttack(), defenseRoll = defender.makeDefense(), damageDone = 0;
        boolean defenderDied = false;
        String attackerName = attacker.getName();
        String defenderName = defender.getName();
        if (attackRoll >= defenseRoll) {
            damageDone = attacker.doDamage();
            defender.takeDamage(damageDone);
        } else {
            exitCode = ExitCodes.MISSED_TARGET;
        }
        if (defender.isDead()) {
            defenderDied = true;
            LastAttack.setAttack(attackRoll, defenseRoll, damageDone, attackerName, defenderName, defenderDied);
            Board b = Storage.getBoard();
            b.removeUnit(defender);
            defender.setPosition(-1, -1);
            if (defender.isUnit()) {
                for (User u : b.getPlayers()) {
                    if (u.belongsToMe((AbstractPlaceable) defender)) {
                        if (u.belongsToMe((AbstractPlaceable) attacker)) {
                            SimpleMode.friendlyFireKills.add((Unit) defender);
                        } else {
                            u.getControlGroup().remove((Unit) defender);
                        }
                    }
                }
            }
            if (defender instanceof Summonable) {
                for (User u : b.getPlayers()) {
                    for (Unit unit : u.getControlGroup()) {
                        if (unit instanceof Wizard) {
                            for (Summonable s : ((Wizard) unit).getSummons()) {
                                if (s.equals(defender)) {

                                }
                            }
                        }
                    }
                }
            }
            exitCode = ExitCodes.TARGET_IS_DEAD;
        }
        LastAttack.setAttack(attackRoll, defenseRoll, damageDone, attackerName, defenderName, defenderDied);
        if (defender.isEmptySpace()) {
            exitCode = ExitCodes.TARGET_IS_EMPTY;
        }
        return exitCode;
    }
    
    public static int doDamage(Unit attacker, Placeable defender, int damage, int attackRoll, int defenseRoll)
    {
        if (defender.isDead())
        {
            System.out.println(attacker.getName()+" has killed "+defender.getName());
        }
        LastAttack.setAttack(attackRoll, defenseRoll, damage, attacker.getName(), defender.getName(), defender.isDead());
        Board b = Storage.getBoard();
        User attackerOwner;
        User defenderOwner = null;
        for (User u : b.getPlayers())
        {
            if (u.belongsToMe((AbstractPlaceable)attacker))
            {
                attackerOwner = u;
            }
            if (u.belongsToMe((AbstractPlaceable)defender))
            {
                defenderOwner = u;
            }
        }
        if (defenderOwner==null)
        {
            //Belongs to no one
            
        }
        return ExitCodes.SUCCESSFUL;
    }
    
    public static int rangedAttack(Unit attacker, char direction) {
        int row = attacker.getRow();
        int col = attacker.getCol();
        while (attacker.hasTurn()) {
            attacker.takeTurn();
        }
        Placeable[][] gameBoard = Storage.getBoard().getBoard();
        int code;
        while (true) {
            System.out.println("Arrow traveling...");
            switch (direction) {
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
            if (gameBoard[row][col].isEmptySpace()) {
                //Keep going
            } else {
                System.out.println("Arrow stopped at " + gameBoard[row][col].getName());
                break;
            }
        }
        code = attack(attacker, gameBoard[row][col]);
        return code;
    }

    //Inner class for all of the exit codes
    public class ExitCodes {

        public static final int SUCCESSFUL = 0;
        public static final int ERR_TARGET_IS_OCCUPIED = 1;
        public static final int ERR_TARGET_IS_WALL = 2;
        public static final int TARGET_IS_DEAD = 3;
        public static final int INVALID_TARGET = 4;
        public static final int MULTIPLE_ERRORS = 5;
        public static final int MISSED_TARGET = 6;
        public static final int FRIENDLY_FIRE = 7;
        public static final int TARGET_IS_EMPTY = 8;
        public static final int INVALID_OPTION = 9;
        public static final int ERR_TOO_EXPENSIVE = 10;
        public static final int UNIT_DOES_NOT_HAVE_ABILITY = 11;
    }

    public static class SpecialAttacks {

        public static int attackSelector(String attackName, int damage, Warrior w, int direction, String extraInfo) {
            int result = ExitCodes.SUCCESSFUL;
            if (!(w instanceof Warrior))
            {
                return ExitCodes.INVALID_OPTION;
            }
            if (!w.hasAttack(attackName))
            {
                return ExitCodes.UNIT_DOES_NOT_HAVE_ABILITY;
            }
            switch (attackName) {
                case "dash":
                    result = dash(w);
                    break;
                case "power attack":
                    result = powerAttack(direction, w);
                    break;
                case "cleaving strike":
                    result = boxEffect(1, 3, direction, damage, w, 3);
                    break;
                default:
                    System.out.println("Invalid attack name");
                    return ExitCodes.INVALID_TARGET;
            }
            w.takeTurn();
            return result;
        }
        
        public static int dash(Warrior w)
        {
            int cost = 4;
            if (w.getStamina()<cost)
            {
                return ExitCodes.ERR_TOO_EXPENSIVE;
            }
            w.spendStamina(cost);
            w.hasteMe(6);
            System.out.println("Turns left: "+w.turnsLeft());
            return 0;
        }

        public static int boxEffect(int length, int width, int direction, int damage, Warrior w, int cost) {
            if (w.getStamina()<cost)
            {
                return ExitCodes.ERR_TOO_EXPENSIVE;
            }
            w.spendStamina(cost);
            int targetRow = w.getRow();
            int targetCol = w.getCol();
            Board b = Storage.getBoard();
            boolean isEven = direction % 2 == 0;
            int startRow, startCol;
            switch (direction) {
                case NORTH:
                    targetRow--;
                    startRow = targetRow;
                    startCol = targetCol - (width / 2);
                    break;
                case EAST:
                    targetCol++;
                    startCol = targetCol;
                    startRow = targetRow - (length / 2);
                    break;
                case SOUTH:
                    targetRow++;
                    startCol = targetCol - (width / 2);
                    startRow = targetRow;
                    break;
                case WEST:
                    targetCol--;
                    startCol = targetCol;
                    startRow = targetRow - (width / 2);
                    break;
                default:
                    System.out.println("Invalid Direction");
                    return ExitCodes.INVALID_TARGET;
            }

            for (int currLength = 0; currLength < length; currLength++) {
                for (int currWidth = 0; currWidth < width; currWidth++) {
                    try {
                        AbstractPlaceable target;
                        switch (direction) {
                            case NORTH:
                                target = (AbstractPlaceable) b.getBoard()[startRow - currLength][startCol + currWidth];
                                break;
                            case EAST:
                                target = (AbstractPlaceable) b.getBoard()[startRow + currWidth][startCol + currLength];
                                break;
                            case SOUTH:
                                target = (AbstractPlaceable) b.getBoard()[startRow + currLength][startCol + currWidth];
                                break;
                            case WEST:
                                target = (AbstractPlaceable) b.getBoard()[startRow + currWidth][startCol - currLength];
                                break;
                            default:
                                target = new EmptySpace();
                                break;
                        }
                        target.takeDamage(damage);
                    } catch (IndexOutOfBoundsException e) {
                        //Ignore this.
                    }
                }
            }
            return ExitCodes.SUCCESSFUL;
        }

        public static int powerAttack(int direction, Warrior w) {
            int cost = 5;
            if (cost > w.getStamina()) {
                return ExitCodes.ERR_TOO_EXPENSIVE;
            }
            w.spendStamina(cost);
            int targetRow = w.getRow();
            int targetCol = w.getCol();
            Board b = Storage.getBoard();
            switch (direction) {
                case NORTH:
                    targetRow--;
                    break;
                case EAST:
                    targetCol++;
                    break;
                case SOUTH:
                    targetRow++;
                    break;
                case WEST:
                    targetCol--;
                    break;
                default:
                    System.out.println("Invalid Direction");
                    return ExitCodes.INVALID_TARGET;
            }
            AbstractPlaceable ap = (AbstractPlaceable) b.getBoard()[targetRow][targetCol];
            if (ap instanceof EmptySpace) {
                return ExitCodes.TARGET_IS_EMPTY;
            }
            int damage = w.doDamage() + Utils.makeRoll(8, 1);
            
            if (ap.isDead())
            {
                
            }
            return ExitCodes.SUCCESSFUL;
        }

    }
}
