package main;

import java.awt.Dimension;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import listeners.ButtonListener;
import listeners.KeyboardListener;

/**
 * Main graphics class for Trash Smash, generates window, starts render thread, creates main menu
 * @author Ben Pinhorn
 *
 */
public class GraphicsMain {
	private static JFrame window = new JFrame("Trash Smash");
	public final static int WIDTH = 1024;
	public final static int HEIGHT = 768;
	private Graphics2D g;
	private Thread renderThread;
	public Render render;
	private ButtonListener l;
	private KeyboardListener kl;
	ImageIcon sbutton = new ImageIcon("Assets/Menu and UI/stbutton.png");
	ImageIcon qbutton = new ImageIcon("Assets/Menu and UI/qbutton.png");
	
	//graphics objects should not be stored here, for drawing game stuff, go to Render.java
	
	public GraphicsMain(KeyboardListener k) {
		l = new ButtonListener();
		kl = k;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		window.setFocusable(true);
		window.addKeyListener(kl);
	}
	
	public void init() {
		
	}
	/**
	 * Starts render thread 
	 */
	public synchronized void start() {
		renderThread = new Thread(render, "Render Thread");
		renderThread.start();
	}
	
	/**
	 * Generates the main menu
	 */
	public void createContentPane() {
		
		GridBagLayout grid = new GridBagLayout();
		JPanel contentPane = new JPanel(grid);

		GridBagConstraints c = new GridBagConstraints();
		
		JButton startButton = new JButton();
		JButton quitButton = new JButton();
		
		startButton.setIcon(sbutton);
		quitButton.setIcon(qbutton);
		startButton.setBorder(null);
		quitButton.setBorder(null);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 2;
		c.gridy = 1;
		
		startButton.addActionListener(l);
		startButton.setActionCommand("start");
		contentPane.add(startButton, c);
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 2;
		c.gridy = 3;
		
		quitButton.addActionListener(l);
		quitButton.setActionCommand("quit");
		contentPane.add(quitButton, c);
		
		//more buttons go here
		
		window.setContentPane(contentPane);
	}
	
	/**
	 * Changes the contentPane of the window to the game pane, repacks the window
	 */
	public void gameStart() {
		window.remove(window.getContentPane());
		JPanel gamePanel = new JPanel();
		window.setContentPane(gamePanel);
		window.pack();
		render = new Render((Graphics2D) window.getGraphics());
	}
}
