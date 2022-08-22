package com.invader;

import java.util.ArrayList;
import java.util.List;

public class InvaderGame {
	
	private static final int FPS = 1;
	private static final int INTERVAL = 1000/FPS;

	public static void main(String[] args) {
		Screen screen = new Screen(prepareInvaders());
		
		long time1 = System.currentTimeMillis();
//		���C�����[�v
		while(true) {
			long time2 = System.currentTimeMillis();
//			�C���^�[�o�����Ԍo�߂�����X�N���[���`��
			if(time2 - time1 >= INTERVAL) {
				time1 = System.currentTimeMillis();
				screen.draw();
			}
		}
	}
	
	private static void Init() {
		
	}
	
	private static List<Invader> prepareInvaders() {
		
		List<Invader> invaders = new ArrayList<Invader>();
//		1��΂���Invader��`�����߂Ƀ|�W�V������}��
		for(int y = 0; y < Screen.INVADER_ROW; y++) {
			for(int x = 0; x < Screen.INVADER_COLUMN; x++) {
				invaders.add(new Invader(x * 2, y * 2));
			}
		}
		
		return invaders;
	}

}
