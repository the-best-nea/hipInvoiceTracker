package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StudentRegister;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the StudentRegister entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRegisterRepository extends JpaRepository<StudentRegister, Long>, JpaSpecificationExecutor<StudentRegister> {
    @Query(value = "SELECT * FROM STUDENT_REGISTER sr WHERE sr.lesson_timetable_id = :id", nativeQuery = true)
    List<StudentRegister> findAllStudentsForRegister(@Param("id") Long id);
}
