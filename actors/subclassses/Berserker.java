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
package actors.subclassses;

import actors.Placeable;
import actors.Unit;
import actors.Warrior;
import utils.Dice;

/**
 *
 * @author austen
 */
public class Berserker extends Warrior{
    
    private void init()
    {
        Dice attackDice = new Dice(100,100);
        Dice defenseDice = new Dice(1,12);
        Dice damageDice = new Dice(1,8);
        super.init(2, attackDice, defenseDice, damageDice, 30);
    }
    
    public Berserker()
    {
        super("Jerry",12,'B');
        init();
        super.addAttack("cleaving strike");
        super.addAttack("dash");
    }
    
    public Berserker(String name)
    {
        super(name,12,'B');
        init();
    }
    
    public Berserker(String name, int maxHP)
    {
        super(name,maxHP,'B');
        init();
    }
    
    public Berserker(String name, int maxHP, char token)
    {
        super(name, maxHP,token);
        init();
    }
    
    public Berserker(String name, int maxHP, char token, int UID)
    {
        super(name,maxHP,token,UID);
        init();
    }
    
    @Override
    public String getUnitType() {
        return "Berserker";
    }

    @Override
    public void levelUp() {
    }
}
