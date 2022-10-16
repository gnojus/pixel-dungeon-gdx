pixel-dungeon-gdx
=================

GDX port of the awesome [Pixel Dungeon](https://github.com/watabou/pixel-dungeon)

This is a fork of Arcnor's GDX port pushed to *Pixel Dungeon* 1.9.2a. In addition, it has a working html backend. You can play it on https://gnojus.github.io/pixel-dungeon-gdx.

This fork is focused on the desktop/html versions, therefore I don't intend to maintain the mobile versions and those are likely to be removed in future.

Quickstart
----------
Download the [latest jar](https://github.com/gnojus/pixel-dungeon-gdx/releases) or build it yourself. 

**Building**
 - `./gradlew desktop:run` to run.
 - `./gradlew desktop:dist` to compile a jar file (located in `desktop/build/libs/` folder).
 - `./gradlew html:dist` to compile a webapp (located in `html/build/dist/`). You may want to remove the super-dev button directly from html.

For more info about gradle tasks: https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline
