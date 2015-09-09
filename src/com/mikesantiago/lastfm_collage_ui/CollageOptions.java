package com.mikesantiago.lastfm_collage_ui;

import java.awt.Font;

public class CollageOptions 
{
	private String Username;
	private CollageSize Size;
	private boolean TextOnTop;
	private Font FontForText = new Font("Arial", Font.PLAIN, 18);
	
	public CollageOptions(){}
	
	public CollageOptions(String _username, CollageSize _size, boolean _text)
	{
		Username = _username;
		Size = _size;
		TextOnTop = _text;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public CollageSize getSize() {
		return Size;
	}

	public void setSize(CollageSize size) {
		Size = size;
	}

	public boolean isTextOnTop() {
		return TextOnTop;
	}

	public void setTextOnTop(boolean textOnTop) {
		TextOnTop = textOnTop;
	}
	
	public void setFontForText(Font font)
	{
		FontForText = font;
	}
	
	public Font getFontForText(){return FontForText;}

}
