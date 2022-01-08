package com.watabou.pixeldungeon.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderCallback;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.watabou.noosa.audio.Music;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.utils.PDPlatformSupport;
import com.watabou.input.NoosaInputProcessor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration();
	}

	@Override
	public PreloaderCallback getPreloaderCallback () {
		return createPreloaderPanel("banner.png");
	}

	@Override
	public ApplicationListener createApplicationListener() {
		String version = "1.9.2a-gdx1.1";
		return new PixelDungeon(new HtmlPlatformSupport(version, null, new HtmlInputProcessor()));
	}

	static class HtmlPlatformSupport<GameActionType> extends PDPlatformSupport {
		boolean isFullscreen = true;

		public HtmlPlatformSupport(String version, String basePath, NoosaInputProcessor<GameActionType> inputProcessor) {
			super(version, basePath, inputProcessor);
		}

		static Preferences files() {
			return Gdx.app.getPreferences("pd-files");
		}

		@Override
		public byte[] readFile(String fileName) throws IOException {
			if (!files().contains(fileName)) {
				throw new IOException("File " + fileName + " doesn't exist");
			}
			return files().getString(fileName).getBytes();
		}

		@Override
		public void writeFile(String fileName, byte[] data) {
			files().putString(fileName, new String(data));
			files().flush();
		}

		@Override
		public boolean deleteFile(String fileName) {
			boolean had = files().contains(fileName);
			files().remove(fileName);
			files().flush();
			return had;
		}

		@Override
		public boolean isFullscreenEnabled() {
			return true;
		}

		@Override
		public boolean fullscreenDefault() {
			return true;
		}

		@Override
		public void fullscreen() {
			windowed(Window.getClientWidth(), Window.getClientHeight());
			isFullscreen = true;
		}

		@Override
		public void windowed(int width, int height) {
			super.windowed(width, height);
			isFullscreen = false;
		}

		@Override
		public boolean isFullscreen() {
			return isFullscreen;
		}

		@Override
		public boolean musicDefault() {
			return false;
		}

		public PDThread newThread(Runnable runnable) {
			return new HtmlThread(runnable);
		}
	}

	static class HtmlThread implements PDPlatformSupport.PDThread {
		Runnable runnable;
		boolean isAlive = false;
		public HtmlThread(Runnable runnable) {
			this.runnable = runnable;
		}

		public void start() {
			isAlive = true;
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					runnable.run();
					isAlive = false;
				}
			});
		}

		public boolean isAlive() {
			return isAlive;
		}
	}
}