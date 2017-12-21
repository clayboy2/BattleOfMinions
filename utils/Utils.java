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

import actors.Peasant;
import actors.Placeable;
import actors.Unit;
import board.Board;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A set of Utilites that the program runs with
 * @author ninja
 */
public class Utils {
    //Define correlations between cardinal directions and integers
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    
    //Makes a roll with the specified upper bound
    public static int makeRoll(int upperBound)
    {
        int toReturn = 0;
        for (int i = 0;i<upperBound;i++)
        {
            if ((int)((Math.random()*4)+1)==1)
                toReturn++;
        }
        return toReturn;
    }
    
    //Randomly places units on the board
    public static ArrayList<Unit> randomPlacement(Board b, int direction, boolean isAI)
    {
        ArrayList<Unit> toReturn = new ArrayList<>();
        Scanner keys = new Scanner(System.in);
        for (int i = 0;i<5;i++)
        {
            while(true)
            {
                int row, col, rows = b.getRows(), cols = b.getCols();
                switch (direction)
                {
                    case NORTH:
                        row = ((int)(Math.random()*5)+1);
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case EAST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+(cols-6));
                        break;
                    case SOUTH:
                        row = ((int)(Math.random()*5)+(rows-6));
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case WEST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+1);
                        break;
                    default:
                        return null;
                }
                if (b.isEmptySpace(row, col))
                {
                    Unit u = new Peasant();
                    if (!isAI)
                    {
                        System.out.print("Name your peasant: ");
                        String name = keys.nextLine();
                        u = new Peasant(name);
                    }
                    toReturn.add(u);
                    u.setPosition(row, col);
                    b.placePlaceable(row, col, u);
                    break;
                }
            }
        }
        return toReturn;
    }
    public static boolean randomPlacement(Board b, ArrayList<Unit> unitsToPlace, int direction)
    {
        for (Unit u : unitsToPlace)
        {
            while(true)
            {
                int row, col, rows = b.getRows(), cols = b.getCols();
                switch (direction)
                {
                    case NORTH:
                        row = ((int)(Math.random()*5)+1);
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case EAST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+(cols-6));
                        break;
                    case SOUTH:
                        row = ((int)(Math.random()*5)+(rows-6));
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case WEST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+1);
                        break;
                    default:
                        return false;
                }
                if (b.isEmptySpace(row, col))
                {
                    u.setPosition(row, col);
                    b.placePlaceable(row, col, u);
                    break;
                }
            }
        }
        return true;
    }
}
