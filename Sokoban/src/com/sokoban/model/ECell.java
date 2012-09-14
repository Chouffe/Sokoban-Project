package com.sokoban.model;

/**
 * Represents the Sokoban's cells
 */
public enum ECell
{
	WALL, 
	PLAYER, 
	PLAYER_ON_GOAL_SQUARE, 
	BOX, 
	BOX_ON_GOAL, 
	GOAL_SQUARE, 
	EMPTY_FLOOR, 
	VISITED;
}