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
public class Archer extends AbstractPlaceableUnit{
    private boolean didCrit;
    @Override
    protected void init(int turnsPerRound,Dice attackDice, Dice defenseDice, Dice damageDice)
    {
        super.init(2,attackDice, defenseDice, damageDice);
    }
    
    private void init()
    {
        Dice attack = new Dice(1,10);
        Dice defense = new Dice(1,6);
        Dice damage = new Dice(1,8);
        super.init(2,attack,defense,damage);
    }
    
    public Archer()
    {
        super("Legolas",8,'A');
        init();
    }
    
    public Archer(String name)
    {
        super(name,8,'A');
        init();
    }
    
    public Archer(String name, int maxHP)
    {
        super(name,maxHP,'A');
        init();
    }
    
    public Archer(int UID, String name)
    {
        super(name,8,'A',UID);
        init();
    }
    
    public Archer(String name, int maxHP, int UID)
    {
        super(name,maxHP,'A',UID);
        init();
    }
    
    public Archer(String name, int maxHP, char token)
    {
        super(name,maxHP,token);
    }
    
    public Archer(String name, int maxHP, char token, int uid)
    {
        super(name,maxHP,token,uid);
        init();
    }
    
    @Override
    public int doDamage() {
        int roll = super.doDamage();
        if (didCrit)
        {
            roll+=super.doDamage();
        }
        didCrit = false;
        return roll;
    }

    @Override
    public int makeAttack() {
        int roll = super.makeAttack();
        if (roll >= super.getMaxRoll("attack"))
        {
            System.out.println(super.getName() + " got a critical hit!");
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
        if (myLevel%3==0)
        {
            super.levelUp(1, 0, 0, 0, 0);
        }
        if (myLevel%2==0)
        {
            super.levelUp(0,0,1,0,0);
        }
        if ((myLevel+1)%2==0)
        {
            super.levelUp(0,0,0,1,0);
        }
        if (myLevel%5==0)
        {
            super.levelUp(0,5,0,0,0);
        }
        super.levelUp(0, 2, 0, 0,1);
    }
}
