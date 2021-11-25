/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.utils;

public class Utils {

	public static String capitalize( String str ) {
		return Character.toUpperCase( str.charAt( 0 ) ) + str.substring( 1 );
	}
	
	public static String format( String format, Object...args ) {
		StringBuilder builder = new StringBuilder();
		int arg = 0;
		for (int i = 0; i < format.length(); i++) {
			if (format.charAt(i) == '%') {
				switch (format.charAt(++i)) {
					case '+':
						if (format.charAt(++i) != 'd')
							throw new RuntimeException("Invalid format");
						if (((Number)args[arg]).doubleValue() >= 0)
							builder.append('+');
					case 'd':
					case 's':
					case 'f':
						builder.append(args[arg++]);
						break;

					case '%':
						builder.append('%');
						break;

					default:
						throw new RuntimeException("Unknown format");
				}
			} else {
				builder.append(format.charAt(i));
			}
		}
		return builder.toString();
	}
	
	public static String VOWELS	= "aoeiu";
	
	public static String indefinite( String noun ) {
		if (noun.length() == 0) {
			return "a";
		} else {
			return (VOWELS.indexOf( Character.toLowerCase( noun.charAt( 0 ) ) ) != -1 ? "an " : "a ") + noun;
		}
	}
}
