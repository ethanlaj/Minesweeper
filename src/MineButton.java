import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class MineButton extends JButton {
	Minesweeper game;
	boolean flagged = false,
			mine = false,
			exposed = false;
	int row, column;
		
	public MineButton(Minesweeper gameInst, int buttonRow, int buttonColumn) {
		game = gameInst;
		row = buttonRow;
		column = buttonColumn;
		
		addMouseListener(new MineListener());
	}
		
	public class MineListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (!game.active || exposed)
				return;
			
			int button = e.getButton();
			
			if (!flagged && button == 1) { // Left Click
				game.expose(row, column);
				game.checkForWin();
			}
			else if (button == 3) // Right Click
				if (flagged) {
					flagged = false;
					setBackground(null);
					setText("");
					game.changeMineCount(true);
				} else { 					
					flagged = true;
					setBackground(Color.YELLOW);
					setText("|>");
					game.changeMineCount(false);
				}
			
			game.debug.hover((MineButton) e.getSource());
		}
		
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {
			game.debug.unHover();
		}
		public void mouseEntered(MouseEvent e) {
			game.debug.hover((MineButton) e.getSource());
		}
	}
}
