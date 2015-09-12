package com.mikesantiago.lastfm_collage_ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CollagePreview extends JFrame {

	private JPanel contentPane;
	private JLabel imageLabel;
	private BufferedImage CollageToPreview;
	

	public void setPreviewImage(BufferedImage image)
	{
		CollageToPreview = image;
		
		if(CollageToPreview != null)
		{
			Color transparentBlack = new Color(1, 1, 1, .35f);
			Color transparentWhite = new Color(0, 0, 0, .35f);
			
			BufferedImage PreviewImage = new BufferedImage(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = PreviewImage.createGraphics();
			g.setFont(new Font("Arial", 0, 24));
			g.setColor(transparentBlack);
			g.drawImage(CollageToPreview.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), BufferedImage.SCALE_SMOOTH), 0, 0, null);
			g.drawString("Preview, not actual size", 6, 24);
			g.setColor(transparentWhite);
			g.drawString("Preview, not actual size", 4, 22);
			
			
			imageLabel.setIcon(new ImageIcon(PreviewImage));
		}
	}
	
	public CollagePreview() {
		setResizable(false);
		setType(Type.POPUP);
		setTitle("Collage Preview");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 519, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		imageLabel = new JLabel("");
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				JFileChooser jfc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
				jfc.setFileFilter(filter);
				if(jfc.showSaveDialog(null) == 0)
				{
					try
					{
						if(!jfc.getSelectedFile().getAbsolutePath().contains(".png"))
							jfc.setSelectedFile(new File(jfc.getSelectedFile().getAbsoluteFile() + ".png"));
						ImageIO.write(CollageToPreview, "png", jfc.getSelectedFile());
						JOptionPane.showMessageDialog(null, "Saved to '" + jfc.getSelectedFile().getAbsolutePath() + "' successfully!");
						Close();
					}
					catch(IOException e)
					{
						JOptionPane.showMessageDialog(null, "Error saving: \n" + e.getStackTrace());
						e.printStackTrace();
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) 
			{
				setCursor(Cursor.HAND_CURSOR);
			}
			@Override
			public void mouseExited(MouseEvent e) 
			{
				setCursor(Cursor.DEFAULT_CURSOR);
			}
		});
		contentPane.add(imageLabel, BorderLayout.CENTER);
	}
	
	private void Close()
	{
		this.dispose();
	}

}
