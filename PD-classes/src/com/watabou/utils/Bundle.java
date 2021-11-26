/*
 * Copyright (C) 2012-2014  Oleg Dolya
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

package com.watabou.utils;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class Bundle {

	private static final String CLASS_NAME = "__className";
	
	private static HashMap<String,String> aliases = new HashMap<String, String>();
	
	private JsonValue data;
	
	public Bundle() {
		this( new JsonValue(ValueType.object) );
	}
	
	public String toString() {
		return data.toString();
	}
	
	private Bundle( JsonValue data ) {
		this.data = data;
	}
	
	public boolean isNull() {
		return data == null;
	}
	
	public boolean contains( String key ) {
		return data.get( key ) != null;
	}
	
	public boolean getBoolean( String key ) {
		return data.getBoolean( key, false );
	}
	
	public int getInt( String key ) {
		return data.getInt( key, 0 );
	}
	
	public float getFloat( String key ) {
		return (float)data.getDouble( key, 0 );
	}
	
	public String getString( String key ) {
		return data.getString( key, "" );
	}
	
	public Bundle getBundle( String key ) {
		return new Bundle( data.get( key ) != null ? data.get( key ) : new JsonValue(ValueType.object));
	}
	
	private Bundlable get() {
		try {
			String clName = getString( CLASS_NAME );
			if (aliases.containsKey( clName )) {
				clName = aliases.get( clName );
			}
			
			Class<?> cl = ClassReflection.forName( clName );
			if (cl != null) {
				Bundlable object = (Bundlable) ClassReflection.newInstance(cl);
				object.restoreFromBundle( this );
				return object;
			} else {
				return null;
			}
		} catch (Exception e) {
			e = null;
			return null;
		}	
	}
	
	public Bundlable get( String key ) {
		return getBundle( key ).get();	
	}
	
	public <E extends Enum<E>> E getEnum( String key, Class<E> enumClass ) {
		try {
			return (E)Enum.valueOf( enumClass, data.getString( key ) );
		} catch (Exception e) {
			return enumClass.getEnumConstants()[0];
		}
	}
	
	public int[] getIntArray( String key ) {
		try {
			return data.get(key).asIntArray();
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean[] getBooleanArray( String key ) {
		try {
			return data.get(key).asBooleanArray();
		} catch (Exception e) {
			return null;
		}
	}
	
	public String[] getStringArray( String key ) {
		try {
			return data.get(key).asStringArray();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Collection<Bundlable> getCollection( String key ) {
		
		ArrayList<Bundlable> list = new ArrayList<Bundlable>();
		
		try {
			JsonValue array = data.get( key );
			for (int i=0; i < array.size; i++) {
				list.add( new Bundle( array.get( i ) ).get() );
			}
		} catch (Exception e) {
			
		}
		
		return list;
	}
	
	public void put( String key, boolean value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {

		}
	}
	
	public void put( String key, int value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {

		}
	}
	
	public void put( String key, float value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {

		}
	}
	
	public void put( String key, String value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {

		}
	}
	
	public void put( String key, Bundle bundle ) {
		try {
			data.addChild( key, bundle.data );
		} catch (Exception e) {

		}
	}
	
	public void put( String key, Bundlable object ) {
		if (object != null) {
			try {
				Bundle bundle = new Bundle();
				bundle.put( CLASS_NAME, object.getClass().getName() );
				object.storeInBundle( bundle );
				data.addChild(key, bundle.data);
			} catch (Exception e) {
			}
		}
	}
	
	public void put( String key, Enum<?> value ) {
		if (value != null) {
			try {
				data.addChild( key, new JsonValue( value.name() ) );
			} catch (Exception e) {
			}
		}
	}
	
	public void put( String key, int[] array ) {
		try {
			JsonValue jsonArray = new JsonValue(ValueType.array);
			for (int i=0; i < array.length; i++) {
				jsonArray.addChild( new JsonValue( array[i]) );
			}
			data.addChild( key, jsonArray );
		} catch (Exception e) {
			
		}
	}
	
	public void put( String key, boolean[] array ) {
		try {
			JsonValue jsonArray = new JsonValue(ValueType.array);
			for (int i=0; i < array.length; i++) {
				jsonArray.addChild( new JsonValue( array[i] ) );
			}
			data.addChild( key, jsonArray );
		} catch (Exception e) {
			
		}
	}
	
	public void put( String key, String[] array ) {
		try {
			JsonValue jsonArray = new JsonValue(ValueType.array);
			for (int i=0; i < array.length; i++) {
				jsonArray.addChild( new JsonValue( array[i] ) );
			}
			data.addChild( key, jsonArray );
		} catch (Exception e) {
			
		}
	}
	
	public void put( String key, Collection<? extends Bundlable> collection ) {
		JsonValue array = new JsonValue(ValueType.array);
		for (Bundlable object : collection) {
			Bundle bundle = new Bundle();
			bundle.put( CLASS_NAME, object.getClass().getName() );
			object.storeInBundle( bundle );
			array.addChild( bundle.data );
		}
		try {
			data.addChild( key, array );
		} catch (Exception e) {
			
		}
	}

	private static final char XOR_KEY = 0x1F;
	
	public static Bundle read( byte[] input ) {
		try {
			byte[] buffer = new byte[input.length];
			for (int i=0; i < input.length; i++) {
				buffer[i] = input[i];
				buffer[i] ^= XOR_KEY;
			}
			JsonValue json = new JsonReader().parse(new String(buffer));
			return new Bundle( json );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] write( Bundle bundle ) {
		byte[] chars = bundle.data.toJson(OutputType.json).getBytes();
		for (int i=0; i < chars.length; i++) {
			chars[i] ^= XOR_KEY;
		}
		return chars;
	}
	
	public static void addAlias( Class<?> cl, String alias ) {
		aliases.put( alias, cl.getName() );
	}
}
