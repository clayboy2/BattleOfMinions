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

import actors.Archer;
import utils.Dice;

/**
 *
 * @author austen
 */
public class Arbalist extends Archer{
    
    private void init()
    {
        Dice attack = new Dice(1,12);
        Dice defense = new Dice(1,8);
        Dice damage = new Dice(12,24);
        super.init(2, attack, defense, damage);
    }
    
    public Arbalist()
    {
        super("Ch Ko Nu",10,'X');
        init();
    }
    
    public Arbalist(String name)
    {
        super(name,10,'X');
        init();
    }
    
    public Arbalist(String name, int uid)
    {
        super(name,10,'X',uid);
        init();
    }
    
    public Arbalist(String name, int maxHP, char token, int uid)
    {
        super(name,maxHP,token, uid);
        init();
    }

    @Override
    public int makeAttack() {
        while (this.hasTurn())
        {
            this.takeTurn();
        }
        return super.makeAttack();
    }
    
    @Override
    public String getUnitType()
    {
        return "Arbalist";
    }
}
