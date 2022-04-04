package utils;

import java.io.*;
import java.util.*;

public class User {
	private static User instance;
	
	private User () {
		//singleton design pattern
	}
	
	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}
	
	public boolean verify (String user, String pass) {
		try {
			//read input from file
			Scanner input = new Scanner(new File("login.txt"));
			String[] curr = {"", ""}; 
			
			while (input.hasNextLine()) {
				curr = input.nextLine().split(" ");
				
				//if user and password matches, return true 
				if (curr[0].equals(user) && curr[1].equals(pass)) {
					return true;
				}
			}
			return false;	
		} 
		catch (FileNotFoundException e) {
			return false;
		}
	}
}
