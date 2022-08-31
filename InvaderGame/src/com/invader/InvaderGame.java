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
//		ゲームをスタートするための初期化
		screen.init();
		
		long time1 = System.currentTimeMillis();
//		メインループ
		while(true) {
			long time2 = System.currentTimeMillis();
//			インターバル時間経過したらスクリーン描画
			if(time2 - time1 >= INTERVAL) {
				
				time1 = System.currentTimeMillis();
				
//				スクリーンを描画する
				screen.draw();
				
//				InvaderのbulletとPlayerが当たったかどうかの判定
				if(screen.isPlayerShotByInvaderBullet()) {
//					爆発
//					draw();
					System.out.println("Game Over");
					sc.nextLine();
					break;/* 当たっていたらループを抜ける */
				}
				
//				InvaderとPlayerが当たったかどうかの判定
				if(screen.invaderIntersectPlayer()) {
					System.out.println("Game Over");
					sc.nextLine();
					break;/* 当たっていたらループを抜ける */
				}
				
//				PlayerとInvaderのbulletの位置を更新する
				screen.updatePlayerBulletPosition();
				screen.updateInvaderBullets();
				
//				全てのInvaderが消されたかどうかの判定
//				この処理の場所がまだ不明確
				if(screen.isGameCleared()) {
					System.out.println("Congrats");
					System.out.println("Game Clear");
					sc.nextLine();
					break;
				}

//				Bulletをそれぞれ放つ
				screen.shootPlayerBullet();
				screen.shootInvaderBulletsRandomly();
				
//				PlayerとInvaderを動かす			
				screen.movePlayer();
				screen.moveInvaders();
			}
		}
	}
}
