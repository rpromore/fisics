package com.sandbox;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class SandboxDesktop {
	public static void main (String[] argv) {
		new LwjglApplication(new Sandbox(), "Sandbox", 800, 600, false);
	}
}
