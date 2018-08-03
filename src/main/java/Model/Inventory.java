package Model;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import Controller.StateMachine;

@SuppressWarnings("serial")
public class Inventory extends JInternalFrame implements ComponentListener {

	Boolean open = false;
	StateMachine sm;
	
	int maxSize = 16;
	int numCols, numRows = 4;

	ArrayList<Item> items;
	ArrayList<JButton> buttons;
	
	ImageIcon emptyIcon;

	public Inventory(int width, int height, StateMachine sm) {
		setSize(width, height);
		this.sm = sm;
		this.addComponentListener(this);
		sm.addComponent(this);

		emptyIcon = new ImageIcon("empty.png");
		emptyIcon = scaleIcon(emptyIcon, width / 10 * 2, height / 10 * 2);

		buttons = new ArrayList<JButton>();
		items = new ArrayList<Item>();

		for (int i = 0; i < maxSize; i++) {
			buttons.add(new JButton());
			buttons.get(i).setIcon(emptyIcon);
			final int buttonID = i;
			buttons.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					buttonMethod(buttonID);
				}
			});
		}
		createAndShowGUI();
	}
	
	public void buttonMethod(int buttonID) {
		if(buttonID < items.size()) {
			items.get(buttonID).use(sm.getPlayer());
			if(items.get(buttonID) instanceof Consumable) {
				removeItem(buttonID);
			}
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
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public boolean addItem(Item item) {
		if(items.size() < maxSize) {
			buttons.get(items.size()).setIcon(new ImageIcon(item.getIcon()));
			items.add(item);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void removeItem(int buttonID) {
		buttons.get(buttonID).setIcon(emptyIcon);
		items.remove(buttonID);
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

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;

		for (int y = 0; y < 4; y++) {
			c.gridy = y;
			for (int x = 0; x < 4; x++) {
				c.gridx = x;
				contentPane.add(buttons.get((y) * 4 + (x)), c);
			}
		}
	}

	public ImageIcon scaleIcon(ImageIcon img, int width, int height) {
		Image newimg = img.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}
}
