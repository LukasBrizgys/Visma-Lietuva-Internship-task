package lt.visma.internship.services;

import java.io.File;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lt.visma.internship.entities.Person;

@Service
public class PersonService implements UserDetailsService{
	@Autowired
	MeetingService meetingService;
	
	final ObjectMapper mapper = new ObjectMapper();
	final File userPath = new File("./src/main/resources/json/users.json");
	public PersonService() throws StreamReadException, DatabindException, IOException {
		try {
			mapper.readValue(userPath, new TypeReference<List<Person>>(){});
		}catch(MismatchedInputException e) { //Jeigu serverio sukūrimo metu JSON failas VISIŠKAI tuščias, sukuria tuščią masyvą jame 
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			writer.writeValue(userPath, new String[0]);
		}
	}
	public List<Person> getPersons() throws StreamReadException, DatabindException, IOException {
		List<Person> persons = mapper.readValue(userPath, new TypeReference<List<Person>>(){});
		return persons;
	}
	public Person getPersonById(UUID id) throws StreamReadException, DatabindException, IOException {
		List<Person> persons = mapper.readValue(userPath, new TypeReference<List<Person>>(){});
		for(Person person : persons) {
			if(person.getId().compareTo(id) == 0) {
				return person;
			}
		}
		return null;
	}
	public Person addNewPerson(Person person) throws StreamReadException, DatabindException, IOException {
		List<Person> persons = mapper.readValue(userPath, new TypeReference<List<Person>>(){});
		person.setPassword((new BCryptPasswordEncoder()).encode(person.getPassword()));
		persons.add(person);
		mapper.writerWithDefaultPrettyPrinter().writeValue(userPath, persons);
		return person;
	}
	public void deletePersonById(UUID id) throws StreamReadException, DatabindException, IOException {
		List<Person> persons = mapper.readValue(userPath, new TypeReference<List<Person>>(){});
		for(Person person : persons) {
			if(person.getId().compareTo(id) == 0) {
				persons.remove(persons.indexOf(person));
				mapper.writerWithDefaultPrettyPrinter().writeValue(userPath, persons);
			}
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			List<Person> persons = mapper.readValue(userPath, new TypeReference<List<Person>>(){});
			for(Person person : persons) {
				if(person.getEmail().equalsIgnoreCase(email)) {
					return person;
				}
			}
			throw new UsernameNotFoundException("Vartotojas nerastas");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
