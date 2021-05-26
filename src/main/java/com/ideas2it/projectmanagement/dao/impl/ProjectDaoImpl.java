package com.ideas2it.projectmanagement.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ideas2it.databaseconnection.DatabaseConnection;
import com.ideas2it.employeemanagement.model.Employee;
import com.ideas2it.projectmanagement.dao.ProjectDao;
import com.ideas2it.projectmanagement.model.Project;
import com.ideas2it.sessionfactory.SessionFactoryYielder;

/**
 * ProjectDaoImpl performs project CRUD operations at database by using connection provided by
 * databaseconnection package and by implementing ProjectDao interface
 *
 * @version 1.1   01-05-2021
 *
 * @author Gopal G
 */

public class ProjectDaoImpl implements ProjectDao {
    //private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    //private SessionFactoryYielder sessionFactoryYielder = SessionFactoryYielder.getInstance();
	
   /**
    * {@inheritdoc}
    */
    public boolean addProject(Project project) {
        Session session = null;
        Transaction transaction = null;
        boolean success = false;
        try {
            SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(project);
            transaction.commit();
            success = true;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e) {
                e.printStackTrace();
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
    public Project fetchIndividualProject(int projectID) {
        Project project = null;
        Session session = null;
        Transaction transaction = null;
        try {
            //SessionFactory sessionFactory = sessionFactoryYielder.getSessionFactory();
        	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            project = (Project) session.get(Project.class, projectID);
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return project;
    }
	
   /**
    * {@inheritdoc}
    */
    public boolean updateProjectDetails(Project project) {
        Session session = null;
        Transaction transaction = null;
        boolean success = false;
        try {
            SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(project);
            transaction.commit();
            success = true;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e) {
                e.printStackTrace();
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
    public List<Project> getAllProjects() {
        Project project = null;
        Session session = null;
        List<Project> allProjects = null;
        try {
            //SessionFactory sessionFactory = sessionFactoryYielder.getSessionFactory();
        	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
            session = sessionFactory.openSession();
            Query query = session.createQuery("from Project where isDeleted=false");
            allProjects = query.list();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return allProjects;		
    }
    
    /**
     * {@inheritdoc}
     */
     public List<Project> getDeletedProjects() {
         Project project = null;
         Session session = null;
         List<Project> deletedProjects = null;
         try {
         	SessionFactory sessionFactory = SessionFactoryYielder.getSessionFactory();
             session = sessionFactory.openSession();
             Query query = session.createQuery("from Project where isDeleted=true");
             deletedProjects = query.list();
         } catch (HibernateException hibernateException) {
             hibernateException.printStackTrace();
         } finally {
             if (null != session) {
                 session.close();
             }
         }
         return deletedProjects;		
     }
}