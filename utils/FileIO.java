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
package utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles all of the File IO that needs to be done.
 * @author Austen Clay
 */
public class FileIO {
    
    //Saves the users in a 'safe' way
    public static void writeUsers(ArrayList<User> users)
    {
        writeData(users, new File("resources/users.bin"), false);
    }
    
    public static ArrayList<User> readUsers()
    {
        return (ArrayList<User>) readData(new File("resources/users.bin"));
    }
    
    public static void writeData(Object o, File f,boolean append)
    {
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(o);
        }
        catch(IOException e)
        {
            System.out.println("Error writing Data");
            e.printStackTrace();
        }
    }
    
    public static Object readData(File f)
    {
        Object o;
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            o = in.readObject();
        }
        catch(IOException | ClassNotFoundException e) 
        {
            System.out.println("Exception occoured");
            return null;
        }
        return o;
    }
}
