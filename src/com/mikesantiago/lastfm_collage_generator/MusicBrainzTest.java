package com.mikesantiago.lastfm_collage_generator;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.json.JSONObject;

public class MusicBrainzTest 
{
	//not that great, only for spaces
	public static String ConvertTextToURL(String text)
	{
	return null;	
	}
	public static void main(String[] args) 
	{
		String request = URLEncoder.encode("Dingir Rings of Saturn");
		try{
            URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + request);
            URLConnection connection = url.openConnection();

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject json = new JSONObject(builder.toString());
            String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("url");

            BufferedImage image = ImageIO.read(new URL(imageUrl));
            JOptionPane.showMessageDialog(null, "", "", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(image));
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Failure", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
	}
}
