package uk.co.lukestevens.server.models;

/**
 * Interface defining properties which can be applied to different application types
 * 
 * @author Luke Stevens
 */
public interface Application {
	
	/**
	 * @return The application id
	 */
	public Integer getId();

	/**
	 * @return The application name.
	 * This will be the maven projectId if the application is a maven project
	 */
	public String getName();

	/**
	 * Sets the application name
	 * @param name The name or maven projectId
	 */
	public void setName(String name);
	
	/**
	 * @return A brief description of this application
	 */
	public String getDescription();

	/**
	 * Sets a brief description for this application
	 * @param description
	 */
	public void setDescription(String description);
	
	
	/**
	 * @return The url of the git repository for this application
	 */
	public String getGitRepo();

	/**
	 * Sets the url of the git repository for this application
	 * @param gitRepo
	 */
	public void setGitRepo(String gitRepo);

}
