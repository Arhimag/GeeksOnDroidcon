package com.arhimag.games.IntelDroid;

import com.arhimag.games.IntelDroid.GameElements.Droidcon;
import com.arhimag.games.IntelDroid.GameElements.DroidconImmutable;
import com.arhimag.games.IntelDroid.framework.Graphics;
import com.arhimag.games.IntelDroid.framework.Input.TouchEvent;

public class GameLevelDrawer
{
	// In my C brain - this is a struct. It contain all calculable droidcon based params.
	// so there we have no constructor, only data update method
	private class DroidconDrawParams
	{
		public int chocoXPosition = 0;
		public int chocoYPosition = 0;
		public int pencilXPosition = 0;
		public int pencilYPosition = 0;
		public int notepadXPosition = 0;
		public int notepadYPosition = 0;
		public double geekSize = 3.0;

		public int mapWidth = 0;
		public int mapHeight = 0;
		public int mapX = 0;
		public int mapY = 0;
		public int floorWidth = 0;
		public int floorHeight = 0;
		public int floorX = 0;
		public int floorY = 0;
		public double floorScale = 0.0;

		public DroidconDrawParams ()
		{
			chocoXPosition = 0;
			chocoYPosition = 0;
			pencilXPosition = 0;
			pencilYPosition = 0;
			notepadXPosition = 0;
			notepadYPosition = 0;
			geekSize = 3.0;

			mapWidth = 0;
			mapHeight = 0;
			mapX = 0;
			mapY = 0;
			floorWidth = 0;
			floorHeight = 0;
			floorX = 0;
			floorY = 0;
			floorScale = 0.0;
		}

		public void updateData (DroidconImmutable droidcon)
		{
			int width = screenWidth - paddingLeft - paddingRight;
			int height = screenHeight - paddingTop - paddingBottom;

			geekSize = 4.0 * Math.min(width / (geekFrameWidth * droidcon.getWidth()), height / (geekFrameHeight * droidcon.getHeight())) / 3.0;
			mapWidth = width - (int) geekSize * geekFrameWidth;
			mapHeight = height - (int) geekSize * geekFrameHeight;
			mapX = paddingLeft + (int) geekSize * geekFrameWidth / 2;
			mapY = paddingTop + (int) geekSize * geekFrameHeight / 2;
			floorWidth = width;
			floorHeight = height - (int) geekSize * geekFrameHeight / 2;
			floorX = paddingLeft;
			floorY = paddingTop + (int) geekSize * geekFrameHeight / 2;
			floorScale = Math.max(((double) floorWidth / (double) Assets.floor.getWidth()), ((double) floorHeight / (double) Assets.floor.getHeight()));

			pencilYPosition = notepadYPosition = chocoYPosition =
					screenHeight
							- paddingBottom
							- (int) (Assets.chokoOn.getHeight() * Math.max(onTargetScale, offTargetScale)) / 2 // Using trick, that all targets have one size and as a coordinate we are using center of the picture
							- 10 //Additional padding
			;

			notepadXPosition = mapX + (int) ((droidcon.getGiftX(Droidcon.GIFT_NOTE) * mapWidth) / droidcon.getWidth());
			chocoXPosition = mapX + (int) ((droidcon.getGiftX(Droidcon.GIFT_CHOCO) * mapWidth) / droidcon.getWidth());
			pencilXPosition = mapX + (int) ((droidcon.getGiftX(Droidcon.GIFT_PEN) * mapWidth) / droidcon.getWidth());
		}


	}

	private final double animationTick; // one frame activity in milliseconds
	private final int maxAnimationFramesCounter; // The value of this variable is Least Common Multiple of all animations lengths
	private double lastAnimationTick;
	private int currentAnimationFrame;

	private int screenHeight;
	private int screenWidth;

	private final int geekShadowShapeFrames[][][];
	private final int geekShadowColor;
	private final int geekDevineLight;

	// Size of One Animation Frame of the geek in original file
	private final int geekFrameWidth;
	private final int geekFrameHeight;

	private double onTargetScale; // Scale of image, when Target is active
	private double offTargetScale; // Scale of image, when Target is not active

	private int paddingTop;
	private int paddingBottom;
	private int paddingLeft;
	private int paddingRight;

		// I created this variable as a member serving to goals:
	// This params are needed in many methods and I want to be sure
	// that they are identically calculated in all uses.
	// the second goal is to avoid calling garbage collector
	// and not to make a lot of rubbish in this class.
	private DroidconDrawParams droidconDrawParams = new DroidconDrawParams();
	private Graphics graphics;
	private String newLevelAdvertisments[];

