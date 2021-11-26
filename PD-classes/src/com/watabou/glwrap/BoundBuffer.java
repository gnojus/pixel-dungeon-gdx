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

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.Buffer;

public class BoundBuffer {

	private int id, size, target;

	public static int ARRAY = GL20.GL_ARRAY_BUFFER;
	public static int ELEMENT_ARRAY = GL20.GL_ELEMENT_ARRAY_BUFFER;


	public BoundBuffer( Buffer vertices, int size, int target ) {
		id = Gdx.gl.glGenBuffer();
	
		this.size = size;
		this.target = target;
		update(vertices);
	}

	public void update( Buffer vertices ){
		vertices.position(0);
		bind();
	
		Gdx.gl.glBufferData(target, vertices.capacity() * size, vertices, GL20.GL_DYNAMIC_DRAW);
	
		release();
	}

	public void bind(){
		Gdx.gl.glBindBuffer(target, id);
	}

	public void release(){
		Gdx.gl.glBindBuffer(target, 0);
	}

	public void destroy(){
		Gdx.gl.glDeleteBuffer( id );
	}
}
