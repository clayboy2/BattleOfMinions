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

import actors.wizardmagic.Summonable;
import actors.wizardmagic.WizardWall;
import board.Board;
import java.io.Serializable;
import java.util.ArrayList;
import utils.Dice;
import utils.Storage;

/**
 *
 * @author austen
 */
public class Wizard extends AbstractPlaceableUnit{
    private SpellBook myBook;
    protected ArrayList<Summonable> mySummons;
    
    @Override
    protected void init(int turnsPerRound, Dice attackDice, Dice defenseDice, Dice damageDice)
    {
        super.init(turnsPerRound, attackDice, defenseDice, damageDice);
        myBook = new SpellBook(this);
        mySummons = new ArrayList<>();
    }
    
    private void init()
    {
        Dice attack = new Dice(1,6);
        Dice defense = new Dice(1,6);
        Dice damage = new Dice(1,4);
        this.init(2, attack, defense, damage);
    }
    
    public Wizard()
    {
        super("Merlin", 6,'M');
        init();
    }
    
    public Wizard(String name)
    {
        super(name,6,'M');
        init();
    }
    
    public Wizard(String name, int maxHP)
    {
        super(name, maxHP,'M');
        init();
    }
    
    public Wizard(int UID, String name)
    {
        super(name,6,'W',UID);
        init();
    }
    
    public Wizard(String name, int maxHP, int UID)
    {
        super(name,maxHP,'M',UID);
        init();
    }

    @Override
    public String getUnitType() {
        return "Wizard";
    }
    
    public void addSummon(Summonable s)
    {
        this.mySummons.add(s);
    }
    
    public void removeSummon(Summonable s)
    {
        this.mySummons.remove(s);
    }
    
    public ArrayList<Summonable> getSummons()
    {
        return mySummons;
    }
    
    public int castSpell(String name, Placeable target, String type)
    {
        int manaSpent = myBook.castSpell(name, target, type);
        this.endTurn();
        System.out.println("You have "+myBook.getSpellPoints()+" Mana remaining.");
        if (manaSpent != 0)
        {
            System.out.println("Spell cast successfully!");
        }
        return manaSpent;
    }
    
    public SpellBook getSpellBook()
    {
        return myBook;
    }
    @Override
    public void levelUp() {
        
        myBook.levelUp(myLevel);
        super.levelUp(1, 1);
        refreshMe();
    }
    
    @Override
    public void refreshMe()
    {
        super.refreshMe();
        myBook.restoreMana();
        mySummons = new ArrayList<>();
    }
    
    public class SpellBook implements Serializable
    {
        private int manaRemaining;
        private int manaMax;
        private int spellStrength;
        private final Wizard owner;
        
        
        public SpellBook(Wizard owner)
        {
            manaMax = 20;
            manaRemaining = manaMax;
            spellStrength = 2;
            this.owner = owner;
        }
        
        public int getSpellPointMax()
        {
            return manaMax;
        }
        
        public int getSpellPoints()
        {
            return manaRemaining;
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
                case "heal":
                    pointsSpent = heal((AbstractPlaceable)target,type);
                    break;
                case "summon":
                    pointsSpent = summon(type);
                    break;
                case "haste":
                    pointsSpent = haste(target);
                    break;
                default:
                    System.out.println("Invalid Spell");
                    pointsSpent = 0;
                    break;
            }
            manaRemaining -= pointsSpent;
            return pointsSpent;
        }
        
