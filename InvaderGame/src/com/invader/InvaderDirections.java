package com.invader;

public enum InvaderDirections {
	RIGHT(1,0), DOWN(0,1), LEFT(-1,0);
	
	private int x;
	private int y;
	
	private InvaderDirections(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
