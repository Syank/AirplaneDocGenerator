package api.crabteam.model.enumarations;

public enum EnvironmentVariables {
	
	PROJECTS_FOLDER(System.getenv("ProjectsWorkFolder"));
	
	private String value;
	
	EnvironmentVariables(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
