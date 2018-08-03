package View;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import Controller.StateMachine;
import Model.Item;

@SuppressWarnings("serial")
public class CharacterSelection extends JInternalFrame implements ComponentListener {

    private Boolean open = false;
    private StateMachine sm;

    private ArrayList<JButton> buttons;

    private ImageIcon emptyIcon;
    BufferedImage characterImage;

    private String[] characters;

    public CharacterSelection(int width, int height, StateMachine sm, String[] chars) {
        setSize(width, height);
        this.sm = sm;
        this.addComponentListener(this);
        this.characters = chars;

        emptyIcon = new ImageIcon("resources/sprites/empty.png");
        emptyIcon = scaleIcon(emptyIcon, width/8, height / 8);

        try {
            characterImage = ImageIO.read(new File("resources/sprites/warriorSprite.png"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        buttons = new ArrayList<JButton>();

        for (int i = 0; i < characters.length; i++) {
            buttons.add(new JButton("Character " + i));
            buttons.get(i).setIcon(emptyIcon);
            final int buttonID = i;
            buttons.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    buttonMethod(buttonID);
                }
            });
        }
        createAndShowGUI();
    }

    public void buttonMethod(int buttonID) {
        if(buttonID < characters.length) {
            System.out.println("Choosing character: " + characters[buttonID]);
        }
        sm.doRequestFocus();
    }

    public Boolean isOpen() {
        return open;
    }

    public void setOpen(Boolean b) {
        if (b)
            show();
        else
            hide();
        open = b;
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        sm.doRequestFocus();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        sm.doRequestFocus();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    private void createAndShowGUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());

        //Create a component that has a list of the buttons of characters that goes vertically
        //Create a component to hold the display of the character
        //Create a component to hold the two buttons "Play" and "Delete"

        /*
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        for (int y = 0; y < 4; y++) {
            for(int x = 0; x < 3; x++) {
                c.gridx = x;
                c.gridy = y;
                if(x == 2 && y < buttons.size()) {
                    contentPane.add(buttons.get(y), c);
                }
                else if(x == 1 || y != 1) {
                    contentPane.add(new JLabel(""), c);
                }
                else {
                    JLabel charImage = new JLabel(new ImageIcon(characterImage));
                    contentPane.add(charImage, c);
                }
            }
        }
        */
    }

    public ImageIcon scaleIcon(ImageIcon img, int width, int height) {
        Image newimg = img.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
}
