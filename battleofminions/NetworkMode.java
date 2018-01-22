/*
 * Copyright 2018 austen.
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

import board.Board;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import utils.GameSocket;

/**
 *
 * @author austen
 */
public class NetworkMode {
    
    public static void startNetworkMode(boolean isHosting)
    {
        if (isHosting)
        {
            launchServerMode();
        }
        else
        {
            launchClientMode();
        }
    }
    
    private static void launchServerMode()
    {
        ServerSocket switchBoard;
        try
        {
            switchBoard = new ServerSocket(1313);
            
            
        }
        catch(IOException e)
        {
            
        }
    }
    
    private static void launchClientMode()
    {
        
    }
    
    private static class GameLogic implements Runnable
    {
        private final GameSocket[] playerSocks;
        
        public GameLogic(GameSocket[] playerSocks, Board b)
        {
            this.playerSocks = playerSocks;
        }
        
        @Override
        public void run() {
            
        }
    }
    
    private static class ConnectionManager implements Runnable
    {
        private final ServerSocket server;
        private boolean isStopped;
        private Thread myThread;
        private GameSocket[] sockets;
        
        public ConnectionManager(ServerSocket server)
        {
            this.server = server;
            sockets = new GameSocket[4];
        }
        
        @Override
        public void run() {
            isStopped = false;
            synchronized(this)
            {
                this.myThread = Thread.currentThread();
            }
            while (!isStopped)
            {
                
            }
        }
        
        public synchronized boolean isStopped()
        {
            return this.isStopped;
        }
        
        public synchronized void stop()
        {
            this.isStopped = true;
            try
            {
                server.close();
            }
            catch(IOException e)
            {
                
            }
        }
        
    }
}
