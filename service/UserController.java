package service;

import java.io.*;
import java.util.*;

public class UserController {

    private static final String USER_FILE = "data/users.txt";

    // Check if user exists
    public static boolean checkUserExistence(String userName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(userName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    // Register a new user
    public static void registerUser(String userName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(userName);
            writer.newLine();
            System.out.println("User registered successfully.");
        } catch (IOException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }
}