	public GameLevelDrawer (int screenWidth, int screenHeight, Graphics graphics)
	{
		animationTick = 333.0;
		maxAnimationFramesCounter = 4;

		lastAnimationTick = 0;
		currentAnimationFrame = 0;

		geekShadowShapeFrames = new int[][][]
				{
						{
								{ 0, 0, 1, 1, 1, 0, 0 },
								{ 1, 1, 1, 1, 1, 1, 1 },
								{ 1, 1, 1, 1, 1, 1, 1 },
								{ 1, 1, 1, 1, 1, 1, 1 },
								{ 0, 0, 1, 1, 1, 0, 0 }
						},
						{
								{ 0, 0, 1, 1, 1, 0, 0 },
								{ 0, 1, 1, 1, 1, 1, 0 },
								{ 0, 1, 1, 1, 1, 1, 0 },
								{ 0, 1, 1, 1, 1, 1, 0 },
								{ 0, 0, 1, 1, 1, 0, 0 }
						}
				};

		geekShadowColor = 0xAA000000;
		geekDevineLight = 0x55FFFF00;
		geekFrameWidth = 15;
		geekFrameHeight = 22;

		onTargetScale = 2.5; // Scale of image, when Target is active
		offTargetScale = 2.0; // Scale of image, when Target is not active

		paddingTop = 20;
		paddingBottom = 20;
		paddingLeft = 20;
		paddingRight = 20;

		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.graphics = graphics;

		onTargetScale = Math.min(screenHeight / (5.0 * Assets.chokoOn.getHeight()), screenWidth / (8.0 * Assets.chokoOn.getWidth()));
		offTargetScale = onTargetScale / 1.5;

		newLevelAdvertisments = new String[]{
				"He is a clever student",
				"He is a top coder",
				"He read the entire Wikipedia",
				"He wrote Linux on Moo",
				"His code like a poem",
				"His bugs like gems",
				"No NDK!No SDK!Only Assembler!",
				"0010001111001100100101",
				"AF07CE67B901EEFF",
				"Coffee I have idea!",
				"a cup of pizza please",
				"He just wrote a great story",
				"His game design is great",
				"have you ever seen gandalf",
				"HIS LOVE IS CPP",
				"He needs more libraries",
				"His Coding is a kind of art",
				"His Coding is a science",
				"Future is his!",
				"His bubble beats your qsort"
		};
	}
	


	private void drawString (String string, int x, int y, int mapPixelSize, int color)
	{
		int currentx = x;
		for (int i = 0; i < string.length(); i++)
			if (string.charAt(i) != ' ')
			{
				for (int xi = 0; xi < Alphabet.getLetterWidth(); xi++)
					for (int yi = 0; yi < Alphabet.getLetterHeight(); yi++)
						graphics.drawRectBySize(currentx + xi * mapPixelSize, y + yi * mapPixelSize, mapPixelSize, mapPixelSize, (Alphabet.getLetterPixel(string.charAt(i), xi, yi, color) == null ? 0x00000000 : color));
				currentx += (Alphabet.getLetterWidth() + 1) * mapPixelSize;
			} else
				// Arhimag 20150103 currentx += (Alphabet.getLetterWidth() + 1) * mapPixelSize;
				currentx += Alphabet.getWordSpace() * mapPixelSize;
	}


	private void drawGeekShadow (int x, int y, double geekScale, int shadowColor)
	{

		int pixelSize = (int) (geekFrameWidth * geekScale / geekShadowShapeFrames[currentAnimationFrame % 2][0].length);

		x = x - geekShadowShapeFrames[0][0].length * pixelSize / 2;
		y = y - geekShadowShapeFrames[0].length * pixelSize / 2;

		for (int i = 0; i < geekShadowShapeFrames[currentAnimationFrame % 2].length; i++)
			for (int j = 0; j < geekShadowShapeFrames[currentAnimationFrame % 2][0].length; j++)
				if (geekShadowShapeFrames[currentAnimationFrame % 2][i][j] == 1)
					graphics.drawRectBySize(x + j * pixelSize, y + i * pixelSize, pixelSize, pixelSize, shadowColor);
	}


