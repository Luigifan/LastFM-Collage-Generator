package com.mikesantiago.lastfm_collage_ui;

import javax.swing.UIManager;


public class BoilerPlate 
{
	public static void SetLookAndFeel()
	{
		String systemLookAndFeel = null;
		OsCheck.OSType osType = OsCheck.getOperatingSystemType();
        switch(osType)
        {
            case Windows:
                try
                {
                    systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
                }
                catch(Exception ex)
                {
                    systemLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                }
                break;
            case MacOS:
                try
                {
                    systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
                }
                catch(Exception ex)
                {
                    systemLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                }
                break;
            case Linux:
                try
                {
                    systemLookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
                }
                catch(Exception ex)
                {
                    systemLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                }
                break;
            case Other:
                systemLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                break;
        }
        setLookAndFeel(systemLookAndFeel);
	}
	private static void setLookAndFeel(String lookAndFeelClass) 
    {
        try
        {
            UIManager.setLookAndFeel(lookAndFeelClass);
        }
        catch(Exception ex)
        {
            System.out.printf("Unable to set look and feel to '%s'\nException output: %s", lookAndFeelClass, ex.getMessage());
        }
    }
}
