package se.h3.ph.tt.tasklist;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Task {
	public static class BlockedBy {
		@XmlAttribute
		public String id;
	}

	@XmlAttribute
	public String title;

	@XmlAttribute
	public String cls;

	@XmlAttribute
	public String id;

	public String description;

	public String dod;

	// @XmlElementWrapper( name="subtasks" )
	@XmlElement(name = "task")
	public List<Task> subTasks = new ArrayList<Task>();

	@XmlElement(name = "blockedby")
	public List<BlockedBy> blockList;
}
