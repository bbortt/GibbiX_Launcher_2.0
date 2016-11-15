package ch.gibb.iet.modul306.vmlauncher.view;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Window extends Application{
	
	private static final String name="";
	private WebEngine engine;
	
	@Override
	public void start(Stage mainStage) throws Exception {
	
		mainStage.setTitle(name);
		mainStage.setScene(new Scene(createWebView(), 800, 600));
		mainStage.show();
		
	}

	public WebView createWebView(){
				
		WebView browser = new WebView();
		engine = browser.getEngine();
		loadPage("index");
		return browser;
	}
	
	private void loadPage(String page){
		
		String path = "file:///" + System.getProperty("user.dir") + "/src/main/resources/pages/" + page + ".html";
		engine.load(path);
	}
}
