import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;



/**
 * Internship application project for 32 Bit Bilgisayar software company
 * This is the main class for game. It has a GameBoard object that represents
 * the game board (chessboard) and the pieces on it and necessary methods
 * (initialize, set, reset, move, e.g) to play the game
 *
 * @author Deniz Can Erdem Yılmaz
 *         18.04.2017
 */
public class Game
{
	/** Game board for the game */
	GameBoard            board;
	/** Total move counter */
	int                  totalMove;
	/** Current row of the player */
	int                  curRow;
	/** Current column of the player */
	char                 curCol;
	/** Previous row of the player */
	int                  preRow;
	/** Previous column of the player */
	char                 preCol;
	/** Path from a1 to the h8 */
	ArrayList<String>    path;
	/** Columns of the encountered blocks */
	ArrayList<Character> blockColumns;
	/** Rows of the encountered blocks */
	ArrayList<Integer>   blockRows;
	
	
	/** Constant definition */
	private final char   PLAYER      = 'w';
	/** Constant definition */
	private final char   BLOCK       = 'x';
	/** Constant definition */
	private final char   PASSED_ROAD = '@';
	/** Constant definition */
	private final char   EMPTY_PIECE = '.';
	/** Constant definition */
	private final char   START_COL   = 'a';
	/** Constant definition */
	private final int    START_ROW   = 1;
	/** Constant definition */
	private final char   END_COL     = 'h';
	/** Constant definition */
	private final int    END_ROW     = 8;
	/** Constant definition */
	private final int    ZERO        = 0;
	/** Constant definition */
	private final int    MAX_ROW     = 8;
	/** Constant definition */
	private final int    MIN_ROW     = 1;
	/** Constant definition */
	private final char   MIN_COL     = 'a';
	/** Constant definition */
	private final char   MAX_COL     = 'h';
	/** Constant definition */
	private final int    MAX_BLOCK   = 9;
	/** Constant definition */
	private final int    MIN_BLOCK   = 3;
	/** Constant definition */
	private final String U           = "up";
	/** Constant definition */
	private final String D           = "down";
	/** Constant definition */
	private final String R           = "right";
	/** Constant definition */
	private final String L           = "left";
	
	
	
	/**
	 * No parameter constructor
	 */
	public Game()
	{
		/* Initializing board with required pieces */
		ArrayList<Integer> blockList = BlockGenerator();
		board = new GameBoard(EMPTY_PIECE);
		board.addPlaceable(PLAYER, "Player");
		board.addPlaceable(BLOCK, "Block");
		board.addPlaceable(PASSED_ROAD, "Passed road");
		board.set(START_COL, START_ROW, PLAYER);
		
		/* Setting blocks on board */
		for (int i = 0; i < blockList.size(); ++i)
		{
			int row = blockList.get(i)/MAX_ROW;
			int col = blockList.get(i)%MAX_ROW;
			board.set(row, col, BLOCK);
		}
		
		/* Initializing path and encountered block arrays */
		path = new ArrayList<>();
		blockColumns = new ArrayList<>();
		blockRows = new ArrayList<>();
		
		/* Initializing variables for game start */
		totalMove = ZERO;
		curRow = START_ROW;
		curCol = START_COL;
		preRow = curRow;
		preCol = curCol;
		
		/* Showing initial board to user */
		System.out.println(toString());
	}
	
	
	
	/**
	 * Function to play the game manually
	 */
	public void PlayGame()
	{
		Scanner input;
		
		/* Taking user input and moving piece until the game ends */
		while (!GameEnd() && !PlayerBlocked())
		{
			System.out.print("Enter the way to move (w/a/s/d): ");
			input = new Scanner(System.in);
			String way = input.next();
			if (IsMoveLegit(way))
			{
				Move(way);
				System.out.println(toString());
			}
			else
				System.out.println("Invalid movement!");
		}
		
		if (GameEnd())
		{
			System.out.println("You win!");
			System.out.println("Path: " + path);
		}
		
		else
			System.out.println("You lose!");
	}
	
	
	
