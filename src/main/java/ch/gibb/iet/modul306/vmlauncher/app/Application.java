package ch.gibb.iet.modul306.vmlauncher.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import ch.gibb.iet.modul306.vmlauncher.config.BeanConfig;
import ch.gibb.iet.modul306.vmlauncher.controller.StartController;

@ComponentScan
public class Application {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new AnnotationConfigApplicationContext(BeanConfig.class);
		StartController main = context.getBean(StartController.class);
		main.startApplication(args);
	}
}
