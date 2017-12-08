/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import board.Board;

/**
 *
 * @author ninja
 */
public class Storage {
    private static Board b;
    
    public static Board getBoard()
    {
        return b;
    }
    
    public static void setBoard(Board b)
    {
        Storage.b = b;
    }
    
}