	public int whichGiftIsClicked (DroidconImmutable droidcon, TouchEvent e)
	{
		droidconDrawParams.updateData(droidcon);
		if (droidcon.isGiftActive(Droidcon.GIFT_CHOCO) &&
				(e.x >= droidconDrawParams.chocoXPosition - (int) (Assets.chokoOn.getWidth() * onTargetScale / 2))
				&& (e.x <= droidconDrawParams.chocoXPosition + (int) (Assets.chokoOn.getWidth() * onTargetScale / 2))
				&& (e.y >= droidconDrawParams.chocoYPosition - (int) (Assets.chokoOn.getHeight() * onTargetScale / 2))
				&& (e.y <= droidconDrawParams.chocoYPosition + (int) (Assets.chokoOn.getHeight() * onTargetScale / 2)))
			return Droidcon.GIFT_CHOCO;
		else if (!droidcon.isGiftActive(Droidcon.GIFT_CHOCO) &&
				(e.x >= droidconDrawParams.chocoXPosition - (int) (Assets.chokoOff.getWidth() * offTargetScale / 2))
				&& (e.x <= droidconDrawParams.chocoXPosition + (int) (Assets.chokoOff.getWidth() * offTargetScale / 2))
				&& (e.y >= droidconDrawParams.chocoYPosition - (int) (Assets.chokoOff.getHeight() * offTargetScale / 2))
				&& (e.y <= droidconDrawParams.chocoYPosition + (int) (Assets.chokoOff.getHeight() * offTargetScale / 2)))
			return Droidcon.GIFT_CHOCO;
		else if (droidcon.isGiftActive(Droidcon.GIFT_PEN) &&
				(e.x >= droidconDrawParams.pencilXPosition - (int) (Assets.pencilOn.getWidth() * onTargetScale / 2))
				&& (e.x <= droidconDrawParams.pencilXPosition + (int) (Assets.pencilOn.getWidth() * onTargetScale / 2))
				&& (e.y >= droidconDrawParams.pencilYPosition - (int) (Assets.pencilOn.getHeight() * onTargetScale / 2))
				&& (e.y <= droidconDrawParams.pencilYPosition + (int) (Assets.pencilOn.getHeight() * onTargetScale / 2)))
			return Droidcon.GIFT_PEN;
		else if (!droidcon.isGiftActive(Droidcon.GIFT_PEN) &&
				(e.x >= droidconDrawParams.pencilXPosition - (int) (Assets.pencilOff.getWidth() * offTargetScale / 2))
				&& (e.x <= droidconDrawParams.pencilXPosition + (int) (Assets.pencilOff.getWidth() * offTargetScale / 2))
				&& (e.y >= droidconDrawParams.pencilYPosition - (int) (Assets.pencilOff.getHeight() * offTargetScale / 2))
				&& (e.y <= droidconDrawParams.pencilYPosition + (int) (Assets.pencilOff.getHeight() * offTargetScale / 2)))
			return Droidcon.GIFT_PEN;
		else if (droidcon.isGiftActive(Droidcon.GIFT_NOTE) &&
				(e.x >= droidconDrawParams.notepadXPosition - (int) (Assets.notepadOn.getWidth() * onTargetScale / 2))
				&& (e.x <= droidconDrawParams.notepadXPosition + (int) (Assets.notepadOn.getWidth() * onTargetScale / 2))
				&& (e.y >= droidconDrawParams.notepadYPosition - (int) (Assets.notepadOn.getHeight() * onTargetScale / 2))
				&& (e.y <= droidconDrawParams.notepadYPosition + (int) (Assets.notepadOn.getHeight() * onTargetScale / 2)))
			return Droidcon.GIFT_NOTE;
		else if (!droidcon.isGiftActive(Droidcon.GIFT_NOTE) &&
				(e.x >= droidconDrawParams.notepadXPosition - (int) (Assets.notepadOff.getWidth() * offTargetScale / 2))
				&& (e.x <= droidconDrawParams.notepadXPosition + (int) (Assets.notepadOff.getWidth() * offTargetScale / 2))
				&& (e.y >= droidconDrawParams.notepadYPosition - (int) (Assets.notepadOff.getHeight() * offTargetScale / 2))
				&& (e.y <= droidconDrawParams.notepadYPosition + (int) (Assets.notepadOff.getHeight() * offTargetScale / 2)))
			return Droidcon.GIFT_NOTE;
		return Droidcon.GIFT_UNDEFINED;
	}

	public boolean isRealGeekClicked (DroidconImmutable droidcon, TouchEvent e)
	{
		droidconDrawParams.updateData(droidcon);

		int geekPositionX;
		int geekPositionY;

		for (int i = droidcon.geeksCount() - 1; i >= 0; i--)
		{
			if (droidcon.getGeek(i).getTarget() < 0)
			{
				geekPositionX = droidconDrawParams.mapX + (int) ((droidcon.getGeek(i).getX() * droidconDrawParams.mapWidth) / droidcon.getWidth());
				geekPositionY = droidconDrawParams.mapY + (int) ((droidcon.getGeek(i).getY() * droidconDrawParams.mapHeight) / droidcon.getHeight());
				if ((Math.abs(geekPositionX - e.x) <= geekFrameWidth * droidconDrawParams.geekSize) &&
						(Math.abs(geekPositionY - e.y) <= geekFrameHeight * droidconDrawParams.geekSize))
					return true;
			}
		}

		return false;
	}

	public boolean isGeekClicked (DroidconImmutable droidcon, TouchEvent e)
	{
		droidconDrawParams.updateData(droidcon);

		int geekPositionX;
		int geekPositionY;

		for (int i = droidcon.geeksCount() - 1; i >= 0; i--)
		{
			geekPositionX = droidconDrawParams.mapX + (int) ((droidcon.getGeek(i).getX() * droidconDrawParams.mapWidth) / droidcon.getWidth());
			geekPositionY = droidconDrawParams.mapY + (int) ((droidcon.getGeek(i).getY() * droidconDrawParams.mapHeight) / droidcon.getHeight());
			if ((Math.abs(geekPositionX - e.x) <= geekFrameWidth * droidconDrawParams.geekSize) &&
					(Math.abs(geekPositionY - e.y) <= geekFrameHeight * droidconDrawParams.geekSize))
				return true;
		}

		return false;
	}

