package com.ideas2it.projectmanagement.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

import com.ideas2it.employeemanagement.model.Employee;
import com.ideas2it.projectmanagement.model.Project;

/**
 * ProjectDao interface provides the method signatures for performing Project CRUD operations At DB
 *
 * @version 1.1   01-05-2021
 *
 * @author Gopal G
 */
public interface ProjectDao {
	
   /**
    * Adds a project record to the database with given project data
    *
    * @param project a Project object with details to be added to the database
    *
    * @return a boolean to denote successful/unsuccessful addition of project record to database
    */
    public boolean addProject(Project project);
	
   /**
    * Fetches the details of all projects and returns the collected data as a list of individual
    * project objects
    *
    * @return projectList A list of individual project objects laden with corresponding details
    *         fetched from the database
    */
    public List<Project> getAllProjects();
	
   /**
    * Updates a project record in the database
    *
    * @param project Instance of the project whose record in the database is to be updated
    *
    * @return a boolean that denotes successful/unsuccessful update of project record in the database
    */
    public boolean updateProjectDetails(Project project);
}