	/**
	 * Function to solve the game without user input. Prints map, path to the
	 * end of game and encountered blocks while trying to solve. Definition of
	 * encountered blocks for this method is the pieces that denies the
	 * movement request of player. Since the solver tries to move top and right
	 * most cell it only adds blocks on its way (not the ones player passed
	 * by)
	 *
	 * @return Status of game, true if solved, false if couldn't solved yet
	 */
	public boolean SolveGame()
	{
		/* Control of game ending */
		if (GameEnd())
		{
			/* Printing final result to user */
			System.out.println(toString());
			
			if ((MAX_COL - MIN_COL + MAX_ROW - MIN_ROW) == totalMove)
				System.out.println("System solved with shortest path!");
			else
				System.out.println("System solved!");
			System.out.println("Path: " + path);
			System.out.print("Block encounters: ");
			for (int i = 0; i < blockColumns.size(); i++)
			{
				System.out.print(" " + blockColumns.get(i) + blockRows.get(i) + " ");
			}
			System.out.println();
			return true;
		}
		
		/* Trying to move top post of game board */
		if (IsMoveLegit("w"))
		{
			Move("w");
			
			/* If game solved by this move return true, else move back and try
		 	   other ways */
			if (SolveGame())
				return true;
			else
				MoveBack("s");
		}
		
		/* If a block has encountered or top of the board has reached try to
		   move right most */
		if (IsMoveLegit("d"))
		{
			CheckBlock("d");
			Move("d");
			
			/* If game solved by this move return true, else move back and try
			   other ways */
			if (SolveGame())
				return true;
			else
				MoveBack("a");
		}
		
		/* If player can't move to both up and right then go down to try
		   another way */
		if (IsMoveLegit("s"))
		{
			CheckBlock("s");
			Move("s");
			
			/* If game solved by this move return true, else move back and try
			   other ways */
			if (SolveGame())
				return true;
			else
				MoveBack("w");
		}
		
		/* If a block has encountered or bottom of the board has reached try to
		   move left */
		if (IsMoveLegit("a"))
		{
			CheckBlock("a");
			Move("a");
			
			/* If game solved by this move return true, else move back and try
			   other ways */
			if (SolveGame())
				return true;
			else
				MoveBack("d");
		}
		return false;
	}
	
	
	
	/**
	 * Checks the the way where movement was denied to control if it's caused
	 * by a block or not. If it is a block adds place information of block to
	 * dedicated lists
	 *
	 * @param way Way where player moves after denial
	 */
	private void CheckBlock(String way)
	{
		int  blockRow = curRow;
		char blockCol = curCol;
		
		
		switch (way)
		{
			case "s":
				blockCol = (char) (blockCol + 1);
				break;
			
			case "a":
				--blockRow;
				break;
			
			case "d":
				++blockRow;
				break;
			default:
				return;
		}
		
		
		if (blockCol >= MIN_COL && blockCol <= MAX_COL && blockRow >= MIN_ROW && blockRow <= MAX_ROW && board.get(blockCol, blockRow) == BLOCK)
		{
			if (blockColumns.isEmpty() || (blockColumns.get(blockColumns.size() - 1) != blockCol || blockRows.get(blockRows.size() - 1) != blockRow))
			{
				blockColumns.add(blockCol);
				blockRows.add(blockRow);
			}
		}
	}
	
	
	
	/**
	 * Toggle for game board legend display
	 */
	public void Legend()
	{
		board.Legend();
	}
	
	
	
	/**
	 * toString override from Object class to print current situation of board
	 * to the screen
	 *
	 * @return Single string version of game
	 */
	@Override
	public String toString()
	{
		/* Adding step counter at the end of the toString method of board */
		StringBuilder build = new StringBuilder(board.toString());
		build.append("\nStep counter: " + totalMove + "\n");
		return build.toString();
	}
	
	
	
	/**
	 * Controls if player has a path to move
	 *
	 * @return False if there is an empty way, else false
	 */
	private boolean PlayerBlocked()
	{
		return !(IsMoveLegit("w") || IsMoveLegit("a") || IsMoveLegit("s") || IsMoveLegit("d"));
	}
	
	
	