	public void drawDroidcon (DroidconImmutable droidcon)
	{
		droidconDrawParams.updateData(droidcon);

		// Update animation frame counter to the next if it is needed
		if (System.nanoTime() / 1000000.0 - lastAnimationTick > animationTick)
		{
			lastAnimationTick = System.nanoTime() / 1000000.0;
			currentAnimationFrame = (currentAnimationFrame + 1) % maxAnimationFramesCounter;
		}

		//Showing the floor
		graphics.drawPixmap(Assets.floor, droidconDrawParams.floorX, droidconDrawParams.floorY, 0, 0, (int) (droidconDrawParams.floorWidth / droidconDrawParams.floorScale), (int) (droidconDrawParams.floorHeight / droidconDrawParams.floorScale), droidconDrawParams.floorScale);

		for (int i = 0; i < droidcon.geeksCount(); i++)
		{
			//Showing geeks
			double geekDirectionSin = Math.sin(droidcon.getGeek(i).getDirection());
			double geekDirectionCos = Math.cos(droidcon.getGeek(i).getDirection());
			int geekPositionX = droidconDrawParams.mapX + (int) ((droidcon.getGeek(i).getX() * droidconDrawParams.mapWidth) / droidcon.getWidth() - geekFrameWidth * droidconDrawParams.geekSize / 2.0);
			int geekPositionY = droidconDrawParams.mapY + (int) ((droidcon.getGeek(i).getY() * droidconDrawParams.mapHeight) / droidcon.getHeight() - geekFrameHeight * droidconDrawParams.geekSize / 2.0);

			// Showing geek's shadow under the geek
			drawGeekShadow(geekPositionX + (int) (geekFrameWidth * droidconDrawParams.geekSize / 2), geekPositionY + (int) (geekFrameHeight * droidconDrawParams.geekSize), droidconDrawParams.geekSize, geekShadowColor);

			// Choosing correct sprite of the geek for his moving direction
			if (geekDirectionCos > 0 && Math.abs(geekDirectionSin) < 0.7072)
				graphics.drawPixmap(Assets.geek,
						geekPositionX, geekPositionY,
						geekFrameWidth * (currentAnimationFrame % 4), 46,
						geekFrameWidth, geekFrameHeight,
						droidconDrawParams.geekSize
				);
			else if (geekDirectionCos < 0 && Math.abs(geekDirectionSin) < 0.7072)
				graphics.drawPixmap(Assets.geek,
						geekPositionX, geekPositionY,
						geekFrameWidth * (currentAnimationFrame % 4), 69,
						geekFrameWidth, geekFrameHeight,
						droidconDrawParams.geekSize
				);
			else if (geekDirectionSin > 0 && Math.abs(geekDirectionCos) < 0.7072)
				graphics.drawPixmap(Assets.geek,
						geekPositionX, geekPositionY,
						geekFrameWidth * (currentAnimationFrame % 4), 0,
						geekFrameWidth, geekFrameHeight,
						droidconDrawParams.geekSize
				);
			else
				graphics.drawPixmap(Assets.geek,
						geekPositionX, geekPositionY,
						geekFrameWidth * (currentAnimationFrame % 4), 23,
						geekFrameWidth, geekFrameHeight,
						droidconDrawParams.geekSize
				);
		}

		// Showing controls of targets/gifts
		if (droidcon.isGiftActive(Droidcon.GIFT_CHOCO))
			graphics.drawPixmap(Assets.chokoOn,
					droidconDrawParams.chocoXPosition - (int) (Assets.chokoOn.getWidth() * onTargetScale / 2),
					droidconDrawParams.chocoYPosition - (int) (Assets.chokoOn.getHeight() * onTargetScale / 2),
					0,
					0,
					Assets.chokoOn.getWidth(),
					Assets.chokoOn.getHeight(),
					onTargetScale);
		else
			graphics.drawPixmap(Assets.chokoOff,
					droidconDrawParams.chocoXPosition - (int) (Assets.chokoOff.getWidth() * offTargetScale / 2),
					droidconDrawParams.chocoYPosition - (int) (Assets.chokoOff.getHeight() * offTargetScale / 2),
					0,
					0,
					Assets.chokoOff.getWidth(),
					Assets.chokoOff.getHeight(),
					offTargetScale);

		if (droidcon.isGiftActive(Droidcon.GIFT_PEN))
			graphics.drawPixmap(Assets.pencilOn,
					droidconDrawParams.pencilXPosition - (int) (Assets.pencilOn.getWidth() * onTargetScale / 2),
					droidconDrawParams.pencilYPosition - (int) (Assets.pencilOn.getHeight() * onTargetScale / 2),
					0,
					0,
					Assets.pencilOn.getWidth(),
					Assets.pencilOn.getHeight(),
					onTargetScale);
		else
			graphics.drawPixmap(Assets.pencilOff,
					droidconDrawParams.pencilXPosition - (int) (Assets.pencilOff.getWidth() * offTargetScale / 2),
					droidconDrawParams.pencilYPosition - (int) (Assets.pencilOff.getHeight() * offTargetScale / 2),
					0,
					0,
					Assets.pencilOff.getWidth(),
					Assets.pencilOff.getHeight(),
					offTargetScale);

		if (droidcon.isGiftActive(Droidcon.GIFT_NOTE))
			graphics.drawPixmap(Assets.notepadOn,
					droidconDrawParams.notepadXPosition - (int) (Assets.notepadOn.getWidth() * onTargetScale / 2),
					droidconDrawParams.notepadYPosition - (int) (Assets.notepadOn.getHeight() * onTargetScale / 2),
					0,
					0,
					Assets.notepadOn.getWidth(),
					Assets.notepadOn.getHeight(),
					onTargetScale);
		else
			graphics.drawPixmap(Assets.notepadOff,
					droidconDrawParams.notepadXPosition - (int) (Assets.notepadOff.getWidth() * offTargetScale / 2),
					droidconDrawParams.notepadYPosition - (int) (Assets.notepadOff.getHeight() * offTargetScale / 2),
					0,
					0,
					Assets.notepadOff.getWidth(),
					Assets.notepadOff.getHeight(),
					offTargetScale);

	}


