package com.arhimag.games.IntelDroid.Screens;

import com.arhimag.games.IntelDroid.Assets;
import com.arhimag.games.IntelDroid.Settings;
import com.arhimag.games.IntelDroid.framework.Game;
import com.arhimag.games.IntelDroid.framework.Graphics;
import com.arhimag.games.IntelDroid.framework.Screen;

import junit.framework.Assert;

public class LoadingScreen extends Screen
{
	public LoadingScreen(Game game)
	{
		super(game);
	}
	
	public void update( float deltaTime)
	{
		Assets.eat = game.getAudio().newSound("eat.ogg");
		Assets.supper = game.getAudio().newSound("super.ogg");
        Assets.gameMusic = game.getAudio().newMusic("main.mp3");
        Assets.gameMusic.setLooping(true);
        Assets.gameMusic.setVolume(0.1f);
        Assets.menuMusic = game.getAudio().newMusic("menu.mp3");
        Assets.menuMusic.setLooping(true);
        Assets.menuMusic.setVolume(0.1f);
		Assets.geek = game.getGraphics().newPixmap("geek.png", Graphics.PixmapFormat.ARGB8888);
		Assets.floor = game.getGraphics().newPixmap("floor.png", Graphics.PixmapFormat.ARGB8888);

		Assets.chokoOn = game.getGraphics().newPixmap("chocoOn.png", Graphics.PixmapFormat.ARGB8888);
		Assets.notepadOn = game.getGraphics().newPixmap("notepadOn.png", Graphics.PixmapFormat.ARGB8888);
		Assets.pencilOn = game.getGraphics().newPixmap("pencilOn.png", Graphics.PixmapFormat.ARGB8888);

		Assets.chokoOff = game.getGraphics().newPixmap("chocoOff.png", Graphics.PixmapFormat.ARGB8888);
		Assets.notepadOff = game.getGraphics().newPixmap("notepadOff.png", Graphics.PixmapFormat.ARGB8888);
		Assets.pencilOff = game.getGraphics().newPixmap("pencilOff.png", Graphics.PixmapFormat.ARGB8888);

		Assets.princess = game.getGraphics().newPixmap("princess.png", Graphics.PixmapFormat.ARGB8888);
		Assets.oldman = game.getGraphics().newPixmap("oldman.png", Graphics.PixmapFormat.ARGB8888);
		Assets.androider = game.getGraphics().newPixmap("androider.png", Graphics.PixmapFormat.ARGB8888);
		Assets.appler = game.getGraphics().newPixmap("appler.png", Graphics.PixmapFormat.ARGB8888);
		Assets.steeve = game.getGraphics().newPixmap("steeve.png", Graphics.PixmapFormat.ARGB8888);
		Assets.gandalf = game.getGraphics().newPixmap("gandalf.png", Graphics.PixmapFormat.ARGB8888);

		Assets.credits =  game.getGraphics().newPixmap("credits.png", Graphics.PixmapFormat.ARGB8888);

		Assets.rose = game.getGraphics().newPixmap("rose.png", Graphics.PixmapFormat.ARGB8888);

		Settings.load(game.getFileIO());
		game.setScreen(new MainMenuScreen(game));
	}
	
	@Override
	public void present(float deltaTime)
	{
		
	}
	
	@Override
	public void pause()
	{
	
	}
	
	@Override
	public void resume()
	{
		
	}
	
	@Override
	public void dispose()
	{
		
	}

	@Override
	public boolean backKeyEvent()
	{
		return true;
	}
}

