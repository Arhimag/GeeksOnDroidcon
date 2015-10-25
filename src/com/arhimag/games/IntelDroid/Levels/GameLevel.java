package com.arhimag.games.IntelDroid.Levels;


import com.arhimag.games.IntelDroid.GameElements.Droidcon;
import com.arhimag.games.IntelDroid.GameElements.DroidconImmutable;

public/* abstract */class GameLevel
{

	protected Droidcon droidcon;
	private final int startGeeksCount;
	private int currentGeeksCount;

    public GameLevel( )
	{
		startGeeksCount = 3;
		currentGeeksCount = startGeeksCount;

		droidcon = new Droidcon();
	}

	public GameLevel( int geeksCount )
	{
		this();
		droidcon.initialyzeDroidcon(geeksCount);
	}

	public void update(double tickTime )
	{
		droidcon.tick( tickTime );
	}

	public DroidconImmutable getDroidcon() { return droidcon; }

	public int click( int giftId )
	{
		if ( giftId != Droidcon.GIFT_UNDEFINED )
		{
			if ( droidcon.isGiftActive( giftId ) )
				droidcon.setActiveGift( Droidcon.GIFT_UNDEFINED );
			else
				droidcon.setActiveGift( giftId );
		}

		return giftId;
	}

	public void restart()
	{
		droidcon.initialyzeDroidcon( startGeeksCount );
		currentGeeksCount = startGeeksCount;
	}

	public void moreGeeksDroidcon()
	{
		droidcon.initialyzeDroidcon( ++currentGeeksCount );
	}

}
