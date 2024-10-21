package payroll;

class EmployeeNotFoundException extends RuntimeException {

	EmployeeNotFoundException(String id) {
		super("Could not find employee " + id);
	}
}
