package utils;

import java.io.*;

/**
 * Created by Vasilii_Pozdeev on 2/10/2017.
 */
public class FileManager {
    public static void saveMacro(String fileName, UserActions userActions) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName + ".mcr"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(userActions);

            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserActions loadMacro(String macroName) {
        try {
            FileInputStream fis = new FileInputStream(new File(macroName + ".mcr"));
            ObjectInputStream ois = new ObjectInputStream(fis);

            UserActions loadedUserActions = (UserActions)ois.readObject();
            return loadedUserActions;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
