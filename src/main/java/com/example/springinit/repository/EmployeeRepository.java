package com.example.springinit.repository;

import com.example.springinit.model.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EmployeeRepository {
    @Autowired
    private static SessionFactory sessionFactory;
    @PersistenceContext
    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml")
                    .buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> findAll(){
        String q = "select c from Employee as c";
        TypedQuery<Employee> str = entityManager.createQuery(q, Employee.class);
        return str.getResultList();

    }

    public Employee findById(int id){
        String q = "select c from Employee as c where c.id = :id";
        TypedQuery<Employee> str = entityManager.createQuery(q, Employee.class);
        str.setParameter("id", id);
        return str.getSingleResult();

    }

    public boolean create(Employee employee){
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
            session.close();
        } catch (Exception e){
            return false;
        }
     return true;
    }

    public boolean update(int id, Employee employee){
        entityManager = sessionFactory.createEntityManager();
        try {
            employee.setId(id);
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.merge(employee);
            transaction.commit();
            session.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean delete(int id){
        try {
            Employee employee = findById(id);
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.remove(employee);
            transaction.commit();
            session.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

}
