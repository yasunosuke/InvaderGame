package com.invader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.invader.InvaderDirections.*;

public class Screen {
	static final int SCREEN_WIDTH = 32;
	static final int SCREEN_HEIGHT = 24;
	
	static final int INVADER_COLUMN = 11;
	static final int INVADER_ROW = 5;
	
	private Player player = new Player("▲", SCREEN_WIDTH / 2, SCREEN_HEIGHT - 2);
	
//	moveInvaders()の頻度のためのカウンター
	private int invaderCount = 0;
	private InvaderDirections invaderDirection = RIGHT;
	private List<Invader> invaders = new ArrayList<>();
//	bulletを放ったInvaderたちを格納
	private List<Invader> invaderWhoShot = new ArrayList<>();

	private String[][] screen = new String[SCREEN_HEIGHT][SCREEN_WIDTH];
	
	public Screen() {
		super();
	}
	
//	ゲームを初期化するためのメソッド
	public void init() {
		prepareInvaders();
	}

//	Invaderを各ポジションに配置するためのメソッド
	private void prepareInvaders() {
		//		1つ飛ばしでInvaderを描くためにポジションを挿入
		for(int y = 0; y < Screen.INVADER_ROW; y++) {
			for(int x = 0; x < Screen.INVADER_COLUMN; x++) {
				this.invaders.add(new Invader("★", x * 2, y * 2));
			}
		}
	}
	
// 　スクリーンを描画するためのメソッド
	public void draw() {
		
//		ドット書き込み
		for(int y = 0; y < SCREEN_HEIGHT; y++) {
			for(int x = 0; x < SCREEN_WIDTH; x++) {
				screen[y][x] = "・";
			}
		}
		
//		プレイヤー書き込み
		screen[player.getY()][player.getX()] = player.getCharacter();
		
//		Player bullet書き込み bulletが発射されていたなら
		if(player.getBullet().isFired()) {
			Bullet b = player.getBullet();
			screen[b.getY()][b.getX()] = b.getCharacter();
		}
		
		if(invaderWhoShot.size() > 0) {
			for(Invader i: invaderWhoShot) {
				Bullet invaderBullet = i.getBullet();
				if(invaderBullet.isFired()) {
					screen[invaderBullet.getY()][invaderBullet.getX()] = invaderBullet.getCharacter();
				}
			}
		}
		
		
//		Invaderをscreenに書きこみ
//		1
		for(Invader i: this.invaders) {
			if(!i.isDead()) {
				screen[i.getY()][i.getX()] = i.getCharacter();
			}
		}
		
//		2
//		for(int i = 0; i < this.invaders.size(); i++) {
//			Invader invader = this.invaders.get(i);
//			String invaderCharacter = invader.getCharacter();
//			screen[invader.getPositionY()][invader.getPositionX()] = invaderCharacter;
//		}
		
//		3
//		for(int y = 0; y < Screen.INVADER_ROW; y++) {
//			for(int x = 0; x < Screen.INVADER_COLUMN; x++) {
//				Invader invader = invaders.get(y * Screen.INVADER_COLUMN + x);
//				String invaderCharacter = invader.getCharacter();
//				screen[invader.getPositionY()][invader.getPositionX()] = invaderCharacter;
//			}
//		}
		
//		codes to clear the screen
		clearScreen();
		
		for(int y = 0; y < SCREEN_HEIGHT; y++) {
			for(int x = 0; x < SCREEN_WIDTH; x++) {
				System.out.print(screen[y][x]);
			}
			System.out.println("");
		}
	}
	
