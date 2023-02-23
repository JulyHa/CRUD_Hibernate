package com.example.springinit.service;

import com.example.springinit.model.Employee;
import com.example.springinit.model.EmployeeForm;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    static private final List<Employee> employees = new ArrayList<>();
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;
    @Value("${file-upload}")
    private String fileUpload;

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

    public List<Employee> findAll() {
        String queryStr = "SELECT c FROM Employee AS c";
        TypedQuery<Employee> query = entityManager.createQuery(queryStr, Employee.class);
        return query.getResultList();
    }
    public Employee findById(int id) {
        String queryStr = "select c from Employee as c where c.id = :id";
        TypedQuery<Employee> query = entityManager.createQuery(queryStr, Employee.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public int findIndexById(int id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public void create(Employee employee) {
        Session session = sessionFactory.openSession();
        try {
            // bắt đầu 1 giao dịch
            session.beginTransaction();
            // thực thi câu query dạng hql
            session.save(employee);
            // kết thúc 1 giao dịch
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            // nếu có lỗi thì trở về trạng thái lúc chưa bắt đầu giao dịch.
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void update(int id, Employee employee) {
//        Transaction transaction = null;
        employee.setId(id);
        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
            Employee origin = findById(id);
            origin.setName(employee.getName());
            origin.setAddress(employee.getAddress());
            origin.setImage(employee.getImage());
            session.saveOrUpdate(origin);
//            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
//            if (transaction != null) {
//                transaction.rollback();
        }

    }

    public boolean delete(int id) {
        int index = findIndexById(id);
        if (index < 0) {
            return false;
        }
        employees.remove(index);
        return true;
    }
    public Employee splitPath(EmployeeForm employeeForm){
       MultipartFile multipartFile = employeeForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(employeeForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new Employee((int) (Math.random() * 10000), employeeForm.getName(),
                employeeForm.getAddress(), fileName);
    }

}
