package com.invader;

public class Bullet extends DrawnItem {
	
	private boolean isFired = false;

	public Bullet(String character, int x, int y) {
		super(character, x, y);
		// TODO Auto-generated constructor stub
	}

	public boolean isFired() {
		return isFired;
	}

	public void setFired(boolean isFired) {
		this.isFired = isFired;
	}
	
}
	
	
