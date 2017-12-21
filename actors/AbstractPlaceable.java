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
package actors;

import java.io.Serializable;
import utils.IOPort;

/**
 * This class is the parent class of all things placeable. 
 * This class defines default behavior for all interface methods.
 * @author Austen Clay
 */
public abstract class AbstractPlaceable implements Placeable,Serializable{
    
    //Class variables
    private final int maxHP;
    private int currHP;
    private final String name;
    private int myRow;
    private int myCol;
    private final char token;
    private final int myUID;
    private static int globalUID;
    
    //This is the 'default' constructor.
    public AbstractPlaceable(String name, int maxHP,char token)
    {
        this.name = name;
        this.maxHP = maxHP;
        this.currHP = this.maxHP;
        this.token = token;
        if (this instanceof Unit)
        {
            this.myUID = IOPort.getGlobalID();
        }
        else
        {
            this.myUID = -1;
        }
    }
    
    public AbstractPlaceable(String name, int maxHP, char token, int uid)
    {
        this.name = name;
        this.maxHP = maxHP;
        this.currHP = maxHP;
        this.token = token;
        this.myUID = uid;
    }
    
    //Gets this placeables row number
    @Override
    public int getRow()
    {
        return myRow;
    }
    
    //Gets the placeables column number
    @Override
    public int getCol()
    {
        return myCol;
    }
    
    //Gets the token representation of this
    @Override
    public char getToken()
    {
        return this.token;
    }
    
    //Returns the placeables name
    @Override
    public String getName()
    {
        return name;
    }
    
    //Returns the maxHP
    public int getMaxHP()
    {
        return maxHP;
    }
    
    
    //Returns the currentHP
    public int getCurrHP()
    {
        return currHP;
    }
    
    //Makes this placeable take damage
    @Override
    public int takeDamage(int amount) {
        currHP -= amount;
        if (currHP<0)
        {
            currHP = 0;
        }
        return amount;
    }
    
    //Signals if this placeable has died
    @Override
    public boolean isDead() {
        return currHP<=0;
    }

    //Signals if placeable is wall
    @Override
    public boolean isWall() {
        return false;
    }

    //Signals if this is an empty space
    @Override
    public boolean isEmptySpace() {
        return false;
    }

    //Signals if this is a unit
    @Override
    public boolean isUnit() {
        return false;
    }
    
    //Make a defense check
    @Override
    public int makeDefense()
    {
        return 0;
    }
    
    //Refulls this placeables health
    public void restoreHealth()
    {
        currHP = maxHP;
    }
    
    
    //Heals this placeable by an amount. Will not exceed max hp
    public void heal(int amount)
    {
        currHP =+ amount;
        if (currHP>maxHP)
            currHP = maxHP;
    }
    
    //Sets this units position on the gameboard
    @Override
    public void setPosition(int row, int col)
    {
        this.myCol = col;
        this.myRow = row;
    }
    
    //Checks myUID for for equality
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AbstractPlaceable)
        {
            AbstractPlaceable ap = (AbstractPlaceable)o;
            if (ap.myUID == this.myUID)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.myUID;
    }
    
    @Override
    public int getUID()
    {
        return this.myUID;
    }
}