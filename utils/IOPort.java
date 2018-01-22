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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for miscellaneous, and eventually Network IO
 * @author austen
 */
public class IOPort {
    
    
    //Gets the global ID count, and increments it by one
    public static int getGlobalID()
    {
        int toReturn = -1;
        try
        {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("resources/id.bin")));
            ArrayList<Integer> id = (ArrayList<Integer>)in.readObject();
            toReturn = id.get(0);
            in.close();
            int currID = id.get(0);
            currID++;
            id.remove(0);
            id.add(currID);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("resources/id.bin")));
            out.writeObject(id);
        }
        catch (IOException e)
        {
            System.out.println("Something went wrong");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found");
        }
        return toReturn;
    }
}
