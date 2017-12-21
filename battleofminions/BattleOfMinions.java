/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleofminions;

import actors.Peasant;
import actors.Placeable;
import actors.Unit;
import actors.Warrior;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import utils.IOPort;
import utils.User;

/**
 * Handles program launch
 * @author Austen Clay
 */
public class BattleOfMinions {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        fileCheck();
        SimpleMode sm = new SimpleMode();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<User> existingUsers = IOPort.readUsers();
        ArrayList<String> argsList = new ArrayList<>();
        if (args.length>=1)
        {
            System.out.println("Checking args");
            for (String s : args)
            {
                argsList.add(s);
            }
            switch(args[0])
            {
                case "upgrade":
                    System.out.println("Upgrading");
                    argsList.remove(0);
                    for (String name : argsList)
                    {
                        User toCheck = new User(name);
                        for (User user : existingUsers)
                        {
                            if (user.equals(toCheck))
                            {
                                System.out.println("Adding user "+name+" to upgrade list");
                                users.add(user);
                            }
                        }
                    }
                    BattleOfMinions.upgradeUnits(users);
                    System.exit(0);
                case "userprefs":
                    argsList.remove(0);
                    for (String name : argsList)
                    {
                        User toCheck = new User(name);
                        for (User user : existingUsers)
                        {
                            if (user.equals(toCheck))
                            {
                                users.add(user);
                            }
                        }
                    }
                    BattleOfMinions.userPrefs(users);
                    System.exit(0);
                case "view":
                    argsList.remove(0);
                    for (String name : argsList)
                    {
                        User toCheck = new User(name);
                        for (User user : existingUsers)
                        {
                            if (user.equals(toCheck))
                            {
                                users.add(user);
                            }
                        }
                    }
                    BattleOfMinions.viewUnits(users);
                    System.exit(0);
                case "admin":
                    runAdmin();
                    System.exit(0);
                default:
                    break;
            }
        }
        if (argsList.isEmpty())
        {
            User u1 = new User();
            u1.setColor("red");
            User u2 = new User();
            u2.setColor("blue");
            users.add(u1);
            users.add(u2);
            sm.runGame(users);
        }
        else if (argsList.size() == 2)
        {
            User u = new User(args[0],args[1]);
            if (existingUsers.contains(u));
            {
                System.out.println("Loading existing user: "+u.getName());
                users.add(existingUsers.get(existingUsers.indexOf(u)));
            }
            u = new User();
            users.add(u);
        }
        else if (argsList.size()%2!=0)
        {
            for (String s : args)
            {
                System.out.println(s);
            }
        }
        else 
        {
            for (int i = 0;i<args.length;i+=2)
            {
                User u = new User(args[i],args[i+1]);
                if (existingUsers.contains(u))
                {
                    users.add(existingUsers.get(existingUsers.indexOf(u)));
                    System.out.println("Loading existing user: "+u.getName());
                }
                else
                {
                    System.out.println("Creating new user: "+u.getName());
                    users.add(u);
                }
            }
            sm.runGame(users);
        }
    }
    
    public static void userPrefs(ArrayList<User> users)
    {
        
    }
    
    public static void fileCheck()
    {
        File resourceFolder = new File("resources");
        if (!resourceFolder.exists())
        {
            resourceFolder.mkdir();
        }
        File userList = new File("resources/users.bin");
        File idCounter = new File("resources/id.bin");
        try
        {
            if (!idCounter.exists())
            {
                idCounter.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(idCounter));
                ArrayList<Integer> id = new ArrayList<>();
                id.add(0);
                out.writeObject(id);
                out.close();
            }
            if (!userList.exists())
            {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userList));
                ArrayList<User> users = new ArrayList<>();
                users.add(new User());
                out.writeObject(users);
                out.close();
            }
        }
        catch(IOException e)
        {
            
        }
    }
    
    public static void upgradeUnits(ArrayList<User> users)
    {
        Scanner keys = new Scanner(System.in);
        for (User u : users)
        {
            System.out.println("Upgrading units for "+u.getName());
            ArrayList<Unit> upgradedUnits = new ArrayList<>();
            ArrayList<Unit> currentBarracks = u.getBarracks();
            if (currentBarracks.isEmpty())
            {
                System.out.println(u.getName()+" has no units to upgrade.");
                continue;
            }
            for (Unit currentUnit : currentBarracks)
            {
                if (currentUnit.getUnitType().equals("Peasant"))
                {
                    System.out.println("Turn "+currentUnit.getName()+" into a warrior?");
                    String input = keys.nextLine().toLowerCase();
                    if (input.equals("yes"))
                    {
                        Unit unit = new Warrior(((Peasant) currentUnit).getName(),10,((Placeable)currentUnit).getUID());
                        upgradedUnits.add(unit);
                    }
                }
            }
            currentBarracks = upgradedUnits;
            u.setBarracks(currentBarracks);
            for (Unit cu : u.getBarracks())
            {
                System.out.println("Name: "+cu.getName()+"| Class: "+cu.getUnitType()+"| ID: "+cu.hashCode());
            }
        }
        IOPort.writeUsers(users);
    }
   
    public static void viewUnits(ArrayList<User> users)
    {
        for (User u : users)
        {
            System.out.println(u.getName()+" is viewing units.");
            for (Unit unit : u.getBarracks())
            {
                System.out.println("Name: "+unit.getName()+"|Class: "+unit.getUnitType());
            }
        }
    }
    
    public static void runAdmin()
    {
        ArrayList<User> users = IOPort.readUsers();
        Scanner keys = new Scanner(System.in);
        while (true)
        {
            System.out.println("Enter a user to edit from the list: ");
            for (User currentUser : users)
            {
                System.out.println(currentUser.getName());
            }
            String input = keys.nextLine().toLowerCase();
            for (User currentUser : users)
            {
                if (currentUser.equals(new User(input)))
                {
                    System.out.println("Modifying "+input);
                    editUser(currentUser);
                    break;
                }
            }
            System.out.println("Exit?");
            if (keys.nextLine().toLowerCase().equals("yes"))
            {
                break;
            }
        }
    }
    
    public static void editUser(User toEdit)
    {
        Scanner keys = new Scanner(System.in);
        System.out.println("What would you like to do with "+toEdit.getName());
        String input = keys.nextLine().toLowerCase();
        while (!input.equals("exit"))
        {
            
            switch (input)
            {
                case "add unit":
                    System.out.println("Enter the name of the new unit: ");
                    String name = keys.nextLine();
                    System.out.println("Enter a unit type");
                    boolean shouldBreak = false;
                    Placeable p;
                    while (!shouldBreak)
                    {
                        switch(keys.nextLine().toLowerCase())
                        {
                            case "peasant":
                                p = new Peasant(name);
                                shouldBreak = true;
                                break;
                            case "warrior":
                                shouldBreak = true;
                                p = new Warrior(name);
                                break;
                            default:
                                System.out.println("Type not recognized");
                                break;
                        }
                    }
                    break;
                case "remove unit":
                    System.out.println("Enter the ID of the unit to replace: ");
                    for (Unit unit : toEdit.getControlGroup())
                    break;
                case "view units":
                    break;
                default:
                    break;
            }
        }
    }
}
