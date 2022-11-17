import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class Minesweeper extends JFrame {
	MineButton [][] display;
	JFrame boardPanel, controlPanel;
	private int minesRemaining = 0;
	JLabel minesRemainingLabel = new JLabel("Mines Remaining: "); 
	JTextField minesRemainingField = new JTextField("" + minesRemaining);
			
	Debug debug = new Debug(this);

	boolean active = true;
		
	public Minesweeper () {
		int size = askBoardSize();
		double percentMine = askPercentMine();
		
		display = new MineButton[size][size];

		JPanel boardPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		
		setTitle("Minesweeper");
		setSize(size*50, size*50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		Random randomClass = new Random();
		
		boardPanel.setLayout(new GridLayout(size, size));
				
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				display[i][j] = new MineButton(this, i, j);
				boardPanel.add(display[i][j]);
			}
		
		
		// Randomly select mines
		int minesNeeded = (int) (size * size * percentMine);
		do {
			MineButton r = display[randomClass.nextInt(size)][randomClass.nextInt(size)];
			
			if (!r.mine) {
				changeMineCount(true);
				r.mine = true;
				minesNeeded--;
			}
		} while (minesNeeded > 0);
		//
		
		controlPanel.add(minesRemainingLabel);
		controlPanel.add(minesRemainingField);
		
		add(boardPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.NORTH);
		
		debug.setVisible(true);
		setVisible(true);
	}
	
	public void changeMineCount (boolean increase) {
		if (increase) minesRemaining++; 
		else minesRemaining--;

		minesRemainingField.setText("" + minesRemaining);
	}
	
	public void expose(int r, int c) {
		if (r < 0 || c < 0 || r >= display.length || c >= display.length)
			return;
		
		MineButton current = display[r][c];
		if (current.exposed)
			return;
		
		if (current.mine) {
			active = false;
			
			for (int i = 0; i < display.length; i++)
				for (int j = 0; j < display.length; j++) {
					if (display[i][j].mine) {
						display[i][j].setText("X");
						display[i][j].setBackground(Color.RED);
					}
				}
			
			JOptionPane.showMessageDialog(null, "Game over!");
			dispose();
			new Minesweeper();
			
			return;
		}
		
		int m = 0;		
		
		// check above button
		if (r-1 > -1) {
			if (c-1 > -1 && display[r-1][c-1].mine) m++;
			if (display[r-1][c].mine) m++;
			if (c+1 < display.length && display[r-1][c+1].mine) m++;
		}
		
		// check to the left & right of button
		if (c-1 > -1 && display[r][c-1].mine) m++;
		if (c+1 < display.length && display[r][c+1].mine) m++;

		// check below button
		if (r+1 < display.length) {
			if (c-1 > -1 && display[r+1][c-1].mine) m++;
			if (display[r+1][c].mine) m++;
			if (c+1 < display.length && display[r+1][c+1].mine) m++;
		}
		
		current.setBackground(new Color(92, 146, 247));
		current.exposed = true;
		
		if (current.flagged) {
			current.flagged = false;
			current.setText("");
			changeMineCount(true);
		}
		
		if (m > 0)
			current.setText("" + m);
		else {
			// recursive
			expose(r-1, c-1);
			expose(r-1, c);
			expose(r-1, c+1);
			
			expose(r, c-1);
			expose(r, c+1);

			expose(r+1, c-1);
			expose(r+1, c);
			expose(r+1, c+1);
			//
		}
		checkForWin();

		return;
	}
	
	public void checkForWin() {
		if (!active)
			return;
		
		boolean win = true;
		
		for (int i = 0; i < display.length; i++)
			for (int j = 0; j < display.length; j++) {
				MineButton b = display[i][j];
				
				if (!b.exposed && !b.mine)
					win = false;
			}
		if (win == true) {
			active = false;
			
			JOptionPane.showMessageDialog(null, "You win!");
			dispose();
			new Minesweeper();		
		}
	}
	
	public int askBoardSize() {
		int n = 10;
		
		String response = JOptionPane.showInputDialog("Input an integer for the board size.\nMin: 3\nMax: 30");
		
		if (response != null && !response.isBlank())
		try {
				int inputtedSize = Integer.parseInt(response);
				if (inputtedSize < 3 || inputtedSize > 30)
					throw new Exception();
				
				n = inputtedSize;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You inputted an invalid integer, so the board size will be the default of 10x10.");
		}
		
		return n;
	}
	
	public double askPercentMine() {
		int n = 15;
		
		String response = JOptionPane.showInputDialog("Input an integer for tne percentage of mines on the board.\nMin: 1\nMax: 100");
		
		if (response != null && !response.isBlank())
		try {
			int inputtedSize = Integer.parseInt(response);
			if (inputtedSize < 1 || inputtedSize > 100)
				throw new Exception();
			
			n = inputtedSize;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You inputted an invalid integer, so the percentage of mines on the board will be the default of 15%.");
		}
		
		return (double) n / 100;
	}
	
	public static void main(String[] args) {
		// Make graphics look normal on an Apple Computer
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		
		new Minesweeper();
	}
}