package com.mikesantiago.lastfm_collage_ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Chart;
import de.umass.lastfm.User;

public class CollageGenerator implements Runnable
{
	private CollageOptions Options;
	private BufferedImage ResultingCollage;
	private boolean Running = false;
	
	public BufferedImage getResultingCollage()
	{
		return ResultingCollage;
	}
	
	public CollageGenerator(CollageOptions options)
	{
		Options = options;
	}
	
	private BufferedImage GetImageFromGoogle(String query)
	{
		String stripped = query.replaceAll("[\\-\\+\\.\\^:,\\]\\[]", "");
		String request = URLEncoder.encode(stripped.toLowerCase());
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
        	System.err.println("---ERROR GETTING FOR QUERY: " + stripped.toLowerCase());
            e.printStackTrace();
        }
		return null;
	}
	
	private BufferedImage CreateCollage()
	{
		Caller.getInstance().setCache(null); //should make sure nothing is persisted to avoid weird things happening
		switch(Options.getSize())
		{
		case x5:
			return Create5x5();
		case x4:
			return Create4x4();
		case x3:
			return Create3x3();
		}
		return null;
	}
	
	private Date getEarlierDateFromCurrent(int days)
	{
		Date dd = new Date();
		dd.setTime(dd.getTime() + (long)days*1000*60*60*24);
		return dd;
	}
	
	//250x250 @ 1250x1250
	private BufferedImage Create5x5()
	{
		Date now = new Date();
		Date sevenDaysAgo = getEarlierDateFromCurrent(-7);
		
		//Get lastfm info
		Chart<Album> albumChart = User.getWeeklyAlbumChart(Options.getUsername(), sevenDaysAgo.toString(), now.toString(), 25, com.mikesantiago.lastfm_collage_generator.Program.APIKEY);
		Collection<Album> albums = albumChart.getEntries();
		Album[] albumsAsArray = albums.toArray(new Album[albums.size()]);
		List<LastfmTopAlbum> pictures = new ArrayList<LastfmTopAlbum>();
		for(int i = 0; i < 25; i++)
		{
			if(i > albumsAsArray.length -1)
				break;
			LastfmTopAlbum l = new LastfmTopAlbum();
			l.setArtistName(albumsAsArray[i].getArtist());
			l.setAlbumName(albumsAsArray[i].getName());
			try
			{
				l.setAlbumCover(GetImageFromGoogle(l.getAlbumName() + " " + l.getArtistName()));
			}
			catch(Exception ex)
			{
				System.out.println("Non urgent: " + ex.getMessage());
			}
			pictures.add(l);
		}
		//////////////////////////////
		//Make actual collage
		BufferedImage collage = new BufferedImage(1250, 1250, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = collage.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 1250, 1250);
		
		int count = 0;
		int ymod = 0;
		g.setFont(Options.getFontForText());
		g.setColor(Color.white);
		for(LastfmTopAlbum album : pictures)
		{
			if(count > 4)
			{
				count = 0;
				ymod++;
			}
			int x = count * 250;
			int y = ymod * 250;
			
			g.drawImage(album.getAlbumCover().getScaledInstance(250, 250, Image.SCALE_SMOOTH), x, y, null);
			if(Options.isTextOnTop())
			{
				g.setColor(Color.black);
				g.drawString(album.getAlbumName(), x + 2, y + 18);
				g.drawString(album.getArtistName(), x + 2, y + 36);
				g.setColor(Color.white);
				g.drawString(album.getAlbumName(), x, y + 18);
				g.drawString(album.getArtistName(), x, y + 36);
			}
			count++;
		}
		return collage;
	}
	
	//228x228 @ 1000x1000
	private BufferedImage Create4x4()
	{Date now = new Date();
	Date sevenDaysAgo = getEarlierDateFromCurrent(-7);
	
	//Get lastfm info
	Chart<Album> albumChart = User.getWeeklyAlbumChart(Options.getUsername(), sevenDaysAgo.toString(), now.toString(), 16, com.mikesantiago.lastfm_collage_generator.Program.APIKEY);
				Collection<Album> albums = albumChart.getEntries();
				Album[] albumsAsArray = albums.toArray(new Album[albums.size()]);
				List<LastfmTopAlbum> pictures = new ArrayList<LastfmTopAlbum>();
				for(int i = 0; i < 16; i++)
				{
					LastfmTopAlbum l = new LastfmTopAlbum();
					l.setArtistName(albumsAsArray[i].getArtist());
					l.setAlbumName(albumsAsArray[i].getName());
					l.setAlbumCover(GetImageFromGoogle(l.getAlbumName() + " " + l.getArtistName()));
					pictures.add(l);
				}
				//////////////////////////////
				//Make actual collage
				BufferedImage collage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = collage.createGraphics();
				g.setColor(Color.black);
				g.fillRect(0, 0, 1000, 1000);
				
				int count = 0;
				int ymod = 0;
				g.setFont(Options.getFontForText());
				g.setColor(Color.white);
				for(LastfmTopAlbum album : pictures)
				{
					if(count > 3)
					{
						count = 0;
						ymod++;
					}
					int x = count * 250;
					int y = ymod * 250;
					
					g.drawImage(album.getAlbumCover().getScaledInstance(250, 250, Image.SCALE_SMOOTH), x, y, null);
					if(Options.isTextOnTop())
					{
						g.setColor(Color.black);
						g.drawString(album.getAlbumName(), x + 2, y + 18 + 2);
						g.drawString(album.getArtistName(), x + 2, y + 36 + 2);
						g.setColor(Color.white);
						g.drawString(album.getAlbumName(), x, y + 18);
						g.drawString(album.getArtistName(), x, y + 36);
					}
					count++;
				}
				return collage;
	}
	
	//300x300 @ 900x900
	private BufferedImage Create3x3()
	{
		Date now = new Date();
		Date sevenDaysAgo = getEarlierDateFromCurrent(-7);
		//Get lastfm info
		Chart<Album> albumChart = User.getWeeklyAlbumChart(Options.getUsername(), sevenDaysAgo.toString(), now.toString(), 9, com.mikesantiago.lastfm_collage_generator.Program.APIKEY);
		Collection<Album> albums = albumChart.getEntries();
		Album[] albumsAsArray = albums.toArray(new Album[albums.size()]);
		List<LastfmTopAlbum> pictures = new ArrayList<LastfmTopAlbum>();
		for(int i = 0; i < 9; i++)
		{
			LastfmTopAlbum l = new LastfmTopAlbum();
			l.setArtistName(albumsAsArray[i].getArtist());
			l.setAlbumName(albumsAsArray[i].getName());
			l.setAlbumCover(GetImageFromGoogle(l.getAlbumName() + " " + l.getArtistName()));
			pictures.add(l);
		}
		//////////////////////////////
		//Make actual collage
		BufferedImage collage = new BufferedImage(900, 900, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = collage.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 900, 900);
		
		int count = 0;
		int ymod = 0;
		g.setFont(Options.getFontForText());
		g.setColor(Color.white);
		for(LastfmTopAlbum album : pictures)
		{
			if(count > 2)
			{
				count = 0;
				ymod++;
			}
			int x = count * 300;
			int y = ymod * 300;
			
			g.drawImage(album.getAlbumCover().getScaledInstance(300, 300, Image.SCALE_SMOOTH), x, y, null);
			if(Options.isTextOnTop())
			{
				g.setColor(Color.black);
				g.drawString(album.getAlbumName(), x + 2, y + 18);
				g.drawString(album.getArtistName(), x + 2, y + 36);
				g.setColor(Color.white);
				g.drawString(album.getAlbumName(), x, y + 18);
				g.drawString(album.getArtistName(), x, y + 36);
			}
			count++;
		}
		return collage;
	}

	@Override
	public void run() 
	{		
		if(Options != null)
		{
			Running = true;
			ResultingCollage = CreateCollage();
		}
		else
			throw new NullPointerException("Collage options are null!");
		Running = false;
	}
	
	public boolean isRunning()
	{
		return Running;
	}

}
