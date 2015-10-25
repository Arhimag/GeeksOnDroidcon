package com.arhimag.games.IntelDroid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

import com.arhimag.games.IntelDroid.GameElements.Droidcon;
import com.arhimag.games.IntelDroid.framework.FileIO;

public class Settings
{

	private static boolean soundEnabled = true;
	private static final int programmVersion = 1;
	private static int currentVersion = programmVersion;
	private static boolean gotPrincess = false;
	private static boolean gotOldman = false;
	private static boolean gotSteeve = false;
	private static boolean gotAndroider = false;
	private static boolean gotAppler = false;
	private static boolean gotGandalf = false;
	private static final int CODE_SOUND = 0;
	private static final int CODE_PROGRAMMVERSION = 3;
	private static final int CODE_PRINCESS = 4;
	private static final int CODE_OLDMAN = 5;
	private static final int CODE_STEEVE = 6;
	private static final int CODE_ANDROIDER = 7;
	private static final int CODE_APPLER = 8;
	private static final int CODE_GANDALF = 9;



	public static void load(FileIO  files)
	{
		BufferedReader in = null;
		try
		{
			in = new BufferedReader( new InputStreamReader( files.readFile(".inteldroid")));
			setToDefault();
			while(in.ready() )
				if( ! readBlock(in) ) break;
		}
		catch (IOException e)
		{
		    setToDefault();
		}
		finally
		{
			try
			{
				if( in != null )
					in.close();
			}
			catch( IOException e)
			{
			    setToDefault();
			}
		}
	}

	public static void save( FileIO files)
	{
		BufferedWriter out = null;
		try
		{
			out = new BufferedWriter( new OutputStreamWriter(files.writeFile(".inteldroid")));
			
			out.write(Integer.toString(CODE_PROGRAMMVERSION));
			out.newLine();
			out.write(Integer.toString(programmVersion));
			out.newLine();
			out.write(Integer.toString(CODE_SOUND));
			out.newLine();
			out.write(Boolean.toString(soundEnabled));
			out.newLine();
			out.write(Integer.toString(CODE_PRINCESS));
			out.newLine();
			out.write(Boolean.toString(gotPrincess));
			out.newLine();
			out.write(Integer.toString(CODE_OLDMAN));
			out.newLine();
			out.write(Boolean.toString(gotOldman));
			out.newLine();
			out.write(Integer.toString(CODE_ANDROIDER));
			out.newLine();
			out.write(Boolean.toString(gotAndroider));
			out.newLine();
			out.write(Integer.toString(CODE_APPLER));
			out.newLine();
			out.write(Boolean.toString(gotAppler));
			out.newLine();
			out.write(Integer.toString(CODE_GANDALF));
			out.newLine();
			out.write(Boolean.toString(gotGandalf));
			out.newLine();
			out.write(Integer.toString(CODE_STEEVE));
			out.newLine();
			out.write(Boolean.toString(gotSteeve));
			out.newLine();
		}
		catch(IOException e)
		{
		}
		finally
		{
			try
			{
				if( out != null)
					out.close();
			}
			catch(IOException e)
			{
			}
		}
	}

	public static boolean isGotPrincess()
	{
		return gotPrincess;
	}

	public static void setGotPrincess(boolean gotPrincess)
	{
		Settings.gotPrincess = gotPrincess;
	}

	public static boolean isGotOldman()
	{
		return gotOldman;
	}

	public static void setGotOldman(boolean gotOldman)
	{
		Settings.gotOldman = gotOldman;
	}

	public static boolean isGotAndroider()
	{
		return gotAndroider;
	}

	public static void setGotAndroider(boolean gotAndroider)
	{
		Settings.gotAndroider = gotAndroider;
	}

	public static boolean isGotAppler()
	{
		return gotAppler;
	}

	public static void setGotAppler(boolean gotAppler)
	{
		Settings.gotAppler = gotAppler;
	}

	public static boolean isGotSteeve()
	{
		return gotSteeve;
	}

	public static void setGotSteeve(boolean gotSteeve)
	{
		Settings.gotSteeve = gotSteeve;
	}

	public static boolean isGotGandalf()
	{
		return gotSteeve;
	}

	public static void setGotGandalf(boolean gotGandalf)
	{
		Settings.gotGandalf = gotGandalf;
	}

	public static boolean isGotSuperGeek( int superGeek )
	{
		switch (superGeek)
		{
			case Droidcon.PRINCESS_ID:
				return gotPrincess;
			case Droidcon.OLDMAN_ID:
				return gotOldman;
			case Droidcon.ANDROIDER_ID:
				return gotAndroider;
			case Droidcon.APPLER_ID:
				return gotAppler;
			case Droidcon.STEEVE_ID:
				return gotSteeve;
			case Droidcon.GANDALF_ID:
				return gotGandalf;
		}
		return false;
	}

	public static boolean isSoundEnabled()
	{
		return soundEnabled;
	}

	public static void setSoundEnabled( boolean newSoundEnabled )
	{
		soundEnabled = newSoundEnabled;
	}
	

	private static void setToDefault()
	{
		soundEnabled = true;
		currentVersion = programmVersion;
		gotPrincess = false;
		gotOldman = false;
		gotSteeve = false;
		gotAndroider = false;
		gotAppler = false;
		gotGandalf = false;
	}
	
	private static boolean readBlock(BufferedReader in)
	{
		try 
		{
			int code = Integer.parseInt(in.readLine());
			switch(code)
			{
				case CODE_SOUND:
					soundEnabled = Boolean.parseBoolean(in.readLine());
					return true;
				case CODE_PROGRAMMVERSION:
					currentVersion = Integer.parseInt( in.readLine() );
					if( currentVersion != programmVersion )
					{
						setToDefault();
						return false;
					}
					else
						return true;
				case CODE_PRINCESS:
					gotPrincess = Boolean.parseBoolean(in.readLine());
					return true;
				case CODE_OLDMAN:
					gotOldman = Boolean.parseBoolean(in.readLine());
					return true;
				case CODE_ANDROIDER:
					gotAndroider = Boolean.parseBoolean(in.readLine());
					return true;
				case CODE_APPLER:
					gotAppler = Boolean.parseBoolean(in.readLine());
					return true;
				case CODE_GANDALF:
					gotGandalf = Boolean.parseBoolean(in.readLine());
					return true;
				case CODE_STEEVE:
					gotSteeve = Boolean.parseBoolean(in.readLine());
					return true;
			}
		}
		catch (IOException e)
		{
			return false;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return false;
	}
}
