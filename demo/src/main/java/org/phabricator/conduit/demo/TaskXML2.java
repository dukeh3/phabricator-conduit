package org.phabricator.conduit.demo;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TaskXML2 {

	public static void main(String[] args) throws Exception {
		try {
			JAXBContext carContext = JAXBContext.newInstance(Task.class);
			Unmarshaller carMarshaller = carContext.createUnmarshaller();
			Task unmarshal = (Task) carMarshaller.unmarshal(new FileReader(new File("/home/rene/git/ph/phabricator-conduit/demo/src/main/java/org/phabricator/conduit/demo/test.xml")));
			

			rec(unmarshal, null);
			
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

	}
	
	static void rec(Task t, Task p) {
		System.out.println(t.title);
		
		for(Task  st:  t.subTasks) {
			rec(st,t);
		}
	}

}
