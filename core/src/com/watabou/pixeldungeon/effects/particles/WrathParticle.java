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
package com.watabou.pixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WrathParticle extends PixelParticle {
    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((WrathParticle) emitter.recycle(WrathParticle.class)).reset(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public WrathParticle() {
        color(0x4488EE);
        lifespan = 0.5f;
    }

    public void reset(float x, float y) {
        revive();
        this.x = x;
        this.y = y;
        left = lifespan;
        float a = Random.Float(PointF.PI2);
        angle = a / PointF.G2R;
        speed.set((float) Math.cos(a) * 64.0f, (float) Math.sin(a) * 64.0f);
        acc.set(speed);
        scale.set(16.0f, 1.0f);
    }

    @Override
    public void update() {
        super.update();
        this.am = this.left / this.lifespan;
        this.scale.x = 16.0f / (this.am + 0.1f);
        this.scale.y = 3.0f - (this.am * 2.0f);
    }
}