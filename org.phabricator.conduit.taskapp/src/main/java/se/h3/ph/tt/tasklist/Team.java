package se.h3.ph.tt.tasklist;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Team {
	public static class Member {
		@XmlAttribute
		public String id;

		@XmlAttribute
		public String email;

		@XmlAttribute
		public String name;

		@XmlAttribute
		public String cls;
	}

	@XmlElementWrapper(name = "members")
	@XmlElement(name = "member")
	public List<Member> members;

	public Member getFirstByCls(String cls) {
		for (Member member : members) {
			if (cls.equals(member.cls))
				return member;
		}

		return null;
	}
}
