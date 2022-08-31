package com.invader;

public class Invader extends DrawnItem implements Shoot {
	
	private boolean isDead = false;
	private Bullet bullet = new Bullet("Å´", 0, 0);
	
	public Invader(String character, int x, int y) {
		super(character, x, y);
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public Bullet getBullet() {
		return bullet;
	}

	@Override
	public void shoot() {
		bullet.setFired(true);
		bullet.setX(super.getX());
		bullet.setY(super.getY() + 1);
		
	}
	
	public void move(int directionX, int directionY) {
		super.setX(super.getX() + directionX);
		super.setY(super.getY() + directionY);
	}
	
	
}
