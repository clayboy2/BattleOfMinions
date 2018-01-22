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

import java.util.ArrayList;
import utils.Dice;
import utils.Utils;

/**
 *
 * @author austen
 */
public class Warrior extends AbstractPlaceableUnit{
    private int staminaMax;
    private int stamina;
    ArrayList<String> attacksAvailable;
    
    protected void init(int turnsPerRound, Dice attackDice, Dice defenseDice, Dice damageDice, int staminaMax)
    {
        super.init(turnsPerRound, attackDice, defenseDice, damageDice);
        this.staminaMax = staminaMax;
        this.attacksAvailable = new ArrayList<>();
        attacksAvailable.add("power attack");
    }
    
    private void init()
    {
        Dice attackDice = new Dice(100,100);
        Dice defenseDice = new Dice(1,8);
        Dice damageDice = new Dice(1,6);
        this.init(2,attackDice,defenseDice,damageDice,10);
    }
    
    public Warrior()
    {
        super("Dave",10,'W');
        init();
    }
    public Warrior(String name, int maxHP) {
        super(name, maxHP, 'W');
        init();
    }
    
    public Warrior(String name)
    {
        super(name, 10,'W');
        init();
    }
    
    public Warrior(int UID, String name)
    {
        super(name,10,'W',UID);
        init();
    }
    
    public Warrior(String name, int maxHP, char token)
    {
        super(name,maxHP,token);
        init();
    }
    
    public Warrior(String name, int maxHP, int UID)
    {
        super(name,maxHP,'W',UID);
        init();
    }
    
    public Warrior(String name, int maxHP, char token, int UID)
    {
        super(name,maxHP,token,UID);
        init();
    }
    
    public int getStamina()
    {
        return stamina;
    }
    
    public void restoreStamina()
    {
        this.stamina = this.staminaMax;
    }
    
    public void spendStamina(int amt)
    {
        this.stamina-=amt;
    }
    
    @Override
    public int makeAttack() {
        return 100+getAttackBuff();
    }
    
    @Override
    public String getUnitType() {
        return "Warrior";
    }

    @Override
    public void levelUp() {
        super.levelUp(0, 1);
    }
    
    public boolean hasAttack(String attackName)
    {
        for (String s : attacksAvailable)
        {
            if (s.equals(attackName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addAttack(String attackName)
    {
        attacksAvailable.add(attackName);
    }
    
    @Override
    public void refreshMe()
    {
        super.refreshMe();
        stamina = staminaMax;
    }
}
