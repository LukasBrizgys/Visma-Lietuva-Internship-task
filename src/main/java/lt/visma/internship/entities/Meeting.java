package lt.visma.internship.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;



public class Meeting {
	
	@JsonProperty
	private UUID id;

	@JsonProperty
	@Length(min = 3, max = 30, message = "pavadinimą turi sudaryti nuo 3 iki 30 simbolių")
	private String name;
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String startDate;
	
	@JsonProperty
	private Person ResponsiblePerson;
	
	@JsonProperty
	private Category category;
	
	@JsonProperty
	private Type type;
	
	@JsonProperty
	private String endDate;

	@JsonProperty
	private List<Person> attendees;
	
	@JsonProperty
	private int attendeeCount;
	
	public Meeting() {
		this.id = UUID.randomUUID();
		this.attendees = new ArrayList<Person>();
		this.attendeeCount = 0;
	}
	
	
	public Meeting(String name, String description, String startDate, String endDate) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendees = new ArrayList<Person>();
		this.attendeeCount = 0;
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@JsonIgnore
	public Person getResponsiblePerson() {
		return ResponsiblePerson;
	}

	@JsonIgnore
	public void setResponsiblePerson(Person responsiblePerson) {
		ResponsiblePerson = responsiblePerson;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}
	

	public List<Person> getAttendees() {
		return attendees;
	}


	public void setAttendees(List<Person> attendees) {
		this.attendees = attendees;
	}


	public int getAttendeeCount() {
		return attendeeCount;
	}
	public void setAttendeeCount(int attendeeCount) {
		this.attendeeCount = attendeeCount;
	}


	@Override
	public String toString() {
		return name + " " + description + attendees.toString();
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}


	@Override
	public boolean equals(Object o) {
		if ( o == this) return true;
		if(!(o instanceof Meeting)) {
			return false;
		}
		Meeting meeting = (Meeting) o;
		return Objects.equals(id, meeting.id)
		&& Objects.equals(name, meeting.name);
	}
	
	
	
}
