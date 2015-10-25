package com.arhimag.games.IntelDroid.GameElements;

import android.util.Log;

import com.arhimag.games.IntelDroid.Settings;

public class Droidcon implements DroidconImmutable
{
	public static final int GIFT_UNDEFINED = 0;
	public static final int GIFT_CHOCO = 1;
	public static final int GIFT_PEN = 2;
	public static final int GIFT_NOTE = 3;
	public static final int GIFT_LOVE = -1;
	public static final int GIFT_PROJECT = -2;
	public static final int PRINCESS_ID = 0;
	public static final int OLDMAN_ID = 1;
	public static final int ANDROIDER_ID = 2;
	public static final int APPLER_ID = 3;
	public static final int STEEVE_ID = 4;
	public static final int GANDALF_ID = 5;
	public static final int SUPER_GEEKS_COUNT = 6;

	private final int GIFTS_COUNT;
	private final int X;
	private final int Y;

	private final int DEFAULT_WIDTH;
	private final int DEFAULT_HEIGHT;
	private final int DEFAULT_GEEKS;
	private final int DEFAULT_CHOCO_X;
	private final int DEFAULT_CHOCO_Y;
	private final int DEFAULT_PEN_X;
	private final int DEFAULT_PEN_Y;
	private final int DEFAULT_NOTE_X;
	private final int DEFAULT_NOTE_Y;

	private final double giftDuration; // Duration of the gift activity duration, duration 0 is infinity
	private final int changeDirectionProbability;
	private final java.util.Random droidconCoin; // Generator of random numbers for defining is change direction for this geek needed. And for definening is any Super Geek reached.

	private int width; // Width of the field in Pseudo-pixels
	private int height; // Height of the field in Pseudo-pixels

	private int activeGift; // ID of the active gift. If there is no active gifts, value is GIFT_UNDEFINED
	private double giftActivateTime; // Time of the gift activation in milliseconds
	private int giftCoords[][]; // Coords of the gifts creation

	private double lastTickTime; // tickedTime is stored in milliseconds

	private Geek geeks[]; // Pool of the geeks on the Droidcon

	public Droidcon()
	{
		GIFTS_COUNT = 3;
		X = 0;
		Y = 1;

		DEFAULT_WIDTH = 16;
		DEFAULT_HEIGHT = 9;
		DEFAULT_GEEKS = 3;
		DEFAULT_CHOCO_X = DEFAULT_WIDTH / 9 + 1;
		DEFAULT_CHOCO_Y = DEFAULT_HEIGHT;
		DEFAULT_PEN_X = (int)( DEFAULT_WIDTH * 8.0 / 9.0);
		DEFAULT_PEN_Y = DEFAULT_HEIGHT;
		DEFAULT_NOTE_X = DEFAULT_WIDTH / 2;
		DEFAULT_NOTE_Y = DEFAULT_HEIGHT;

		giftDuration = 0.0; // Duration of the gift activity duration, duration 0 is infinity
		changeDirectionProbability = 150;
		droidconCoin = new java.util.Random( System.nanoTime() );

		initialyzeDroidcon();
	}

	public Droidcon( int width, int height, int geeksCount )
	{
		this();
		initialyzeDroidcon( width, height, geeksCount );
	}

	public Droidcon( int width, int height, int geeksCount,
	                 int chocoX, int chocoY,
	                 int penX, int penY,
	                 int noteX, int noteY)
	{
		this();
		initialyzeDroidcon( width, height, geeksCount, chocoX, chocoY, penX, penY, noteX, noteY);
	}

	public void initialyzeDroidcon( )
	{
		initialyzeDroidcon( DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_GEEKS,
				DEFAULT_CHOCO_X, DEFAULT_CHOCO_Y,
				DEFAULT_PEN_X, DEFAULT_PEN_Y,
				DEFAULT_NOTE_X, DEFAULT_NOTE_Y);
	}

	public void initialyzeDroidcon( int geeksCount )
	{
		initialyzeDroidcon( DEFAULT_WIDTH, DEFAULT_HEIGHT, geeksCount,
				DEFAULT_CHOCO_X, DEFAULT_CHOCO_Y,
				DEFAULT_PEN_X, DEFAULT_PEN_Y,
				DEFAULT_NOTE_X, DEFAULT_NOTE_Y);
	}

	public void initialyzeDroidcon( int width, int height, int geeksCount )
	{
		initialyzeDroidcon( width, height, geeksCount,
				DEFAULT_CHOCO_X, DEFAULT_CHOCO_Y,
				DEFAULT_PEN_X, DEFAULT_PEN_Y,
				DEFAULT_NOTE_X, DEFAULT_NOTE_Y);
	}

