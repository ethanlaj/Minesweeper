import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;

public class Debug extends JFrame {
	JButton b = new JButton();
	Minesweeper game;
	public static Debug lastInstance;

	public Debug(Minesweeper parentGame) {
		game = parentGame;
	
		setTitle("Debugging Window");
		setSize(250, 250);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(1, 1));
		add(b);
		
		if (lastInstance != null)
			lastInstance.dispose();
		lastInstance = this;
	}
	
	public void hover(MineButton mB) {
		if (mB.mine)
			b.setBackground(Color.RED);
		else if (mB.flagged) {
			b.setBackground(Color.YELLOW);
		} else {
			b.setBackground(new Color(92, 146, 247));
		}
		
		b.setText("M: " + mB.mine + "\nF: " + mB.flagged + "\nE: " + mB.exposed);
	}
	
	public void unHover() {
		b.setBackground(null);
		b.setText("");
	}
}
