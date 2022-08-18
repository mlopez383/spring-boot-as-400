package com.example.as400connector.controller;

import com.example.as400connector.repository.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jde")
public class PersonController {

  private final PersonRepository personRepository;

  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @GetMapping("/person/{id}")
  public String getPersonById(@PathVariable Integer id) {
    String res = personRepository.getPersonById(id);
    if (res == null) {
      return "Person not found!";
    } else {
      return "Found person: <b>" + res + "</b>";
    }
  }

}
