package com.invader;

public class Player extends DrawnItem implements Shoot {
	
	private int direction = 1;
	private Bullet bullet = new Bullet("Å™", 0, 0);
	
	public Player(String character, int x, int y) {
		super(character, x, y);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Bullet getBullet() {
		return bullet;
	}

	@Override
	public void shoot() {
//		Bullet b = player.getBullet();
//		bulletÇ™î≠éÀÇ≥ÇÍÇƒÇ¢Ç»Ç©Ç¡ÇΩÇÁ
		if(!bullet.isFired()) {
			bullet.setX(super.getX());
			bullet.setY(super.getY() - 1);
			bullet.setFired(true);
		}
		
	}
	
	public void move() {
		int dx = super.getX() + this.direction;
		super.setX(dx);
		if(dx < 0) {
			super.setX(1);
			this.direction = 1;
		}
		if(dx >= Screen.SCREEN_WIDTH) {
			super.setX(Screen.SCREEN_WIDTH - 2);
			this.direction = -1;
		}
	}
	
}
	
	
	
	
