package az.code.servletdemo;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import az.code.servletdemo.model.Employee;
import az.code.servletdemo.service.EmployeeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "employeeServlet", value = "/employees/*")
public class EmployeeController extends HttpServlet {

    private final EmployeeService employeeService = new EmployeeService();

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Employee.class, new EmployeeSerializer())
            .create();
    private String message;

    public void init() {
        message = "Hello World!";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/getAll")) {
            List<Employee> employees = employeeService.getAllEmployees();
            String employeesToJson = this.gson.toJson(employees);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            out.print(employeesToJson);
            out.flush();

        } else if (pathInfo != null && pathInfo.equals("/getEmployeeById")) {
            Long id = Long.valueOf(request.getParameter("id"));

            Employee employeeById = employeeService.getEmployeeById(id);
            String employeeToJson = this.gson.toJson(employeeById);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            out.print(employeeToJson);
            out.flush();
        } else if (pathInfo != null && pathInfo.equals("/getEmployeeByNameSurname")) {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");

            List<Employee> employeesByNames = employeeService.getEmployeesByNames(name, surname);
            String employeesToJson = this.gson.toJson(employeesByNames);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            out.print(employeesToJson);
            out.flush();

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));

        Employee newEmployee = employeeService.postEmployee(name, surname, email, birthDate);

        if (pathInfo != null && pathInfo.equals("/addEmployee")) {
            response.setContentType("text/plain");

            PrintWriter out = response.getWriter();
            out.println("Employee added successfully:");
            out.println(newEmployee);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        Long id = Long.valueOf(req.getParameter("id"));
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        LocalDate birthDate = LocalDate.parse(req.getParameter("birthDate"));


        if (pathInfo != null && pathInfo.equals("/putEmployee")) {
            Employee newEmployee = employeeService.putEmployee(id, name, surname, email, birthDate);
            String employeeToJson = this.gson.toJson(newEmployee);

            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            out.print(employeeToJson);
            out.flush();
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        Long id = Long.valueOf(req.getParameter("id"));


        if (pathInfo != null && pathInfo.equals("/deleteEmployee")) {
            employeeService.delete(id);

            PrintWriter out = resp.getWriter();

            out.print("Deleted successfully");
            out.flush();
        }
    }
}