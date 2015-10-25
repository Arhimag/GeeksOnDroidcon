package com.arhimag.games.IntelDroid.Screens;

import java.util.List;

import com.arhimag.games.IntelDroid.Assets;
import com.arhimag.games.IntelDroid.GameLevelDrawer;
import com.arhimag.games.IntelDroid.Settings;
import com.arhimag.games.IntelDroid.framework.Game;
import com.arhimag.games.IntelDroid.framework.Graphics;
import com.arhimag.games.IntelDroid.framework.Input;
import com.arhimag.games.IntelDroid.framework.Input.TouchEvent;
import com.arhimag.games.IntelDroid.framework.Input.KeyEvent;
import com.arhimag.games.IntelDroid.framework.Screen;
import com.arhimag.games.IntelDroid.framework.impl.AndroidGame;


public class MainMenuScreen extends Screen
{
	GameLevelDrawer levelDrawer;
	int geeksCount;

	public MainMenuScreen(Game game)
	{
		super(game);
		AndroidGame andrGame = (AndroidGame) game;
		levelDrawer = new GameLevelDrawer( andrGame.getDisplayWidth(),andrGame.getDisplayHeight() , game.getGraphics());
		geeksCount = 0;
	}

	public MainMenuScreen(Game game, int geeksCount)
	{
		this(game);
		this.geeksCount = geeksCount;
	}

	@Override
	public void update(float deltaTime)
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
        int len = keyEvents.size();
        for(int i = 0; i < len; i++)
        {
            Input.KeyEvent event = keyEvents.get(i);
        }

		len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			game.getAudio().vibrate( 50 );
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP)
			{
				switch ( levelDrawer.menuItemClick(event))
				{
					case 1: // play game
						if ( geeksCount <= 0 )
						{
							game.setScreen(new GameScreen(game));
							((GameScreen) game.getCurrentScreen()).setModeStartAdvertisement();
						}
						else
							game.setScreen( new GameScreen(game, geeksCount ) );
						break;
					case 2: // Hall of fame
						if ( geeksCount <= 0 )
							game.setScreen( new HallOfFameScreen(game) );
						else
							game.setScreen( new HallOfFameScreen(game, geeksCount ) );
						break;
					case 3: // Credits
						if ( geeksCount <= 0 )
							game.setScreen( new CreditsScreen(game) );
						else
							game.setScreen( new CreditsScreen(game, geeksCount ) );
						break;
					case 4: // Switch sound
						if ( Settings.isSoundEnabled())
						{
							Settings.setSoundEnabled(false);
							if (Assets.menuMusic.isPlaying()) Assets.menuMusic.stop();
						}
						else
						{
							Settings.setSoundEnabled(true);
							if (!Assets.menuMusic.isPlaying()) Assets.menuMusic.play();
						}
						break;
				}
			}
		}
		
	}


	@Override
	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.clear(0);
		levelDrawer.drawMenu();
	}

	@Override
	public void pause()
	{
		Settings.save(game.getFileIO());
	}

	@Override
	public void resume(){ }

	@Override
	public void dispose(){ }

	@Override
	public boolean backKeyEvent()
	{
		return false;
	}
}
