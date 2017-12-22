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

import utils.Utils;

/**
 * This class represents the most basic battle unit. 
 * @author Austen Clay
 */
public class Peasant extends AbstractPlaceable implements Unit{
    private boolean hasTurn;
    private boolean isStunned;
    //Default Constructor
    public Peasant()
    {
        super("Bob Farmer", 5,'P');
        hasTurn = true;
        isStunned = false;
    }
    
    public Peasant(int UID)
    {
        super("Bob Farmer", 5, 'P',UID);
    }
    
    //Named peasant
    public Peasant(String name)
    {
        super(name, 5, 'P');
        hasTurn = true;
        isStunned = false;
    }
    
    //Stuns this unit
    @Override
    public void stunMe()
    {
        isStunned = true;
    }
    
    //Signals if I am stunned or not
    @Override
    public boolean isStunned()
    {
        return isStunned;
    }
    
    //Unstuns this unit
    @Override
    public void unStunMe()
    {
        isStunned = false;
    }
    
    //Signals that this is indeed, a unit
    @Override
    public boolean isUnit()
    {
        return true;
    }

    //Does damage
    @Override
    public int doDamage() {
        return Utils.makeRoll(3,0) + 1 + super.getDefenseBuff();
    }
    
    //Makes a defense check
    @Override
    public int makeDefense()
    {
        return Utils.makeRoll(3,0) + super.getDefenseBuff();
    }
    
    //Makes an attack roll
    @Override
    public int makeAttack()
    {
        return Utils.makeRoll(3,0)+super.getAttackBuff();
    }

    @Override
    public boolean hasTurn() {
        return hasTurn;
    }
    
    @Override
    public void takeTurn()
    {
        hasTurn = false;
    }
    
    @Override
    public void resetTurn()
    {
        hasTurn = true;
    }

    @Override
    public String getUnitType() {
        return "Peasant";
    }
    
    @Override
    public void levelUp()
    {
        
    }
}
