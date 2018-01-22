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
import utils.Utils;

/**
 * This class represents the most basic battle unit. 
 * @author Austen Clay
 */
public class Peasant extends AbstractPlaceableUnit{
    Dice defaultDice;
    //Default Constructor
    private void init()
    {
        defaultDice = new Dice(1,4);
        super.init(1,defaultDice,defaultDice,defaultDice);
    }
    
    public Peasant()
    {
        super("Bob Farmer", 5,'P');
        init();
    }
    
    public Peasant(int UID)
    {
        super("Bob Farmer", 5, 'P',UID);
        init();
    }
    
    //Named peasant
    public Peasant(String name)
    {
        super(name, 5, 'P');
        init();
    }

    @Override
    public String getUnitType() {
        return "Peasant";
    }
    
    @Override
    public void levelUp()
    {
        
    }
}
