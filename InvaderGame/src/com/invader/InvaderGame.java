package com.invader;

import java.util.ArrayList;
import java.util.List;

public class InvaderGame {

	public static void main(String[] args) {
		Screen screen = new Screen(prepareInvaders());
		screen.draw();
	}
	
	private static void Init() {
		
	}
	
	private static List<Invader> prepareInvaders() {
		
		List<Invader> invaders = new ArrayList<Invader>();
//		1‚Â”ò‚Î‚µ‚ÅInvader‚ð•`‚­‚½‚ß‚Éƒ|ƒWƒVƒ‡ƒ“‚ð‘}“ü
		for(int y = 0; y < Screen.INVADER_ROW; y++) {
			for(int x = 0; x < Screen.INVADER_COLUMN; x++) {
				invaders.add(new Invader(x * 2, y * 2));
			}
		}
		
		return invaders;
	}

}