	public void initialyzeDroidcon( int width, int height, int geeksCount,
	                                int chocoX, int chocoY,
	                                int penX, int penY,
	                                int noteX, int noteY)
	{
		java.util.Random targetGenerator = new java.util.Random();

		this.width = width;
		this.height = height;

		geeks = new Geek[geeksCount];
		for( int i = 0; i < geeks.length; i++ )
		{
			geeks[i] = new Geek(this.width, this.height, (int) (System.nanoTime() % 1000) + i);
			geeks[i].setTarget( targetGenerator.nextInt( GIFTS_COUNT ) + 1 );
		}
		if( geeksCount > 0 )
			geeks[geeksCount - 1].setTarget( GIFT_PROJECT );

		giftCoords = new int[GIFTS_COUNT + 1][2];
		giftCoords[GIFT_UNDEFINED][X] = 0;
		giftCoords[GIFT_UNDEFINED][Y] = 0;
		giftCoords[GIFT_CHOCO][X] = chocoX;
		giftCoords[GIFT_CHOCO][Y] = chocoY;
		giftCoords[GIFT_PEN][X] = penX;
		giftCoords[GIFT_PEN][Y] = penY;
		giftCoords[GIFT_NOTE][X] = noteX;
		giftCoords[GIFT_NOTE][Y] = noteY;
		activeGift = GIFT_UNDEFINED;
		giftActivateTime = 0.0;

		lastTickTime = System.nanoTime() / 1000000.0;
	}


	public boolean isGiftActive( int giftId, double tickTime )
	{
		if ( giftDuration > 0.0 )
			return ( activeGift == giftId ) && ( giftActivateTime + giftDuration > tickTime );
		else
			return ( activeGift == giftId );
	}

	public boolean isGiftActive( int giftId )
	{
		return isGiftActive(giftId, (System.nanoTime() / 1000000.0));
	}

	public void tick( double currentTickTime )
	{
		double deltaTicks = currentTickTime - lastTickTime;

		if ( deltaTicks < 1.0 )
			return;

		lastTickTime = currentTickTime;

		for( Geek currentGeek : geeks )
		{
			double distance = deltaTicks * currentGeek.getSpeed();
			// Chance to change this geek's movement direction.
			if ( droidconCoin.nextInt( changeDirectionProbability ) == 0 )
				currentGeek.updateDirection();

			if ( isGiftActive(currentGeek.getTarget(), currentTickTime) )
				currentGeek.setDirection( Math.atan(
						( giftCoords[ currentGeek.getTarget() ][Y] - currentGeek.getY()) / (giftCoords[ currentGeek.getTarget() ][X] - currentGeek.getX() ) )
						+ (giftCoords[ currentGeek.getTarget() ][X] - currentGeek.getX() < 0 ? Math.PI : 0.0 ) );


			double newX = currentGeek.getX() + Math.cos( currentGeek.getDirection() ) * distance;
			double newY = currentGeek.getY() + Math.sin( currentGeek.getDirection() ) * distance;

			if ( newX < 0.0 || newX > (double) width )
				currentGeek.updateDirection();
			else if ( newY < 0.0 || newY > (double) height )
				currentGeek.updateDirection();
			else
			{
				currentGeek.setX( newX );
				currentGeek.setY( newY );
			}
		}
	}

	public int geeksCount()
	{
		return geeks.length;
	}

	public GeekImmutable getGeek( int i )
	{
		return geeks[i];
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setActiveGift( int giftId )
	{
		if (giftId <= GIFTS_COUNT)
		{
			activeGift = giftId;
			giftActivateTime = System.nanoTime()/1000000.0;
		}
	}

	public float getGiftX( int  giftId )
	{
		if( giftId >= 0 && giftId < giftCoords.length )
			return giftCoords[giftId][X];
		else
			return 0;
	}

	public float getGiftY( int  giftId )
	{
		if( giftId >= 0 && giftId < giftCoords.length )
			return giftCoords[giftId][Y];
		else
			return 0;
	}

	public int getSuperGeek()
	{
		int flipACoin = droidconCoin.nextInt(4);
		for( int i = 0; i < Math.min( (geeks.length - DEFAULT_GEEKS), SUPER_GEEKS_COUNT); i++ )
			if ( ( ! Settings.isGotSuperGeek(i) ) && ( 0 == flipACoin ) )
				return i;
		return -1;
	}
}
