package az.code.servletdemo.service;

import az.code.servletdemo.EmployeeSerializer;
import az.code.servletdemo.model.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.lang.System.out;

public class EmployeeService {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("az.code.servletdemo");
    EntityManager em = emf.createEntityManager();

    public List<Employee> getAllEmployees() {
        TypedQuery<Employee> query = em.createQuery("SELECT e FROM employees e", Employee.class);
        return query.getResultList();
    }

    public Employee getEmployeeById(Long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            if (id != null) {
                Employee employee = em.find(Employee.class, id);

                if (employee != null) {
                    transaction.commit();
                    return employee;
                } else {
                    transaction.rollback();
                    out.println("Employee with ID " + id + " not found");
                    return null;
                }
            } else {
                transaction.rollback();
                out.println("Invalid employee ID.");
                return null;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public List<Employee> getEmployeesByNames(String firstName, String lastName) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM employees e WHERE e.name = :firstName AND e.surname = :lastName", Employee.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            List<Employee> filteredEmployees = query.getResultList();

            transaction.commit();
            return filteredEmployees;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Employee postEmployee(String name, String surname, String email, LocalDate birthDate) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Employee employee = new Employee();
        employee.setName(name);
        employee.setSurname(surname);
        employee.setEmail(email);
        employee.setBirthDate(birthDate);

        try {
            em.persist(employee);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return employee;
    }

    public Employee putEmployee(Long id, String name, String surname, String email, LocalDate birthDate) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Employee employee = new Employee();

        if (id != null && (employee = em.find(Employee.class, id)) != null) {
            employee.setName(name);
            employee.setSurname(surname);
            employee.setEmail(email);
            employee.setBirthDate(birthDate);
        } else {
            out.println("This employee doesnt exist");
        }
        try {
            em.persist(employee);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return employee;
    }

    public void delete(Long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Query query = em.createQuery("DELETE FROM employees e WHERE e.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


}
