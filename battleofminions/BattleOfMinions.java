/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleofminions;

import actors.AbstractPlaceable;
import actors.Archer;
import actors.Peasant;
import actors.Unit;
import actors.Warrior;
import actors.Wizard;
import actors.subclassses.Arbalist;
import actors.subclassses.Berserker;
import actors.subclassses.Samurai;
import board.Board;
import java.util.ArrayList;
import java.util.Scanner;
import utils.FileIO;
import utils.User;
import utils.Utils;
import utils.Utils.UnitUpgrader;

/**
 * Handles program launch
 *
 * @author Austen Clay
 */
public class BattleOfMinions
{

    private static final Scanner keys = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Utils.fileCheck();
        System.out.println("Launching Battle of Minions...");
        System.out.println("Test Graphical Mode?");
        if(keys.nextLine().toLowerCase().equals("yes"))
        {
            
        }
        while (true)
        {
            System.out.println("Please select an option from the menu:");
            generateMenu();
            System.out.println("Please select a number from the menu:");
            try
            {
                int choice = Integer.parseInt(keys.nextLine());
                switch (choice)
                {
                    case 1:
                        prepGame();
                        break;
                    case 2:
                        runAdmin();
                        break;
                    case 3:
                        prepUpgrader();
                        break;
                    case 4:
                        Utils.resetFiles();
                        break;
                    case 5:
                        System.exit(0);
                    default:
                        System.out.println("Invalid Option.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Input must be an integer");
            }
        }
    }

    public static void prepGame()
    {
        Board b = new Board(new ArrayList<>());
        int count = 0;
        String choice = "";
        ArrayList<User> players = new ArrayList<>();
        while (count < 4 && !choice.equals("stop"))
        {
            ArrayList<User> existingUsers = FileIO.readUsers();
            for (User u : existingUsers)
            {
                System.out.println(u.getName());
            }
            System.out.println("Select a user from the list, or enter a new one. Type 'stop' to exit:");
            choice = keys.nextLine();
            if (choice.equals("stop"))
            {
                break;
            }
            if (existingUsers.contains(new User(choice)))
            {
                players.add(existingUsers.get(existingUsers.indexOf(new User(choice))));
                count++;
                existingUsers.remove(new User(choice));
            }
            else
            {
                System.out.println("Create new User?");
                if (keys.nextLine().toLowerCase().equals("yes"))
                {
                    players.add(new User(choice));
                    count++;
                }
            }
        }
        FileIO.writeUsers(players);
        ArrayList<Integer> directionsUsed = new ArrayList<>();
        ArrayList<User> AIPlayers = new ArrayList<>();
        while (players.size() + AIPlayers.size() < 2)
        {
            User AI = new User();
            int num;
            while (true)
            {
                //Generate Direction
                num = Utils.makeRoll(3, 0);
                if (!directionsUsed.contains(num))
                {
                    directionsUsed.add(num);
                    break;
                }
            }
            AI.setControlGroup(Utils.randomPlacement(b, num, true));
            AIPlayers.add(AI);
        }
        for (User user : players)
        {
            //Generate this Users control group
            if (user.getBarracks().size() > 0)
            {
                viewUnits(user);
                while (user.getControlGroup().size() < 5)
                {
                    System.out.println("Select a unit by ID:");
                    int ID = Integer.parseInt(keys.nextLine());
                    boolean idFound = false;
                    for (Unit unit : user.getBarracks())
                    {
                        if (unit.hashCode() == ID)
                        {
                            user.getControlGroup().add(unit);
                            idFound = true;
                        }
                    }
                    if (!idFound)
                    {
                        System.out.println("ID not found. Create new Peasant?");
                        if (keys.nextLine().toLowerCase().equals("yes"))
                        {
                            System.out.println("Enter new name: ");
                            user.getControlGroup().add(new Peasant(keys.nextLine()));
                        }
                    }
                }
            }
            else
            {
                while (user.getControlGroup().size() < 5)
                {
                    System.out.println(user.getName() + ", enter a name for your new Peasant: ");
                    choice = keys.nextLine();
                    if (choice.equals(""))
                    {
                        user.getControlGroup().add(new Peasant());
                    }
                    else
                    {
                        user.getControlGroup().add(new Peasant(choice));
                    }
                }
            }
            user.getBarracks().removeAll(user.getControlGroup());
            int num;
            while (true)
            {
                num = Utils.makeRoll(3, 0);
                if (!directionsUsed.contains(num))
                {
                    directionsUsed.add(num);
                    Utils.randomPlacement(b, user.getControlGroup(), num);
                    break;
                }
            }
        }
        players.addAll(AIPlayers);
        b.getPlayers().addAll(players);
        //Color Selection
        ArrayList<String> colors = getColors();
        for (User u : players)
        {
            while (true)
            {
                listColors();
                System.out.println(u.getName() + ", please select a color from the list:");
                for (String s : colors)
                {
                    System.out.println(s);
                }
                choice = keys.nextLine().toLowerCase();
                if (colors.contains(choice))
                {
                    u.setColor(choice);
                    colors.remove(choice);
                    break;
                }
            }
        }
        SimpleMode sm = new SimpleMode(b);
        sm.runGame(players);
    }

