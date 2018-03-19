/*
 * Copyright (c) 2000-2018 Metro Group of company.
 * All rights reserved.
 */
package com.metro.assignment.game.handler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.*;
import com.metro.assignment.game.constant.GameConstants;


/**
 * Handles Tic Tac Toe and finds winner
 */
public class GameHandler extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private final int size;
	private final String player1;
	private final String player2;
	private JButton[][] b;
	private int turn = 0;

	/**
	 * Constructor initializes player object and Size of the board
	 */
	public GameHandler()
	{
		Properties properties = null;
		try
		{
			properties = loadDatahubProperties();
		}
		catch (IOException e)
		{
			System.out.println("Error loading propery file ");
		}
		final String size = properties.getProperty(GameConstants.BOARD_SIZE);
		final String player1 = properties.getProperty(GameConstants.PLAYER_1);
		final String player2 = properties.getProperty(GameConstants.PLAYER_2);

		this.size = Integer.parseInt(size);
		this.player1 = player1;
		this.player2 = player2;
	}

	/**
	 * Method is the entry point of games
	 * Builds Board
	 */
	public void execute()
	{
		setTitle("Tic-Tac-Toe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);
		setLocation(200, 200);
		setVisible(true);

		b = new JButton[size][size];

		setLayout(new GridLayout(size, size));

		buildBoard();
		invalidate();
		validate();
	}

	/**
	 * Builds Board
	 */
	private void buildBoard()
	{
		for (int row = 0; row < size; row++)
		{
			for (int col = 0; col < size; col++)
			{
				System.out.println("Adding button for position: " + row + ", " + col);

				b[row][col] = new JButton();
				b[row][col].setText("__");
				b[row][col].addActionListener(this);
				b[row][col].setActionCommand(row + "," + col);
				add(b[row][col]);
			}
		}
	}



	/**
	 * Handles on the boar Button pressed event
	 * @param event
	 * 			Event includes position of the board used by player
	 */
	@Override
	public void actionPerformed(ActionEvent event)
	{
		String player;
		JButton press = (JButton) event.getSource();
		if (press.getText().equals(GameConstants.PLAYER_BOT))
		{
			player = GameConstants.PLAYER_BOT;
		}
		else
		{
			turn++;

			if (turn % 2 == 0)
			{
				player = player1;
			}
			else
			{
				player = player2;
			}

		}
		compareWinner(player, press);
		playComputer(player);
	}

	/**
	 * Login to generate random number for player Computer
	 * @param player
	 * 			Player object to identify if it is computer's turn
	 */
	private void playComputer(final String player)
	{
		// Afer last player computer will play
		if (player.equals("O"))
		{
			// Play computer
			int values[][] = new int[size][size];

			outer:
			for (int i = 0; i < size; i++)
			{
				// do the for in the row according to the column size
				inner:
				for (int j = 0; j < values[i].length; j++)
				{
					// multiple the random by 10 and then cast to in
					values[i][j] = ((int) (Math.random() * 10));
					final JButton jButton = b[i][j];
					if (jButton.isEnabled())
					{
						jButton.setText(GameConstants.PLAYER_BOT);
						jButton.doClick();
						break outer;
					}
				}
			}

		}
	}

	/**
	 * Compares Winner
	 * @param press
	 * 			JButton object which is pressed by player
	 * @param player
	 * 			Player identification
	 */
	private void compareWinner(final String player, final JButton press)
	{
		press.setText(player);
		press.setEnabled(false);
		// End game if winning conditon is true or no more turns.
		if (checkWin(press))
		{
			JOptionPane.showMessageDialog(null, "Congratulations!\n" + player + " has won!");

			System.exit(0);
		}
		else if (turn >= (size * size))
		{
			JOptionPane.showMessageDialog(null, "Draw!\n No winners. ");

			System.exit(0);
		}
	}

	/**
	 * Check Winner
	 * @param jButton
	 * 			JButton Object on the board
	 * @return
	 */
	private boolean checkWin(JButton jButton)
	{
		String position[] = jButton.getActionCommand().split(",");

		int row = Integer.parseInt(position[0]);
		int col = Integer.parseInt(position[1]);

		System.out.println(b[row][col].getText() + " played @ " + row + ", " + col);

		String winner = b[row][col].getText() + b[row][col].getText() + b[row][col].getText();
		String field;

		// row
		field = "";

		for (int testCol = Math.max(0, col - 2); testCol < Math.min(size, col + 3); testCol++)
		{
			field += b[row][testCol].getText();
		}

		System.out.println("Testing row field: " + field);

		if (field.contains(winner))
		{
			System.out.println("Row winner!");

			return true;
		}

		// col
		field = "";

		for (int testRow = Math.max(0, row - 2); testRow < Math.min(size, row + 3); testRow++)
		{
			field += b[testRow][col].getText();
		}

		System.out.println("Testing column field: " + field);

		if (field.contains(winner))
		{
			System.out.println("Column winner!");

			return true;
		}

		// diagonals
		int lowerBound = 0;
		int upperBound = 0;

		// diagonal down
		field = "";

		// top left
		lowerBound = -Math.min(2, Math.min(col, row));

		// bottom right
		upperBound = Math.min(3, size - Math.max(row, col));

		System.out.println("Bounds: " + lowerBound + ", " + upperBound);

		for (int offset = lowerBound; offset < upperBound; offset++)
		{
			field += b[row + offset][col + offset].getText();
		}

		System.out.println("Testing diagonal down field: " + field);

		if (field.contains(winner))
		{
			System.out.println("Diagonal down winner!");

			return true;
		}

		// diagonal up
		field = "";


		if (field.contains(winner))
		{
			System.out.println("Diagonal up winner!");

			return true;
		}

		return false;
	}

	/**
	 * Loads Property file
	 * @return
	 * 	Property Object
	 * @throws IOException
	 */
	private Properties loadDatahubProperties() throws IOException
	{
		File configDir = new File(System.getProperty(GameConstants.PROJECT_BASE_PATH), GameConstants.PROJECT_CONF_DIR);
		File configFile = new File(configDir, GameConstants.PROJECT_PROPERTY_FILE_NAME);
		InputStream inputStream = new FileInputStream(configFile);
		Properties props = new Properties();
		props.load(inputStream);
		return props;
	}
}
