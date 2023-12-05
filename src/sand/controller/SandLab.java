package sand.controller;

import java.awt.*;
import java.util.*;

import sand.view.SandDisplay;

public class SandLab
{
  //Step 4,6
  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;
  public static final int PORTAL = 4;
  public static final int FLOW = 5;
  
  //do not add any more fields below
  private int[][] grid;
  private SandDisplay display;
  
  
  /**
   * Constructor for SandLab
   * @param numRows The number of rows to start with
   * @param numCols The number of columns to start with;
   */
  public SandLab(int numRows, int numCols)
  {
    String[] names;
    // Change this value to add more buttons
    //Step 4,6
    names = new String[6];
    // Each value needs a name for the button
    names[EMPTY] = "Empty";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[PORTAL] = "Portal";
    names[FLOW] = "Flow";
    
    //1. Add code to initialize the data member grid with same dimensions
    grid = new int[numRows][numCols];
    
    
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    //2. Assign the values associated with the parameters to the grid
	  grid[row][col] = tool;
  }

  //copies each element of grid into the display
  public void updateDisplay()
  {
      //Step 3
   //Hint - use a nested for loop
	  for (int row = 0; row < grid.length; row++)
	  {
		  for (int col = 0; col < grid[0].length; col++)
		  {
			  Color [] colors = {Color.black, Color.gray, new Color(194, 178, 128), Color.blue, Color.pink, Color.lightGray};
			  display.setColor(row, col, colors[grid[row][col]]);
		  }
	  }
    
  }
  
  public void updateSand(int currentX, int currentY)
  {
	  if (currentY + 1 < grid.length && (grid[currentY + 1][currentX] == EMPTY || grid[currentY + 1][currentX] == WATER))
	  {
		  swapParticles(currentY, currentX, currentY + 1, currentX);
	  }
  }
  
  public void updateWater(int currentX, int currentY)
  {
	  int waterDirection = (int)(Math.random() * 3);
	  
	  if (waterDirection == 0)
	  {
		  if (currentY + 1 < grid.length && grid[currentY + 1][currentX] == EMPTY)
		  {
			  swapParticles(currentY, currentX, currentY + 1, currentX);
		  }
	  }
	  else if (waterDirection == 1)
	  {
		  if (currentX - 1 >= 0 && grid[currentY][currentX - 1] == EMPTY)
		  {
			  swapParticles(currentY, currentX, currentY, currentX - 1);
		  }
	  }
	  else
	  {
		  if (currentX + 1 < grid[0].length && grid[currentY][currentX + 1] == EMPTY)
		  {
			  swapParticles(currentY, currentX, currentY, currentX + 1);
		  }
	  }
  }
  
  public void updatePortal(int currentX, int currentY)
  {
	  int[] positions = {-1, 0, 1};
	  
	  //int randomRow = (int)(Math.random() * grid.length);
	  //int randomCol = (int)(Math.random() * grid[0].length);
	  
	  // Funny little generator.
	  int randomRow = currentX ^ currentY % grid.length;
	  int randomCol = randomRow ^ currentX % grid[0].length;
	  
	  while (Math.hypot(currentX - randomCol, currentY - randomRow) <= 6)
	  {
		  randomRow += (randomCol % 3);
		  randomCol += (randomRow % 3);
		  
		  randomRow %= grid.length - 4;
		  randomCol %= grid[0].length - 4;
		  randomRow += 2;
		  randomCol += 2;
	  }
	  
	  for (int y : positions)
	  {
		  for (int x : positions)
		  {
			  int row = y + currentY;
			  int col = x + currentX;
			  
			  if (x == 0 && y == 0)
			  {
				  continue;
			  }
			  
			  // Out of bounds.
			  if (row >= grid.length || row < 0 || col >= grid[0].length || col < 0)
			  {
				  continue;
			  }
			  
			  if (grid[row][col] != EMPTY)
			  {
				  
				  int outRow = randomRow + y;
				  int outCol = randomCol + x;
				  
				  if (outRow < grid.length && outRow >= 0 && outCol < grid[0].length && outCol >= 0)
				  {
					  grid[outRow][outCol] = grid[row][col];
				  }
				  
				  grid[row][col] = EMPTY;
			  }
		  }
	  }
  }
  
  public void updateFlow(int currentX, int currentY)
  { 
	  if (currentY + 1 < grid.length && grid[currentY + 1][currentX] == EMPTY)
	  {
		  grid[currentY + 1][currentX] = FLOW;
	  }
	  else if (currentY + 1 >= grid.length || (grid[currentY + 1][currentX] != EMPTY && grid[currentY + 1][currentX] != FLOW))
	  {
		  if (currentX + 1 < grid[0].length && grid[currentY][currentX + 1] == EMPTY)
		  {
			  grid[currentY][currentX + 1] = FLOW;
		  }
		  if (currentX - 1 >= 0 && grid[currentY][currentX - 1] == EMPTY)
		  {
			  grid[currentY][currentX - 1] = FLOW;
		  }
	  }
  }

  //Step 5,7
  //called repeatedly.
  //causes one random particle in grid to maybe do something.
  public void step()
  {
    //Remember, you need to access both row and column to specify a spot in the array
    //The scalar refers to how big the value could be
    //int someRandom = (int) (Math.random() * scalar)
    //remember that you need to watch for the edges of the array
    
	  int randomRow = (int)(Math.random() * grid.length);
	  int randomCol = (int)(Math.random() * grid[0].length);
	  
	  if (grid[randomRow][randomCol] == SAND)
	  {
		  updateSand(randomCol, randomRow);
	  }
	  else if (grid[randomRow][randomCol] == WATER)
	  {
		  updateWater(randomCol, randomRow);
	  }
	  else if (grid[randomRow][randomCol] == PORTAL)
	  {
		  updatePortal(randomCol, randomRow);
	  }
	  else if (grid[randomRow][randomCol] == FLOW)
	  {
		  updateFlow(randomCol, randomRow);
	  }
  }
  
  private void swapParticles(int rowOne, int colOne, int rowTwo, int colTwo)
  {
	  int tool = grid[rowOne][colOne];
	  grid[rowOne][colOne] = grid[rowTwo][colTwo];
	  grid[rowTwo][colTwo] = tool;
  }
  
  //do not modify this method!
  public void run()
  {
    while (true) // infinite loop
    {
      for (int i = 0; i < display.getSpeed(); i++)
      {
        step();
      }
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
      {
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
      }
    }
  }
}
