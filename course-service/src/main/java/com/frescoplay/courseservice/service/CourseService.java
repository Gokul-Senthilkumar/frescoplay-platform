
package com.frescoplay.courseservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frescoplay.courseservice.dto.CourseRequest;
import com.frescoplay.courseservice.entity.Course;
import com.frescoplay.courseservice.entity.Enrollment;
import com.frescoplay.courseservice.repository.CourseRepository;
import com.frescoplay.courseservice.repository.EnrollmentRepository;
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository) {

        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Enrollment> getEmployeeEnrollments(Long employeeId) {
    return enrollmentRepository.findByEmployeeId(employeeId);
}

    public Course createCourse(CourseRequest request) {

        Course course = new Course();

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setDurationHours(request.getDurationHours());
        course.setLevel(request.getLevel());

        return courseRepository.save(course);
    }

    @Transactional
    public Enrollment enrollEmployee(Long courseId, Long employeeId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Course not found"));

        if (enrollmentRepository
                .findByEmployeeIdAndCourseId(employeeId, courseId)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Employee already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEmployeeId(employeeId);
        enrollment.setCourse(course);
        enrollment.setProgressPercentage(0);
        enrollment.setStatus("ENROLLED");

        return enrollmentRepository.save(enrollment);
    }
}
