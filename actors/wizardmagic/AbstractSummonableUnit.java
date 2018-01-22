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
package actors.wizardmagic;

import actors.Unit;
import utils.Utils;

/**
 *
 * @author austen
 */
public abstract class AbstractSummonableUnit extends AbstractSummonable implements Unit{
    private int upperRoll;
    private int lowerRoll;
    private int turnsLeft;
    private int maxTurns;
    private boolean isStunned;
    private boolean isHasted;
    private int attackBuff;
    private int defenseBuff;
    private int damageBuff;
    
    public AbstractSummonableUnit(int summonerUID, char token, int row, int col, int turnsTillDesummon, int maxHP, String name, int upperRoll, int lowerRoll, int maxTurns) {
        super(summonerUID, token, row, col, turnsTillDesummon, maxHP, name);
        this.upperRoll = upperRoll;
        this.lowerRoll = lowerRoll;
        this.maxTurns = maxTurns;
        this.turnsLeft = maxTurns;
        isStunned = false;
        isHasted = false;
        this.attackBuff = 0;
        this.damageBuff = 0;
        this.defenseBuff = 0;
    }

    @Override
    public int doDamage() {
        return Utils.makeRoll(upperRoll, lowerRoll);
    }

    @Override
    public int makeAttack() {
        return Utils.makeRoll(upperRoll, lowerRoll);
    }

    @Override
    public boolean hasTurn() {
        return turnsLeft > 0;
    }

    @Override
    public void resetTurn() {
        if (this.isHasted)
        {
            this.turnsLeft += this.maxTurns;
            this.isHasted = false;
        }
        else
        {
            this.turnsLeft = this.maxTurns;
        }
    }

    @Override
    public void takeTurn() {
        super.takeTurn();
        turnsLeft--;
    }

    @Override
    public boolean isStunned() {
        return this.isStunned;
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
    public void hasteMe(int amount) {
        turnsLeft += amount;
        isHasted = true;
    }

    @Override
    public String[] getBuffInfo() {
        String[] toReturn = new String[3];
        toReturn[0] = "-Attack Buff: "+this.attackBuff;
        toReturn[1] = "-Defense Buff: "+this.defenseBuff;
        toReturn[2] = "-Damage Buff: "+this.damageBuff;
        return toReturn;
    }

    @Override
    public boolean isUnit() {
        return true;
    }

}
