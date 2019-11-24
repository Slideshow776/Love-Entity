package no.sandramoen.loveentity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.sandramoen.loveentity.LoveEntityGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Love Entity";
		int scale = 2;
		config.width = 1440 / scale;
		config.height = 2560 / scale;
		new LwjglApplication(new LoveEntityGame(), config);
	}
}