    public static void prepUpgrader()
    {
        ArrayList<User> users = FileIO.readUsers();
        ArrayList<User> toUpgrade = new ArrayList<>();
        while (true)
        {
            for (User u : users)
            {
                System.out.println(u.getName());
            }
            System.out.println("Select a user to add to upgrade list, or 'stop'");
            String name = keys.nextLine();
            if (name.toLowerCase().equals("stop"))
            {
                break;
            }
            if (users.contains(new User(name)))
            {
                toUpgrade.add(users.get(users.indexOf(new User(name))));
            }
        }
        upgradeUnits(toUpgrade);
    }

    public static void generateMenu()
    {
        System.out.println("1. Play Game");
        System.out.println("2. Admin Interface");
        System.out.println("3. Unit Upgrader");
        System.out.println("4. Reset Files");
        System.out.println("5. Exit");
    }

    public static void userPrefs(ArrayList<User> users)
    {

    }

    public static void upgradeUnits(ArrayList<User> users)
    {
        for (User u : users)
        {
            while (true)
            {
                viewUnits(u);
                System.out.println("Select an ID to upgrade, or 'stop': ");
                String choice = keys.nextLine();
                if (choice.equals("stop"))
                {
                    break;
                }
                int id = Integer.parseInt(choice);
                ArrayList<Unit> barracks = u.getBarracks();
                Unit unit = null;
                for (Unit toCheck : barracks)
                {
                    if (toCheck.hashCode() == id)
                    {
                        unit = toCheck;
                        System.out.println("Found unit. ID " + unit.hashCode() + " matches input: " + id);
                        break;
                    }
                }
                if (unit == null)
                {
                    System.out.println("Did not find unit");
                    unit = new Peasant();
                }
                for (String s : UnitUpgrader.upgradeOptions(unit))
                {
                    System.out.println(s);
                }
                System.out.println("Select an option from the list or 'cancel': ");
                String input = keys.nextLine();
                unit = UnitUpgrader.upgrade(unit, input);
                u.getBarracks().remove(unit);
                u.addUnitBarracks(unit);
            }
        }
        FileIO.writeUsers(users);
    }

    public static void viewUnits(User user)
    {
        System.out.println(user.getName() + " is viewing units.");
        for (Unit unit : user.getBarracks())
        {
            System.out.println("Name: " + unit.getName());
            System.out.println("-Class: " + unit.getUnitType());
            System.out.println("-Level: " + ((AbstractPlaceable) unit).getLevel());
            System.out.println("-ID: " + unit.hashCode());
            System.out.println("-HP: " + unit.getCurrHP() + "/" + unit.getMaxHP());
            if (unit instanceof Wizard)
            {
                Wizard w = (Wizard) unit;
                System.out.println("-Spell Points: " + w.getSpellBook().getSpellPointMax());
            }
        }
    }

