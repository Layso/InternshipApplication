import java.util.ArrayList;



/**
 * Internship application project for 32 Bit Bilgisayar software company
 * This is a class for game board (simply a chess board) for the game. Class
 * includes some basic functions for board, like setter and getters, to ease
 * operations while playing the game
 *
 * @author Deniz Can Erdem Yılmaz
 *         17.04.2017.
 */
public class GameBoard
{
	/** 2D array for game board representation */
	private char[][]             gameboard;
	/** Storage for pieces can be placed on board */
	private ArrayList<Character> placeables;
	/** Definitions of pieces */
	private ArrayList<String>    definitions;
	/** Toggle for legend at the end of game map */
	private boolean              legend;
	
	/** Constant definition */
	private final int  BOARD_SIZE    = 8;
	/** Constant definition */
	private final int  DEFAULT_INDEX = 0;
	/** Constant definition */
	private final char DEFAULT_PIECE = ' ';
	
	
	
	/**
	 * No parameter constructor
	 * Initializes a 8x8 (chess) board using 2D char array and fills all cells
	 * with default empty representation
	 */
	public GameBoard()
	{
		this(' ');
	}
	
	
	
	/**
	 * Constructor with char parameter
	 * Initializes a 8x8 (chess) board using 2D char array and takes default
	 * character representation of empty board cell to fill all cells with
	 */
	public GameBoard(char def)
	{
		gameboard = new char[BOARD_SIZE][BOARD_SIZE];
		placeables = new ArrayList<>();
		definitions = new ArrayList<>();
		legend = true;
		
		
		placeables.add(def);
		definitions.add("Default piece (empty)");
		
		for (int i = 0; i < BOARD_SIZE; ++i)
			for (int j = 0; j < BOARD_SIZE; ++j)
				gameboard[i][j] = placeables.get(DEFAULT_INDEX);
		
	}
	
	
	
	/**
	 * Inserts new placeable piece to the placeables array
	 *
	 * @param piece Piece to insert
	 */
	public void addPlaceable(char piece)
	{
		/* Inserting piece as undefined */
		addPlaceable(piece, "Undefined");
	}
	
	
	
	/**
	 * Inserts new placeable piece to the placeables array along with its
	 * definition
	 *
	 * @param piece      Piece to insert
	 * @param definition Definition of piece
	 */
	public void addPlaceable(char piece, String definition)
	{
		if (!placeables.contains(piece))
		{
			placeables.add(piece);
			definitions.add(definition);
		}
	}
	
	
	
	/**
	 * Setter for game board, changes element using index numbers
	 *
	 * @param i     Row index
	 * @param j     Column index
	 * @param input Data to change as
	 * @return True if valid operation, else false
	 */
	public boolean set(int i, int j, char input)
	{
		if (placeables.contains(input) && i < BOARD_SIZE && j < BOARD_SIZE)
		{
			gameboard[i][j] = input;
			return true;
		}
		
		else
			return false;
	}
	
	
	
	/**
	 * Setter for game board, changes element using board location
	 *
	 * @param row   Row index
	 * @param col   Column index
	 * @param input Data to change as
	 * @return True if valid operation, else false
	 */
	public boolean set(char col, int row, char input)
	{
		return set(BOARD_SIZE - row, col - 'a', input);
	}
	
	
	
	/**
	 * Getter for game board, reaches element using board location
	 *
	 * @param row Row number
	 * @param col Column letter
	 * @return A character that represents the piece on cell
	 */
	public char get(char col, int row)
	{
		return get(BOARD_SIZE - row, (col - 'a'));
	}
	
	
	
	/**
	 * Getter for gameboard, reaches element using index numbers
	 *
	 * @param i Row index
	 * @param j Column index
	 * @return A character that represents the piece on cell
	 * @throws IndexOutOfBoundsException If operation is invalid
	 */
	public char get(int i, int j) throws IndexOutOfBoundsException
	{
		if (i < BOARD_SIZE && j < BOARD_SIZE)
			return gameboard[i][j];
		
		else
			throw new IndexOutOfBoundsException();
	}
	
	
	
	/**
	 * Toggle for legend display on board map returned from toString method
	 */
	public void Legend()
	{
		legend = !legend;
	}
	
	
	
	/**
	 * toString function override from class Object
	 * Converts the board to a simple string to print with help of StringBuilder
	 *
	 * @return String map of board
	 */
	@Override
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		
		
		build.append("    ");
		for (int i = 0; i < BOARD_SIZE; ++i)
			build.append(" " + (char) ('a' + i) + " ");
		
		build.append("\n   -");
		
		for (int i = 0; i < BOARD_SIZE; ++i)
			build.append("---");
		
		build.append("- \n");
		
		
		for (int i = 0; i < BOARD_SIZE; ++i)
		{
			build.append((BOARD_SIZE - i) + " | ");
			for (int j = 0; j < BOARD_SIZE; ++j)
				build.append(" " + gameboard[i][j] + " ");
			
			build.append(" |");
			build.append("\n");
		}
		
		build.append("   --");
		for (int i = 0; i < BOARD_SIZE; ++i)
			build.append("---");
		
		
		if (legend)
		{
			build.append("- \n\n  LEGEND\n");
			for (int i = 0; i < placeables.size(); ++i)
			{
				build.append("(" + placeables.get(i) + ")");
				build.append(" -- ");
				build.append(definitions.get(i) + "\n");
			}
		}
		
		
		return build.toString();
	}
}
