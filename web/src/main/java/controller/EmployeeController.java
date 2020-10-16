package controller;

import org.example.dao.EmployeeDao;
import org.example.model.Employee;
import ui.EmployeeForm;
import ui.SearchForm;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@RequestScoped
@Named
public class EmployeeController{

    @Inject
    private EmployeeDao employeeDao;
    @Inject
    private EmployeeForm employeeForm;

    @Inject
    private SearchForm searchForm;

    @Produces
    @Named
    public List<Employee> getEmployees() {
        if (searchForm.getHiredAfter() == null) {
            return employeeDao.queryAll();
        } else {
            return employeeDao.queryHiredAfter(searchForm.getHiredAfter());
        }
    }

    public void save() {
        employeeDao.merge(employeeForm.getEmployee());
    }

    public void preRenderViewEvent() {
        if (employeeForm.getEmployee() == null) {
            initializeEmployee();
        }
    }

    private void initializeEmployee() {
        if (employeeForm.getEmployeeId() == null) {
            employeeForm.setEmployee(new Employee());
            return;
        }
        Employee employee = employeeDao.find(employeeForm.getEmployeeId());
        employeeForm.setEmployee(employee);
    }

    public void remove(Employee employee) {
        employeeDao.remove(employee);
        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                "Successfully deleted employee " + employee.getEmployeeNo()));
    }

}
