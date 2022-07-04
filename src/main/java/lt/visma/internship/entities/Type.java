package lt.visma.internship.entities;




public class Type {
	
	private Integer id;
	
	private String name;

	
	
	public Type() {}
	
	public Type(String name) {
		super();
		this.name = name;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
