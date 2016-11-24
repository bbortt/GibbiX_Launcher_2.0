package ch.gibb.iet.modul306.vmlauncher.view;

import ch.gibb.iet.modul306.vmlauncher.controller.LauncherController;
import ch.gibb.iet.modul306.vmlauncher.model.objects.XMLMachine;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MachineView extends AbstractView<LauncherController> {

	public MachineView(Stage mainStage, LauncherController controller) {
		super(mainStage, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void loadScene() {
		this.webView = new WebView();

		mainStage.setTitle(DISPLAY_NAME);
		mainStage.setScene(new Scene(super.loadPage("machine_view.html")));

		mainStage.show();
	}

	@Override
	protected void bindButtonListener() throws Exception {
		// TODO Auto-generated method stub

	}

	public void addMachinesToView(XMLMachine[] machines) {

	}

	private String createHtmlElement(XMLMachine machine) {
		StringBuilder builder = new StringBuilder();
		// <div class="col s12 m4">
		builder.append("<div class=\"col s12 m4\">");
		// <div class="icon-block">
		builder.append("<div class=\"icon-block\">");
		// <a id="[ID]" href="[DETAIL_VIEW]" class="black-text">
		builder.append("<a id=\"" + machine.path + "\" href=\"" + machine.path + "\" class=\"black-text\">");
		// <h2 class="center light-blue-text">
		builder.append("<h2 class=\"center light-blue-text\">");
		// <img alt="Launch machine" src="images/ic_build_black_24dp_2x.png" />
		builder.append("<img alt=\"Launch machine\" src=\"images/ic_launch_black_24dp_2x.png\" />");
		// </h2>
		builder.append("</h2>");
		// <h5 class="center">[NAME]</h5>
		builder.append("<h5 class=\"center\">" + machine.name + "</h5>");
		// </a>
		builder.append("</a>");
		// TODO: Might add the path or some information here!
		// TODO: To get information refactor getData() from zimmermannj

		// <p class="light">Manage your virtual machines. Not only the
		// default ones! You can easily import custom machines, start,
		// configure or repare them.</p>
		// </div>
		builder.append("</div>");
		// </div>
		builder.append("</div>");

		return builder.toString();
	}

	private void addListenerToMachine(XMLMachine machine) {
		// TODO: Add Listener --> Item-ID ist machine.path!
	}
}