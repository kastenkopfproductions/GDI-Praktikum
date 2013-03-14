package rps.client.ui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import rps.game.data.FigureKind;
import rps.game.data.Figure;

/**
 * creates the fields of the board and manages the dark and light fieldimages
 * @author Felix Klose
 *
 */
public class GameSquare extends JButton {

	static final long serialVersionUID = 1L;
	
	//Datapath
	private static final String projPath = System.getProperty("user.dir");
	private static final String data = projPath + "/data";
	
	//Imagepaths (visible)
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
	private static final String questionMarkNormalLight = data + "fragezeichen_normal_hellblau.png";
	private static final String questionMarkNormalDark = data + "fragezeichen_normal_dunkelblau.png";
	
	//Imagepaths (not visible)
	private static final String rockNormalLightNV = data + "stein_normal_hellblau_nv.png";
	private static final String rockNormalDarkNV = data + "/stein_normal_dunkelblau_nv.png";
	private static final String paperNormalLightNV = data + "/papier_normal_hellblau_nv.png";
	private static final String paperNormalDarkNV = data + "/papier_normal_dunkelblau_nv.png";
	private static final String scissorsNormalLightNV = data + "/schere_normal_hellblau_nv.png";
	private static final String scissorsNormalDarkNV = data + "/schere_normal_dunkelblau_nv.png";
	
	//Imagepaths (last move)
	private static final String rockNormalLightLM = data + "/stein_normal_hellblau_lm.png";
	private static final String rockNormalDarkLM = data + "/stein_normal_dunkelblau_lm.png";
	private static final String paperNormalLightLM = data + "/papier_normal_hellblau_lm.png";
	private static final String paperNormalDarkLM = data + "/papier_normal_dunkelblau_lm.png";
	private static final String scissorsNormalLightLM = data + "/schere_normal_hellblau_lm.png";
	private static final String scissorsNormalDarkLM = data + "/schere_normal_dunkelblau_lm.png";
	private static final String questionMarkNormalLightLM = data + "fragezeichen_normal_hellblau_lm.png";
	private static final String questionMarkNormalDarkLM = data + "fragezeichen_normal_dunkelblau_lm.png";
	private static final String rockNormalLightNVLM = data + "stein_normal_hellblau_nv_lm.png";
	private static final String rockNormalDarkNVLM= data + "/stein_normal_dunkelblau_nv_lm.png";
	private static final String paperNormalLightNVLM = data + "/papier_normal_hellblau_nv_lm.png";
	private static final String paperNormalDarkNVLM = data + "/papier_normal_dunkelblau_nv_lm.png";
	private static final String scissorsNormalLightNVLM = data + "/schere_normal_hellblau_nv_lm.png";
	private static final String scissorsNormalDarkNVLM = data + "/schere_normal_dunkelblau_nv_lm.png";
	
	//Imagepaths (possible moves)
	private static final String bgLightPM = data + "/hellblau_pm.png";
	private static final String bgDarkPM = data + "/dunkelblau_pm.png";
	private static final String rockNormalLightPM = data + "/stein_normal_hellblau_pm.png";
	private static final String rockNormalDarkPM = data + "/stein_normal_dunkelblau_pm.png";
	private static final String paperNormalLightPM = data + "/papier_normal_hellblau_pm.png";
	private static final String paperNormalDarkPM = data + "/papier_normal_dunkelblau_pm.png";
	private static final String scissorsNormalLightPM = data + "/schere_normal_hellblau_pm.png";
	private static final String scissorsNormalDarkPM = data + "/schere_normal_dunkelblau_pm.png";
	private static final String questionMarkNormalLightPM = data + "fragezeichen_normal_hellblau_pm.png";
	private static final String questionMarkNormalDarkPM = data + "fragezeichen_normal_dunkelblau_pm.png";
	
	//Images (visible)
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
	private static final ImageIcon iQuestionMarkNormalLight = new ImageIcon(questionMarkNormalLight);
	private static final ImageIcon iQuestionMarkNormalDark = new ImageIcon(questionMarkNormalDark);
	
	//Images (not visible)
	private static final ImageIcon iRockNormalLightNV = new ImageIcon(rockNormalLightNV);
	private static final ImageIcon iRockNormalDarkNV = new ImageIcon(rockNormalDarkNV);
	private static final ImageIcon iPaperNormalLightNV = new ImageIcon(paperNormalLightNV);
	private static final ImageIcon iPaperNormalDarkNV = new ImageIcon(paperNormalDarkNV);
	private static final ImageIcon iScissorsNormalLightNV = new ImageIcon(scissorsNormalLightNV);
	private static final ImageIcon iScissorsNormalDarkNV = new ImageIcon(scissorsNormalDarkNV);
	
