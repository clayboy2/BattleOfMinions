/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import actors.AbstractPlaceable;
import actors.Unit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents anyone who plays this game
 * @author Austen Clay
 */
public class User implements Serializable{
    
    //Class variables
    private final String name;
    private ArrayList<Unit> barracks;
    private ArrayList<Unit> currentControlGroup;
    private String myCurrentColor;
    
    //Default Constructor
    public User()
    {
        this.name = "A.I. Opponent";
        currentControlGroup = new ArrayList<>();
        barracks = new ArrayList<>();
        myCurrentColor = "red";
    }
    
    public User(String name)
    {
        this.name = name;
        currentControlGroup = new ArrayList<>();
        barracks = new ArrayList<>();
        myCurrentColor = "red";
    }
    
    public User(String name, String color)
    {
        this.name = name;
        this.myCurrentColor = color;
        currentControlGroup = new ArrayList<>();
        barracks = new ArrayList<>();
    }
    
    //Sets this players control group
    public void setControlGroup(ArrayList<Unit> units)
    {
        currentControlGroup = units;
    }
    
    //Returns this players name
    public String getName()
    {
        return name;
    }
    
    //Returns this users control group
    public ArrayList<Unit> getControlGroup()
    {
        return currentControlGroup;
    }
    
    //Removes this users control group
    public void clearControlGroup()
    {
        currentControlGroup = new ArrayList<>();
    }
    
    //Removes a unit from this users control group
    public void unitDeath(Unit u)
    {
        currentControlGroup.remove(u);
    }
    
    //Signals if this unit is out of units
    public boolean outOfUnits()
    {
        return currentControlGroup.isEmpty();
    }
    
    //Signals if the specifed unit belongs to this user
    public boolean belongsToMe(AbstractPlaceable u)
    {
        for (Unit unit : currentControlGroup)
        {
            AbstractPlaceable ap = (AbstractPlaceable)u;
            AbstractPlaceable mine = (AbstractPlaceable)unit;
            if (mine.equals(ap))
            {
                return true;
            }
        }
        return false;
    }
    
    //Sets all of my unit's hasTurn variable to true
    public void startNewTurn()
    {
        for (Unit u : currentControlGroup)
        {
            u.resetTurn();
        }
    }
    
    //Sets my current game color
    public void setColor(String color)
    {
        this.myCurrentColor = color;
    }
    
    //Gets my color
    public String getColor()
    {
        return this.myCurrentColor;
    }
    
    //Saves units from the temporary group, to the barracks (if any)
    public void saveUnits()
    {
        for (Unit u : this.currentControlGroup)
        {
            barracks.add(u);
        }
        this.currentControlGroup = new ArrayList<>();
    }
    
    //Gets units from the barracks
    public ArrayList<Unit> getBarracks()
    {
        return this.barracks;
    }
    
    //Sets the default barracks
    public void setBarracks(ArrayList<Unit> barracks)
    {
        this.barracks = barracks;
    }
    
    
    public void removeUnitBarracks(Unit u)
    {
        this.barracks.remove(u);
    }
    
    public void addUnitBarracks(Unit u)
    {
        this.barracks.add(u);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof User)
        {
            User u = (User)o;
            return this.name.equals(u.name);
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