        public int stun(Placeable target)
        {
            int cost = 2;
            if (this.manaRemaining<cost)
            {
                System.out.println("Not enough Mana");
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
            if (this.manaRemaining<cost)
            {
                System.out.println("Not enough Mana");
                return 0;
            }
            if (!(target instanceof Unit))
            {
                System.out.println("Must target unit");
            }
            AbstractPlaceableUnit spellTarget = (AbstractPlaceableUnit)target;
            switch(type)
            {
                
                case "attack":
                    spellTarget.setAttackBuff(this.spellStrength);
                    break;
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
        
        public int heal(AbstractPlaceable target,String type)
        {
            int convert = Integer.parseInt(type);
            if (this.manaRemaining<convert)
            {
                System.out.println("Not enough Mana");
                return 0;
            }
            int healPower = convert*spellStrength;
            target.heal(healPower);
            return convert;
        }
        
        public int debuff(Placeable target, String type)
        {
            int cost = 5;
            if (this.manaRemaining<cost)
            {
                System.out.println("Not enough Mana");
                return 0;
            }
            if (!(target instanceof Unit))
            {
                System.out.println("You must target a unit");
                return 0;
            }
            AbstractPlaceableUnit spellTarget = (AbstractPlaceableUnit)target;
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
            this.manaRemaining = this.manaMax;
        }
        
        public void restoreMana(int amount)
        {
            this.manaRemaining+=amount;
            if (this.manaRemaining > this.manaMax)
            {
                this.manaRemaining = this.manaMax;
            }
        }
        
        public void levelUp(int myLevel)
        {
            this.manaMax =+ 2;
            this.manaRemaining = this.manaMax;
            this.spellStrength =+ 1;
        }
        
        public int summon(String type)
        {
            String summon;
            String direction;
            if (!type.contains(","))
            {
                System.out.println("Spell failed");
                return 0;
            }
            else
            {
                summon = type.substring(0, type.indexOf(","));
                direction = type.substring(type.indexOf(",")+1);
            }
            int pointsSpent = 0;
            Board b = Storage.getBoard();
            int row = owner.getRow();
            int col = owner.getCol();
            System.out.println("Attempting to summon: "+summon);
            System.out.println("Attempting to summon towards: "+direction);
            switch (summon)
            {
                case "wall":
                    int cost = 3;
                    if (this.manaRemaining<cost)
                    {
                        System.out.println("You don't have enough spell points");
                        return 0;
                    }
                    pointsSpent = cost;
                        WizardWall wall = new WizardWall(owner.getUID(), spellStrength,-1,-1);
                        WizardWall wall2 = new WizardWall(owner.getUID(),spellStrength,-1,-1);
                        WizardWall wall3 = new WizardWall(owner.getUID(),spellStrength,-1,-1);
                        owner.mySummons.add(wall);
                        owner.mySummons.add(wall2);
                        owner.mySummons.add(wall3);
                        System.out.println("Summoned wall with "+wall.getMaxHP()+" HP");
                        {
                            switch(direction)
                            {
                                case "w": //North
                                    row--;
                                    wall.setPosition(row, col-1);
                                    wall2.setPosition(row, col);
                                    wall3.setPosition(row, col+1);
                                    if (b.isEmptySpace(row, col-1))
                                    {
                                        b.placePlaceable(row, col-1, wall);
                                    }
                                    if (b.isEmptySpace(row,col))
                                    {
                                        b.placePlaceable(row, col, wall2);
                                    }
                                    if (b.isEmptySpace(row, col+1));
                                    {
                                        b.placePlaceable(row, col+1, wall3);
                                    }
                                    break;
                                case "d": //East
                                    col++;
                                    wall.setPosition(row-1, col);
                                    wall2.setPosition(row, col);
                                    wall3.setPosition(row+1, col);
                                    if (b.isEmptySpace(row-1, col))
                                    {
                                        b.placePlaceable(row-1, col, wall);
                                    }
                                    if (b.isEmptySpace(row,col))
                                    {
                                        b.placePlaceable(row, col, wall2);
                                    }
                                    if (b.isEmptySpace(row+1, col));
                                    {
                                        b.placePlaceable(row+1, col, wall3);
                                    }
                                    break;
                                case "s": //South
                                    row++;
                                    wall.setPosition(row,col-1);
                                    wall2.setPosition(row,col);
                                    wall3.setPosition(row, col+1);
                                    if (b.isEmptySpace(row, col-1))
                                    {
                                        b.placePlaceable(row, col-1, wall);
                                    }
                                    if (b.isEmptySpace(row,col))
                                    {
                                        b.placePlaceable(row, col, wall2);
                                    }
                                    if (b.isEmptySpace(row, col+1));
                                    {
                                        b.placePlaceable(row, col+1, wall3);
                                    }
                                    break;
                                case "a": //West
                                    col--;
                                    wall.setPosition(row-1, col);
                                    wall2.setPosition(row, col);
                                    wall3.setPosition(row+1, col);
                                    if (b.isEmptySpace(row-1, col))
                                    {
                                        b.placePlaceable(row-1, col, wall);
                                    }
                                    if (b.isEmptySpace(row,col))
                                    {
                                        b.placePlaceable(row, col, wall2);
                                    }
                                    if (b.isEmptySpace(row+1, col));
                                    {
                                        b.placePlaceable(row+1, col, wall3);
                                    }
                                    break;
                            }
                        }
                    break;
                case "":
                    break;
                default:
                    break;
            }
            return pointsSpent;
        }
        
        public int haste(Placeable target)
        {
            int pointsSpent;
            int cost = 3;
            if (this.manaRemaining<cost)
            {
                System.out.println("You don't have enough spell points.");
                return 0;
            }
            pointsSpent = cost;
            if (target instanceof Unit)
            {
                System.out.println("Hasting " + target.getName());
                Unit u = (Unit)target;
                u.hasteMe(this.spellStrength);
            }
            else
            {
                System.out.println("Error casting haste");
                return 0;
            }
            return pointsSpent;
        }
    }
}
