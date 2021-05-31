package com.ideas2it.projectmanagement.controller;

import java.io.IOException;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ideas2it.projectmanagement.service.ProjectService;
import com.ideas2it.projectmanagement.service.impl.ProjectServiceImpl;


/**
 * 
 * Handles server requests at Project side by acting as a controller and makes
 * appropriate function calls according to every specific action
 * 
 * @version 1.1 24-05-2021
 * 
 * @author Gopal G
 *
 */
public class ProjectServlet extends HttpServlet {
	ProjectServiceImpl projectServiceImpl = new ProjectServiceImpl();
	
	/**
	 * Fetches details of all projects from service layer for display purposes
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void fetchAllProjects(HttpServletRequest request, HttpServletResponse response) {
		List<List<String[]>> allProjects = projectServiceImpl.fetchAllProjects();
		request.setAttribute("allProjectsList", allProjects);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Project.jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fetches details of a particular project for update
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void getProjectForEdit(HttpServletRequest request, HttpServletResponse response) {
		int projectID = Integer.parseInt((String) request.getParameter("proj_id"));
		List<String[]> projectDetails = projectServiceImpl.searchIndividualProject(projectID);
		request.setAttribute("projectDetails", projectDetails);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ProjectForm.jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes the to-be-updated project data, performs validation and passes on the data
	 * to service layer for update.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void updateProjectDetails(HttpServletRequest request, HttpServletResponse response) {
		int projectID = Integer.parseInt((String)request.getParameter("proj_id"));
		Date deadline = null;
		String name = request.getParameter("name");
		String manager = request.getParameter("manager");
		String client = request.getParameter("client");
		String deadlineDate = request.getParameter("deadline");
		deadline = projectServiceImpl.getDeadline(deadlineDate);
		boolean success = false;
                String message = "";
		if (null != deadline) {
			 success = projectServiceImpl.updateProjectDetails(projectID, name, manager, client, deadline);
		} else {
			 message += "Invalid date input for deadline. ";
		}
		message += (success ? "Project update successful." : "Project update unsuccessful.");
                String attributeName = (success ? "successMessage" : "errorMessage");
                String dispatchDestination = (success ? "/SuccessDisplay.jsp" : "/ErrorDisplay.jsp");
                request.setAttribute(attributeName, message);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(dispatchDestination);
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initiates input entry for new project addition request
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void createNewProject(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("operation", "createProject");
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ProjectForm.jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes received input data, performs validation and passes the clean data on to service layer
	 * for new project addition.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void addNewProject(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String manager = request.getParameter("manager");
		String client = request.getParameter("client");
		String deadlineDate = request.getParameter("deadline");
		Date deadline = projectServiceImpl.getDeadline(deadlineDate);
                String message = (null != deadline ? "" : "Invalid input date for deadline. ") ;
                boolean success = (null != deadline ? projectServiceImpl.addProject(name, manager, client, deadline) : false);
                message += (success ? "Project creation successful." : "Project creation unsuccessful.");
                String attributeName = (success ? "successMessage" : "errorMessage");
                String dispatchDestination = (success ? "/SuccessDisplay.jsp" : "/ErrorDisplay.jsp");
                request.setAttribute(attributeName, message);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(dispatchDestination);
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes the delete project request by forwarding it to the service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void deleteProject(HttpServletRequest request, HttpServletResponse response) {
		int projectID = Integer.parseInt((String)request.getParameter("proj_id"));
		boolean success = projectServiceImpl.deleteProject(projectID);
		String message = (success ? "Project deletion successful." : "Project deletion unsuccessful.");
                String attributeName = (success ? "successMessage" : "errorMessage");
                String dispatchDestination = (success ? "/SuccessDisplay.jsp" : "/ErrorDisplay.jsp");
                request.setAttribute(attributeName, message);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(dispatchDestination);
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fetches the details of all deleted projects for restore user interaction module
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void getDeletedProjects(HttpServletRequest request, HttpServletResponse response) {
		List<List<String[]>> deletedProjects = projectServiceImpl.getDeletedProjects();
		request.setAttribute("deletedProjects", deletedProjects);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/DeletedProjects.jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes restore project request by issuing restore call to service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void restoreProject(HttpServletRequest request, HttpServletResponse response) {
		int projectID = Integer.parseInt((String)request.getParameter("proj_id"));
		boolean success = projectServiceImpl.restoreProject(projectID);
		String message = (success ? "Project restoration successful." : "Project restoration unsuccessful.");
                String attributeName = (success ? "successMessage" : "errorMessage");
                String dispatchDestination = (success ? "/SuccessDisplay.jsp" : "/ErrorDisplay.jsp");
                request.setAttribute(attributeName, message);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(dispatchDestination);
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes unassign employee request by making appropriate call to service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void unassignEmployee(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("emp_id");
		int projectID = Integer.parseInt((String)request.getParameter("proj_id"));
		boolean success = projectServiceImpl.unassignEmployee(employeeID, projectID);
		String message = (success ? "Employee unassign successful." : "Employee unassign unsuccessful.");
                String attributeName = (success ? "successMessage" : "errorMessage");
                String dispatchDestination = (success ? "/SuccessDisplay.jsp" : "/ErrorDisplay.jsp");
                request.setAttribute(attributeName, message);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(dispatchDestination);
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fetches the details of assignable employees for a project for assign employee user interaction module
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void getAssignableEmployees(HttpServletRequest request, HttpServletResponse response) {
		int projectID = Integer.parseInt((String)request.getParameter("proj_id"));
		List<String[]> assignableEmployees = projectServiceImpl.getAssignableEmployees(projectID);
		request.setAttribute("projectID", projectID);
		request.setAttribute("assignableEmployees", assignableEmployees);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/AssignEmployees.jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Receives a list of employees as request, processes it and passes it on to service layer for assignment.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void assignEmployees(HttpServletRequest request, HttpServletResponse response) {
		String employeeIdSet[] = request.getParameterValues("employees");
		int projectID = Integer.parseInt(request.getParameter("proj_id"));
		Set<String> assignableIdSet = new LinkedHashSet<String>();
		for(String id : employeeIdSet) {
			assignableIdSet.add(id);
		}
		List<String> unassignableEmployees = projectServiceImpl.assignEmployee(projectID, assignableIdSet);
		String message = "" + (assignableIdSet.size() - unassignableEmployees.size()) + " out of " + assignableIdSet.size() + " employees successfully assigned.";
		request.setAttribute("successMessage", message);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
	
	public void fetchSingleProject(HttpServletRequest request, HttpServletResponse response) {
		boolean invalidInput = false;
		int projectID = 0;
		try {
		    projectID = Integer.parseInt((String) request.getParameter("proj_id"));
		} catch (NumberFormatException nfe) {
	            invalidInput = true;
		}
		List<String[]> projectDetails = projectServiceImpl.searchIndividualProject(projectID);
		RequestDispatcher requestDispatcher = null;
		if (1 == projectDetails.size()) {
			String arr[] = projectDetails.get(0);
			String message = (invalidInput ? "Invalid input warning!!!" : "NO SUCH PROJECT FOUND");
			if ("NULL".equals(arr[0])) {
				request.setAttribute("errorMessage", message);
				requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
			}
		} else {
			request.setAttribute("singleProject", projectDetails);
			requestDispatcher = request.getRequestDispatcher("/SingleProject.jsp");
		}
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Receives all incoming server requests with get method and delegates calls according to
	 * every specific action.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		if (null == action) {
			//System.out.print("\nAction is null");
			String errorMessage = "Warning: Invalid action";
			request.setAttribute("errorMsg", errorMessage);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
			try {
				requestDispatcher.forward(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			switch(action) {
			    case "displayAll":
		    	    fetchAllProjects(request, response);
		    	    break;
			    case "singleProject":
			        fetchSingleProject(request, response);
			        break;
			    case "getDeletedProjects":
			    	getDeletedProjects(request, response);
			    	break;
                            case "getAssignableEmployees":
                                getAssignableEmployees(request, response);
                                break;
			    default:
			    	break;
		    }	
		}
	}
	
	/**
	 * Receives all incoming server requests with post method and delegates calls according to
	 * every specific action.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String action = (String)request.getParameter("action");
		if (null == action) {
			//System.out.print("\nAction is null");
			String errorMessage = "Warning: Invalid action";
			request.setAttribute("errorMsg", errorMessage);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
			try {
				requestDispatcher.forward(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			switch(action) {
			    case "editDetails":
			    	getProjectForEdit(request, response);
			    	break;
			    case "updateProject":
			    	updateProjectDetails(request, response);
			    	break;
			    case "createProject":
			    	createNewProject(request, response);
			    	break;
			    case "addNewProject":
			    	addNewProject(request, response);
			    	break;
			    case "deleteProject":
			    	deleteProject(request, response);
			    	break;
			    case "restoreProject":
			    	restoreProject(request, response);
			    	break;
			    case "unassignEmployee":
			    	unassignEmployee(request, response);
			    	break;
			    case "assignEmployees":
			    	assignEmployees(request, response);
			    	break;
			    default:
			    	break;
			}
		}
	}

}
