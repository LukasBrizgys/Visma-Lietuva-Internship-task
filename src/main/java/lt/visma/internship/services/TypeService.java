package lt.visma.internship.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lt.visma.internship.entities.Type;

@Service
public class TypeService {
	final ObjectMapper mapper = new ObjectMapper();
	final File typePath = new File("./src/main/resources/json/types.json");
	
	public List<Type> getAllTypes() throws StreamReadException, DatabindException, IOException {
		List<Type> types = mapper.readValue(typePath, new TypeReference<List<Type>>(){});
		return types;
	}
	public Type getTypeById(Integer id) throws StreamReadException, DatabindException, IOException  {
		List<Type> types = mapper.readValue(typePath, new TypeReference<List<Type>>(){});
		for(Type type : types) {
			if(type.getId().equals(id)) {
				return type;
			}
		}
		return null;
	}
}
