package no.sandramoen.loveentity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.sandramoen.loveentity.LoveEntityGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Love Entity";
		float scale = 2.0f;
		config.width = (int) (1440 / scale);
		config.height = (int) (2560 / scale);
		config.resizable = false;
		new LwjglApplication(new LoveEntityGame(), config);
	}
}
