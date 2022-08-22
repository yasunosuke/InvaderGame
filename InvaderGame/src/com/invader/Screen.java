package com.invader;

import java.io.IOException;
import java.util.List;

public class Screen {
	static final int SCREEN_WIDTH = 32;
	static final int SCREEN_HEIGHT = 24;
	static final int INVADER_COLUMN = 11;
	static final int INVADER_ROW = 5;
	
	private List<Invader> invaders;
	private String[][] field = new String[SCREEN_HEIGHT][SCREEN_WIDTH];
	
	public Screen(List<Invader> invaders) {
		super();
		this.invaders = invaders;
	}

	public void draw() {
		
		for(int y = 0; y < SCREEN_HEIGHT; y++) {
			for(int x = 0; x < SCREEN_WIDTH; x++) {
				field[y][x] = "E";
			}
		}
		
//		Invader‚ðfield‚É‘‚«‚±‚Ý
//		1
		for(Invader i: this.invaders) {
			field[i.getPositionY()][i.getPositionX()] = i.getCharacter();
		}
//		2
//		for(int i = 0; i < this.invaders.size(); i++) {
//			Invader invader = this.invaders.get(i);
//			String invaderCharacter = invader.getCharacter();
//			field[invader.getPositionY()][invader.getPositionX()] = invaderCharacter;
//		}
//		3
//		for(int y = 0; y < Screen.INVADER_ROW; y++) {
//			for(int x = 0; x < Screen.INVADER_COLUMN; x++) {
//				Invader invader = invaders.get(y * Screen.INVADER_COLUMN + x);
//				String invaderCharacter = invader.getCharacter();
//				field[invader.getPositionY()][invader.getPositionX()] = invaderCharacter;
//			}
//		}
		
//		codes to clear the screen
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int y = 0; y < SCREEN_HEIGHT; y++) {
			for(int x = 0; x < SCREEN_WIDTH; x++) {
				System.out.print(field[y][x]);
			}
			System.out.println("");
		}
	}
}
