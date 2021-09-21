package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LessonInstance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LessonInstance entity.
 */
@Repository
public interface LessonInstanceRepository extends JpaRepository<LessonInstance, Long>, JpaSpecificationExecutor<LessonInstance> {
    @Query("select lessonInstance from LessonInstance lessonInstance where lessonInstance.internalUser.login = ?#{principal.username}")
    List<LessonInstance> findByInternalUserIsCurrentUser();

    @Query(
        value = "select distinct lessonInstance from LessonInstance lessonInstance left join fetch lessonInstance.students",
        countQuery = "select count(distinct lessonInstance) from LessonInstance lessonInstance"
    )
    Page<LessonInstance> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct lessonInstance from LessonInstance lessonInstance left join fetch lessonInstance.students")
    List<LessonInstance> findAllWithEagerRelationships();

    @Query("select lessonInstance from LessonInstance lessonInstance left join fetch lessonInstance.students where lessonInstance.id =:id")
    Optional<LessonInstance> findOneWithEagerRelationships(@Param("id") Long id);
}