	public void drawSuperGeekAdvertisment (int superGeek)
	{
		String advertisement = "It is ";
		int pixelSize = screenWidth / ((advertisement.length() + 10) * Alphabet.getLetterWidth()); // 10 - is max length of super geeks name.
		switch (superGeek)
		{
			case Droidcon.PRINCESS_ID:
				advertisement += "princess";
				break;
			case Droidcon.OLDMAN_ID:
				advertisement += "oldman";
				break;
			case Droidcon.ANDROIDER_ID:
				advertisement += "androider";
				break;
			case Droidcon.APPLER_ID:
				advertisement += "appler";
				break;
			case Droidcon.STEEVE_ID:
				advertisement += "Steve Jobs";
				break;
			case Droidcon.GANDALF_ID:
				advertisement += "Gandalf";
				break;
		}

		int backgroundSourceWidth;
		int backgroundSourceHeight;
		double backgroundScale;

		if (((double) screenHeight / screenWidth) >= ((double) Assets.floor.getHeight() / Assets.floor.getWidth()))
		{
			backgroundSourceHeight = Assets.floor.getHeight();
			backgroundSourceWidth = Assets.floor.getHeight() * screenWidth / screenHeight;
			backgroundScale = (double) screenHeight / Assets.floor.getHeight();
		} else
		{
			backgroundSourceHeight = Assets.floor.getWidth() * screenHeight / screenWidth;
			backgroundSourceWidth = Assets.floor.getWidth();
			backgroundScale = (double) screenWidth / Assets.floor.getWidth();
		}

		graphics.drawPixmap(
				Assets.floor,
				0,
				0,
				0,
				0,
				backgroundSourceWidth,
				backgroundSourceHeight,
				backgroundScale);

		//graphics.drawRectBySize(0, 0, screenWidth, screenHeight, 0xFFCCCCCC);

		int x = 0;
		for (int i = 0; i < advertisement.length(); i++)
			if (advertisement.charAt(i) != ' ')
				x += (Alphabet.getLetterWidth() + 1) * pixelSize;
			else
				x += Alphabet.getWordSpace() * pixelSize;

		x = (screenWidth - x) / 2;
		int y = 10;

		drawString(advertisement, x, y, pixelSize, 0xAAAAAAAA);

		double pictureScale = Math.min((double) screenWidth / Assets.gandalf.getWidth(),
				(double) (screenHeight - pixelSize * Alphabet.getLetterHeight() - 3 * y) / Assets.gandalf.getHeight());

		switch (superGeek)
		{
			case Droidcon.PRINCESS_ID:
				graphics.drawPixmap(Assets.princess,
						(screenWidth - (int) (Assets.princess.getWidth() * pictureScale)) / 2,
						2 * y + Alphabet.getLetterHeight() * pixelSize,
						0,
						0,
						Assets.princess.getWidth(),
						Assets.princess.getHeight(),
						pictureScale);
				break;
			case Droidcon.OLDMAN_ID:
				graphics.drawPixmap(Assets.oldman,
						(screenWidth - (int) (Assets.oldman.getWidth() * pictureScale)) / 2,
						2 * y + Alphabet.getLetterHeight() * pixelSize,
						0,
						0,
						Assets.oldman.getWidth(),
						Assets.oldman.getHeight(),
						pictureScale);
				break;
			case Droidcon.ANDROIDER_ID:
				graphics.drawPixmap(Assets.androider,
						(screenWidth - (int) (Assets.androider.getWidth() * pictureScale)) / 2,
						2 * y + Alphabet.getLetterHeight() * pixelSize,
						0,
						0,
						Assets.androider.getWidth(),
						Assets.androider.getHeight(),
						pictureScale);
				break;
			case Droidcon.APPLER_ID:
				graphics.drawPixmap(Assets.appler,
						(screenWidth - (int) (Assets.appler.getWidth() * pictureScale)) / 2,
						2 * y + Alphabet.getLetterHeight() * pixelSize,
						0,
						0,
						Assets.appler.getWidth(),
						Assets.appler.getHeight(),
						pictureScale);
				break;
			case Droidcon.STEEVE_ID:
				graphics.drawPixmap(Assets.steeve,
						(screenWidth - (int) (Assets.steeve.getWidth() * pictureScale)) / 2,
						2 * y + Alphabet.getLetterHeight() * pixelSize,
						0,
						0,
						Assets.steeve.getWidth(),
						Assets.steeve.getHeight(),
						pictureScale);
				break;
			case Droidcon.GANDALF_ID:
				graphics.drawPixmap(Assets.gandalf,
						(screenWidth - (int) (Assets.gandalf.getWidth() * pictureScale)) / 2,
						2 * y + Alphabet.getLetterHeight() * pixelSize,
						0,
						0,
						Assets.gandalf.getWidth(),
						Assets.gandalf.getHeight(),
						pictureScale);
				break;
		}
	}

