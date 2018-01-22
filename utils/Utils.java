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
package utils;

import actors.AbstractPlaceable;
import actors.Archer;
import actors.Peasant;
import actors.Unit;
import actors.Warrior;
import actors.Wizard;
import actors.subclassses.Samurai;
import board.Board;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A set of Utilites that the program runs with
 * @author ninja
 */
public class Utils {
    //Define correlations between cardinal directions and integers
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    
    //Makes a roll with the specified upper bound
    public static int makeRoll(int upperBound,int lowerBound)
    {
        int roll = (int)(Math.random()*upperBound+1-lowerBound)+lowerBound;
        return roll;
    }
    
    //Randomly places units on the board
    public static ArrayList<Unit> randomPlacement(Board b, int direction, boolean isAI)
    {
        ArrayList<Unit> toReturn = new ArrayList<>();
        Scanner keys = new Scanner(System.in);
        for (int i = 0;i<5;i++)
        {
            while(true)
            {
                int row, col, rows = b.getRows(), cols = b.getCols();
                switch (direction)
                {
                    case NORTH:
                        row = ((int)(Math.random()*5)+1);
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case EAST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+(cols-6));
                        break;
                    case SOUTH:
                        row = ((int)(Math.random()*5)+(rows-6));
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case WEST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+1);
                        break;
                    default:
                        return null;
                }
                if (b.isEmptySpace(row, col))
                {
                    Unit u = new Peasant();
                    if (!isAI)
                    {
                        System.out.print("Name your peasant: ");
                        String name = keys.nextLine();
                        u = new Peasant(name);
                    }
                    toReturn.add(u);
                    u.setPosition(row, col);
                    b.placePlaceable(row, col, u);
                    break;
                }
            }
        }
        return toReturn;
    }
    public static boolean randomPlacement(Board b, ArrayList<Unit> unitsToPlace, int direction)
    {
        for (Unit u : unitsToPlace)
        {
            while(true)
            {
                int row, col, rows = b.getRows(), cols = b.getCols();
                switch (direction)
                {
                    case NORTH:
                        row = ((int)(Math.random()*5)+1);
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case EAST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+(cols-6));
                        break;
                    case SOUTH:
                        row = ((int)(Math.random()*5)+(rows-6));
                        col = ((int)(Math.random()*5)+(cols/2)-2);
                        break;
                    case WEST:
                        row = ((int)(Math.random()*5)+(rows/2)-2);
                        col = ((int)(Math.random()*5)+1);
                        break;
                    default:
                        return false;
                }
                if (b.isEmptySpace(row, col))
                {
                    u.setPosition(row, col);
                    b.placePlaceable(row, col, u);
                    break;
                }
            }
        }
        return true;
    }
    
    public static void fileCheck() {
        File resourceFolder = new File("resources");
        if (!resourceFolder.exists()) {
            resourceFolder.mkdir();
        }
        File userList = new File("resources/users.bin");
        File idCounter = new File("resources/id.bin");
        File replays = new File("resources/replay.bin");
        try {
            if (!idCounter.exists()) {
                idCounter.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(idCounter));
                ArrayList<Integer> id = new ArrayList<>();
                id.add(0);
                out.writeObject(id);
                out.close();
            }
            if (!userList.exists()) {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userList));
                ArrayList<User> users = new ArrayList<>();
                users.add(new User());
                out.writeObject(users);
                out.close();
            }
            if (!replays.exists())
            {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(replays));
                ArrayList<Replay> rp = new ArrayList<>();
                out.writeObject(rp);
            }
        } catch (EOFException e) {
            //Ignore
        } catch (IOException e) {

        }
    }
    
    public static void resetFiles()
    {
        File userList = new File("resources/users.bin");
        File idCounter = new File("resources/id.bin");
        File replays = new File("resources/replay.bin");
        try
        {
            ArrayList<User> users = new ArrayList<>();
            ArrayList<Replay> replayList = new ArrayList<>();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userList));
            out.writeObject(users);
            out.close();
            out = new ObjectOutputStream(new FileOutputStream(idCounter));
            ArrayList<Integer> globalID = new ArrayList<>();
            globalID.add(0);
            out.writeObject(globalID);
            out.close();
            out = new ObjectOutputStream(new FileOutputStream(replays));
            out.writeObject(replayList);
            out.close();
            System.out.println("Success");
        }
        catch(IOException e)
        {
            System.out.println("Error resetting files");
        }
    }
    
    public static class Replay implements Serializable
    {
        //First index is for each game step. The next two Fields are for row and column, respectivly
        private final ArrayList<char[][]> gameSteps;
        private int uid;
        
        public Replay()
        {
            gameSteps = new ArrayList<>();
        }
        
        public void saveStep(Board b)
        {
            char[][] snapshot = new char[b.getRows()][b.getCols()];
            for (int row = 0; row<b.getRows()-1; row++)
            {
                for (int col = 0; col<b.getCols()-1;col++)
                {
                    snapshot[row][col] = b.getTokenAt(row, col);
                }
            }
            gameSteps.add(snapshot);
        }
        
        public void saveReplay()
        {
            ArrayList<Replay> replays = (ArrayList<Replay>)FileIO.readData(new File("resources/replay.bin"));
            replays.add(this);
            FileIO.writeData(replays,new File("resources/replay.bin"),false);
        }
    }
    
    public static class UnitUpgrader
    {
        //Displays Upgrades
        public static ArrayList<String> upgradeOptions(Unit toUpgrade)
        {
            ArrayList<String> toReturn = new ArrayList<>();
            switch(toUpgrade.getUnitType())
            {
                case "Peasant":
                    toReturn.add("Warrior");
                    toReturn.add("Wizard");
                    toReturn.add("Archer");
                    break;
                case "Warrior":
                    if (((AbstractPlaceable)toUpgrade).getLevel()>=5)
                    {
                        toReturn.add("Samurai");
                    }
                    else
                    {
                        toReturn.add("Can't Upgrade");
                    }
                    break;
                default:
                    toReturn.add("Can't Upgrade");
                    break;
            }
            return toReturn;
        }
        
        public static Unit upgrade(Unit target, String upgradeTo)
        {
            Unit u = target;
            ArrayList<String> possibleUpgrades = upgradeOptions(target);
            if (possibleUpgrades.contains("Can't Upgrade"))
            {
                return target;
            }
            if (!possibleUpgrades.contains(upgradeTo))
            {
                return target;
            }
            switch(upgradeTo)
            {
                case "Samurai":
                    u = new Samurai(target.getUID(), target.getName());
                    break;
                case "Warrior":
                    u = new Warrior(target.getUID(), target.getName());
                    break;
                case "Wizard":
                    u = new Wizard(target.getUID(), target.getName());
                    break;
                case "Archer":
                    u = new Archer(target.getUID(), target.getName());
                    break;
                default:
                    break;
            }
            return u;
        }
    }
}