	/**
	 * Method to move player on board
	 *
	 * @param way Way to move on board
	 */
	private void Move(String way)
	{
		/* Controlling input and moving the desired way. Also adding moved
		   way to the path list */
		switch (way)
		{
			case "w":
				MoveUp();
				path.add("up");
				break;
			
			case "s":
				MoveDown();
				path.add("down");
				break;
			
			case "a":
				MoveLeft();
				path.add("left");
				break;
			
			case "d":
				MoveRight();
				path.add("right");
				break;
		}
		
		/* Setting board pieces according to the current and previous places */
		board.set(preCol, preRow, PASSED_ROAD);
		board.set(curCol, curRow, PLAYER);
		++totalMove;
	}
	
	
	
	/**
	 * Method to move player back if moved way couldn't helped to solve the game
	 * Differently from move method, this method deletes path and encountered
	 * block entries
	 *
	 * @param way Way to move player
	 */
	private void MoveBack(String way)
	{
		/* Moving given way */
		Move(way);
		
		/* Clearing path log to take move back */
		path.remove(path.size() - 1);
		path.remove(path.size() - 1);
		board.set(preCol, preRow, EMPTY_PIECE);
		totalMove -= 2;
	}
	
	
	
	/**
	 * Controls if given way of player is movable
	 *
	 * @param way Way that player wants to go
	 * @return True if path is clear, else false
	 */
	private boolean IsMoveLegit(String way)
	{
		boolean result;
		
		switch (way)
		{
			case "w":
				result = ((curRow != MAX_ROW) && (board.get(curCol, curRow + 1) == EMPTY_PIECE));
				break;
			case "a":
				result = ((curCol != MIN_COL) && (board.get((char) (curCol - 1), curRow) == EMPTY_PIECE));
				break;
			case "s":
				result = ((curRow != MIN_ROW) && (board.get(curCol, curRow - 1) == EMPTY_PIECE));
				break;
			case "d":
				result = ((curCol != MAX_COL) && (board.get((char) (curCol + 1), curRow) == EMPTY_PIECE));
				break;
			default:
				result = false;
				break;
		}
		
		
		return result;
	}
	
	
	
	/**
	 * Checks if player reached the end of game
	 *
	 * @return True if player reached the end, else falses
	 */
	private boolean GameEnd()
	{
		return (board.get(END_COL, END_ROW) == PLAYER);
	}
	
	
	
	/** Move up function */
	private void MoveUp()
	{
		preCol = curCol;
		preRow = curRow++;
	}
	
	
	
	/** Move down function */
	private void MoveDown()
	{
		preCol = curCol;
		preRow = curRow--;
	}
	
	
	
	/** Move left function */
	private void MoveLeft()
	
	{
		preCol = curCol;
		preRow = curRow;
		curCol = (char) (curCol - 1);
	}
	
	
	
	/** Move right */
	private void MoveRight()
	{
		preCol = curCol;
		preRow = curRow;
		curCol = (char) (curCol + 1);
	}
	
	
	
	/**
	 * Generates a list of indexes to place block on game board. It guarantees
	 * a solvable set of blocks in any situation
	 *
	 * @return List of indexes to place block
	 */
	private ArrayList<Integer> BlockGenerator()
	{
		final int[]        upperArray     = {40, 32, 24, 16, 8, 0, 1, 2, 3, 4, 5};
		final int[]        emptyArray     = {56, 57, 48, 6, 7, 15};
		ArrayList<Integer> emptyArrayList = new ArrayList<>();
		Random             generator      = new Random();
		ArrayList<Integer> blocks         = new ArrayList<>();
		int                numOfBlocks    = generator.nextInt(MAX_BLOCK - MIN_BLOCK) + MIN_BLOCK;
		int                upper          = generator.nextInt(upperArray.length);
		
		
		/* Passing index values where cell must be empty to guarantee a
		   finishable maze */
		for (int i = 0; i < emptyArray.length - 1; ++i)
			emptyArrayList.add(emptyArray[i]);
		
		/* Picking first block on route of self solver by hand to provide at
		   least an encounter with a block */
		blocks.add(upperArray[upper]);


		/* Picking other blocks randomly where they don't block player to
		   finish the game */
		for (int i = 0; i < numOfBlocks - 1; ++i)
		{
			int number = generator.nextInt((MAX_COL - MIN_COL + 1)*(MAX_ROW - MIN_ROW + 1));
			if (blocks.contains(number) || emptyArrayList.contains(number))
				--i;
			else
				blocks.add(number);
		}
		
		return blocks;
	}
}