	public void drawNextGeekAdvertisement (int advertisementId )
	{
		int pixelSize = Math.min(screenWidth, screenHeight);
		for( String advertisement : newLevelAdvertisments )
			pixelSize = Math.min(pixelSize, screenWidth / (advertisement.length() * ( Alphabet.getLetterWidth() + 2) ) );

		int backgroundSourceWidth;
		int backgroundSourceHeight;
		double backgroundScale;

		if (((double) screenHeight / screenWidth) >= ((double) Assets.floor.getHeight() / Assets.floor.getWidth()))
		{
			backgroundSourceHeight = Assets.floor.getHeight();
			backgroundSourceWidth = Assets.floor.getHeight() * screenWidth / screenHeight;
			backgroundScale = (double) screenHeight / Assets.floor.getHeight();
		} else
		{
			backgroundSourceHeight = Assets.floor.getWidth() * screenHeight / screenWidth;
			backgroundSourceWidth = Assets.floor.getWidth();
			backgroundScale = (double) screenWidth / Assets.floor.getWidth();
		}

		graphics.drawPixmap(
				Assets.floor,
				0,
				0,
				0,
				0,
				backgroundSourceWidth,
				backgroundSourceHeight,
				backgroundScale);

		//graphics.drawRectBySize(0, 0, screenWidth, screenHeight, 0xFFCCCCCC);

		int x = 0;
		for (int i = 0; i < newLevelAdvertisments[advertisementId].length(); i++)
			if (newLevelAdvertisments[advertisementId].charAt(i) != ' ')
				x += (Alphabet.getLetterWidth() + 1) * pixelSize;
			else
				x += Alphabet.getWordSpace() * pixelSize;

		x = (screenWidth - x) / 2;
		int y = ( screenHeight - Alphabet.getLetterHeight() ) / 2;

		drawString(newLevelAdvertisments[advertisementId], x, y, pixelSize, 0xAAAAAAAA);

		if ( BuildConfig.FORANN == 1)
		{
			double scale = (screenHeight - Alphabet.getLetterHeight()) / (4 * Assets.rose.getHeight());
			graphics.drawPixmap(Assets.rose,
					(screenWidth - (int) (Assets.rose.getWidth() * scale)) / 2,
					(5 * screenHeight + 3 * Alphabet.getLetterHeight()) / 8,
					0,
					0,
					Assets.rose.getWidth(),
					Assets.rose.getHeight(),
					scale);
		}
	}

	public void drawFailAdvertisement ( )
	{
		String advertisement = "Oh no! nom! nom! nom!";
		int pixelSize = screenWidth / (advertisement.length() * ( Alphabet.getLetterWidth() + 2) );

		int backgroundSourceWidth;
		int backgroundSourceHeight;
		double backgroundScale;

		if (((double) screenHeight / screenWidth) >= ((double) Assets.floor.getHeight() / Assets.floor.getWidth()))
		{
			backgroundSourceHeight = Assets.floor.getHeight();
			backgroundSourceWidth = Assets.floor.getHeight() * screenWidth / screenHeight;
			backgroundScale = (double) screenHeight / Assets.floor.getHeight();
		} else
		{
			backgroundSourceHeight = Assets.floor.getWidth() * screenHeight / screenWidth;
			backgroundSourceWidth = Assets.floor.getWidth();
			backgroundScale = (double) screenWidth / Assets.floor.getWidth();
		}

		graphics.drawPixmap(
				Assets.floor,
				0,
				0,
				0,
				0,
				backgroundSourceWidth,
				backgroundSourceHeight,
				backgroundScale);

		//graphics.drawRectBySize(0, 0, screenWidth, screenHeight, 0xFFCCCCCC);

		int x = 0;
		for (int i = 0; i < advertisement.length(); i++)
			if (advertisement.charAt(i) != ' ')
				x += (Alphabet.getLetterWidth() + 1) * pixelSize;
			else
				x += Alphabet.getWordSpace() * pixelSize;

		x = (screenWidth - x) / 2;
		int y = ( screenHeight - Alphabet.getLetterHeight() ) / 2;

		drawString(advertisement, x, y, pixelSize, 0xAAAAAAAA);
	}