	private void clearScreen() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void moveInvaders() {
//		invader移動
		invaderCount++;
		if(invaderCount > 10) {
			
			invaderCount = 0;
			
			if(this.invaderDirection == DOWN && invadersFadeOutFromScreen()) {
				moveInvadersToInitialPosition();
				this.invaderDirection = RIGHT;
			} else {
				InvaderDirections nextDirection = this.invaderDirection;
				for(Invader i: this.invaders) {
					i.move(invaderDirection.getX(), invaderDirection.getY());
					
					switch(invaderDirection) {
					case RIGHT: 
						if(i.getX() >= SCREEN_WIDTH - 1) {
							nextDirection = DOWN;
						}
						break;
					case DOWN: 
						if(i.getX() >= SCREEN_WIDTH - 1) {
							nextDirection = LEFT;
						}
						if(i.getX() <= 0) {
							nextDirection = RIGHT;
						}
						break;
					case LEFT: 
						if(i.getX() <= 0) {
							nextDirection = DOWN;
						}
						break;
					}
				}
				invaderDirection = nextDirection;
			}
		}
	}
	
//	Invaderが枠外に出るかどうか判定するためのメソッド
	private boolean invadersFadeOutFromScreen() {
		for(int x = 0; x < INVADER_COLUMN; x++) {
			int invaderWhoShootIndex = -1;
			Invader invader = null;
			
			for(int y = 0; y < INVADER_ROW; y++) {
				int index = x + y * INVADER_COLUMN;
				if(!this.invaders.get(index).isDead()) {
					invaderWhoShootIndex = index;
					invader = this.invaders.get(index);
				}
			}
//			一番下のInvaderが見つかったなら
			if(invaderWhoShootIndex >= 0 && invader != null && invader.getY() + 1 >= Screen.SCREEN_HEIGHT) {
				return true;
			}
		}
		return false;
	}
	
//	Invaderたちのポジションを初期の位置に戻すためのメソッド
	private void moveInvadersToInitialPosition() {
		for(int x = 0; x < INVADER_COLUMN; x++) {
			for(int y = 0; y < INVADER_ROW; y++) {
				int index = x + y * INVADER_COLUMN;
				if(!this.invaders.get(index).isDead()) {
					Invader invader = this.invaders.get(index);
					invader.setX(x * 2);
					invader.setY(y * 2);
				}
			}
		}
	}
	
