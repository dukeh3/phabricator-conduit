package org.phabricator.conduit.demo;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class TaskXML {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Task t = new Task();
		t.cls = "CLS";
		t.description = "sfsdfs";
		t.title = "test";

		for (int i = 0; i < 3; i++) {

			Task st = new Task();
			st.cls = "xxx"+i;
			st.description = "XVXVXXVXvx" +i;
			st.title = "ssss" +i;

			t.subTasks.add(st);
		}

		try {
			JAXBContext carContext = JAXBContext.newInstance(Task.class);
			Marshaller carMarshaller = carContext.createMarshaller();
			StringWriter sw = new StringWriter();
			carMarshaller.marshal(t, sw);

			System.out.println(sw.toString());
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

	}

}
