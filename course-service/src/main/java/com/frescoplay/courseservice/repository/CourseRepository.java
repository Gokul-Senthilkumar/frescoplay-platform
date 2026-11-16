package com.frescoplay.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.frescoplay.courseservice.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
