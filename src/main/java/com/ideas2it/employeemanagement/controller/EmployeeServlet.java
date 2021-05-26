package com.ideas2it.employeemanagement.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ideas2it.employeemanagement.service.EmployeeService;
import com.ideas2it.employeemanagement.service.impl.EmployeeServiceImpl;

/**
 * 
 * Handles server requests at Employee side by acting as a controller and makes
 * appropriate function calls according to every specific action
 * 
 * @version 1.1 21-05-2021
 * 
 * @author Gopal G
 *
 */
    public class EmployeeServlet extends HttpServlet {
    	EmployeeService employeeService = new EmployeeServiceImpl();

       /**
    	* Fetches details of all employees from service layer for display purposes
    	*
   	* @param request Instance of HttpServletRequest which has necessary parameters and
    	*                attributes required for delivering a web service.
    	* @param response Instance of HttpServletResponse which has the necessary response 
    	*                that is to be delivered to the web client.
    	*                
    	*/
    	public void fetchAllEmployees(HttpServletRequest request, HttpServletResponse response) {
       	 	List<List<String[]>> allEmployees = employeeService.fetchAllEmployees();
       		request.setAttribute("allEmployeesList", allEmployees);
        		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/Employee.jsp");
        		try {
            			requestDispatcher.forward(request,  response);
        		} catch (ServletException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
        		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
        		}
     	}
	
                /**
                  * Fetches details of a particular employee for update
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void getEmployeeForEdit(HttpServletRequest request, HttpServletResponse response) {
		String action = (String) request.getParameter("action");
		String employeeID = (String) request.getParameter("emp_id");
		List<String[]> employeeDetails = employeeService.searchIndividualEmployee(employeeID);
		request.setAttribute("employeeDetails", employeeDetails);
		if ("editDetails".equals(action)) {
                                      request.setAttribute("updateAction", "basicDetails");
                                     RequestDispatcher requestDispatcher = request.getRequestDispatcher("/FormFill.jsp");
		} else if ("editAddress".equals(action)) {
			request.setAttribute("updateAction", "address");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/FormFill.jsp");
		} else if("addNewAddress".equals(action)) {
			request.setAttribute("updateAction", "addNewAddress");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/FormFill.jsp");
		}
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
	 * Initiates input entry for new employee addition request
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void createNewEmployee(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("operation", "createEmployee");
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/FormFill.jsp");
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
	 * Processes received input data, performs validation and passes the clean data on to service layer
	 * for new employee addition.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void addNewEmployee(HttpServletRequest request, HttpServletResponse response) {
		String name = (String)request.getParameter("name");
		String employeeID = (String)request.getParameter("employeeId");
		String dob = (String)request.getParameter("dateOfBirth");
		String email = (String)request.getParameter("email");
		Double salary = Double.parseDouble((String)request.getParameter("salary"));
		String perm = (String)request.getParameter("isPermanent");
		boolean isPermanent = ("true".equals(perm) ? true : false);
		String doorNo = (String)request.getParameter("doorNo");
		String street = (String)request.getParameter("street");
		String locality = (String)request.getParameter("locality");
		String pincode = (String)request.getParameter("pincode");
		String district = (String)request.getParameter("district");
		String state = (String)request.getParameter("state");
		String message = "";Date dateOfBirth = null;
		String address[] = new String[8];
		List<String[]> addresses = new LinkedList<String[]>();
		boolean success = false;
		if (!(employeeService.checkIfIdExists(employeeID))) {
			dateOfBirth = employeeService.getBirthDate(dob);
			if(null != dateOfBirth) {
				address[0] = perm;
				address[1] = doorNo;
				address[2] = street;
				address[3] = locality; 
				address[4] = pincode;
				address[5] = district;
				address[6] = state;
				address[7] = employeeID;
				addresses.add(address);
				success = employeeService.addEmployee(name, employeeID, salary, dateOfBirth, email, addresses);
				if (!success) {
					message += "Error in adding employee to the roster.";
				} else {
					message += "New employee added successfully";
				}
			} else {
				message += "Invalid date of birth. ";
			}
		} else {
			message += "Invalid employeeID as it exists already.";
		}
		if (!success) {
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
		try {
			requestDispatcher.forward(request, response);
	    	} catch (ServletException | IOException e) {
            			// TODO Auto-generated catch block
	        		e.printStackTrace();
	    	}
    	}
	
	/**
	 * Processes the to-be-updated employee data, performs validation and passes on the data
	 * to service layer for update.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void updateEmployeeDetails(HttpServletRequest request, HttpServletResponse response) {
		String name = (String)request.getParameter("name");
		String employeeID = (String)request.getParameter("employeeId");
		String dob = (String)request.getParameter("dateOfBirth");
		String email = (String)request.getParameter("email");
		Double salary = Double.parseDouble((String)request.getParameter("salary"));
		Date dateOfBirth = null;
		boolean success = false;
		String message = "";
	    	dateOfBirth = employeeService.getBirthDate(dob);
	    	if(null != dateOfBirth) {
	       		success = employeeService.updateEmployeeDetails(employeeID, name, dateOfBirth, salary, email);
			if (!success) {
				message += "Error in updating employee details";
			} else {
		        		message += "Employee details updated successfully";
			}
	    	} else {
			message += "Invalid date of birth. ";
	    	}
		if (!success) {
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
		try {
           		requestDispatcher.forward(request, response);
        	} catch (ServletException | IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Processes the to-be-updated address information, performs validation and passes on the data
	 * to service layer for update.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void updateEmployeeAddress(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("employeeId");
		String doorNo = (String)request.getParameter("doorNo");
		int addressID = Integer.parseInt((String)request.getParameter("addressId"));
		String street = (String)request.getParameter("street");
		String locality = (String)request.getParameter("locality");
		String pincode = (String)request.getParameter("pincode");
		String district = (String)request.getParameter("district");
		String state = (String)request.getParameter("state");
		boolean success = employeeService.updateExistingAddress(addressID, doorNo, street, locality, pincode, 
                                                                district, state, employeeID);
		String message = "";
		if (!success) {
			message += "Address update unsuccessful.";
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			message += "Address update successful.";
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
        	try {
            		requestDispatcher.forward(request, response);
        	} catch (ServletException | IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Processes the delete employee request by forwarding it to the service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void deleteEmployee(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("emp_id");
		boolean success = employeeService.deleteEmployee(employeeID);
		String message = "";
		if (!success) {
			message += "Employee deletion unsuccessful.";
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			message += "Employee deletion successful.";
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
        	try {
			requestDispatcher.forward(request, response);
	    	} catch (ServletException | IOException e) {
	        	// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Fetches the details of all deleted employees for restore user interaction module
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void getDeletedEmployees(HttpServletRequest request, HttpServletResponse response) {
		List<List<String[]>> deletedEmployees = employeeService.getDeletedEmployees();
		request.setAttribute("deletedEmployees", deletedEmployees);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/DeletedEmployees.jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes restore employee request by issuing restore call to service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void restoreEmployee(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("emp_id");
		boolean success = employeeService.restoreEmployee(employeeID);
		String message = "";
		if (!success) {
			message += "Employee restoration unsuccessful.";
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			message += "Employee restoration successful.";
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
        	try {
            		requestDispatcher.forward(request, response);
        	} catch (ServletException | IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Processes the to-be-added address data, performs validation and passes on the data
	 * to service layer for associating it with the respective employee.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void addNewAddress(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("employeeId");
		String address[] = new String[7];
		address[0] = (String)request.getParameter("isPermanent");
		address[1] = (String)request.getParameter("doorNo");
		address[2] = (String)request.getParameter("street");
		address[3] = (String)request.getParameter("locality");
		address[4] = (String)request.getParameter("pincode");
		address[5] = (String)request.getParameter("district");
		address[6] = (String)request.getParameter("state");
		boolean success = employeeService.addNewAddress(employeeID, address);
		String message = "";
		if (!success) {
			message += "Address addition unsuccessful.";
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			message += "Address addition successful.";
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
		try {
            		requestDispatcher.forward(request, response);
        	} catch (ServletException | IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Processes the delete address request by making delete call to service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void deleteAddress(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("emp_id");
		int addressID = Integer.parseInt((String)request.getParameter("addressId"));
		boolean success = employeeService.deleteAddress(addressID,  employeeID);
		String message = "";
		if (!success) {
			message += "Address deletion unsuccessful.";
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			message += "Address deletion successful.";
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
		try {
            		requestDispatcher.forward(request, response);
        	} catch (ServletException | IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Processes permanent address update request by making appropriate call to service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void updatePermanentAddress(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("emp_id");
		int newPermanent = Integer.parseInt((String)request.getParameter("newPermanent"));
		int oldPermanent = Integer.parseInt((String)request.getParameter("oldPermanent"));
		boolean success = employeeService.updatePermanentAddress(oldPermanent, newPermanent, employeeID);
		String message = "";
		if (!success) {
			message += "Permanent address update unsuccessful.";
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			message += "Permanent address update successful.";
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
        	try {
            		requestDispatcher.forward(request, response);
        	} catch (ServletException | IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Processes unassign project request by making appropriate call to service layer
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void unassignProject(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("emp_id");
		int projectID = Integer.parseInt((String)request.getParameter("proj_id"));
		boolean success = employeeService.unassignProject(employeeID, projectID);
		String message = "";
		if (!success) {
			message += "Project unassign unsuccessful.";
			request.setAttribute("errorMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
		} else {
			message += "Project unassign successful.";
			request.setAttribute("successMessage", message);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
		}
        	try {
            		requestDispatcher.forward(request, response);
        	} catch (ServletException | IOException e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
        	}
	}
	
	/**
	 * Fetches the details of assignable projects for an employee for assign employee user interaction module
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void getAssignableProjects(HttpServletRequest request, HttpServletResponse response) {
		String employeeID = (String)request.getParameter("emp_id");
		List<String[]> assignableProjects = employeeService.getAssignableProjects(employeeID);
		request.setAttribute("employeeID", employeeID);
		request.setAttribute("assignableProjects", assignableProjects);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/AssignProjects.jsp");
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
	 * Receives a list of projects as request, processes it and passes it on to service layer for assignment.
	 *
	 * @param request Instance of HttpServletRequest which has necessary parameters and
	 *                attributes required for delivering a web service.
	 * @param response Instance of HttpServletResponse which has the necessary response 
	 *                that is to be delivered to the web client.
	 *                
	 */
	public void assignProjects(HttpServletRequest request, HttpServletResponse response) {
		String projectIdSet[] = request.getParameterValues("projects");
		String employeeID = request.getParameter("emp_id");
		Set<Integer> assignableIdSet = new LinkedHashSet<Integer>();
		for(String id : projectIdSet) {
			int projId = Integer.parseInt(id);
			assignableIdSet.add(projId);
		}
		List<Integer> unassignableProjects = employeeService.assignProject(employeeID, assignableIdSet);
		String message = "" + (assignableIdSet.size() - unassignableProjects.size()) 
				+ " out of " + assignableIdSet.size() + " projects successfully assigned.";
		request.setAttribute("successMessage", message);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SuccessDisplay.jsp");
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
		String type = (String) request.getParameter("type");
		String employeeID = (String) request.getParameter("emp_id");
		List<String[]> employeeDetails = employeeService.searchIndividualEmployee(employeeID);
		if (1 == employeeDetails.size()) {
			String arr[] = employeeDetails.get(0);
			if ("NULL".equals(arr[0])) {
				request.setAttribute("errorMessage", "NO SUCH EMPLOYEE FOUND");
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ErrorDisplay.jsp");
			}
		} else {
			request.setAttribute("singleEmployee", employeeDetails);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SingleEmployee.jsp"); 
		}
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
		String action = (String) request.getParameter("action");
		if (null == action) {
			//System.out.print("\nAction is null");
			String errorMessage = "Warning: Invalid action";
			request.setAttribute("errorMsg", errorMessage);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
			try {
				requestDispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
		    switch(action) {
		        case "displayAll" :
		                fetchAllEmployees(request, response);
		                break;
		        case "createEmployee":
		        	createNewEmployee(request, response);
		        	break;
		        case "editDetails":
		        	getEmployeeForEdit(request, response);
		        	break;
		        case "editAddress":
		        	getEmployeeForEdit(request,response);
		        	break;
		        case "addNewEmployee":
		            	addNewEmployee(request, response);
		            	break;
		        case "updateEmployeeDetails":
		        	updateEmployeeDetails(request, response);
		        	break;
		        case "updateEmployeeAddress":
		        	updateEmployeeAddress(request, response);
		        	break;
		        case "deleteEmployee":
		        	deleteEmployee(request, response);
		        	break;
		        case "getDeletedEmployees":
		        	getDeletedEmployees(request, response);
		        	break;
		        case "restoreEmployee":
		        	restoreEmployee(request, response);
		        	break;
		        case "addNewAddress":
		        	getEmployeeForEdit(request, response);
		        	break;
		        case "insertAddress":
		        	addNewAddress(request, response);
		        	break;
		        case "deleteAddress":
		        	deleteAddress(request, response);
		        	break;
		        case "markAsPermanent":
		        	updatePermanentAddress(request, response);
		        	break;
		        case "unassignProject":
		        	unassignProject(request, response);
		        	break;
		        case "getAssignableProjects":
		        	getAssignableProjects(request, response);
		        	break;
		        case "assignProjects":
		        	assignProjects(request, response);
		        	break;
		        default:
		    	    break;
		    }
		}
	}
}
