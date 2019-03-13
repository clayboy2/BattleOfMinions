/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Austen
 */
public class MainMenuController implements Initializable
{
    @FXML Button debugSubmit;
    @FXML Button playGame;
    @FXML Button adminConsole;
    @FXML TextField debugText;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
    
    private class Actions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            debugText.setText("Success!");
        }
        
    }
    
}
