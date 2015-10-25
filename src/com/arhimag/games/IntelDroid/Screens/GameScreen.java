package com.arhimag.games.IntelDroid.Screens;

import java.util.List;


import com.arhimag.games.IntelDroid.GameElements.Droidcon;
import com.arhimag.games.IntelDroid.GameLevelDrawer;
import com.arhimag.games.IntelDroid.Settings;
import com.arhimag.games.IntelDroid.Levels.GameLevel;
import com.arhimag.games.IntelDroid.framework.Game;
import com.arhimag.games.IntelDroid.framework.Graphics;
import com.arhimag.games.IntelDroid.framework.Input.KeyEvent;
import com.arhimag.games.IntelDroid.framework.Input.TouchEvent;
import com.arhimag.games.IntelDroid.framework.Point;
import com.arhimag.games.IntelDroid.framework.Screen;
import com.arhimag.games.IntelDroid.framework.impl.AndroidGame;


public class GameScreen extends Screen
{
	GameLevelDrawer levelDrawer;

	Point old[];
	private GameLevel level;
	private final int GAME_MODE;
	private final int GEEK_ADVERTISMENT_MODE;
	private final int ADVERTISMENT_MODE;
	private final int FAIL_ADVERTISMENT_MODE;
	private final int START_ADVERTISMENT_MODE;
	private int currentMode;
	private int superGeek;
	private int advertisementTextId;
	int thisSessionLevel = 0;
    protected int viberTimer = 50;



	public GameScreen(Game game)
	{
		super(game);
		AndroidGame andrGame = (AndroidGame) game;
		level = new GameLevel();
		old = new Point[10];
		for(int i  = 0; i < 10 ; i++) old[i] = new Point();
		thisSessionLevel = 0;
		GAME_MODE = 1;
		ADVERTISMENT_MODE = 2;
		GEEK_ADVERTISMENT_MODE = 3;
		FAIL_ADVERTISMENT_MODE = 4;
		START_ADVERTISMENT_MODE = 5;
		currentMode = GAME_MODE;
		levelDrawer = new GameLevelDrawer(  andrGame.getDisplayWidth() , andrGame.getDisplayHeight(), game.getGraphics());
	}

	public GameScreen(Game game, int geeksCount)
	{
		this(game);
		level = new GameLevel( geeksCount );
	}
	@Override
	public void update(float deltaTime)
	{
		//Log.d("OmNomNomTrace","Update start");
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		
		int len = keyEvents.size();
		
		for(int i = 0; i < len; i++)
		{
			KeyEvent event = keyEvents.get(i);
			if ( event.keyCode == android.view.KeyEvent.KEYCODE_BACK )
				game.setScreen( new MainMenuScreen(game, level.getDroidcon().geeksCount()) );
			event = null;
		}

		len = touchEvents.size();

		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if( event.type == TouchEvent.TOUCH_UP )
			{
				if ( currentMode == GAME_MODE )
				{
					game.getAudio().vibrate( 50 );
					if ( (level.click(levelDrawer.whichGiftIsClicked(level.getDroidcon(), event)) == Droidcon.GIFT_UNDEFINED) && ( levelDrawer.isGeekClicked(level.getDroidcon(), event) ) )
					{
						if (levelDrawer.isRealGeekClicked(level.getDroidcon(), event))
						{
							superGeek = level.getDroidcon().getSuperGeek();
							if (superGeek >= 0)
							{
								switch (superGeek)
								{
									case Droidcon.PRINCESS_ID:
										Settings.setGotPrincess(true);
										break;
									case Droidcon.OLDMAN_ID:
										Settings.setGotOldman(true);
										break;
									case Droidcon.ANDROIDER_ID:
										Settings.setGotAndroider(true);
										break;
									case Droidcon.APPLER_ID:
										Settings.setGotAppler(true);
										break;
									case Droidcon.STEEVE_ID:
										Settings.setGotSteeve(true);
										break;
									case Droidcon.GANDALF_ID:
										Settings.setGotGandalf(true);
										break;
								}
								currentMode = GEEK_ADVERTISMENT_MODE;
							}
							else
							{
								currentMode = ADVERTISMENT_MODE;
								advertisementTextId = (int) Math.floor( Math.random() * levelDrawer.getLevelAdvertisementsCount() );
							}
						} else
							currentMode = FAIL_ADVERTISMENT_MODE;
					}
				}
				else if ( currentMode == ADVERTISMENT_MODE ||
						currentMode == GEEK_ADVERTISMENT_MODE )
				{
					currentMode = GAME_MODE;
					level.moreGeeksDroidcon();
				}
				else if ( currentMode == FAIL_ADVERTISMENT_MODE )
				{
					currentMode = GAME_MODE;
					level.restart();
				}
				else if ( currentMode == START_ADVERTISMENT_MODE )
					currentMode = GAME_MODE;
			}
		}

		level.update( System.nanoTime() / 1000000.0);
	}

	@Override
	public void present(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.clear(0);

		if ( currentMode == GAME_MODE )
			levelDrawer.drawDroidcon(level.getDroidcon());
		else if ( currentMode == GEEK_ADVERTISMENT_MODE )
			levelDrawer.drawSuperGeekAdvertisment(superGeek);
		else if ( currentMode == ADVERTISMENT_MODE )
			levelDrawer.drawNextGeekAdvertisement(advertisementTextId);
		else if ( currentMode == FAIL_ADVERTISMENT_MODE )
			levelDrawer.drawFailAdvertisement();
		else if ( currentMode == START_ADVERTISMENT_MODE )
			levelDrawer.drawStartAdvertisement();
	}

	@Override
	public void pause()
	{
		Settings.save(game.getFileIO());
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


	public void setModeStartAdvertisement()
	{
		currentMode = START_ADVERTISMENT_MODE;
	}
}