    public static void viewUnits(User user, ArrayList<Unit> toView)
    {
        System.out.println(user.getName() + "'s units.");
        for (Unit unit : toView)
        {
            System.out.println("Name: " + unit.getName());
            System.out.println("-Class: " + unit.getUnitType());
            System.out.println("-Level: " + ((AbstractPlaceable) unit).getLevel());
            System.out.println("-ID: " + unit.hashCode());
            System.out.println("-HP: " + unit.getCurrHP() + "/" + unit.getMaxHP());
            for (String s : unit.getBuffInfo())
            {
                System.out.println(s);
            }
            if (unit instanceof Wizard)
            {
                Wizard w = (Wizard) unit;
                System.out.println("-Spell Points: " + w.getSpellBook().getSpellPoints());
            }
        }
    }

    public static void runAdmin()
    {
        ArrayList<User> users = FileIO.readUsers();
        while (true)
        {
            System.out.println("Enter a user to edit from the list: ");
            for (User currentUser : users)
            {
                System.out.println(currentUser.getName());
            }
            String input = keys.nextLine();
            boolean userFound = false;
            for (User currentUser : users)
            {
                if (currentUser.equals(new User(input)))
                {
                    System.out.println("Modifying " + input);
                    editUser(currentUser);
                    userFound = true;
                    break;
                }
            }
            if (!userFound)
            {
                System.out.println("Create new user?");
                if (keys.nextLine().toLowerCase().equals("yes"))
                {
                    User u = new User(input);
                    users.add(u);
                    editUser(u);
                }
            }
            System.out.println("Exit?");
            if (keys.nextLine().toLowerCase().equals("yes"))
            {
                break;
            }
        }
        FileIO.writeUsers(users);
    }

    public static void editUser(User toEdit)
    {
        System.out.println("What would you like to do with " + toEdit.getName());
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
                    Unit p = new Peasant(name);
                    while (!shouldBreak)
                    {
                        switch (keys.nextLine().toLowerCase())
                        {
                            case "peasant":
                                p = new Peasant(name);
                                shouldBreak = true;
                                break;
                            case "warrior":
                                shouldBreak = true;
                                p = new Warrior(name);
                                break;
                            case "archer":
                                shouldBreak = true;
                                p = new Archer(name);
                                break;
                            case "wizard":
                                shouldBreak = true;
                                p = new Wizard(name);
                                break;
                            case "samurai":
                                shouldBreak = true;
                                p = new Samurai(name);
                                break;
                            case "berserker":
                                shouldBreak = true;
                                p = new Berserker(name);
                                break;
                            case "arbalist":
                                shouldBreak = true;
                                p = new Arbalist(name);
                                break;
                            default:
                                System.out.println("Type not recognized");
                                break;
                        }

                    }
                    toEdit.addUnitBarracks(p);
                    break;
                case "remove unit":
                    System.out.println("Enter the ID of the unit to remove: ");
                    for (Unit unit : toEdit.getBarracks())
                    {
                        System.out.println("Name: " + unit.getName() + "| Class: " + unit.getUnitType() + "|ID: " + unit.hashCode());
                    }
                    String stringID = keys.nextLine();
                    int id = Integer.parseInt(stringID);
                    Unit pID = null;
                    for (Unit unit : toEdit.getBarracks())
                    {
                        if (unit.hashCode() == id)
                        {
                            pID = unit;
                        }
                    }
                    toEdit.getBarracks().remove(pID);
                    break;
                case "view units":
                    viewUnits(toEdit);
                    break;
                default:
                    break;
            }
            System.out.println("Enter next action: ");
            input = keys.nextLine();
        }
        ArrayList<User> toSave = new ArrayList<>();
        toSave.add(toEdit);
        FileIO.writeUsers(toSave);
    }

    public static void listColors()
    {
        System.out.println("Cyan");
        System.out.println("Red");
        System.out.println("Yellow");
        System.out.println("Black");
        System.out.println("Blue");
        System.out.println("Magenta");
    }

    public static ArrayList<String> getColors()
    {
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add("cyan");
        toReturn.add("red");
        toReturn.add("black");
        toReturn.add("blue");
        toReturn.add("magenta");
        toReturn.add("yellow");
        return toReturn;
    }

    public static void listColors(ArrayList<String> list)
    {
        for (String s : list)
        {
            System.out.println(s);
        }
    }
}
