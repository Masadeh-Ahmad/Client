package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Connected to server");

            // Create input and output streams for the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Read the username and password from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your username: ");
            String username = reader.readLine();
            System.out.print("Enter your password: ");
            String password = reader.readLine();

            // Send the username and password to the server
            out.println(username);
            out.println(password);

            // Wait for a response from the server
            String response = in.readLine();
            if (response.equals("SUCCESS")) {
                System.out.println("Login successful");
                boolean done = false;
                while (!done) {
                    // Prompt the user to select a type of request
                    System.out.println("Select a type of request:");
                    System.out.println("1. View marks");
                    System.out.println("2. View class stats");
                    System.out.println("3. Exit");
                    int choice = Integer.parseInt(reader.readLine());

                    // Send the request type to the server
                    out.println(choice);

                    // Process the response from the server based on the type of request
                    if (choice == 1) {
                        // Code to handle viewing marks
                        List<String> marks = (List<String>)inputStream.readObject();
                        System.out.println("Your marks:");
                        for (String mark : marks){
                            System.out.println(mark);
                        }
                    } else if (choice == 2) {
                        // Code to handle viewing class stats
                        System.out.print("Enter a course ID:");
                        out.println(reader.readLine());
                        String courseName = in.readLine();
                        if(!courseName.equals("null")){
                            System.out.println(courseName);
                            List<String> data = (List<String>)inputStream.readObject();
                            System.out.println("Class stats:");
                            for (String field : data){
                                System.out.println(field);
                            }
                        }
                        else
                            System.out.println("Course does not exist");

                    } else if (choice == 3) {
                        done = true;
                        System.out.println("Exiting");
                    }
                }
            } else {
                System.out.println("Login failed");
            }

            // Close the socket
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Disconnected");
        }
    }
}