package com.mikesantiago.lastfm_collage_generator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Chart;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.User;
import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.CoverArtImage;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;

public class Program 
{
	public static String APIKEY = "4de0532fe30150ee7a553e160fbbe0e0";
	public static String SECRET = "0686c5e41f20d2dc80b64958f2df0f0c";
	
	
	//5x5 @ 250x250 per image
	//4x4
	//3x3 @ 300x300 per image
	
	private static BufferedImage GetImageFromGoogle(String query)
	{
		String request = URLEncoder.encode(query);
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
            return image;
        } catch(Exception e){
            e.printStackTrace();
        }
		return null;
	}
	
	public static void main(String[] args) 
	{		
		
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter size (5, 4, 3): ");
		
		int asInt = reader.nextInt();
		
		Chart<Album> albumChart = User.getWeeklyAlbumChart("mrmiketheripper", APIKEY);
		DateFormat format = DateFormat.getDateInstance();
		String from = format.format(albumChart.getFrom());
		String to = format.format(albumChart.getTo());
		//System.out.println(String.format("Charts for %s for the week from %s to %s:\n", "mrmiketheripper", from, to));
		Collection<Album> albums = albumChart.getEntries();
		
		int limiter = 25;
		switch(asInt)
		{
		case 5:
			limiter = 5*5;
			break;
		case 4:
			limiter = 4*4;
			break;
		case 3:
			limiter = 3*3;
			break;
		default:
			limiter = 3*3;
			break;
		}
		
		Album[] albumsAsArray = albums.toArray(new Album[albums.size()]);		
		
		CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();
		UUID mbid;
		
		List<LastfmTopAlbum> pictures = new ArrayList<LastfmTopAlbum>();
		for(int i = 0; i < limiter; i++)
		{
			LastfmTopAlbum l = new LastfmTopAlbum();
			l.setArtistName(albumsAsArray[i].getArtist());
			l.setAlbumName(albumsAsArray[i].getName());
			l.setAlbumCover(GetImageFromGoogle(l.getAlbumName() + " " + l.getArtistName()));
			pictures.add(l);
			
			System.out.println(String.format("%s. %s by %s (%s)", i, albumsAsArray[i].getName(), albumsAsArray[i].getArtist(), 
					albumsAsArray[i].getMbid()));
		}
		switch(asInt)
		{
		case 5:
			try
			{
				BufferedImage collage = Create5x5(pictures, asInt);
				File output = new File("collage.png");
				ImageIO.write(collage, "png", output);
			}
			catch(Exception ex)
			{}
			break;
		case 3:
			try
			{
				BufferedImage collage = Create3x3(pictures, asInt);
				File output = new File("collage.png");
				ImageIO.write(collage, "png", output);
			}
			catch(Exception ex)
			{}
			break;
		}
		
		System.out.println("\nDone!");
	}
	
	private static BufferedImage Create3x3(List<LastfmTopAlbum> pictures, int type)
	{
		BufferedImage img = new BufferedImage(900, 900, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 900, 900);
		int count = 0;
		int ymod = 0;
		g.setFont(new Font("Comic Sans MS", 0, 18));
		g.setColor(Color.white);
		for(LastfmTopAlbum image : pictures)
		{
			if(count > 2)
			{
				count = 0;
				ymod++;
			}
			int x = count * 300;
			int y = ymod * 300;
			
			g.drawImage(image.getAlbumCover().getScaledInstance(300, 300, Image.SCALE_SMOOTH), x, y, null);
			//first black shadows
			g.setColor(Color.black);
			g.drawString(image.getAlbumName(), x + 2, y + 18);
			g.drawString(image.getArtistName(), x + 2, y + 36);
			//Then the white foreground
			g.setColor(Color.white);
			g.drawString(image.getAlbumName(), x, y + 18);
			g.drawString(image.getArtistName(), x, y + 36);
			count++;
		}
		
		return img;
	}
	
	private static BufferedImage Create5x5(List<LastfmTopAlbum> pictures, int type)
	{
		BufferedImage img = new BufferedImage(1250, 1250, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 1250, 1250);
		int count = 0;
		int ymod = 0;
		g.setFont(new Font("Comic Sans MS", 0, 18));
		g.setColor(Color.white);
		for(LastfmTopAlbum image : pictures)
		{
			if(count > 4)
			{
				count = 0;
				ymod++;
			}
			int x = count * 250;
			int y = ymod * 250;
			
			g.drawImage(image.getAlbumCover().getScaledInstance(250, 250, Image.SCALE_SMOOTH), x, y, null);
			//first black shadows
			g.setColor(Color.black);
			g.drawString(image.getAlbumName(), x + 2, y + 18);
			g.drawString(image.getArtistName(), x + 2, y + 36);
			//Then the white foreground
			g.setColor(Color.white);
			g.drawString(image.getAlbumName(), x, y + 18);
			g.drawString(image.getArtistName(), x, y + 36);
			count++;
		}
		
		return img;
	}
	
}
