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
package battleofminions;

import actors.AbstractPlaceable;
import actors.Archer;
import actors.Peasant;
import actors.Placeable;
import actors.Unit;
import actors.Wizard;
import board.Board;
import java.util.ArrayList;
import java.util.Scanner;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
import utils.Action;
import utils.IOPort;
import utils.Storage;
import utils.Storage.LastAttack;
import utils.Utils;
import utils.User;

/**
 * The default method of playing the game
 *
 * @author Austen Clay
 */
public class SimpleMode {

    public static ArrayList<Unit> friendlyFireKills;

    //Runs the game with the given args
    public void runGame(ArrayList<User> users) {
        friendlyFireKills = new ArrayList<>();
        Board b = new Board(22, 22, users);
        ArrayList<Integer> directionsUsed = new ArrayList<>();
        for (User u : users) {
            while (true) {
                System.out.println(u.getName() + " is selecting units.");
                boolean successful = true;
                int direction = (int) (Math.random() * 4);
                for (Integer i : directionsUsed) {
                    if (direction == i) {
                        successful = false;
                        break;
                    }
                }
                if (successful) {
                    directionsUsed.add(direction);
                    if (u.getBarracks().isEmpty()) {
                        u.setControlGroup(Utils.randomPlacement(b, direction, (u.getName().equals("A.I. Opponent"))));
                    } else {
                        ArrayList<Unit> currentBarracks = u.getBarracks();
                        ArrayList<Unit> controlGroup = new ArrayList<>();
                        System.out.println("Use saved units?");
                        Scanner keys = new Scanner(System.in);
                        if (keys.nextLine().toLowerCase().equals("yes")) {
                            if (currentBarracks.size() < 5) {
                                System.out.println("Filling with Peasants...");
                                while (currentBarracks.size() < 5) {
                                    System.out.println("Enter a name: ");
                                    Unit tempUnit = new Peasant(keys.nextLine());
                                    currentBarracks.add(tempUnit);
                                }
                                u.setControlGroup(currentBarracks);
                            } else if (currentBarracks.size() == 5) {
                                for (Unit tempUnit : currentBarracks) {
                                    controlGroup.add(tempUnit);
                                }
                                u.setControlGroup(controlGroup);
                            } else {
                                System.out.println("You have the following units: ");
                                for (Unit currentUnit : currentBarracks) {
                                    System.out.println(currentUnit.getName() + "|ID: " + currentUnit.hashCode() + "|Class: " + currentUnit.getUnitType());
                                }
                                while (controlGroup.size() < 5) {
                                    System.out.println("Enter the ID of the Units to use:");
                                    String choice = keys.nextLine();
                                    int id = Integer.parseInt(choice);
                                    for (Unit currentUnit : currentBarracks) {
                                        if (currentUnit.hashCode() == id) {
                                            controlGroup.add(currentUnit);
                                        }
                                    }
                                }
                                u.setControlGroup(controlGroup);
                            }
                            currentBarracks.removeAll(controlGroup);
                            Utils.randomPlacement(b, u.getControlGroup(), direction);
                        } else {
                            u.setControlGroup(Utils.randomPlacement(b, direction, (u.getName().equals("A.I. Opponent"))));
                        }
                    }
                    break;
                }
            }
        }
        Storage.setBoard(b);
        SimpleOutput out = new SimpleOutput();
        out.textOutput(b, new Peasant());
        gameLoop(b);
    }

