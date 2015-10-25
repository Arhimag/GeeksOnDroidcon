package com.arhimag.games.IntelDroid.GameElements;

public class Geek implements GeekImmutable
{
	private static final double BASIC_SPEED = 0.004;

	private int target;
	private double x; // x position of geek in pseudo-pixels
	private double y; // y position of geek in pseudo-pixels
	private double speed; // geek's speed in milliseconds
	private double direction; // direction of the geek's movement in radians
	private java.util.Random degreeGenerator;

	public Geek ()
	{
		target = Droidcon.GIFT_UNDEFINED;
		x = 0.0;
		y = 0.0;
		speed = BASIC_SPEED;
		degreeGenerator = new java.util.Random();
		direction = degreeGenerator.nextDouble() * 2 * Math.PI;
	}

	public Geek ( int randomInit )
	{
		target = Droidcon.GIFT_UNDEFINED;
		x = 0.0;
		y = 0.0;
		speed = BASIC_SPEED;
		degreeGenerator = new java.util.Random( randomInit );
		direction = degreeGenerator.nextDouble() * 2 * Math.PI;
	}

	public Geek ( int width, int height )
	{
		this();
		initializeGeek( width, height);
	}

	public Geek ( int width, int height, int randomInit )
	{
		this(randomInit);
		initializeGeek(width, height);
	}

	public void initializeGeek( int width, int height )
	{
		target = Droidcon.GIFT_UNDEFINED;
		// Flipping a coin to define from which wall this geek will start his journey
		int coin = degreeGenerator.nextInt(4);
		switch (coin)
		{
			// Top wall
			case 0:
				x = degreeGenerator.nextDouble() * width;
				y = 0;
				break;
			// Bottom wall
			case 1:
				x = degreeGenerator.nextDouble() * width;
				y = height;
				break;
			// left wall
			case 2:
				x = 0;
				y = degreeGenerator.nextDouble() * height;
				break;
			// right wall
			case 3:
				x = width;
				y = degreeGenerator.nextDouble() * height;
				break;
		}
		speed = BASIC_SPEED;
		direction = degreeGenerator.nextDouble() * 2 * Math.PI;
	}

	public double getX()
	{
		return x;
	}

	public void setX( double x )
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY( double y )
	{
		this.y = y;
	}

	public int getTarget()
	{
		return target;
	}
	public void setTarget( int target ) { this.target = target; }

	public void setSpeed ( double speed )
	{
		this.speed = speed;
	}

	public double getDirection()
	{
		return direction;
	}

	public void setDirection  ( double direction )
	{
		this.direction = direction;
	}
	public void updateDirection() { direction = degreeGenerator.nextDouble() * 2 * Math.PI; }

	public double getSpeed ()
	{
		return speed;
	}
}
