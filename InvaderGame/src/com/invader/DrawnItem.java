package com.invader;

public class DrawnItem extends VEC2 {
	private String character;
	
	public DrawnItem(String character, int x, int y) {
		super(x, y);
		this.character = character;
	}

	public String getCharacter() {
		return character;
	}
}
	
	
	
