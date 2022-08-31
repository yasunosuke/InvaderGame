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
	
	private Player player = new Player("��", SCREEN_WIDTH / 2, SCREEN_HEIGHT - 2);
	
//	moveInvaders()�̕p�x�̂��߂̃J�E���^�[
	private int invaderCount = 0;
	private InvaderDirections invaderDirection = RIGHT;
	private List<Invader> invaders = new ArrayList<>();
//	bullet�������Invader�������i�[
	private List<Invader> invaderWhoShot = new ArrayList<>();

	private String[][] screen = new String[SCREEN_HEIGHT][SCREEN_WIDTH];
	
	public Screen() {
		super();
	}
	
//	�Q�[�������������邽�߂̃��\�b�h
	public void init() {
		prepareInvaders();
	}

//	Invader���e�|�W�V�����ɔz�u���邽�߂̃��\�b�h
	private void prepareInvaders() {
		//		1��΂���Invader��`�����߂Ƀ|�W�V������}��
		for(int y = 0; y < Screen.INVADER_ROW; y++) {
			for(int x = 0; x < Screen.INVADER_COLUMN; x++) {
				this.invaders.add(new Invader("��", x * 2, y * 2));
			}
		}
	}
	
// �@�X�N���[����`�悷�邽�߂̃��\�b�h
	public void draw() {
		
//		�h�b�g��������
		for(int y = 0; y < SCREEN_HEIGHT; y++) {
			for(int x = 0; x < SCREEN_WIDTH; x++) {
				screen[y][x] = "�E";
			}
		}
		
//		�v���C���[��������
		screen[player.getY()][player.getX()] = player.getCharacter();
		
//		Player bullet�������� bullet�����˂���Ă����Ȃ�
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
		
		
//		Invader��screen�ɏ�������
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
//		invader�ړ�
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
	
//	Invader���g�O�ɏo�邩�ǂ������肷�邽�߂̃��\�b�h
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
//			��ԉ���Invader�����������Ȃ�
			if(invaderWhoShootIndex >= 0 && invader != null && invader.getY() + 1 >= Screen.SCREEN_HEIGHT) {
				return true;
			}
		}
		return false;
	}
	
//	Invader�����̃|�W�V�����������̈ʒu�ɖ߂����߂̃��\�b�h
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
	
//	Player��bullet�𔭎˂��邽�߂̃��\�b�h
	public void shootPlayerBullet() {
		player.shoot();
	}
	
//	Invader����bullet�𔭎˂��邽�߂̃��\�b�h
//	��Ĕ���
	public void shootAllInvaderBullets() { 
		if(invaderWhoShot.size() == 0) {
			
//			�e��̈�ԉ���Invader��bullet��ł��̂Ƃ���
//			invaders�̗񂲂Ƃɑ��삷��
			for(int x = 0; x < INVADER_COLUMN; x++) {
				int invaderWhoShootIndex = -1;
				for(int y = 0; y < INVADER_ROW; y++) {
					int index = x + y * INVADER_COLUMN;
					if(!this.invaders.get(index).isDead()) {
						invaderWhoShootIndex = index;
					}
				}
//				��ԉ���Invader�����������Ȃ�
				if(invaderWhoShootIndex >= 0) {
					Invader invader = invaders.get(invaderWhoShootIndex);
					Bullet invaderBullet = invader.getBullet();
					invaderBullet.setFired(true);
					invaderBullet.setX(invader.getX());
					invaderBullet.setY(invader.getY() + 1);
//					�N���ł����̂��X�V���邷��ꍇ(���ڈȍ~�̔���)
					if(invaderWhoShot.size() >= INVADER_COLUMN) {
						invaderWhoShot.set(x, invader);
					} else {
						invaderWhoShot.add(invader);
					}
					
				}
			}
		} 
//		���ڈȍ~�̔���
		else if(isNotFiredInvaderShotsExisted()) {

			List<Integer> foundIndexes = findIndexOfNotFiredInvaderShots();
//          ��ԉ���Invader���e�����
			for(Integer x: foundIndexes) {
				int invaderWhoShootIndex = -1;
				for(int y = 0; y < INVADER_ROW; y++) {
					int index = x + y * INVADER_COLUMN;
					if(!this.invaders.get(index).isDead()) {
						invaderWhoShootIndex = index;
					}
				}
				//				��ԉ���Invader�����������Ȃ�
				if(invaderWhoShootIndex >= 0) {
					Invader invader = invaders.get(invaderWhoShootIndex);
					invader.shoot();
					//					�N���ł����̂��X�V���邷��ꍇ(���ڈȍ~�̔���)
					if(invaderWhoShot.size() >= INVADER_COLUMN) {
						invaderWhoShot.set(x, invader);
					} else {
						invaderWhoShot.add(invader);
					}

				}
			}
		}

	}
	
