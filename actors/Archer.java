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
 *
 * @author austen
 */
public class Archer extends AbstractPlaceable implements Unit{
    private int turnsLeft;
    private int turnsPerRound;
    private boolean isStunned;
    private boolean didCrit;
    private int upperBound;
    private int lowerBound;
    
    public Archer()
    {
        super("Legolas",8,'A');
        this.turnsLeft = 2;
        this.turnsPerRound = 2;
        this.isStunned = false;
        this.didCrit = false;
        upperBound = 6;
        lowerBound = 0;
    }
    
    public Archer(String name)
    {
        super(name,8,'A');
        this.turnsLeft = 2;
        this.turnsPerRound = 2;
        this.isStunned = false;
        this.didCrit = false;
        upperBound = 8;
        lowerBound = 0;
    }
    
    public Archer(String name, int maxHP)
    {
        super(name,maxHP,'A');
        this.turnsLeft = 2;
        this.turnsPerRound = 2;
        this.isStunned = false;
        this.didCrit = false;
        upperBound = 8;
        lowerBound = 0;
    }
    
    public Archer(String name, int maxHP, int UID)
    {
        super(name,maxHP,'A',UID);
        this.turnsLeft = 2;
        this.turnsPerRound = 2;
        this.isStunned = false;
        this.didCrit = false;
        upperBound = 8;
        lowerBound = 0;
    }
    
    @Override
    public int doDamage() {
        int roll = Utils.makeRoll(upperBound,lowerBound)+1 + super.getDamageBuff();
        if (didCrit)
        {
            didCrit = false;
            roll+=(Utils.makeRoll(upperBound,lowerBound)+1);
        }
        return roll;
    }

    @Override
    public int makeAttack() {
        int roll = Utils.makeRoll(upperBound,lowerBound);
        if (roll >=upperBound)
        {
            this.didCrit = true;
            return 100;
        }
        else
        {
            roll+=super.getAttackBuff();
        }
        return roll;
    }
    
    @Override
    public int makeDefense()
    {
        return Utils.makeRoll(upperBound,lowerBound)+super.getDefenseBuff();
    }

    @Override
    public boolean hasTurn() {
        return turnsLeft>0;
    }

    @Override
    public void resetTurn() {
        this.turnsLeft = this.turnsPerRound;
    }

    @Override
    public void takeTurn() {
        this.turnsLeft--;
    }

    @Override
    public boolean isStunned() {
        return isStunned;
    }

    @Override
    public void stunMe() {
        this.isStunned = true;
    }

    @Override
    public void unStunMe() {
        this.isStunned = false;
    }

    @Override
    public String getUnitType() {
        return "Archer";
    }
    
    @Override
    public boolean isUnit()
    {
        return true;
    }
    
    @Override
    public void levelUp()
    {
        super.levelUp(2);
        upperBound+=2;
        lowerBound+=1;
    }
}
