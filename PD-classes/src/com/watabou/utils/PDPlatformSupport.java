package com.watabou.utils;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.watabou.input.NoosaInputProcessor;

public abstract class PDPlatformSupport<GameActionType> {
	private final String version;
	private final String basePath;
	private final NoosaInputProcessor<GameActionType> inputProcessor;

	public PDPlatformSupport(String version, String basePath, NoosaInputProcessor<GameActionType> inputProcessor) {
		this.version = version;
		this.basePath = basePath;
		this.inputProcessor = inputProcessor;
	}

	public String getVersion() {
		return version;
	}

	public String getBasePath() {
		return basePath;
	}

	public NoosaInputProcessor<GameActionType> getInputProcessor() {
		return inputProcessor;
	}

	public boolean fullscreenDefault() {
		return false;
	}

	public void fullscreen() {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
	}

	public void windowed(int width, int height) {
		Gdx.graphics.setWindowedMode(width, height);
	}
	
	public boolean isFullscreen() {
		return Gdx.graphics.isFullscreen();
	}
	
	public void tick() {
	}

	public boolean isFullscreenEnabled() {
		return false;
	}

	public byte[] readFile(String fileName) throws IOException {
		final FileHandle fh = Gdx.files.external(basePath != null ? basePath + fileName : fileName);
		if (!fh.exists())
			throw new IOException("File " + fileName + " doesn't exist");
		return fh.readBytes();
	}

	public void writeFile(String fileName, byte[] data) {
		final FileHandle fh = Gdx.files.external(basePath != null ? basePath + fileName : fileName);
		fh.writeBytes(data, false);
	}

	public boolean deleteFile(String fileName) {
		final FileHandle fh = Gdx.files.external(basePath != null ? basePath + fileName : fileName);
		return fh.exists() && fh.delete();
	}

	public abstract PDThread newThread(Runnable runnable);

	public interface PDThread {
		public void start();
		public boolean isAlive();
	}
}