//	�e�������Invader�̂Ȃ��ɂ��ł�isFired��false�A�܂���isDead��true�ɂȂ��Ă�����̂����邩���肷��
	private boolean isNotFiredInvaderShotsExisted() {
		for(Invader i: this.invaderWhoShot) {
			if(!i.getBullet().isFired() || i.isDead()) {
				return true;
			}
		}
		return false;
	}
	
//	�e�������Invader�̂Ȃ��ɂ��ł�isFired��false�ɂȂ��Ă���index�������邽�߂̃��\�b�h
	private List<Integer> findIndexOfNotFiredInvaderShots() {
		List<Integer> l = new ArrayList<>();
		for(int i = 0; i < this.invaderWhoShot.size(); i++) {
			if(!this.invaderWhoShot.get(i).getBullet().isFired() || invaderWhoShot.get(i).isDead()) {
				l.add(i);
			}
		}
		return l;
	}
	
//	Invader���烉���_����bullet�𔭎˂��邽�߂̃��\�b�h
//	���܂��������g���Ă���
//	Invader����e������\�b�h��shootAllInvaderBullets()�Ƃ��̃��\�b�h������
	public void shootInvaderBulletsRandomly() { 
		if(invaderWhoShot.size() == 0) {
			
//			�e��̈�ԉ���Invader��bullet��ł��̂Ƃ���
//			invaders�̗񂲂Ƃɑ��삷��
			for(int x = 0; x < INVADER_COLUMN; x++) {
//              1/INVADER_COLUMN �̊m���Ŕ���
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
//				��ԉ���Invader�����������Ȃ�
				if(invaderWhoShootIndex >= 0) {
					Invader invader = invaders.get(invaderWhoShootIndex);
					invader.shoot();
//					bullet�������Invader��o�^
					invaderWhoShot.add(invader);
					
				}
			}
		}
	}
	
//	Player��bullet�̈ʒu���X�V���邽�߂̃��\�b�h
	public void updatePlayerBulletPosition() {
		Bullet b = player.getBullet();
		if(b.isFired()) {
			int nextY = b.getY() - 1;
//			Player��bullet���g�O�ɍs���Ă��܂��ꍇ�A�܂���bullet��Invader���Փ˂����ꍇ
			if(nextY < 0) {
				b.setFired(false);
			} else if(isInvaderShotByBullet(b)) {
				this.invaderWhoShot.remove(findInvaderShot(b));
				b.setFired(false);
			}
//			Invader����bullet��������Ă��āAPlayer��Invader��bullet���Փ˂�����
			else if(invaderWhoShot.size() > 0 && isInvaderBulletShotByPlayerBullet(b)) {
				b.setFired(false);
				int index = findBulletCollapsedInvaderIndex(b);
				if(index != -1) {
					invaderWhoShot.get(index).getBullet().setFired(false);
					invaderWhoShot.remove(index);
				}
			} else {
//				�ʒu���X�V
				b.setY(nextY);
			}
		}
	}
	
