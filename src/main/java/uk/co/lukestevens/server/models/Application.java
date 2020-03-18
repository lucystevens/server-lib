package uk.co.lukestevens.server.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import uk.co.lukestevens.models.IEntity;

/**
 * An object class to represent the configuration for
 * an application
 * 
 * @author luke.stevens
 */
@Entity
@Table(name = "applications", schema = "core")
public class Application extends IEntity{
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	
	@Column(name = "name", unique=true)
	private String name;
	
	@Column(name = "domain")
	private String domain;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "git_repo")
	private String gitRepo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private ApplicationType type;
	
	@Column(name = "running_port")
	private int runningPort;
	
	@Column(name = "upgrade_port")
	private int upgradePort;
	
	@Column(name = "internal_port")
	private int internalPort;
	
	/**
	 * @return The application id
	 */
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return super.toString() + this.name;
	}

	/**
	 * @return The application name.
	 * This will be the maven projectId if the application is a maven project
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the application name
	 * @param name The name or maven projectId
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return The domain (domain.lukecmstevens.co.uk) that
	 * will forward to this application, or null if not applicable
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain that will forward to this application
	 * @param domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	/**
	 * @return A brief description of this application
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets a brief description fo this application
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return The type of application
	 */
	public ApplicationType getType() {
		return type;
	}

	/**
	 * Sets the type of application
	 * @param type
	 */
	public void setType(ApplicationType type) {
		this.type = type;
	}
	
	/**
	 * @return The port this application is currently running on
	 */
	public int getRunningPort() {
		return runningPort;
	}

	/**
	 * Sets the port this application is currently running on
	 * @param runningPort
	 */
	public void setRunningPort(int runningPort) {
		this.runningPort = runningPort;
	}
	
	/**
	 * @return The port this application will run upgrade instances on during
	 * deployment
	 */
	public int getUpgradePort() {
		return upgradePort;
	}

	/**
	 * Sets the port to run upgrade instance on during deployment
	 * @param upgradePort
	 */
	public void setUpgradePort(int upgradePort) {
		this.upgradePort = upgradePort;
	}
	
	/**
	 * @return The port exposed internally for health checks and shutdown
	 */
	public int getInternalPort() {
		return internalPort;
	}
	
	/**
	 * Sets the port to be exposed for internal tools
	 * @param internalPort
	 */
	public void setInternalPort(int internalPort) {
		this.internalPort = internalPort;
	}
	
}
