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
package com.watabou.pixeldungeon.windows;

import java.util.ArrayList;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.watabou.input.NoosaInputProcessor;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.input.GameAction;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.potions.Potion;
import com.watabou.pixeldungeon.items.scrolls.Scroll;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;

public class WndCatalogus extends WndTabbed {
	
	private static final int WIDTH_P	= 112;
	private static final int HEIGHT_P	= 160;
	
	private static final int WIDTH_L	= 128;
	private static final int HEIGHT_L	= 128;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private static final int TAB_WIDTH		= 50;
	
	private static final String TXT_POTIONS	= "Potions";
	private static final String TXT_SCROLLS	= "Scrolls";
	private static final String TXT_TITLE	= "Catalogus";
	
	private BitmapText txtTitle;
	private ScrollPane list;
	
	private ArrayList<ListItem> items = new ArrayList<WndCatalogus.ListItem>();
	
	private static boolean showPotions = true;
	
	public WndCatalogus() {
		
		super();
		
		if (PixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}
		
		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		add( txtTitle );
		
		list = new ScrollPane( new Component() ) {
			@Override
			public void onClick( float x, float y ) {
				int size = items.size();
				for (int i=0; i < size; i++) {
					if (items.get( i ).onClick( x, y )) {
						break;
					}
				}
			}
		};
		add( list );
		list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );
		
		boolean showPotions = WndCatalogus.showPotions;
		Tab[] tabs = {
			new LabeledTab( TXT_POTIONS ) {
				protected void select( boolean value ) {
					super.select( value );
					WndCatalogus.showPotions = value;
					updateList();
				};
			},
			new LabeledTab( TXT_SCROLLS ) {
				protected void select( boolean value ) {
					super.select( value );
					WndCatalogus.showPotions = !value;
					updateList();
				};
			}
		};
		for (Tab tab : tabs) {
			tab.setSize( TAB_WIDTH, tabHeight() );
			add( tab );
		}
		
		select( showPotions ? 0 : 1 );
	}
	
	private void updateList() {
		
		txtTitle.text( Utils.format( TXT_TITLE, showPotions ? TXT_POTIONS : TXT_SCROLLS ) );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );
		
		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		float pos = 0;
		for (Class<? extends Item> itemClass : showPotions ? Potion.getKnown() : Scroll.getKnown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, width, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			
			pos += item.height();
		}
		
		for (Class<? extends Item> itemClass : showPotions ? Potion.getUnknown() : Scroll.getUnknown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, width, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			
			pos += item.height();
		}
		
		content.setSize( width, pos );
		list.setSize( list.width(), list.height() );
	}

	@Override
	protected void onKeyUp( NoosaInputProcessor.Key<GameAction> key ) {
		if (key.action == GameAction.CATALOGUS) {
			hide();
		} else {
			super.onKeyUp( key );
		}
	}
	
	public class ListItem extends Component {
		
		private Item item;
		private boolean identified;
		
		private ItemSprite sprite;
		private BitmapText label;
		private BitmapText topLeft;
		
		public ListItem( Class<? extends Item> cl ) {
			super();
			
			try {
				item = ClassReflection.newInstance(cl);
				if (identified = item.isIdentified()) {
					sprite.view( item.image(), null );
					label.text( item.name() );
				} else {
					sprite.view( ItemSpriteSheet.SMTH , null );
					label.text( item.trueName() );
					label.hardlight( 0xCCCCCC );
				}
			} catch (Exception e) {
				// Do nothing
			}
			Item instance = Dungeon.hero.belongings.getItem(cl);
            topLeft.text((instance == null || !identified) ? "" : Integer.toString(instance.quantity()));
		}
		
		@Override
		protected void createChildren() {
			sprite = new ItemSprite();
			add( sprite );
			
			topLeft = new BitmapText(PixelScene.font1x);
            topLeft.hardlight(Window.TITLE_COLOR);
            add(topLeft);

			label = PixelScene.createText( 8 );
			add( label );
		}
		
		@Override
		protected void layout() {
			sprite.y = PixelScene.align( y + (height - sprite.height) / 2 );
			
			topLeft.x = sprite.x;
            topLeft.y = sprite.y;

			label.x = sprite.x + sprite.width;
			label.y = PixelScene.align( y + (height - label.baseLine()) / 2 );
		}
		
		public boolean onClick( float x, float y ) {
			if (identified && inside( x, y )) {
				Item instance = Dungeon.hero.belongings.getItem(item.getClass());
				if (instance == null) {
					GameScene.show(new WndInfoItem(item));
				} else {
					GameScene.show(new WndItem(WndCatalogus.this, instance));
				}

				return true;
			} else {
				return false;
			}
		}
	}
}
