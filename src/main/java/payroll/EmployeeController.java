package payroll;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// tag::hateoas-imports[]
// end::hateoas-imports[]

@RestController
class EmployeeController {

	private final EmployeeRepository repository;

	EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	// tag::get-aggregate-root[]
	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all() {

		List<EntityModel<Employee>> employees = repository.findAll().stream()
				.map(EntityModel::of)
				.collect(Collectors.toList());

		return CollectionModel.of(employees);
	}
	// end::get-aggregate-root[]

	// Single item

	// tag::get-single-item[]
	@GetMapping("/employees/{id}")
	EntityModel<Employee> one(@PathVariable String id) {

		Employee employee = repository.findById(id) //
				.orElseThrow(() -> new EmployeeNotFoundException(id));

		return EntityModel.of(employee);
	}
	// end::get-single-item[]

	@PostMapping("/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
	}

	@PutMapping("/employees/{id}")
	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable String id) {

		return repository.findById(id) //
				.map(employee -> {
					employee.setFirstname(newEmployee.getFirstname());
					employee.setLastname(newEmployee.getLastname());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				}) //
				.orElseGet(() -> {
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});
	}

	@DeleteMapping("/employees/{id}")
	void deleteEmployee(@PathVariable String id) {
		repository.deleteById(id);
	}
}
