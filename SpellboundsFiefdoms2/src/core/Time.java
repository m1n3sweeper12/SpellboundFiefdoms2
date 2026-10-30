package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Time {
	
	public static int day;
	public static int hour;
	public static int minute;
	public static int second;
	public static String time;
	
	private static int timer;
	
	public Time() {
		day = 0;
		hour = 0;
		minute = 0;
		second = 0;
		timer = 0;
		time = "early morning";
	}
	
	public static void getTimeFromFile(String filepath) {
		try {
			Scanner in = new Scanner(new File(filepath));
			
			day = in.nextInt();
			hour = in.nextInt();
			minute = in.nextInt();
			second = in.nextInt();
			timer = in.nextInt();
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("Time: " + day + " | " + hour + " | " + minute + " | " + second + " | " + timer);
	}
	
	public static void writeTimeToFile(String filepath) {
		String timeString = day + " " + hour + " " + minute + " " + second + " " + timer + " ";
		try {
			Files.writeString(Path.of(filepath), timeString);
		} catch(IOException e) {
			
		}
	}
	
	public static void tick() {
		// track tick
		// 60 ticks/second,
		// 10 in-game seconds/second
		timer++;
		if(timer >= 6) {
			second++;
			timer = 0;
		}
		// 60 seconds/minute
		if(second >= 60) {
			minute++;
			second = 0;
		}
		// 60 minutes/hour
		if(minute >= 60) {
			hour++;
			minute = 0;
		}
		// 24 hours/day
		if(hour >= 24) {
			day++;
			hour = 0;
		}
		
		// set time of day based on hour
		switch(hour) {
		case 0,1,2,3,4,5 -> time = "early morning";
		case 6,7,8,9,10,11 -> time = "morning";
		case 12,13,14,15,16,17 -> time = "afternoon";
		case 18,19,20,21,22,23 -> time = "night";
		}
	}
	
}
