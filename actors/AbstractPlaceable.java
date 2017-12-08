/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

/**
 *
 * @author ninja
 */
public abstract class AbstractPlaceable implements Placeable{
    private int maxHP;
    private int currHP;
    private String name;
    private int myRow;
    private int myCol;
    private final char token;
    
    public AbstractPlaceable(String name, int maxHP,char token)
    {
        this.name = name;
        this.maxHP = maxHP;
        this.currHP = this.maxHP;
        this.token = token;
    }
    
    public int getRow()
    {
        return myRow;
    }
    
    public int getCol()
    {
        return myCol;
    }
    public char getToken()
    {
        return this.token;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getMaxHP()
    {
        return maxHP;
    }
    
    public int getCurrHP()
    {
        return currHP;
    }
    
    @Override
    public int takeDamage(int amount) {
        currHP -= amount;
        if (currHP<0)
        {
            currHP = 0;
        }
        return amount;
    }

    @Override
    public boolean isDead() {
        return currHP<=0;
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public boolean isEmptySpace() {
        return false;
    }

    @Override
    public boolean isUnit() {
        return false;
    }
    
    @Override
    public int makeDefense()
    {
        return 0;
    }
    
    public void restoreHealth()
    {
        currHP = maxHP;
    }
    
    public void heal(int amount)
    {
        currHP =+ amount;
        if (currHP>maxHP)
            currHP = maxHP;
    }
    
    public void setPosition(int row, int col)
    {
        this.myCol = col;
        this.myRow = row;
    }
        
}
