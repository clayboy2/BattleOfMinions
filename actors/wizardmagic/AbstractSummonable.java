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

import actors.AbstractPlaceable;
import utils.IOPort;

/**
 *
 * @author austen
 */
public abstract class AbstractSummonable extends AbstractPlaceable implements Summonable{
    private final int summonerUID;
    private int turnsTillDesummon;
    
    public AbstractSummonable(int summonerUID, char token, int row, int col, int turnsTillDesummon, int maxHP, String name)
    {
        super(name, maxHP, token);
        super.setPosition(row, col);
        this.summonerUID = summonerUID;
        this.turnsTillDesummon = turnsTillDesummon;
    }
    
    @Override
    public int getSummoner() {
        return this.summonerUID;
    }

    @Override
    public boolean shouldDie() {
        return this.turnsTillDesummon==0;
    }
    
    @Override
    public boolean isStunned()
    {
        return true;
    }

    @Override
    public void takeTurn() {
        this.turnsTillDesummon--;
    }

    @Override
    public void removeMe() {
        
    }

    @Override
    public int makeDefense() {
        return 0;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AbstractSummonable)
        {
            AbstractSummonable as = (AbstractSummonable)o;
            return as.getUID()==this.getUID();
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
}
