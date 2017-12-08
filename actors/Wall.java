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
public class Wall extends AbstractPlaceable {
    public Wall() {
        super("Wall", 100,'#');
    }
    
    @Override
    public boolean isWall()
    {
        return true;
    }
    
    public int makeDefense()
    {
        return 10;
    }
}
