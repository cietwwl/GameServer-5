package com.mi.game.module.gameserver;

public class Test {

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			DebugThread t = new DebugThread(1000);
			t.setName(i + " t");
			t.start();
		}
	}
}
