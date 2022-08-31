package com.invader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InvaderGame {
	
	private static final int FPS = 40;
	private static final int INTERVAL = 1000/FPS;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Screen screen = new Screen();
//		�Q�[�����X�^�[�g���邽�߂̏�����
		screen.init();
		
		long time1 = System.currentTimeMillis();
//		���C�����[�v
		while(true) {
			long time2 = System.currentTimeMillis();
//			�C���^�[�o�����Ԍo�߂�����X�N���[���`��
			if(time2 - time1 >= INTERVAL) {
				
				time1 = System.currentTimeMillis();
				
//				�X�N���[����`�悷��
				screen.draw();
				
//				Invader��bullet��Player�������������ǂ����̔���
				if(screen.isPlayerShotByInvaderBullet()) {
//					����
//					draw();
					System.out.println("Game Over");
					sc.nextLine();
					break;/* �������Ă����烋�[�v�𔲂��� */
				}
				
//				Invader��Player�������������ǂ����̔���
				if(screen.invaderIntersectPlayer()) {
					System.out.println("Game Over");
					sc.nextLine();
					break;/* �������Ă����烋�[�v�𔲂��� */
				}
				
//				Player��Invader��bullet�̈ʒu���X�V����
				screen.updatePlayerBulletPosition();
				screen.updateInvaderBullets();
				
//				�S�Ă�Invader�������ꂽ���ǂ����̔���
//				���̏����̏ꏊ���܂��s���m
				if(screen.isGameCleared()) {
					System.out.println("Congrats");
					System.out.println("Game Clear");
					sc.nextLine();
					break;
				}

//				Bullet�����ꂼ�����
				screen.shootPlayerBullet();
				screen.shootInvaderBulletsRandomly();
				
//				Player��Invader�𓮂���			
				screen.movePlayer();
				screen.moveInvaders();
			}
		}
	}
}
