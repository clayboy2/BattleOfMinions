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
public interface Placeable {
    
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
    
}
