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
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.effects.CellEmitter;
import com.watabou.pixeldungeon.effects.Speck;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;


public class Disguise extends FlavourBuff {
    private static final String COSTUME = "costume";
    public static final float DURATION = 100.0f;
    public HeroClass costume;

    public void choose(HeroClass notThis, HeroClass notThat) {
        HeroClass[] costumes = {HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS};
        while (true) {
            this.costume = (HeroClass) Random.element(costumes);
            if (this.costume != notThis && this.costume != notThat) {
                return;
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COSTUME, this.costume);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.costume = (HeroClass) bundle.getEnum(COSTUME, HeroClass.class);
    }

    @Override
    public boolean attachTo(Char target) {
        if (!super.attachTo(target)) {
            return false;
        }
        if (target.sprite != null) {
            CellEmitter.get(target.pos).burst(Speck.factory(7), 6);
        }
        return true;
    }

    @Override
    public void detach() {
        CellEmitter.get(this.target.pos).burst(Speck.factory(7), 6);
        super.detach();
        if (this.target.sprite != null) {
            ((HeroSprite) this.target.sprite).updateTexture();
        }
    }

    @Override
    public int icon() {
        return 32;
    }

    public String toString() {
        switch (costume) {
            case WARRIOR:
                return "Disguised as a warrior";
            case MAGE:
                return "Disguised as a mage";
            case ROGUE:
                return "Disguised as a rogue";
            case HUNTRESS:
                return "Disguised as a huntress";
            default:
                return "";
        }
    }
}