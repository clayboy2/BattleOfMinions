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

/**
 * This interface extends the placeable interface, and represents a unit
 * @author Austen Clay
 */
public interface Unit extends Placeable{
    
    public int doDamage();
    public int makeAttack();
    public boolean hasTurn();
    public void resetTurn();
    public void takeTurn();
    public boolean isStunned();
    public void stunMe();
    public void unStunMe();
    public String getUnitType();
    public int getCurrHP();
    public void levelUp();
    
}
