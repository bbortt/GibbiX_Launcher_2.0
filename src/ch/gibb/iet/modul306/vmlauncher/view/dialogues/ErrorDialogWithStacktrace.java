package ch.gibb.iet.modul306.vmlauncher.view.dialogues;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ErrorDialogWithStacktrace extends Alert {
	public ErrorDialogWithStacktrace() {
		super(AlertType.ERROR);
	}

	public ErrorDialogWithStacktrace appendStracktrace(Exception e) {
		setTitle(e.getClass().toString());
		setHeaderText("We are sorry: An error occured!");
		setContentText("Stacktrace:");

		// Create expandable Exception.
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		String exceptionText = stringWriter.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane stacktrace = new GridPane();
		stacktrace.setMaxWidth(Double.MAX_VALUE);
		stacktrace.add(label, 0, 0);
		stacktrace.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		getDialogPane().setExpandableContent(stacktrace);

		return this;
	}
}