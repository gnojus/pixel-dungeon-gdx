package com.watabou.utils;

import com.watabou.input.NoosaInputProcessor;

public class JVMPlatformSupport<GameActionType> extends PDPlatformSupport<GameActionType> {
    
    public JVMPlatformSupport(String version, String basePath, NoosaInputProcessor<GameActionType> inputProcessor) {
        super(version, basePath, inputProcessor);
    }

    public PDThread newThread(Runnable runnable) {
		return new JavaThread(runnable);
	}

    static class JavaThread extends Thread implements PDThread {
		public JavaThread(Runnable runnable) {
			super(runnable);
		}
	}
}
