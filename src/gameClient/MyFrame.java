package gameClient;

import javax.swing.*;

public class MyFrame extends JFrame {
	MyPanel panel;

	MyFrame() {
		super("Ex2 - Pokemon Game");
		initFrame();
		initPanel();
	}

	private void initFrame(){
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		ImageIcon iconApplication = new ImageIcon("./resources/iconApplication.png");
		setIconImage(iconApplication.getImage());
	}

	private void initPanel() {
		panel = new MyPanel();
		add(panel);
	}

	public void update(Arena ar) {
		panel.update(ar);
	}

}
