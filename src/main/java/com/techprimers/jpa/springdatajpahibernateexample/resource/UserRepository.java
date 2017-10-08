package com.techprimers.jpa.springdatajpahibernateexample.resource;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <Users, Integer> {
}
