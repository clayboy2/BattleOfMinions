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
import actors.Placeable;
import actors.Unit;
import actors.Warrior;
import actors.Wizard;
import actors.wizardmagic.Summonable;
import board.Board;
import java.util.ArrayList;
import java.util.Scanner;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import utils.Action;
import utils.FileIO;
import utils.Storage;
import utils.Storage.LastAttack;
import utils.User;
import utils.Utils.Replay;

/**
 * The default method of playing the game
 *
 * @author Austen Clay
 */
public class SimpleMode {

    public static ArrayList<Unit> friendlyFireKills;
    private final Board b;
    
    public SimpleMode(Board b)
    {
        this.b = b;
        friendlyFireKills = new ArrayList<>();
    }

    //Runs the game with the given args
    public void runGame(ArrayList<User> users) {
        gameLoop(b);
    }

    //Main game loop
    private void gameLoop(Board b) {
        Scanner keys = new Scanner(System.in);
        ArrayList<User> toRemove = new ArrayList<>();
        ArrayList<User> toSave = new ArrayList<>();
        Replay replay = new Replay();
        while (true) {
            Storage.setBoard(b);
            replay.saveStep(b);
            SimpleOutput display = new SimpleOutput();
            for (User currentUser : b.getPlayers()) {
                currentUser.startNewTurn(); 
                System.out.println("It is " + currentUser.getName() + "'s turn.");
                for (Unit currentUnit : currentUser.getControlGroup()) {
                    ArrayList<Unit> toView = new ArrayList<>();
                    toView.add(currentUnit);
                    BattleOfMinions.viewUnits(currentUser, toView);
                    if (currentUnit instanceof Wizard)
                    {
                        Wizard w = (Wizard)currentUnit;
                        ArrayList<Summonable> deadUnits = new ArrayList<>();
                        for (Summonable mySummon : ((Wizard) currentUnit).getSummons())
                        {
                            mySummon.takeTurn();
                            if (mySummon.shouldDie())
                            {
                                deadUnits.add(mySummon);
                            }
                            if (!mySummon.isStunned())
                            {
                                //Take a turn;
                            }
                        }
                        w.getSummons().removeAll(deadUnits);
                        for (Summonable s : deadUnits)
                        {
                            Placeable p = (Placeable)s;
                            b.removeUnit(p);
                        }
                    }
                    System.out.println(currentUnit.getName()+" has "+currentUnit.getCurrHP()+" HP left.");
                    while (currentUnit.hasTurn()) {
                        if (currentUnit.isStunned())
                        {
                            System.out.println(currentUnit.getName()+" has been stunned.");
                            currentUnit.unStunMe();
                            break;
                        }
                        display.textOutput(b, currentUnit);
                        System.out.println("Enter an action for " + currentUnit.getName());
                        int code = -1;
                        try {
                            switch (keys.nextLine().toLowerCase()) {
                                case "":
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
                                            BattleOfMinions.viewUnits(spellUser,spellUser.getControlGroup());
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
                                case "power attack":
                                    if (currentUnit instanceof Warrior)
                                    {
                                        System.out.println("Please enter the name of an attack:");
                                        String attackName = keys.nextLine();
                                        System.out.println("If your attack specifies damage, enter it now: ");
                                        int damage = Integer.parseInt(keys.nextLine());
                                        System.out.println("Enter an attack direction: ");
                                        String input = keys.nextLine().toLowerCase();
                                        if (input.equals(""))
                                        {
                                            System.out.println("Invalid input");
                                            break;
                                        }
                                        int direction = input.charAt(0);
                                        switch(direction)
                                        {
                                            case 'w':
                                                direction = Action.NORTH;
                                                break;
                                            case 'a':
                                                direction = Action.WEST;
                                                break;
                                            case 's':
                                                direction = Action.SOUTH;
                                                break;
                                            case 'd':
                                                direction = Action.EAST;
                                                break;
                                            default:
                                                System.out.println("Invalid Direction");
                                                break;
                                        }
                                        Action.SpecialAttacks.attackSelector(attackName, damage, (Warrior)currentUnit, direction, input);
                                    }
                                    else
                                    {
                                        System.out.println(currentUnit.getName()+" is too weak for that.");
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid Selection");
                                    break;
                            }
                        } 
                        catch (IndexOutOfBoundsException e)
                        {
                            System.out.println("Error out of bounds");
                        }
                        catch (NumberFormatException e) {
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
                        levelMe.refreshMe();
                        if (levelMe instanceof Wizard)
                        {
                            Wizard w = (Wizard) levelMe;
                            w.refreshMe();
                        }
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
        FileIO.writeUsers(toSave);
        System.out.println("Save replay?");
        if (keys.nextLine().toLowerCase().equals("yes"))
        {
            replay.saveReplay();
        }
        
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
            for (int row = 0; row < b.getRows()-1; row++) {
                for (int col = 0; col < b.getCols()-1; col++) {
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