	public void movePlayer() {
		player.move();
	}
	
//	Playerがbulletを発射するためのメソッド
	public void shootPlayerBullet() {
		player.shoot();
	}
	
//	Invaderからbulletを発射するためのメソッド
//	一斉発射
	public void shootAllInvaderBullets() { 
		if(invaderWhoShot.size() == 0) {
			
//			各列の一番下のInvaderをbulletを打つものとする
//			invadersの列ごとに操作する
			for(int x = 0; x < INVADER_COLUMN; x++) {
				int invaderWhoShootIndex = -1;
				for(int y = 0; y < INVADER_ROW; y++) {
					int index = x + y * INVADER_COLUMN;
					if(!this.invaders.get(index).isDead()) {
						invaderWhoShootIndex = index;
					}
				}
//				一番下のInvaderが見つかったなら
				if(invaderWhoShootIndex >= 0) {
					Invader invader = invaders.get(invaderWhoShootIndex);
					Bullet invaderBullet = invader.getBullet();
					invaderBullet.setFired(true);
					invaderBullet.setX(invader.getX());
					invaderBullet.setY(invader.getY() + 1);
//					誰が打ったのか更新するする場合(二回目以降の発射)
					if(invaderWhoShot.size() >= INVADER_COLUMN) {
						invaderWhoShot.set(x, invader);
					} else {
						invaderWhoShot.add(invader);
					}
					
				}
			}
		} 
//		二回目以降の発射
		else if(isNotFiredInvaderShotsExisted()) {

			List<Integer> foundIndexes = findIndexOfNotFiredInvaderShots();
//          一番下のInvaderが弾を放つ
			for(Integer x: foundIndexes) {
				int invaderWhoShootIndex = -1;
				for(int y = 0; y < INVADER_ROW; y++) {
					int index = x + y * INVADER_COLUMN;
					if(!this.invaders.get(index).isDead()) {
						invaderWhoShootIndex = index;
					}
				}
				//				一番下のInvaderが見つかったなら
				if(invaderWhoShootIndex >= 0) {
					Invader invader = invaders.get(invaderWhoShootIndex);
					invader.shoot();
					//					誰が打ったのか更新するする場合(二回目以降の発射)
					if(invaderWhoShot.size() >= INVADER_COLUMN) {
						invaderWhoShot.set(x, invader);
					} else {
						invaderWhoShot.add(invader);
					}

				}
			}
		}

	}
	
//	弾を放ったInvaderのなかにすでにisFiredがfalse、またはisDeadがtrueになっているものがあるか判定する
	private boolean isNotFiredInvaderShotsExisted() {
		for(Invader i: this.invaderWhoShot) {
			if(!i.getBullet().isFired() || i.isDead()) {
				return true;
			}
		}
		return false;
	}
	
//	弾を放ったInvaderのなかにすでにisFiredがfalseになっているindexを見つけるためのメソッド
	private List<Integer> findIndexOfNotFiredInvaderShots() {
		List<Integer> l = new ArrayList<>();
		for(int i = 0; i < this.invaderWhoShot.size(); i++) {
			if(!this.invaderWhoShot.get(i).getBullet().isFired() || invaderWhoShot.get(i).isDead()) {
				l.add(i);
			}
		}
		return l;
	}
	
//	Invaderからランダムにbulletを発射するためのメソッド
//	いまこっちを使っている
//	Invaderから弾を放つメソッドはshootAllInvaderBullets()とこのメソッドがある
	public void shootInvaderBulletsRandomly() { 
		if(invaderWhoShot.size() == 0) {
			
//			各列の一番下のInvaderをbulletを打つものとする
//			invadersの列ごとに操作する
			for(int x = 0; x < INVADER_COLUMN; x++) {
//              1/INVADER_COLUMN の確率で発射
				Random r = new Random();
				if(r.nextInt() % INVADER_COLUMN != 0) continue;
//	test			if(true) continue;
				int invaderWhoShootIndex = -1;
				for(int y = 0; y < INVADER_ROW; y++) {
					int index = x + y * INVADER_COLUMN;
					if(!this.invaders.get(index).isDead()) {
						invaderWhoShootIndex = index;
					}
				}
//				一番下のInvaderが見つかったなら
				if(invaderWhoShootIndex >= 0) {
					Invader invader = invaders.get(invaderWhoShootIndex);
					invader.shoot();
//					bulletを放ったInvaderを登録
					invaderWhoShot.add(invader);
					
				}
			}
		}
	}
	
//	Playerのbulletの位置を更新するためのメソッド
	public void updatePlayerBulletPosition() {
		Bullet b = player.getBullet();
		if(b.isFired()) {
			int nextY = b.getY() - 1;
//			Playerのbulletが枠外に行ってしまう場合、またはbulletとInvaderが衝突した場合
			if(nextY < 0) {
				b.setFired(false);
			} else if(isInvaderShotByBullet(b)) {
				this.invaderWhoShot.remove(findInvaderShot(b));
				b.setFired(false);
			}
//			Invaderからbulletが放たれていて、PlayerとInvaderのbulletが衝突したら
			else if(invaderWhoShot.size() > 0 && isInvaderBulletShotByPlayerBullet(b)) {
				b.setFired(false);
				int index = findBulletCollapsedInvaderIndex(b);
				if(index != -1) {
					invaderWhoShot.get(index).getBullet().setFired(false);
					invaderWhoShot.remove(index);
				}
			} else {
//				位置を更新
				b.setY(nextY);
			}
		}
	}
	
//	Playerのbulletの位置を更新するためのメソッド
//	public void updatePlayerBulletPosition() {
//		Bullet b = player.getBullet();
//		if(b.isFired()) {
//			int nextY = b.getY() - 1;
////			Playerのbulletが枠外に行ってしまう場合、またはbulletとInvaderが衝突した場合
//			if(nextY < 0 || isInvaderShotByBullet(b)) {
//				b.setFired(false);
//			} 
////			Invaderからbulletが放たれていて、PlayerとInvaderのbulletが衝突したら
//			else if(invaderWhoShot.size() > 0 && isInvaderBulletShotByPlayerBullet(b)) {
//				b.setFired(false);
//				int index = findBulletCollapsedInvaderIndex(b);
//				if(index != -1) {
//					invaderWhoShot.get(index).getBullet().setFired(false);
//					invaderWhoShot.remove(index);
//				}
//			} else {
////				位置を更新
//				b.setY(nextY);
//			}
//		}
//	}
	
	public void updateInvaderBullets() {
		List<Invader> toRemove = new ArrayList<>();
		if(invaderWhoShot.size() > 0) {
			for(Invader i: invaderWhoShot) {
				if(i.getBullet().isFired()) {
					int nextY = i.getBullet().getY() + 1;
//					bulletが枠外に出てしまう場合
					if(nextY >= SCREEN_HEIGHT) {
						i.getBullet().setFired(false);
						toRemove.add(i);
					} else {
//						位置を更新
						i.getBullet().setY(nextY);
					}
				}
				
			}
			invaderWhoShot.removeAll(toRemove);
		}
	}
	
	
	
//	InvaderのbulletとPlayerが当たったかどうかの判定
	private boolean isPlayerShotByInvaderBullet(Invader invader) {
		Bullet invaderBullet = invader.getBullet();
		if(this.player.getX() == invaderBullet.getX() && this.player.getY() == invaderBullet.getY()) {
			return true;
		}
		return false;
	}
	
//	InvaderのbulletとPlayerが当たったかどうかの判定
	public boolean isPlayerShotByInvaderBullet() {
		if(invaderWhoShot.size() > 0) {
			for(Invader i: this.invaderWhoShot) {
				Bullet invaderBullet = i.getBullet();
				if(this.player.getX() == invaderBullet.getX() && this.player.getY() == invaderBullet.getY() && invaderBullet.isFired()) {
					return true;
				}
			}
		}
		return false;
	}
	
//	インベーダーとPlayerの弾との衝突判定
	private boolean isInvaderShotByBullet(Bullet b) {
		for(Invader i: this.invaders) {
			if(i.getX() == b.getX() && i.getY() == b.getY() - 1 && !i.isDead()) {
				wipeOutInvader(i);
				return true;
			}
		}
		return false;
	}
	
