package com.gamewolves.openwolves;

public class Main {
	
	private static DesktopApplication desktopApplication;

	public static void main(String[] args) {
		desktopApplication = new DesktopApplication();
		desktopApplication.init(1280, 720, false, "OpenWolvesTest");
	}

}
