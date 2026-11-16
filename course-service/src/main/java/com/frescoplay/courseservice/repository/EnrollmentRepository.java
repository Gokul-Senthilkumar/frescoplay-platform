
package com.frescoplay.courseservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.frescoplay.courseservice.entity.Enrollment;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    @Query("""
            SELECT e FROM Enrollment e
            JOIN FETCH e.course
            WHERE e.employeeId = :employeeId
            """)
    List<Enrollment> findByEmployeeId(
            @Param("employeeId") Long employeeId
    );

    Optional<Enrollment> findByEmployeeIdAndCourseId(
            Long employeeId,
            Long courseId
    );
}
