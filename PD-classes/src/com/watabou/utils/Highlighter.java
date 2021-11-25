package com.watabou.utils;

public class Highlighter {
	
	public String text;
	
	public boolean[] mask;
	
	public Highlighter( String text ) {
		StringBuilder sb = new StringBuilder();
		boolean[] buffer = new boolean[text.length()];
		int buffLen = 0;
		boolean highNow = false;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '_') {
				highNow = !highNow;
			} else {
				if (c != ' ' && c != '\n') {
					buffer[buffLen++] = highNow;
				}
				sb.append(c);
			}
		}
		mask = new boolean[buffLen];
		System.arraycopy(buffer, 0, mask, 0, buffLen);

		this.text = sb.toString();
	}
	
	public boolean[] inverted() {
		boolean[] result = new boolean[mask.length];
		for (int i=0; i < result.length; i++) {
			result[i] = !mask[i];
		}
		return result;
	}
	
	public boolean isHighlighted() {
		for (int i=0; i < mask.length; i++) {
			if (mask[i]) {
				return true;
			}
		}
		return false;
	}
}