package com.example.springwork.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import com.example.springwork.dao.repository.EmployeeRepository;
import com.example.springwork.domain.Employee;
import com.example.springwork.support.assembler.EmployeeModelAssembler;
import com.example.springwork.support.exception.EmployeeNotFoundException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@ApiOperation(value = "EmployeeController")
public class EmployeeController {

  private final EmployeeRepository repository;
  private final EmployeeModelAssembler assembler;

  // EmployeeController(EmployeeRepository repository) {
  // this.repository = repository;
  // }

  EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // @GetMapping("/employees")
  // List<Employee> all() {
  // return repository.findAll();
  // }

  @GetMapping("/employees")
  public CollectionModel<EntityModel<Employee>> all() {

    // List<EntityModel<Employee>> employees = repository.findAll().stream()
    // .map(employee -> EntityModel.of(employee,
    // linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
    // linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
    // .collect(Collectors.toList());

    // return CollectionModel.of(employees,
    // linkTo(methodOn(EmployeeController.class).all()).withSelfRel());

    List<EntityModel<Employee>> employees = repository.findAll().stream() //
        .map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
  }

  // @PostMapping("/employees")
  // Employee newEmployee(@RequestBody Employee newEmployee) {
  // return repository.save(newEmployee);
  // }

  @PostMapping("/employees")
  ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

    EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

    return ResponseEntity //
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
  }

  // @GetMapping("/employees/{id}")
  // Employee one(@PathVariable Long id) {
  // return repository.findById(id).orElseThrow(() -> new
  // EmployeeNotFoundException(id));
  // }

  @GetMapping("/employees/{id}")
  public EntityModel<Employee> one(@PathVariable Long id) {

    Employee employee = repository.findById(id) //
        .orElseThrow(() -> new EmployeeNotFoundException(id));

    return assembler.toModel(employee);
    // return EntityModel.of(employee,
    // linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
    // linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
  }

  // @PutMapping("/employees/{id}")
  // Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable
  // Long id) {

  // return repository.findById(id)
  // .map(employee -> {
  // employee.setName(newEmployee.getName());
  // employee.setRole(newEmployee.getRole());
  // return repository.save(employee);
  // })
  // .orElseGet(() -> {
  // newEmployee.setId(id);
  // return repository.save(newEmployee);
  // });
  // }

  @PutMapping("/employees/{id}")
  ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

    Employee updatedEmployee = repository.findById(id) //
        .map(employee -> {
          employee.setName(newEmployee.getName());
          employee.setRole(newEmployee.getRole());
          return repository.save(employee);
        }) //
        .orElseGet(() -> {
          newEmployee.setId(id);
          return repository.save(newEmployee);
        });

    EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

    return ResponseEntity //
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }
}