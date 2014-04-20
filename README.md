TiC-Tooltips
============

Minecraft mod that adds tooltips to Tinkers' Construct tools and tool parts

[Minecraft Forum Thread](http://www.minecraftforum.net/topic/2509840-)

###Building

1. Clone this repository into a folder named TiCTooltips
2. Create a folder called 'libs' inside the TiCTooltips folder and put [Tinkers' Construct](http://www.minecraftforum.net/topic/1659892-164tinkers-construct/) [version [1.5.5](http://minecraft.curseforge.com/mc-mods/tinkers-construct/files/28-tconstruct-1-5-5-1/)], [CoFHCore](http://teamcofh.com/index.php?page=downloads), [IndustrialCraft 2](http://ic2api.player.to:8080/job/IC2_experimental/) in it
3. If you have [Gradle](http://www.gradle.org/) installed, open a command line in the TiCTooltips folder and execute: ```gradle build```. To give the build a version number, use ```gradle build -Pversion=<version>``` instead (example: ```gradle build -Pversion=1.0.0```)
 * If you don't have Gradle installed, you can use [ForgeGradle](http://www.minecraftforge.net/forum/index.php?topic=14048.0)'s gradlew/gradlew.bat instead