	//Images (last move)
	private static final ImageIcon iRockNormalLightLM = new ImageIcon(rockNormalLightLM);
	private static final ImageIcon iRockNormalDarkLM = new ImageIcon(rockNormalDarkLM);
	private static final ImageIcon iPaperNormalLightLM = new ImageIcon(paperNormalLightLM);
	private static final ImageIcon iPaperNormalDarkLM = new ImageIcon(paperNormalDarkLM);
	private static final ImageIcon iScissorsNormalLightLM = new ImageIcon(scissorsNormalLightLM);
	private static final ImageIcon iScissorsNormalDarkLM = new ImageIcon(scissorsNormalDarkLM);
	private static final ImageIcon iQuestionMarkNormalLightLM = new ImageIcon(questionMarkNormalLightLM);
	private static final ImageIcon iQuestionMarkNormalDarkLM = new ImageIcon(questionMarkNormalDarkLM);
	private static final ImageIcon iRockNormalLightNVLM = new ImageIcon(rockNormalLightNVLM);
	private static final ImageIcon iRockNormalDarkNVLM = new ImageIcon(rockNormalDarkNVLM);
	private static final ImageIcon iPaperNormalLightNVLM = new ImageIcon(paperNormalLightNVLM);
	private static final ImageIcon iPaperNormalDarkNVLM = new ImageIcon(paperNormalDarkNVLM);
	private static final ImageIcon iScissorsNormalLightNVLM = new ImageIcon(scissorsNormalLightNVLM);
	private static final ImageIcon iScissorsNormalDarkNVLM = new ImageIcon(scissorsNormalDarkNVLM);
	
	//Images (possible move)
	private static final ImageIcon iBgLightPM = new ImageIcon(bgLightPM);
	private static final ImageIcon iBgDarkPM = new ImageIcon(bgDarkPM);
	private static final ImageIcon iRockNormalLightPM = new ImageIcon(rockNormalLightPM);
	private static final ImageIcon iRockNormalDarkPM = new ImageIcon(rockNormalDarkPM);
	private static final ImageIcon iPaperNormalLightPM = new ImageIcon(paperNormalLightPM);
	private static final ImageIcon iPaperNormalDarkPM = new ImageIcon(paperNormalDarkPM);
	private static final ImageIcon iScissorsNormalLightPM = new ImageIcon(scissorsNormalLightPM);
	private static final ImageIcon iScissorsNormalDarkPM = new ImageIcon(scissorsNormalDarkPM);
	private static final ImageIcon iQuestionMarkNormalLightPM = new ImageIcon(questionMarkNormalLightPM);
	private static final ImageIcon iQuestionMarkNormalDarkPM = new ImageIcon(questionMarkNormalDarkPM);
	
	//Controlls the backgroundcolor of the button
	boolean darkField;
	
	//Position and type of the figure represented by this button
	int position;
	Figure type;				

	public GameSquare (int position, Figure type, ActionListener listener) {
		
		super();
		
		setEnabled(true);
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createEmptyBorder());

		//Set backgroundcolor
		darkField = (position % 2 == 0);
		
		//Set Icon and type
		setType(type);
		
		//Set position and Actionlistener
		this.position = position;
		this.addActionListener(listener);
		
	}
	
	/**
	 * @param type setting type of the field
	 */
	public void setType(Figure type) {
		this.type = type;
		setImage();
	}
	
	/**
	 * Set Image depending on backgroundcolor and figure-type
	 */
	private void setImage() {
		if(type.getKind() != null)
			switch(type.getKind()) {
			case ROCK:
				if(darkField)
					this.setIcon(iRockNormalDark);
				else
					this.setIcon(iRockNormalLight);
				break;
			case PAPER:
				if(darkField)
					this.setIcon(iPaperNormalDark);
				else
					this.setIcon(iPaperNormalLight);
				break;
			case SCISSORS:
				if(darkField)
					this.setIcon(iScissorsNormalDark);
				else
					this.setIcon(iScissorsNormalLight);
				break;
			case TRAP:
				if(darkField)
					this.setIcon(iBombDark);
				else
					this.setIcon(iBombLight);
				break;
			case FLAG:
				if(darkField)
					this.setIcon(iFlagDark);
				else
					this.setIcon(iFlagLight);
				break;
			case HIDDEN:
				if(darkField)
					this.setIcon(iQuestionMarkNormalDark);
				else
					this.setIcon(iQuestionMarkNormalLight);
			}
		else {
			if(darkField)
				this.setIcon(iBgDark);
			else
				this.setIcon(iBgLight);
		}
	}
	
	/**
	 * @return the type of the field
	 */
	public Figure getType() {
		return type;
	}
	
	/**
	 * @return the position; of the field
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * @param icon setting the icon of the field
	 */
	public void setIcon(ImageIcon icon) {
		super.setIcon(icon);
		super.setDisabledIcon(icon);
	}
	
}
