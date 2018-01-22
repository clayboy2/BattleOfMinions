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

import actors.Unit;
import actors.Warrior;
import utils.Dice;

/**
 *
 * @author austen
 */
public class Samurai extends Warrior implements Unit{

    private void init()
    {
        Dice attack = new Dice(100,100);
        Dice defense = new Dice(1,10);
        Dice damage = new Dice(1,13);
        super.init(3, attack, defense, damage, 25);
    }
    
    public Samurai()
    {
        super("Jack", 15, 'S');
        init();
    }
    
    public Samurai(String name)
    {
        super(name,15,'S');
        init();
    }
    
    public Samurai(String name, int maxHP)
    {
        super(name,maxHP,'S');
        init();
    }
    
    public Samurai(int UID, String name)
    {
        super(name, 15, 'S', UID);
        init();
    }
    
    public Samurai(String name, int maxHP, char token, int UID)
    {
        super(name,maxHP,token,UID);
        init();
    }

    @Override
    public String getUnitType() {
        return "Samurai";
    }

    @Override
    public void levelUp() {
    }    
}
