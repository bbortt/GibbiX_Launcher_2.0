package ch.gibb.iet.modul306.vmlauncher.view;

import javax.swing.JFrame;

public abstract class AbstractView {
	
	private static JFrame frame;
	private static String title = "VM Launcher";
	
	public AbstractView(){
		
		initFrame();
	}
	private static void initFrame(){
		
		frame = new JFrame(title);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}
