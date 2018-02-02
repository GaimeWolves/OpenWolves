package com.gamewolves.openwolves;

public class Main {
	
	private static Application application;

	public static void main(String[] args) {
		application = new Application();
		application.init(1280, 720, false, "OpenWolvesTest");
	}

}
