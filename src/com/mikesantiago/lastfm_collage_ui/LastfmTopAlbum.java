package com.mikesantiago.lastfm_collage_ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LastfmTopAlbum 
{
	private BufferedImage AlbumCover;
	private String AlbumName;
	private String ArtistName;
	public LastfmTopAlbum()
	{
		BufferedImage tempImg = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics2D fromTempImage = tempImg.createGraphics();
		fromTempImage.setColor(Color.black);
		fromTempImage.fillRect(0, 0, 500, 500);
		AlbumCover = tempImg;
	}
	public LastfmTopAlbum(BufferedImage _albumCover, String _albumName, String _artistName)
	{
		AlbumCover = _albumCover;
		AlbumName = _albumName;
		ArtistName = _artistName;
	}
	public BufferedImage getAlbumCover() {
		return AlbumCover;
	}
	public void setAlbumCover(BufferedImage albumCover) {
		if(albumCover != null)
			AlbumCover = albumCover;
	}
	public String getAlbumName() {
		return AlbumName;
	}
	public void setAlbumName(String albumName) {
		AlbumName = albumName;
	}
	public String getArtistName() {
		return ArtistName;
	}
	public void setArtistName(String artistName) {
		ArtistName = artistName;
	}

}
