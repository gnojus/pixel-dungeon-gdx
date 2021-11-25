package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.actors.buffs.Slow;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Slowness extends Weapon.Enchantment {
    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x0044FF);
    private static final String TXT_CHILLING = "chilling %s";

    @Override
    public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max(0, weapon.effectiveLevel());
        if (Random.Int(level + 4) < 3) {
            return false;
        }
        Buff.prolong(defender, Slow.class, Random.Float(1.0f, level + 2) * Slow.durationFactor(defender));
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_CHILLING, weaponName);
    }
}