	private Invader findInvaderShot(Bullet b) {
		Invader invader = null;
		for(Invader i: this.invaders) {
			if(i.getX() == b.getX() && i.getY() == b.getY() - 1 && !i.isDead()) {
				invader = i;
			}
		}
		return invader;
	}
	
//	InvaderのbulletとPlayerのbulletの当たり判定
	private boolean isInvaderBulletShotByPlayerBullet(Bullet b) {
		for(Invader i: this.invaderWhoShot) {
			Bullet invaderBullet = i.getBullet();
			if(invaderBullet.getX() == b.getX() && (invaderBullet.getY() == b.getY() - 1 || invaderBullet.getY() + 1 == b.getY() - 1)) {
				return true;
			}
		}
		return false;
	}
	
//	Playerの弾と衝突したInvaderの弾を放ったInvaderのIndexを見つけるためのメソッド
	private int findBulletCollapsedInvaderIndex(Bullet b) {
		for(int i = 0; i < this.invaderWhoShot.size(); i++) {
			Bullet invaderBullet = this.invaderWhoShot.get(i).getBullet();
			if(invaderBullet.getX() == b.getX() && (invaderBullet.getY() == b.getY() - 1 || invaderBullet.getY() + 1 == b.getY() - 1)) {
				return i;
			}
		}
		return -1;
	}
	
//	PlayerのbulletがInvaderに当たった時Invaderを消去するためのメソッド
	private void wipeOutInvader(Invader invader) {
//		this.invaders.remove(this.invaders.indexOf(invader));
		this.invaders.get(this.invaders.indexOf(invader)).setDead(true);
		this.invaders.get(this.invaders.indexOf(invader)).getBullet().setFired(false);
	}
	
	public boolean invaderIntersectPlayer() { 
		for(Invader i: invaders) {
			if(!i.isDead() && i.getX() == this.player.getX() && i.getY() == this.player.getY()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isGameCleared() {
		return isAllInvaderShot();
	}
	
	private boolean isAllInvaderShot() {
		for(Invader i: this.invaders) {
			if(!i.isDead()) {
				return false;
			}
		}
		return true;
	}
	

//	2.Playerのbulletの位置を更新するためのメソッド
//	Invaderがbulletを全発射する場合使用する
//	public void updateBulletPosition() {
//		Bullet b = player.getBullet();
//		if(b.isFired()) {
//			int nextY = b.getY() - 1;
////			Playerのbulletが枠外に行ってしまう場合、またはbulletとInvaderが衝突した場合
//			if(nextY < 0 || isInvaderShotByBullet(b)) {
//				b.setFired(false);
//			} else if(invaderWhoShot.size() > 0 && isInvaderBulletShotByPlayerBullet(b)) {
//				b.setFired(false);
//				int index = findBulletCollapsedInvaderIndex(b);
//				if(index != -1) {
//					invaderWhoShot.get(index).getBullet().setFired(false);
//				}
//			} else {
//				b.setY(nextY);
//			}
//		}
//	}
	
//	2.Invaderのbulletの位置を更新するためのメソッド
//	Invaderがbulletを全発射する場合使用する
//	public void updateInvaderBullets() {
//		if(invaderWhoShot.size() > 0) {
//			for(Invader i: invaderWhoShot) {
//				if(i.getBullet().isFired()) {
//					int nextY = i.getBullet().getY() + 1;
////					bulletが枠外に出てしまう場合
//					if(nextY >= SCREEN_HEIGHT) {
//						i.getBullet().setFired(false);
//					} else {
//						i.getBullet().setY(nextY);
//					}
//				}
//				
//			}
//		}
//	}
		
}
