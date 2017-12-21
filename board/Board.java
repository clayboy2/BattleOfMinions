/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package board;

import actors.EmptySpace;
import actors.Placeable;
import actors.Unit;
import actors.Wall;
import java.io.Serializable;
import java.util.ArrayList;
import utils.User;

/**
 *
 * @author ninja
 */
public class Board implements Serializable{
    private Placeable[][] gameBoard;
    private ArrayList<User> players;
    private final int numRows;
    private final int numCols;
    
    public Board(ArrayList<User> players)
    {
        this.numRows = 22;
        this.numCols = 22;
        initBoard(21,21);
        this.players = players;
    }
    
    public Board(int rows, int cols, ArrayList<User> players)
    {
        this.numCols = cols;
        this.numRows = rows;
        this.players = players;
        initBoard(rows,cols);
    }
    
    private void initBoard(int rows, int cols)
    {
        gameBoard = new Placeable[rows][cols];
        for (int row = 0; row<rows;row++)
        {
            for (int col = 0;col<cols;col++)
            {
                if(row==0||row==rows-1||col==0||col==cols-1)
                {
                    gameBoard[row][col] = new Wall();
                    gameBoard[row][col].setPosition(row,col);
                }
                else
                {
                    gameBoard[row][col] = new EmptySpace();
                    gameBoard[row][col].setPosition(row, col);
                }
            }
        }
    }
    
    public ArrayList<User> getPlayers()
    {
        return players;
    }
    
    public int getRows()
    {
        return numRows;
    }
    
    public int getCols()
    {
        return numCols;
    }
    
    public boolean isEmptySpace(int row, int col)
    {
        return gameBoard[row][col].isEmptySpace();
    }
    
    public boolean placePlaceable(int row, int col, Placeable p)
    {
        if (!gameBoard[row][col].isEmptySpace())
        {
            return false;
        }
        gameBoard[row][col] = p;
        return true;
    }
    
    public void removeUnit(Placeable p)
    {
        gameBoard[p.getRow()][p.getCol()] = new EmptySpace();
    }
    
    public Placeable[][] getBoard()
    {
        return gameBoard;
    }
    
    public char getTokenAt(int row, int col)
    {
        return gameBoard[row][col].getToken();
    }
    
    public void removePlayer(User toRemove)
    {
        players.remove(toRemove);
    }
    
    public boolean onePlayerLeft()
    {
        return players.size()==1;
    }
    
    public User getLastManStanding()
    {
        return players.get(0);
    }
}
