/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleofminions;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Austen
 */
public class ServerMode
{
    private Greeter greeter;
    
    public ServerMode()
    {
        greeter = new Greeter();
    }
    
    public void start()
    {
        
    }
    
    private class Greeter implements Runnable
    {
        private ServerSocket server;
        private Socket client;
        private PrintWriter out;
        private BufferedReader in;
        private volatile boolean serverRunning;

        @Override
        public void run()
        {
            try
            {
                server = new ServerSocket(2564);
                serverRunning = true;
                while (serverRunning)
                {
                    if(!serverRunning)
                    {
                        break;
                    }
                    client = server.accept();
                    
                }
            }
            catch (Exception e)
            {
                
            }
        }        
    }
    
    private class GameServer implements Runnable
    {

        @Override
        public void run()
        {
            
        }
        
    }
}