    //Main game loop
    private void gameLoop(Board b) {
        Scanner keys = new Scanner(System.in);
        ArrayList<User> toRemove = new ArrayList<>();
        ArrayList<User> toSave = new ArrayList<>();
        while (true) {
            SimpleOutput display = new SimpleOutput();
            for (User currentUser : b.getPlayers()) {
                currentUser.startNewTurn();
                System.out.println("It is " + currentUser.getName() + "'s turn.");
                for (Unit currentUnit : currentUser.getControlGroup()) {
                    System.out.println(currentUnit.getName()+" has "+currentUnit.getCurrHP()+" HP left.");
                    while (currentUnit.hasTurn()) {
                        if (currentUnit.isStunned())
                        {
                            currentUnit.unStunMe();
                            break;
                        }
                        display.textOutput(b, currentUnit);
                        System.out.println("Enter an action for " + currentUnit.getName());
                        int code = -1;
                        try {
                            switch (keys.nextLine().toLowerCase()) {
                                case "skip":
                                    while (currentUnit.hasTurn())
                                    {
                                        currentUnit.takeTurn();
                                    }
                                    break;
                                case "move":
                                    System.out.println("Please enter wsad for movement");
                                    code = Action.move(currentUnit, keys.nextLine().toLowerCase().charAt(0));
                                    break;
                                case "attack":
                                    System.out.println("Please enter wsad for attack");
                                    if (currentUnit.getUnitType().equals("Archer"))
                                    {
                                        code = Action.rangedAttack(currentUnit, keys.nextLine().toLowerCase().charAt(0));
                                    }
                                    else
                                    {
                                        code = Action.attack(currentUnit, keys.nextLine().toLowerCase().charAt(0));
                                    }
                                    if (code == Action.ExitCodes.MISSED_TARGET) {
                                        System.out.println(LastAttack.attacker + " missed " + LastAttack.defender);
                                    } else {
                                        System.out.println(LastAttack.attacker + " did " + LastAttack.damageDone + " to " + LastAttack.defender);
                                    }
                                    break;
                                case "moveall":
                                    System.out.println("Please enter wsad for movement all");
                                    code = Action.moveall(currentUser, keys.nextLine().toLowerCase().charAt(0));
                                    break;
                                case "exit":
                                    System.out.println("Exiting game");
                                    System.exit(0);
                                    break;
                                case "cast spell":
                                    if (currentUnit instanceof Wizard)
                                    {
                                        System.out.println("Please enter a spell");
                                        String toCast = keys.nextLine().toLowerCase();
                                        System.out.println("Enter a stat, or amount of spell points to burn.");
                                        String type = keys.nextLine();
                                        for (User spellUser : b.getPlayers())
                                        {
                                            for (Unit spellUnit : spellUser.getControlGroup())
                                            {
                                                System.out.println("Name: "+spellUnit.getName()+"| Class: "+spellUnit.getUnitType()+"| ID: "+spellUnit.hashCode());
                                            }
                                        }
                                        System.out.println("Select a target by ID: ");
                                        int targetID = Integer.parseInt(keys.nextLine());
                                        Placeable target = null;
                                        for (User spellUser: b.getPlayers())
                                        {
                                            for (Unit spellUnit : spellUser.getControlGroup())
                                            {
                                                if (targetID == spellUnit.hashCode())
                                                {
                                                    target = (Placeable)spellUnit;
                                                }
                                            }
                                        }
                                        if (target == null)
                                        {
                                            System.out.println("Your spell doesn't work...");
                                            break;
                                        }
                                        Wizard w = (Wizard)currentUnit;
                                        w.castSpell(toCast, target, type);
                                    }
                                    else
                                    {
                                        System.out.println(currentUnit.getName()+" has not mastered the arcane arts.");
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            code = -2;
                        }
                        switch (code) {
                            case Action.ExitCodes.INVALID_TARGET:
                                System.out.println("INVALID_TARGET");
                                break;
                            case Action.ExitCodes.ERR_TARGET_IS_OCCUPIED:
                                System.out.println("ERR_TARGET_IS_OCCUPIED");
                                break;
                            case Action.ExitCodes.ERR_TARGET_IS_WALL:
                                System.out.println("ERR_TARGET_IS_WALL");
                                break;
                            case Action.ExitCodes.MULTIPLE_ERRORS:
                                System.out.println("MULTIPLE_ERRORS");
                                break;
                            case Action.ExitCodes.SUCCESSFUL:
                                System.out.println("SUCCESSFUL");
                                break;
                            case Action.ExitCodes.TARGET_IS_DEAD:
                                System.out.println(LastAttack.defender + " has died.");
                                break;
                            case -2:
                                System.out.println("Exception thrown");
                                break;
                            default:
                                System.out.println("Some other error has occoured");
                        }
                    }
                }
                if (!SimpleMode.friendlyFireKills.isEmpty()) {
                    for (Unit u : friendlyFireKills) {
                        currentUser.getControlGroup().remove(u);
                    }
                }
                if (currentUser.outOfUnits()) {
                    toRemove.add(currentUser);
                }
                LastAttack.reset();
            }
            if (!toRemove.isEmpty()) {
                for (User u : toRemove) {
                    toSave.add(u);
                    b.getPlayers().remove(u);
                }
            }
            if (b.getPlayers().isEmpty()) {
                System.out.println("Tie Game!");
                break;
            } else if (b.getPlayers().size() == 1) {
                for (User u : b.getPlayers()) {
                    System.out.println(u.getName() + " wins!");
                    for (Unit levelMe : u.getControlGroup())
                    {
                        levelMe.levelUp();
                    }
                }
                toSave.addAll(b.getPlayers());
                b.getPlayers().remove(0);
                break;
            }
        }
        for (User u : toSave) {
            u.saveUnits();
        }
        IOPort.writeUsers(toSave);
    }

    //Inner class with input tools
    private class SimpleInput {

    }

    //Inner class that displays the game state
    private class SimpleOutput {

        //Prints the game state to the console.
        public void textOutput(Board b, Unit u) {
            AnsiConsole.systemInstall();
            ArrayList<User> players = b.getPlayers();
            Placeable[][] gameBoard = b.getBoard();
            for (int row = 0; row < b.getRows(); row++) {
                for (int col = 0; col < b.getCols(); col++) {
                    if (gameBoard[row][col] instanceof Unit) {
                        char token = gameBoard[row][col].getToken();
                        if (gameBoard[row][col].equals(u)) {
                            AnsiConsole.out.print(new Ansi().render("@|green " + gameBoard[row][col].getToken() + "|@"));
                        } else {
                            for (User user : b.getPlayers()) {
                                if (user.belongsToMe((AbstractPlaceable) gameBoard[row][col])) {
                                    AnsiConsole.out.print(new Ansi().render("@|" + user.getColor() + " " + token + "|@"));
                                }
                            }
                        }
                    } else {
                        System.out.print(gameBoard[row][col].getToken());
                    }
                }
                System.out.println();
            }
            AnsiConsole.systemUninstall();
        }
    }
}
