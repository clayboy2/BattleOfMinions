/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import actors.Peasant;
import actors.Unit;
import board.Board;
import java.util.ArrayList;

/**
 *
 * @author ninja
 */
public class Utils {
    
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
    
    public ArrayList<Unit> randomPlacement(Board b, boolean isNorth)
    {
        ArrayList<Unit> toReturn = new ArrayList<>();
        for (int i = 0;i<5;i++)
        {
            while(true)
            {
                int row, col;
                if (isNorth)
                {
                    row = (int)(Math.random()*5)+1;
                    col = (int)(Math.random()*5)+7;
                }
                else
                {
                    row = (int)(Math.random()*5)+14;
                    col = (int)(Math.random()*5)+7;
                }
                if (b.isEmptySpace(row, col))
                {
                    Unit u = new Peasant();
                    toReturn.add(u);
                    u.setPosition(row, col);
                    b.placePlaceable(row, col, u);
                    toReturn.add(u);
                }
            }
        }
        return toReturn;
    }
}
