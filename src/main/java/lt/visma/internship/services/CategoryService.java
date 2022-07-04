package lt.visma.internship.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lt.visma.internship.entities.Category;

@Service
public class CategoryService {
	final ObjectMapper mapper = new ObjectMapper();
	final File categoryPath = new File("./src/main/resources/json/categories.json");
	
	public List<Category> getAllCategories() throws StreamReadException, DatabindException, IOException {
		List<Category> categories = mapper.readValue(categoryPath, new TypeReference<List<Category>>(){});
		return categories;
	}
	public Category getCategoryById(Integer id) throws StreamReadException, DatabindException, IOException {
		List<Category> categories = mapper.readValue(categoryPath, new TypeReference<List<Category>>(){});
		for(Category category : categories) {
			if(category.getId().equals(id)) {
				return category;
			}
		}
		return null;
	}
	
}
