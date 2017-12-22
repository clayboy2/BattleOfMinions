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

import board.Board;
import java.io.Serializable;
import java.util.ArrayList;
import utils.Storage;
import utils.User;
import utils.Utils;

/**
 *
 * @author austen
 */
public class Wizard extends AbstractPlaceable implements Unit{
    private int numTurnsLeft;
    private final int numTurnsPerRound = 2;
    private boolean isStunned;
    private SpellBook myBook;
    private int upperBound;
    private int lowerBound;
    
    
    public Wizard()
    {
        super("Merlin", 6,'M');
        myBook = new SpellBook();
        upperBound = 4;
        lowerBound = 0;
    }
    
    public Wizard(String name)
    {
        super(name,6,'M');
        myBook = new SpellBook();
        upperBound = 4;
        lowerBound = 0;
    }
    
    public Wizard(String name, int maxHP)
    {
        super(name, maxHP,'M');
        myBook = new SpellBook();
        upperBound = 4;
        lowerBound = 0;
    }
    
    public Wizard(String name, int maxHP, int UID)
    {
        super(name,maxHP,'M',UID);
        myBook = new SpellBook();
        upperBound = 4;
        lowerBound = 0;
    }
    
    @Override
    public int doDamage() {
        return Utils.makeRoll(upperBound,lowerBound) + 1 + super.getDamageBuff();
    }

    @Override
    public int makeAttack() {
        return Utils.makeRoll(upperBound,lowerBound) + super.getAttackBuff();
    }

    @Override
    public int makeDefense()
    {
        return Utils.makeRoll(upperBound, lowerBound) + super.getDefenseBuff();
    }
    
    @Override
    public boolean hasTurn() {
        return numTurnsLeft>=1;
    }

    @Override
    public void resetTurn() {
        numTurnsLeft = this.numTurnsPerRound;
    }

    @Override
    public void takeTurn() {
        numTurnsLeft--;
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
    public String getUnitType() {
        return "Wizard";
    }
    
    @Override
    public boolean isUnit()
    {
        return true;
    }
    
    public int castSpell(String name, Placeable target, String type)
    {
        int manaSpent = myBook.castSpell(name, target, type);
        this.numTurnsLeft = 0;
        return manaSpent;
    }
    
    public SpellBook getSpellBook()
    {
        return myBook;
    }

    @Override
    public void levelUp() {
        super.levelUp(1);
        myBook.levelUp();
        upperBound++;
    }
    
    public class SpellBook implements Serializable
    {
        private int spellPoints;
        private int spellPointMax;
        private int spellStrength;
        
        
        public SpellBook()
        {
            spellPointMax = 20;
            spellPoints = spellPointMax;
            spellStrength = 2;
        }
        
        public int getSpellPointMax()
        {
            return spellPointMax;
        }
        
        public int castSpell(String spell, Placeable target,String type)
        {
            int pointsSpent;
            switch(spell)
            {
                case "stun":
                    pointsSpent = stun(target);
                    break;
                case "buff":
                    pointsSpent = buff(target,type);
                    break;
                case "debuff":
                    pointsSpent = debuff(target,type);
                    break;
                case "attack":
                    pointsSpent = attack(target,type);
                    break;
                default:
                    System.out.println("Invalid Spell");
                    pointsSpent = 0;
                    break;
            }
            spellPoints -= pointsSpent;
            return pointsSpent;
        }
        
        public int stun(Placeable target)
        {
            int cost = 2;
            if (this.spellPoints<cost)
            {
                System.out.println("Not enough spell points");
                return 0;
            }
            if (!(target instanceof Unit))
            {
                System.out.println("Must target unit");
                return 0;
            }
            Unit toStun = (Unit)target;
            toStun.stunMe();
            return cost;
        }
        
        public int buff(Placeable target,String type)
        {
            int cost = 5;
            if (this.spellPoints<cost)
            {
                System.out.println("Not enough spell points");
                return 0;
            }
            if (!(target instanceof Unit))
            {
                System.out.println("Must target unit");
            }
            AbstractPlaceable spellTarget = (AbstractPlaceable)target;
            switch(type)
            {
                /*
                case "attack":
                    spellTarget.setAttackBuff(this.spellStrength);
                    break;
                */
                case "damage":
                    spellTarget.setDamageBuff(this.spellStrength);
                    break;
                case "defense":
                    spellTarget.setDefenseBuff(this.spellStrength);
                    break;
                default:
                    System.out.println("Invalid option");
                    return 0;
            }
            return cost;
        }
        
        public int attack(Placeable target,String type)
        {
            int convert = Integer.parseInt(type);
            if (this.spellPoints<convert)
            {
                System.out.println("Not enough spell points");
                return 0;
            }
            int damage = convert*spellStrength;
            target.takeDamage(damage);
            if (target.isDead())
            {
                Board b = Storage.getBoard();
                b.removeUnit(target);
                for (User u : b.getPlayers())
                {
                    if (u.belongsToMe((AbstractPlaceable)target))
                    {
                        u.getControlGroup().remove((Unit)target);
                    }
                }
                target.setPosition(-1, -1);
            }
            return convert;
        }
        
        public int debuff(Placeable target, String type)
        {
            int cost = 5;
            if (this.spellPoints<cost)
            {
                System.out.println("Not enough mana");
                return 0;
            }
            if (!(target instanceof Unit))
            {
                System.out.println("You must target a unit");
                return 0;
            }
            AbstractPlaceable spellTarget = (AbstractPlaceable)target;
            switch(type.toLowerCase())
            {
                case "attack":
                    spellTarget.setAttackBuff(-1*(spellStrength));
                    break;
                case "defense":
                    spellTarget.setDefenseBuff(-1*(spellStrength));
                    break;
                case "damage":
                    spellTarget.setDamageBuff(-1*(spellStrength));
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
            return cost;
        }
        
        public void restoreMana()
        {
            this.spellPoints = this.spellPointMax;
        }
        
        public void restoreMana(int amount)
        {
            this.spellPoints+=amount;
        }
        
        public void levelUp()
        {
            this.spellPointMax =+ 2;
            this.spellPoints = this.spellPointMax;
            this.spellStrength =+ 1;
        }
    }
}
