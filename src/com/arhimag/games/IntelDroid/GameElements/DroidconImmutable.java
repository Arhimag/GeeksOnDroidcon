package com.arhimag.games.IntelDroid.GameElements;

/**
 * Created by arhimag on 28.09.15.
 */
public interface DroidconImmutable
{

	boolean isGiftActive (int giftId, double tickTime);
	boolean isGiftActive (int giftId);
	int geeksCount ();
	GeekImmutable getGeek (int i);
	int getWidth();
	int getHeight();
	float getGiftX( int  giftId );
	float getGiftY( int  giftId );
	int getSuperGeek();
}
