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
public interface Unit extends Placeable{
    public int doDamage();
    public int makeAttack();
}
