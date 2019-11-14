package no.sandramoen.loveclicker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.sandramoen.loveclicker.LoveclickerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Love Clicker";
		int scale = 2;
		config.width = 1440 / scale;
		config.height = 2560 / scale;
		new LwjglApplication(new LoveclickerGame(), config);
	}
}
