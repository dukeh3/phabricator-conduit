package org.phabricator.conduit.demo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Task {
	public String title;
	public String description;
	public String cls;
	
	public List<Task> subTasks = new ArrayList<>();
}
