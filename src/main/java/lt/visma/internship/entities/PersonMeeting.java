package lt.visma.internship.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonMeeting {
	
	@JsonProperty
	private Person person;
	
	@JsonProperty
	private Meeting meeting;

	
	public PersonMeeting() {}

	public PersonMeeting(Person person, Meeting meeting) {
		this.person = person;
		this.meeting = meeting;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	
	
}
