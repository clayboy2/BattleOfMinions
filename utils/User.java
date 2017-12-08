/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import actors.Unit;
import java.util.ArrayList;

/**
 *
 * @author ninja
 */
public class User {
    private String name;
    private ArrayList<Unit> currentControlGroup;
    
    public User()
    {
        this.name = "A.I. Opponent";
    }
    
    public void setControlGroup(ArrayList<Unit> units)
    {
        currentControlGroup = units;
    }
    
    public String getName()
    {
        return name;
    }
    
    public ArrayList<Unit> getControlGroup()
    {
        return currentControlGroup;
    }
    
    public void clearControlGroup()
    {
        currentControlGroup = new ArrayList<>();
    }
    
    public void unitDeath(Unit u)
    {
        currentControlGroup.remove(u);
    }
    
    public boolean outOfUnits()
    {
        return currentControlGroup.isEmpty();
    }
    
    public boolean belongsToMe(Unit u)
    {
        return currentControlGroup.indexOf(u)!=-1;
    }
}
