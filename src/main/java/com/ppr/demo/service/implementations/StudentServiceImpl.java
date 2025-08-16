package com.ppr.demo.service.implementations;

import com.ppr.demo.dto.AddStudentRequestDto;
import com.ppr.demo.dto.StudentResponseDto;
import com.ppr.demo.dto.UpdateStudentRequestDto;
import com.ppr.demo.entity.StudentEntity;
import com.ppr.demo.exception.ConflictException;
import com.ppr.demo.exception.StudentNotFoundException;
import com.ppr.demo.repository.StudentRepository;
import com.ppr.demo.service.interfaces.StudentService;
import com.ppr.demo.util.MapperUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@PropertySource("classpath:/application.properties")
@Transactional
public class StudentServiceImpl implements StudentService {
    private static final String CREATED_USER = "PPR_C";
    private static final String UPDATED_USER = "PPR_U";

    @Autowired
    Environment environment;

    @Autowired
    StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public List<StudentResponseDto> getStudents() {
        return MapperUtils.mapAll(studentRepository.findAll(), StudentResponseDto.class);
    }

    @Override
    public StudentResponseDto getStudent(int id) throws StudentNotFoundException {
        StudentEntity studentEntity = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(environment.getProperty("STUDENT_NOT_FOUND")));
        return MapperUtils.map(studentEntity, StudentResponseDto.class);
    }

    @Override
    public StudentResponseDto addStudent(AddStudentRequestDto addStudentRequestDto) throws ConflictException {
        if (!studentRepository.findByRollNumber(addStudentRequestDto.getRollNumber()).isEmpty()) {
            throw new ConflictException("ROLL_NO_CANNOT_BE_DUPLICATED");
        }
        StudentEntity studentEntity = MapperUtils.map(addStudentRequestDto, StudentEntity.class);
        studentEntity.setCreatedBy(CREATED_USER);
        studentEntity.setCreatedDate(LocalDateTime.now());
        return MapperUtils.map(studentRepository.save(studentEntity), StudentResponseDto.class);
    }

    @Override
    public StudentResponseDto updateStudent(UpdateStudentRequestDto updateStudentRequestDto) throws StudentNotFoundException {
        List<StudentEntity> studentEntities = studentRepository.findByRollNumber(updateStudentRequestDto.getRollNumber());
        if (studentEntities.isEmpty()) {
            throw new StudentNotFoundException(environment.getProperty("STUDENT_NOT_FOUND"));
        } else {
            StudentEntity studentEntityToBeUpdated = MapperUtils.map(updateStudentRequestDto, StudentEntity.class);
            studentEntityToBeUpdated.setUpdatedBy(UPDATED_USER);
            studentEntityToBeUpdated.setUpdatedDate(LocalDateTime.now());
            studentEntityToBeUpdated.setCreatedDate(studentEntities.get(0).getCreatedDate());
            studentEntityToBeUpdated.setCreatedBy(studentEntities.get(0).getCreatedBy());
            studentEntityToBeUpdated.setId(studentEntities.get(0).getId());
            return MapperUtils.map(studentRepository.save(studentEntityToBeUpdated), StudentResponseDto.class);
        }
    }

    @Override
    public StudentResponseDto updateCollege(int id, String college) throws StudentNotFoundException {
        StudentEntity studentEntity = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(environment.getProperty("STUDENT_NOT_FOUND")));
        studentEntity.setCollege(college);
        studentEntity.setUpdatedBy(UPDATED_USER);
        studentEntity.setUpdatedDate(LocalDateTime.now());
        return MapperUtils.map(studentRepository.save(studentEntity), StudentResponseDto.class);
    }

    @Override
    public StudentResponseDto deleteStudent(int id) throws StudentNotFoundException {
        StudentEntity studentEntity = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(environment.getProperty("STUDENT_NOT_FOUND")));
        studentRepository.deleteById(id);
        return MapperUtils.map(studentEntity, StudentResponseDto.class);
    }

    @Override
    public StudentResponseDto deleteStudentByRollNumber(String rollNumber) throws StudentNotFoundException {
        List<StudentEntity> studentEntities = studentRepository.findByRollNumber(rollNumber);
        if (studentEntities.isEmpty()) {
            throw new StudentNotFoundException(environment.getProperty("STUDENT_NOT_FOUND"));
        }
        studentRepository.deleteByRollNumber(rollNumber);
        return MapperUtils.map(studentEntities.get(0), StudentResponseDto.class);
    }

    @Override
    public List<StudentResponseDto> getStudentsAverageGreaterThan(double average) {
        return MapperUtils.mapAll(studentRepository.findByAverageGreaterThan(average), StudentResponseDto.class);
//        return MapperUtils.mapAll(studentRepository.getStudentsAvgGt(average), StudentResponseDto.class);
    }
}
