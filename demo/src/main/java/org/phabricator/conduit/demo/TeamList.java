package org.phabricator.conduit.demo;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TeamList {
	public Map<String, String> members = new HashMap<String, String>();	
}