	public void drawStartAdvertisement ( )
	{
		String advertisement[] = {
				"There is a lot of man on event",
				"and only one real geek",
				"usual man runs to stuff",
				"real geek dont need any stuff",
				"touch the stuff and look at man",
				"Find and touch a real geek"};
		int pixelSize = Math.min(screenWidth, screenHeight);
		for( String advertisementLine : advertisement )
			pixelSize = Math.min(pixelSize, screenWidth / (advertisementLine.length() * ( Alphabet.getLetterWidth() + 2) ) );

		pixelSize = Math.min( pixelSize, screenHeight / ( ( advertisement.length * 2 + 1) * Alphabet.getLetterHeight() ));

		int backgroundSourceWidth;
		int backgroundSourceHeight;
		double backgroundScale;

		if (((double) screenHeight / screenWidth) >= ((double) Assets.floor.getHeight() / Assets.floor.getWidth()))
		{
			backgroundSourceHeight = Assets.floor.getHeight();
			backgroundSourceWidth = Assets.floor.getHeight() * screenWidth / screenHeight;
			backgroundScale = (double) screenHeight / Assets.floor.getHeight();
		} else
		{
			backgroundSourceHeight = Assets.floor.getWidth() * screenHeight / screenWidth;
			backgroundSourceWidth = Assets.floor.getWidth();
			backgroundScale = (double) screenWidth / Assets.floor.getWidth();
		}

		graphics.drawPixmap(
				Assets.floor,
				0,
				0,
				0,
				0,
				backgroundSourceWidth,
				backgroundSourceHeight,
				backgroundScale);

		//graphics.drawRectBySize(0, 0, screenWidth, screenHeight, 0xFFCCCCCC);

		for ( int line = 0; line < advertisement.length; line++)
		{
			int x = 0;
			for (int i = 0; i < advertisement[line].length(); i++)
				if (advertisement[line].charAt(i) != ' ')
					x += (Alphabet.getLetterWidth() + 1) * pixelSize;
				else
					x += Alphabet.getWordSpace() * pixelSize;

			x = (screenWidth - x) / 2;
			int y = (1 + line * 2 ) * screenHeight / ( advertisement.length * 2 + 1)  -  Alphabet.getLetterHeight() / 2;

			drawString(advertisement[line], x, y, pixelSize, 0xAAAAAAAA);
		}
	}

	public void drawHallOfFame ()
	{
		/*int backgroundSourceWidth;
		int backgroundSourceHeight;
		double backgroundScale;

		if (((double) screenHeight / screenWidth) >= ((double) Assets.floor.getHeight() / Assets.floor.getWidth()))
		{
			backgroundSourceHeight = Assets.floor.getHeight();
			backgroundSourceWidth = Assets.floor.getHeight() * screenWidth / screenHeight;
			backgroundScale = (double) screenHeight / Assets.floor.getHeight();
		} else
		{
			backgroundSourceHeight = Assets.floor.getWidth() * screenHeight / screenWidth;
			backgroundSourceWidth = Assets.floor.getWidth();
			backgroundScale = (double) screenWidth / Assets.floor.getWidth();
		}

		graphics.drawPixmap(
				Assets.floor,
				0,
				0,
				0,
				0,
				backgroundSourceWidth,
				backgroundSourceHeight,
				backgroundScale); */

		graphics.drawRectBySize(0, 0, screenWidth, screenHeight, 0xFF000049);

		int geekWidth = Assets.princess.getWidth();
		int geekHeight = Assets.princess.getHeight();

		double geekScale = Math.min((double) screenHeight / (5 * geekHeight), (double) screenWidth / (7 * geekWidth));
		int verticalSpace = (int) (geekScale * geekHeight);
		int horizontalSpace = (int) (geekScale * geekWidth);
		int horizontalPadding = (screenWidth - horizontalSpace * 7) / 2;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 2; j++)
				drawGeekShadow(horizontalPadding + horizontalSpace + 2 * i * horizontalSpace + (int) (geekWidth * geekScale / 2.0),
						verticalSpace + 2 * j * verticalSpace + (int) (geekHeight * geekScale),
						geekScale,
						geekDevineLight
				);


		if (Settings.isGotPrincess())
			graphics.drawPixmap(Assets.princess,
					horizontalPadding + horizontalSpace,
					verticalSpace,
					0,
					0,
					geekWidth,
					geekHeight,
					geekScale);

		if (Settings.isGotOldman())
			graphics.drawPixmap(Assets.oldman,
					horizontalPadding + horizontalSpace * 3,
					verticalSpace,
					0,
					0,
					geekWidth,
					geekHeight,
					geekScale);

		if (Settings.isGotSteeve())
			graphics.drawPixmap(Assets.steeve,
					horizontalPadding + horizontalSpace * 5,
					verticalSpace,
					0,
					0,
					geekWidth,
					geekHeight,
					geekScale);

		if (Settings.isGotAppler())
			graphics.drawPixmap(Assets.appler,
					horizontalPadding + horizontalSpace,
					verticalSpace * 3,
					0,
					0,
					geekWidth,
					geekHeight,
					geekScale);

