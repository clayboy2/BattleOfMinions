/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleofminions;

import board.Board;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 *
 * @author ninja
 */
public class SimpleMode {
    
    public void runGame(String[] args)
    {
        //Assume we have 3 args
        Board b = new Board();
        gameLoop(b);
    }
    
    private void gameLoop(Board b)
    {
        
    }
    
    private class SimpleInput
    {
        
    }
    
    private class SimpleOutput
    {
        public void textOutput(Board b)
        {
            AnsiConsole.systemInstall();
            for (int row = 0; row<b.getRows();row++)
            {
                for (int col = 0; col<b.getCols();col++)
                {
                    if ()
                }
            }
            AnsiConsole.systemUninstall();
        }
    }
    
}
