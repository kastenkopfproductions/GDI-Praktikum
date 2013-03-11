package rps.client.ui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameSquare extends JButton {

	static final long serialVersionUID = 1L;
	
	//Datapath
	private static final String projPath = System.getProperty("user.dir");
	private static final String data = projPath + "/data";
	
	//Imagepaths
	private static final String bgLight = data + "/hellblau.png";
	private static final String bgDark = data + "/dunkelblau.png";
	private static final String rockNormalLight = data + "/stein_normal_hellblau.png";
	private static final String rockNormalDark = data + "/stein_normal_dunkelblau.png";
	private static final String paperNormalLight = data + "/papier_normal_hellblau.png";
	private static final String paperNormalDark = data + "/papier_normal_dunkelblau.png";
	private static final String scissorsNormalLight = data + "/schere_normal_hellblau.png";
	private static final String scissorsNormalDark = data + "/schere_normal_dunkelblau.png";
	private static final String bombLight = data + "/bombe_hellblau.png";
	private static final String bombDark = data + "/bombe_dunkelblau.png";
	private static final String flagLight = data + "/fahne_hellblau.png";
	private static final String flagDark = data + "/fahne_dunkelblau.png";
	
	//Images
	private static final ImageIcon iBgLight = new ImageIcon(bgLight);
	private static final ImageIcon iBgDark = new ImageIcon(bgDark);
	private static final ImageIcon iRockNormalLight = new ImageIcon(rockNormalLight);
	private static final ImageIcon iRockNormalDark = new ImageIcon(rockNormalDark);
	private static final ImageIcon iPaperNormalLight = new ImageIcon(paperNormalLight);
	private static final ImageIcon iPaperNormalDark = new ImageIcon(paperNormalDark);
	private static final ImageIcon iScissorsNormalLight = new ImageIcon(scissorsNormalLight);
	private static final ImageIcon iScissorsNormalDark = new ImageIcon(scissorsNormalDark);
	private static final ImageIcon iBombLight = new ImageIcon(bombLight);
	private static final ImageIcon iBombDark = new ImageIcon(bombDark);
	private static final ImageIcon iFlagLight = new ImageIcon(flagLight);
	private static final ImageIcon iFlagDark = new ImageIcon(flagDark);
	
	//Controlls the backgroundcolor of the button
	boolean darkField;
	
	//Position and type of the figure represented by this button
	int row;
	int column;
	int type;				//0 is empty, 1 is rock, 2 is paper, 3 is scissors
							//4 is trap and 5 is flag

	public GameSquare (int row, int column, int type, ActionListener listener) {
		
		super();
		
		setEnabled(true);
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createEmptyBorder());

		//Set backgroundcolor
		darkField = ((row + 1) % 2 == (column + 1) % 2);
		
		//Set Icon and type
		setType(type);
		
		//Set position and Actionlistener
		this.row = row;
		this.column = column;
		this.addActionListener(listener);
		
	}
	
	public void setType(int type) {
		this.type = type;
		setImage();
	}
	
	private void setImage() {
		//Set Image depending on backgroundcolor and figure-type
		switch(type) {
		case 0:
			if(darkField)
				this.setIcon(iBgDark);
			else
				this.setIcon(iBgLight);
			break;
		case 1:
			if(darkField)
				this.setIcon(iRockNormalDark);
			else
				this.setIcon(iRockNormalLight);
			break;
		case 2:
			if(darkField)
				this.setIcon(iPaperNormalDark);
			else
				this.setIcon(iPaperNormalLight);
			break;
		case 3:
			if(darkField)
				this.setIcon(iScissorsNormalDark);
			else
				this.setIcon(iScissorsNormalLight);
			break;
		case 4:
			if(darkField)
				this.setIcon(iBombDark);
			else
				this.setIcon(iBombLight);
			break;
		case 5:
			if(darkField)
				this.setIcon(iFlagDark);
			else
				this.setIcon(iFlagLight);
			break;
		default:
			if(darkField)
				this.setIcon(iBgDark);
			else
				this.setIcon(iBgLight);
			break;
		}
		
	}
	
	public int getType() {
		return type;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setIcon(ImageIcon icon) {
		super.setIcon(icon);
		super.setDisabledIcon(icon);
	}
	
}
