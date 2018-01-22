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

import utils.Dice;

/**
 *
 * @author austen
 */
public abstract class AbstractPlaceableUnit extends AbstractPlaceable implements Unit{

    private int turnsLeft;
    private int turnsPerRound;
    private boolean isHasted;
    private boolean isStunned;
    private Dice[] dice;
    private final static int ATTACK = 0;
    private final static int DEFENSE = 1;
    private final static int DAMAGE = 2;
    
    public AbstractPlaceableUnit(String name, int maxHP, char token) {
        super(name, maxHP, token);
    }
    
    public AbstractPlaceableUnit(String name, int maxHP, char token, int UID)
    {
        super(name,maxHP,token);
    }
    
    protected void init(int turnsPerRound, Dice attackDice, Dice defenseDice, Dice damageDice)
    {
        dice = new Dice[3];
        dice[0] = attackDice;
        dice[1] = defenseDice;
        dice[2] = damageDice;
        this.turnsLeft = turnsPerRound;
        this.turnsPerRound = turnsPerRound;
        this.isHasted = false;
        this.isStunned = false;
    }
    
    @Override
    public int doDamage() {
        return dice[DAMAGE].makeRoll();
    }

    @Override
    public int makeAttack() {
        return dice[ATTACK].makeRoll();
    }
    
    @Override 
    public int makeDefense()
    {
        return dice[DEFENSE].makeRoll();
    }

    @Override
    public boolean hasTurn() {
        return this.turnsLeft > 0;
    }

    @Override
    public void resetTurn() {
        if (isHasted)
        {
            this.turnsLeft += this.turnsPerRound;
        }
        else
        {
            this.turnsLeft = this.turnsPerRound;
        }
    }

    public int turnsLeft()
    {
        return this.turnsLeft;
    }
    
    @Override
    public void takeTurn() {
        turnsLeft--;
    }

    @Override
    public boolean isStunned() {
        return isStunned;
    }

    @Override
    public void stunMe() {
        isStunned = true;
    }

    @Override
    public void unStunMe() {
        isStunned = false;
    }

    @Override
    public void hasteMe(int amount) {
        turnsLeft += amount;
        isHasted = true;
    }

    @Override
    public boolean isUnit() {
        return true;
    }
    
    protected void endTurn()
    {
        this.turnsLeft = 0;
    }
    
    protected void levelUp(int turnIncrease, int healthIncrease, int upperBoundIncrease, int lowerBoundIncrease,int levelsGained)
    {
        super.levelUp(healthIncrease,levelsGained);
        refreshMe();
        this.turnsPerRound += turnIncrease;
    }
    
    @Override
    public void refreshMe()
    {
        super.refreshMe();
        for (Dice d : dice)
        {
            d.setTempModifer(0);
        }
        isStunned = false;
        isHasted = false;
    }
    
    public int getMaxRoll(String type)
    {
        int option;
        switch(type.toLowerCase())
        {
            case "attack":
                option = ATTACK;
                break;
            case "defense":
                option = DEFENSE;
                break;
            case "damage":
                option = DAMAGE;
                break;
            default:
                return -1;
        }
        Dice toGet = dice[option];
        return toGet.getUpperBound()-toGet.getPermModifier()-toGet.getTempModifier();
    }
    
    public int getAttackBuff()
    {
        return dice[ATTACK].getTempModifier();
    }
    
    public int getDefenseBuff()
    {
        return dice[DEFENSE].getTempModifier();
    }
    
    public int getDamageBuff()
    {
        return dice[DAMAGE].getTempModifier();
    }
    
    public void setAttackBuff(int amt)
    {
        dice[ATTACK].setTempModifer(amt);
    }
    
    public void setDefenseBuff(int amt)
    {
        dice[DEFENSE].setTempModifer(amt);
    }

        
    public void setDamageBuff(int amt)
    {
        dice[DAMAGE].setTempModifer(amt);
    }
    
    @Override
    public String[] getBuffInfo()
    {
        String[] toReturn = new String[3];
        toReturn[0] = "Attack Buff: "+getAttackBuff();
        toReturn[1] = "Defense Buff: "+getDefenseBuff();
        toReturn[2] = "Damage Buff: "+getDefenseBuff();
        return toReturn;
    }
}
