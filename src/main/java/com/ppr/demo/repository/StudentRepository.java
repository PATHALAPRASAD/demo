package com.ppr.demo.repository;

import com.ppr.demo.entity.StudentEntity;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
    List<StudentEntity> findByRollNumber(String rollNumber);
    List<StudentEntity> findByCollege(String college);
    List<StudentEntity> findByName(String name);
    List<StudentEntity> findByCourse(String course);
    List<StudentEntity> findByBranch(String branch);
    List<StudentEntity> findByAverage(double average);
    List<StudentEntity> findByGrade(Character grade);
    List<StudentEntity> findByAverageGreaterThan(double average);
//    @Query("select s from student s where s.average > ?1")
//    List<StudentEntity> findByAverage2(double average);

    StudentEntity deleteByRollNumber(String rollNumber);
}
