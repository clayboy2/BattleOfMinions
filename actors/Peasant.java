/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import utils.Utils;

/**
 *
 * @author ninja
 */
public class Peasant extends AbstractPlaceable implements Unit{
    
    public Peasant()
    {
        super("Bob Farmer", 5,'P');
    }
    
    @Override
    public boolean isUnit()
    {
        return true;
    }

    @Override
    public int doDamage() {
        return Utils.makeRoll(3) + 1;
    }
    
    @Override
    public int makeDefense()
    {
        return Utils.makeRoll(3);
    }
    
    @Override
    public int makeAttack()
    {
        return Utils.makeRoll(3);
    }
}
