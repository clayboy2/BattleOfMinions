/*
 * Copyright 2018 austen.
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

import java.io.Serializable;

/**
 *
 * @author austen
 */
public class Dice implements Serializable{
    
    private int lowerBound;
    private int upperBound;
    private int temporaryModifier;
    private int permanentModifier;
    
    public Dice(int lowerBound, int upperBound)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        temporaryModifier = 0;
        permanentModifier = 0;
    }
    
    public Dice(int lowerBound, int upperBound, int permanentModifer)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.permanentModifier = permanentModifer;
        this.temporaryModifier = 0;
    }
    
    public int makeRoll()
    {
        int toReturn = (int)((Math.random()*upperBound-lowerBound)+lowerBound);
        toReturn+=permanentModifier+temporaryModifier;
        return toReturn;
    }
    
    public void modifyDice(int lowerBound, int upperBound, int permanentModifier)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.permanentModifier = permanentModifier;
    }
    
    public void setTempModifer(int temporaryModifier)
    {
        this.temporaryModifier = temporaryModifier;
    }

    public int getUpperBound()
    {
        return upperBound;
    }
    
    public int getLowerBound()
    {
        return lowerBound;
    }
    
    public int getTempModifier()
    {
        return temporaryModifier;
    }
    
    public int getPermModifier()
    {
        return permanentModifier;
    }
}
