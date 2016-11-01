package ch.gibb.iet.modul306.vmlauncher.model;

import ch.gibb.iet.modul306.vmlauncher.controller.AbstractController;

public abstract class AbstractModel<C extends AbstractController> {
	C controller;

	public AbstractModel(C controller) {
		this.controller = controller;
	}
}
