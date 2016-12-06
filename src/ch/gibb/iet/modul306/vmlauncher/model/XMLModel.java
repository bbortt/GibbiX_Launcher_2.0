package ch.gibb.iet.modul306.vmlauncher.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.gibb.iet.modul306.vmlauncher.controller.AbstractController;

public class XMLModel<C extends AbstractController> extends AbstractModel<C> {
	protected JAXBContext context;
	protected Marshaller xmlWriter;
	protected Unmarshaller xmlReader;

	public XMLModel(C controller) {
		super(controller);
	}

	public void initialize(Class<?> clazz) throws JAXBException {
		context = JAXBContext.newInstance(clazz);
		xmlWriter = context.createMarshaller();
		xmlReader = context.createUnmarshaller();
	}
}