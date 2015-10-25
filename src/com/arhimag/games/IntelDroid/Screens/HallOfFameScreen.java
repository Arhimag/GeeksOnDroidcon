package com.arhimag.games.IntelDroid.Screens;

import com.arhimag.games.IntelDroid.GameLevelDrawer;
import com.arhimag.games.IntelDroid.Settings;
import com.arhimag.games.IntelDroid.framework.Game;
import com.arhimag.games.IntelDroid.framework.Graphics;
import com.arhimag.games.IntelDroid.framework.Input.KeyEvent;
import com.arhimag.games.IntelDroid.framework.Input.TouchEvent;
import com.arhimag.games.IntelDroid.framework.Screen;
import com.arhimag.games.IntelDroid.framework.impl.AndroidGame;

import java.util.List;


public class HallOfFameScreen extends Screen
{
	GameLevelDrawer levelDrawer;
	int geeksCount;

	public HallOfFameScreen (Game game)
	{
		super(game);
		AndroidGame andrGame = (AndroidGame) game;
		levelDrawer = new GameLevelDrawer( andrGame.getDisplayWidth(),andrGame.getDisplayHeight() , game.getGraphics());
		geeksCount = 0;
	}

	public HallOfFameScreen (Game game, int geeksCount)
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
            KeyEvent event = keyEvents.get(i);
	        if ( event.keyCode == android.view.KeyEvent.KEYCODE_BACK )
		        game.setScreen( new MainMenuScreen(game, geeksCount) );
        }

		len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP)
			{
				game.setScreen( new MainMenuScreen(game) );

			}
		}

	}


	@Override
	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.clear(0);
		levelDrawer.drawHallOfFame();
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
		return true;
	}
}
