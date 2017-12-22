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

import board.Board;

/**
 * This is a simple storage class to store any variables and objects that are 
 * shared with multiple classes, such as the board object
 * @author Austen Clay
 */

public class Storage {
    
    //A copy of the main game object
    private static Board b;
    
    //Gets the copy of the game object
    public static Board getBoard()
    {
        return b;
    }
    
    //Sets the game board
    public static void setBoard(Board b)
    {
        Storage.b = b;
    }
    
    //Inner class that represents the last attack done
    public static class LastAttack
    {
        //Data about the last attack
        public static int attackRoll;
        public static int defenseRoll;
        public static int damageDone;
        public static String attacker;
        public static String defender;
        public static boolean defenderDied;
        
        public static void setAttack(int attackRoll, int defenseRoll, int damageDone, String attacker, String defender, boolean defenderDied)
        {
            LastAttack.attackRoll = attackRoll;
            LastAttack.defenseRoll = defenseRoll;
            LastAttack.damageDone = damageDone;
            LastAttack.attacker = attacker;
            LastAttack.defender = defender;
            LastAttack.defenderDied = defenderDied;
        }
        
        public static void reset()
        {
            LastAttack.attackRoll = 0;
            LastAttack.defenseRoll = 0;
            LastAttack.defender = "";
            LastAttack.attackRoll = 0;
            LastAttack.damageDone = 0;
            LastAttack.attacker = "";
            LastAttack.defenderDied = false;
        }
    }
}
