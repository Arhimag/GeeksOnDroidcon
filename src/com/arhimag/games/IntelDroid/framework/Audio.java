package com.arhimag.games.IntelDroid.framework;

public interface Audio 
{
	public Music newMusic(String filename);
	public Sound newSound(String filename);
    public void vibrate( int  time );
}
