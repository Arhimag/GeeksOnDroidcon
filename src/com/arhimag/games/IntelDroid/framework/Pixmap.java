package com.arhimag.games.IntelDroid.framework;

import com.arhimag.games.IntelDroid.framework.Graphics.PixmapFormat;

public interface Pixmap 
{
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}
