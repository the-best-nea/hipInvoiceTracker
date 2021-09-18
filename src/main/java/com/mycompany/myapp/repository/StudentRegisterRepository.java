package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StudentRegister;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentRegister entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRegisterRepository extends JpaRepository<StudentRegister, Long> {}
