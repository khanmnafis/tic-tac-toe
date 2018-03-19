/*
 * Copyright (c) 2000-2018 Metro Group of company.
 * All rights reserved.
 */
package com.metro.assignment.game.main;

import com.metro.assignment.game.handler.GameHandler;


/**
 * Tic Tac Toe Game main class
 */
public class GameHome
{
	public static void main(String args[])
	{
		final GameHandler gameHandler = new GameHandler();
		gameHandler.execute();
	}
}
