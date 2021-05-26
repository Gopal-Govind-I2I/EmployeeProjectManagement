package com.ideas2it.employeemanagement.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ideas2it.databaseconnection.DatabaseConnection;
import com.ideas2it.employeemanagement.dao.EmployeeDao;
import com.ideas2it.employeemanagement.model.Employee;
import com.ideas2it.employeemanagement.model.Address;
import com.ideas2it.sessionfactory.SessionFactoryYielder;

/**
 * EmployeeDaoImpl performs employee CRUD operations at database by using connection provided by
 * databaseconnection package and by implementing EmployeeDao interface
 *
 * @version 1.1   30-04-2021
 *
 * @author Gopal G
 */

public class EmployeeDaoImpl implements EmployeeDao{
    //private SessionFactoryYielder sessionFactoryYielder = SessionFactoryYielder.getInstance();
	
   /**
    * {@inheritdoc}
    */
    public boolean addEmployee(Employee employee) {
        boolean success = false;
        Session session =  null;
        Transaction transaction = null;
        try {
            //SessionFactory sessionFactory = sessionFactoryYielder.getSessionFactory();
        	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            String id = (String)session.save(employee);
            transaction.commit();
            success = true;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            try {
                if (!success) {
                    transaction.rollback();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return success;
    }
	
   /**
    * {@inheritdoc}
    */
    public Employee fetchIndividualEmployee(String employeeID) {
        Employee employee = null;
        Session session = null;
        try {
            //SessionFactory sessionFactory = sessionFactoryYielder.getSessionFactory();
        	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
            session = sessionFactory.openSession();
            employee = (Employee) session.get(Employee.class, employeeID);
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return employee;
    }
	
   /**
    * {@inheritdoc}
    */
    public boolean updateEmployeeDetails(Employee employee) {
        boolean success = false;
        Session session = null;
		Transaction transaction = null;
        try {
            //SessionFactory sessionFactory = sessionFactoryYielder.getSessionFactory();
        	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(employee);
            transaction.commit();
            success = true;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            try {
                if (!success) {
                    transaction.rollback();
                }
			} catch (Exception exception) {
                exception.printStackTrace();
            }
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return success;
    }
	
   /**
    * {@inheritdoc}
    */
    public List<Employee> getAllEmployees() {
        Employee employee = null;
        Session session = null;
        List<Employee> allEmployees = null;
        //System.out.print("\n\n\n\t\t\tBefore try block");
        try {
        	//System.out.print("\n\n\n\t\t\tBefore session creation");
            //SessionFactory sessionFactory = sessionFactoryYielder.getSessionFactory();
        	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
        	//System.out.print("\n\n\n\t\t\tAfter session creation");
            session = sessionFactory.openSession();
            /*System.out.print("\n\n\n\t\t\tAfter session open");
            if (null == session) {
            	System.out.print("\n\n\n\t\t\tSession is null");
            } else {
            	System.out.print("\n\n\n\t\t\tSession is active");
            }*/
            Query query = session.createQuery("from Employee where isDeleted=false");
            //System.out.print("\n\n\n\t\t\tQuery created");
            allEmployees = query.list();
            //System.out.print("\n\nWITHIN TRY" + allEmployees.size() + "\n\n");
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            //System.out.print("\n\nWITHIN CATCH\n\n");
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return allEmployees;
    }
    
    
    /**
     * {@inheritdoc}
     */
     public List<Employee> getDeletedEmployees() {
         Employee employee = null;
         Session session = null;
         List<Employee> deletedEmployees = null;
         try {
         	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
             session = sessionFactory.openSession();
             Query query = session.createQuery("from Employee where isDeleted=true");
             deletedEmployees = query.list();
         } catch (HibernateException hibernateException) {
             hibernateException.printStackTrace();
         } finally {
             if (null != session) {
                 session.close();
             }
         }
         return deletedEmployees;
     }
}