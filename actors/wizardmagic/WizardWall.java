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

import board.Board;
import utils.Storage;

/**
 *
 * @author austen
 */
public class WizardWall extends AbstractSummonable{

    public WizardWall(int summonerUID, int spellStrength, int row, int col) {
        super(summonerUID, '!', row, col, -1, spellStrength*5, "Magic Wall");
    }
    
    @Override
    public void takeTurn()
    {
        
    }
    
    @Override
    public void removeMe()
    {
        Board b = Storage.getBoard();
        b.removeUnit(this);
    }
    
    @Override
    public boolean isWall() {
        return true;
    }
    
    @Override
    public int makeDefense() {
        return 5;
    }

    public String getUnitType() {
        return "Wizard Wall";
    }
}
