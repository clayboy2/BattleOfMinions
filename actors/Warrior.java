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
public class Warrior extends AbstractPlaceable implements Unit{
    private boolean hasTurn;
    private boolean isStunned;

    public Warrior()
    {
        super("Dave",10,'W');
    }
    public Warrior(String name, int maxHP) {
        super(name, maxHP, 'W');
    }
    
    public Warrior(String name)
    {
        super(name, 10,'W');
    }
    
    public Warrior(String name, int maxHP, int UID)
    {
        super(name,maxHP,'W',UID);
    }
    
    @Override
    public int doDamage() {
        return Utils.makeRoll(6) + 2;
    }

    @Override
    public int makeAttack() {
        return 100;
    }

    @Override
    public boolean hasTurn() {
        return hasTurn;
    }

    @Override
    public void resetTurn() {
        hasTurn = true;
    }

    @Override
    public void takeTurn() {
        hasTurn = false;
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
    public boolean isUnit()
    {
        return true;
    }

    @Override
    public String getUnitType() {
        return "Warrior";
    }
}