		if (Settings.isGotAndroider())
			graphics.drawPixmap(Assets.androider,
					horizontalPadding + horizontalSpace * 3,
					verticalSpace * 3,
					0,
					0,
					geekWidth,
					geekHeight,
					geekScale);

		if (Settings.isGotGandalf())
			graphics.drawPixmap(Assets.gandalf,
					horizontalPadding + horizontalSpace * 5,
					verticalSpace * 3,
					0,
					0,
					geekWidth,
					geekHeight,
					geekScale);

	}


	public int menuItemClick (TouchEvent e)
	{

		String lines[];

		if ( BuildConfig.FORANN == 1 )
			lines = new String[] {"Intel on Droidcon", "Play", "Hall of Fame", "Credits", "Switch sound"};
		else
			lines = new String[] {"Geeks on Droidcon", "Play", "Hall of Fame", "Credits", "Switch sound"};

		int pixelSize = Math.min(screenWidth, screenHeight);
		for ( String line : lines )
			pixelSize = Math.min( pixelSize, screenWidth / ( line.length() * ( Alphabet.getLetterWidth() + 1 ) ) );

		pixelSize = Math.min( pixelSize, screenHeight / ( lines.length * 2 * Alphabet.getLetterHeight() ) );


		for ( int lineNumber = 0; lineNumber < lines.length; lineNumber ++ )
		{
			if ( ( e.y >= (int) Math.ceil( (double) lineNumber * screenHeight  / lines.length ) + ( ( (int) Math.ceil( (double) screenHeight  / lines.length ) - pixelSize * Alphabet.getLetterHeight() ) / 2 ) )
				&& ( e.y < (int) Math.ceil( (double) (lineNumber + 1) * screenHeight  / lines.length ) + ( ( (int) Math.ceil( (double) screenHeight  / lines.length ) - pixelSize * Alphabet.getLetterHeight() ) / 2 ) ) )
				return lineNumber;

		}
		return -1;
	}

	public void drawMenu( )
	{
		String lines[];

		if ( BuildConfig.FORANN == 1 )
			lines = new String[] {"Intel on Droidcon", "Play", "Hall of Fame", "Credits", "Switch sound"};
		else
			lines = new String[] {"Geeks on Droidcon", "Play", "Hall of Fame", "Credits", "Switch sound"};

		int backgroundSourceWidth;
		int backgroundSourceHeight;
		double backgroundScale;

		if (((double)screenHeight/screenWidth) >= ((double)Assets.floor.getHeight()/Assets.floor.getWidth()))
		{
			backgroundSourceHeight = Assets.floor.getHeight();
			backgroundSourceWidth = Assets.floor.getHeight() * screenWidth / screenHeight;
			backgroundScale = (double) screenHeight / Assets.floor.getHeight();
		}
		else
		{
			backgroundSourceHeight = Assets.floor.getWidth() * screenHeight / screenWidth;
			backgroundSourceWidth = Assets.floor.getWidth();
			backgroundScale = (double) screenWidth / Assets.floor.getWidth();
		}

		graphics.drawPixmap(
				Assets.floor,
				droidconDrawParams.floorX,
				droidconDrawParams.floorY,
				0,
				0,
				backgroundSourceWidth,
				backgroundSourceHeight,
				backgroundScale);


		int pixelSize = Math.min(screenWidth, screenHeight);
		for ( String line : lines )
			pixelSize = Math.min( pixelSize, screenWidth / ( line.length() * ( Alphabet.getLetterWidth() + 1 ) ) );

		pixelSize = Math.min( pixelSize, screenHeight / ( lines.length * 2 * Alphabet.getLetterHeight() ) );

//		graphics.drawRectBySize(0, 0, screenWidth, screenHeight, 0xFF000049);


		for ( int lineNumber = 0; lineNumber < lines.length; lineNumber ++ )
		{
			int x = 0;
			for (int i = 0; i < lines[lineNumber].length(); i++)
				if (lines[lineNumber].charAt(i) != ' ')
					x += (Alphabet.getLetterWidth() + 1) * pixelSize;
				else
					x += Alphabet.getWordSpace() * pixelSize;

			x = (screenWidth - x) / 2;
			int y = (int) Math.ceil( (double) lineNumber * screenHeight  / lines.length ) + ( ( (int) Math.ceil( (double) screenHeight  / lines.length ) - pixelSize * Alphabet.getLetterHeight() ) / 2 );

			if ( lineNumber == 0 )
				drawString(lines[lineNumber], x, y, pixelSize, 0xFFCCCCCC);
			else
				drawString(lines[lineNumber], x, y, pixelSize, 0xFF0000AA);
		}
	}

	public void drawCredits( )
	{
		graphics.drawPixmap( Assets.credits,0,0,screenWidth,screenHeight,0,0,Assets.credits.getWidth(),Assets.credits.getHeight() );
	}

	public int getLevelAdvertisementsCount()
	{
		return newLevelAdvertisments.length;
	}
}
