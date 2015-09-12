package com.mikesantiago.lastfm_collage_ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import say.swing.JFontChooser;


public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField usernameTextField;
	private JComboBox typeCombo;
	private JCheckBox chckbxNewCheckBox;
	private CollageOptions Options = new CollageOptions();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		BoilerPlate.SetLookAndFeel();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		this.setResizable(false);
		setTitle("Last.FM Collage Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 475, 110);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblUsername = new JLabel("Username:");
		contentPane.add(lblUsername);
		
		usernameTextField = new JTextField();
		contentPane.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		chckbxNewCheckBox = new JCheckBox("Include Album Info Overlayed");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(chckbxNewCheckBox.isSelected())
				{
					JFontChooser fontChooser = new JFontChooser();
					if(fontChooser.showDialog(null) == JFontChooser.OK_OPTION)
					{
						Options.setFontForText(fontChooser.getSelectedFont());
					}
				}
			}
		});
		contentPane.add(chckbxNewCheckBox);
		
		typeCombo = new JComboBox();
		typeCombo.setModel(new DefaultComboBoxModel(CollageSize.values()));
		contentPane.add(typeCombo);
		
		JButton btnGenerate = new JButton("Generate!");
		btnGenerate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(Options.getFontForText().getSize() < 18)
				{
					if(JOptionPane.showConfirmDialog(null, "Warning! Font size is less than 18. Text may not look good. Proceed?", "Font Size Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
					{
						return;
					}
				}
				DoStuff();
			}
		});
		contentPane.add(btnGenerate);
	}
	private void DoStuff()
	{
		Options.setSize((CollageSize)typeCombo.getSelectedItem());
		Options.setUsername(usernameTextField.getText().trim());
		Options.setTextOnTop(chckbxNewCheckBox.isSelected());
		
		CollageGenerator g = new CollageGenerator(Options);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		enableComponents(this.getContentPane(), false);
		g.run();
		while(g.isRunning());
		if(g.getResultingCollage() != null)
		{
			CollagePreview p = new CollagePreview();
			p.setAlwaysOnTop(true);
			p.setVisible(true);
			p.setPreviewImage(g.getResultingCollage());
		}
		enableComponents(this.getContentPane(), true);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	public void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

}
