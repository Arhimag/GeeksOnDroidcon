package com.arhimag.games.IntelDroid;

import com.arhimag.games.IntelDroid.Screens.GameScreen;
import com.arhimag.games.IntelDroid.Screens.LoadingScreen;
import com.arhimag.games.IntelDroid.framework.Screen;
import com.arhimag.games.IntelDroid.framework.impl.AndroidGame;

public class IntelDroid extends AndroidGame
{
	@Override
	public Screen getStartScreen()
	{
		return new LoadingScreen(this);
	}

    @Override
    public void setScreen(Screen screen)
    {
        Screen oldScreen = this.getCurrentScreen();
        super.setScreen(screen);
        if( screen instanceof GameScreen )
        {
            if ( Assets.gameMusic != null && !Assets.gameMusic.isPlaying()  && Settings.isSoundEnabled() ) Assets.gameMusic.play();
        }
        else
        {
            if ( Assets.gameMusic != null && !Assets.menuMusic.isPlaying()  && Settings.isSoundEnabled() ) Assets.menuMusic.play();
        }
    }

    @Override
    public void onResume()
    {
        if( getCurrentScreen() instanceof GameScreen  )
        {
            if ( Assets.gameMusic != null && !Assets.gameMusic.isPlaying() && Settings.isSoundEnabled() ) Assets.gameMusic.play();
        }
        else
        {
            if ( Assets.menuMusic != null && !Assets.menuMusic.isPlaying() && Settings.isSoundEnabled() ) Assets.menuMusic.play();
        }

        super.onResume();
    }

    @Override
    public void onPause()
    {
        if ( Assets.menuMusic != null && Assets.menuMusic.isPlaying() ) Assets.menuMusic.stop();
        if ( Assets.gameMusic != null && Assets.gameMusic.isPlaying() ) Assets.gameMusic.stop();
        super.onPause();
    }

}
