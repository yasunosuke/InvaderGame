package com.invader;

public class Invader {
	private final String character = "Åö";
	private int positionX;
	private int positionY;
	
	public Invader(int positionX, int positionY) {
		super();
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public String getCharacter() {
		return character;
	}
	
	
}
