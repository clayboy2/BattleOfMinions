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
import utils.User;

/**
 *
 * @author ninja
 */
public class Board {
    private Placeable[][] gameBoard;
    private User[] players;
    private int numRows;
    private int numCols;
    
    public Board()
    {
        this.numRows = 21;
        this.numCols = 21;
        initBoard(21,21);
        players = new User[2];
        players[0] = new User();
        players[1] = new User();
    }
    
    public Board(int rows, int cols)
    {
        this.numCols = cols;
        this.numRows = rows;
        initBoard(rows,cols);
        players = new User[2];
        players[0] = new User();
        players[1] = new User();
    }
    
    public Board(int rows, int cols, User[] players)
    {
        this.numCols = cols;
        this.numRows = rows;
        this.players = players;
        initBoard(rows,cols);
    }
    
    private void initBoard(int rows, int cols)
    {
        for (int row = 0; row<rows;row++)
        {
            for (int col = 0;col<cols;col++)
            {
                if(row==0||row==20||col==0||col==20)
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
    
    public User[] getPlayers()
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
}
