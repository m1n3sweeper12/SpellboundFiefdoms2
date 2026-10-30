package utils;

import java.awt.Color;

public class Colors {
	
	public static Color red, orange, yellow, green, blue, purple, white, black;
	public static COLORTHEME theme;
	
	// TODO: finish adding color themes
	public static void initColors() {
		theme = COLORTHEME.Default;
		
		switch(theme) {
		case Default:
			red = new Color(181, 62, 68);
			orange = new Color(209, 126, 63);
			yellow = new Color(223, 231, 90);
			green = new Color(10, 169, 29);
			blue = new Color(35, 92, 149);
			purple = new Color(151, 94, 132);
			white = new Color(231, 223, 231);
			black = new Color(20, 41, 28);
			break;
		case Pastel:
			break;
		case Dark:
			break;
		case MonochromeBlue:
			break;
		case MonochromeGreen:
			break;
		case MonochromeRed:
			break;
		default:
			break;
		}
	}
	
}
