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

import java.io.Serializable;

/**
 * This interface defines what any object on the game board needs to do
 * @author Austen Clay
 */
public interface Placeable extends Serializable{
    
    public char getToken();
    public int takeDamage(int amount);
    public boolean isDead();
    public boolean isWall();
    public boolean isEmptySpace();
    public boolean isUnit();
    public int makeDefense();
    public void setPosition(int row, int col);
    public int getRow();
    public int getCol();
    public String getName();
    @Override
    public int hashCode();
    public int getUID();
}
