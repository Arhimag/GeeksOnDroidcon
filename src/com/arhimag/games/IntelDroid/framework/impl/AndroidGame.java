package com.arhimag.games.IntelDroid.framework.impl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.arhimag.games.IntelDroid.framework.Audio;
import com.arhimag.games.IntelDroid.framework.FileIO;
import com.arhimag.games.IntelDroid.framework.Game;
import com.arhimag.games.IntelDroid.framework.Graphics;
import com.arhimag.games.IntelDroid.framework.Input;
import com.arhimag.games.IntelDroid.framework.Screen;

public abstract class AndroidGame extends Activity implements Game
{
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	//WakeLock wakeLock;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON ,  WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
		
		// boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		// ����������  ������ ������� getWidth ���� ����, ����� ������������ 8�� ����������� API
		@SuppressWarnings("deprecation")
		int frameBufferWidth = getWindowManager().getDefaultDisplay().getWidth(); //isLandscape ? 800 : 600;
		// ����������  ������ ������� getHeight ���� ����, ����� ������������ 8�� ����������� API
		@SuppressWarnings("deprecation")
		int frameBufferHeight = getWindowManager().getDefaultDisplay().getHeight(); //isLandscape ? 600 : 800;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,frameBufferHeight,Config.RGB_565);
		// ����������  ������ ������� getWidth ���� ����, ����� ������������ 8�� ����������� API
		@SuppressWarnings("deprecation")
		float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
		// ����������  ������ ������� getHeight ���� ����, ����� ������������ 8�� ����������� API
		@SuppressWarnings("deprecation")
		float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
		
		renderView = new AndroidFastRenderView( this, frameBuffer);
		graphics = new AndroidGraphics( getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput( this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);
		
		//PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		//wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		//wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		//wakeLock.release();
		renderView.pause();
		screen.pause();
		
		if( isFinishing())
			screen.dispose();
	}
	
	@Override
	public Input getInput()
	{
		return input;
	}

	@Override
	public FileIO getFileIO()
	{
		return fileIO;
	}

	@Override
	public Graphics getGraphics()
	{
		return graphics;
	}

	@Override
	public Audio getAudio()
	{
		return audio;
	}

	@Override
	public void setScreen(Screen screen)
	{
		if( screen == null)
			throw new IllegalArgumentException("Screen Must not be null");
		
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
		this.input.setBackKeyListener( screen );
	}

	@Override
	public Screen getCurrentScreen()
	{
		return screen;
	}
	
	@SuppressWarnings("deprecation")
	public int getDisplayWidth()
	{
		// ����������  ������ ������� getWidth ���� ����, ����� ������������ 8�� ����������� API
		return  getWindowManager().getDefaultDisplay().getWidth();
	}
	
	@SuppressWarnings("deprecation")
	public int getDisplayHeight()
	{
		// ����������  ������ ������� getHeight ���� ����, ����� ������������ 8�� ����������� API
		return  getWindowManager().getDefaultDisplay().getHeight();
	}

	@Override
	public abstract Screen getStartScreen();

}