//	Player��bullet�̈ʒu���X�V���邽�߂̃��\�b�h
//	public void updatePlayerBulletPosition() {
//		Bullet b = player.getBullet();
//		if(b.isFired()) {
//			int nextY = b.getY() - 1;
////			Player��bullet���g�O�ɍs���Ă��܂��ꍇ�A�܂���bullet��Invader���Փ˂����ꍇ
//			if(nextY < 0 || isInvaderShotByBullet(b)) {
//				b.setFired(false);
//			} 
////			Invader����bullet��������Ă��āAPlayer��Invader��bullet���Փ˂�����
//			else if(invaderWhoShot.size() > 0 && isInvaderBulletShotByPlayerBullet(b)) {
//				b.setFired(false);
//				int index = findBulletCollapsedInvaderIndex(b);
//				if(index != -1) {
//					invaderWhoShot.get(index).getBullet().setFired(false);
//					invaderWhoShot.remove(index);
//				}
//			} else {
////				�ʒu���X�V
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
//					bullet���g�O�ɏo�Ă��܂��ꍇ
					if(nextY >= SCREEN_HEIGHT) {
						i.getBullet().setFired(false);
						toRemove.add(i);
					} else {
//						�ʒu���X�V
						i.getBullet().setY(nextY);
					}
				}
				
			}
			invaderWhoShot.removeAll(toRemove);
		}
	}
	
	
	
//	Invader��bullet��Player�������������ǂ����̔���
	private boolean isPlayerShotByInvaderBullet(Invader invader) {
		Bullet invaderBullet = invader.getBullet();
		if(this.player.getX() == invaderBullet.getX() && this.player.getY() == invaderBullet.getY()) {
			return true;
		}
		return false;
	}
	
//	Invader��bullet��Player�������������ǂ����̔���
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
	
//	�C���x�[�_�[��Player�̒e�Ƃ̏Փ˔���
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
	
//	Invader��bullet��Player��bullet�̓����蔻��
	private boolean isInvaderBulletShotByPlayerBullet(Bullet b) {
		for(Invader i: this.invaderWhoShot) {
			Bullet invaderBullet = i.getBullet();
			if(invaderBullet.getX() == b.getX() && (invaderBullet.getY() == b.getY() - 1 || invaderBullet.getY() + 1 == b.getY() - 1)) {
				return true;
			}
		}
		return false;
	}
	
//	Player�̒e�ƏՓ˂���Invader�̒e�������Invader��Index�������邽�߂̃��\�b�h
	private int findBulletCollapsedInvaderIndex(Bullet b) {
		for(int i = 0; i < this.invaderWhoShot.size(); i++) {
			Bullet invaderBullet = this.invaderWhoShot.get(i).getBullet();
			if(invaderBullet.getX() == b.getX() && (invaderBullet.getY() == b.getY() - 1 || invaderBullet.getY() + 1 == b.getY() - 1)) {
				return i;
			}
		}
		return -1;
	}
	
//	Player��bullet��Invader�ɓ���������Invader���������邽�߂̃��\�b�h
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
	

//	2.Player��bullet�̈ʒu���X�V���邽�߂̃��\�b�h
//	Invader��bullet��S���˂���ꍇ�g�p����
//	public void updateBulletPosition() {
//		Bullet b = player.getBullet();
//		if(b.isFired()) {
//			int nextY = b.getY() - 1;
////			Player��bullet���g�O�ɍs���Ă��܂��ꍇ�A�܂���bullet��Invader���Փ˂����ꍇ
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
	
//	2.Invader��bullet�̈ʒu���X�V���邽�߂̃��\�b�h
//	Invader��bullet��S���˂���ꍇ�g�p����
//	public void updateInvaderBullets() {
//		if(invaderWhoShot.size() > 0) {
//			for(Invader i: invaderWhoShot) {
//				if(i.getBullet().isFired()) {
//					int nextY = i.getBullet().getY() + 1;
////					bullet���g�O�ɏo�Ă��܂��ꍇ
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
