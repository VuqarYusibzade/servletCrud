package az.code.servletdemo;

import az.code.servletdemo.model.Employee;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class EmployeeSerializer implements JsonSerializer<Employee> {

    @Override
    public JsonObject serialize(Employee employee, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", employee.getId());
        jsonObject.addProperty("name", employee.getName());
        jsonObject.addProperty("surname", employee.getSurname());
        jsonObject.addProperty("email", employee.getEmail());
        jsonObject.addProperty("birthDate", employee.getBirthDate().toString());
        return jsonObject;
    }
}