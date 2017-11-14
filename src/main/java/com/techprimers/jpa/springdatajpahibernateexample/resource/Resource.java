package com.techprimers.jpa.springdatajpahibernateexample.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/all")
public class Resource {

    private UserRepository usersRepository;

    public Resource(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @GetMapping("/")
    public List<Users> all() {

        Stream.of("How are you?", "Namaste",
                "Hello", "Welcome to Youtube", "Hi", "Hello", "Namaste")
                .filter(text -> text.startsWith("H"))
                .map(text -> text.substring(2))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return usersRepository.findAll();
    }

}
