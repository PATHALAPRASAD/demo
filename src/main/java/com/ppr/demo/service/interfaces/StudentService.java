package com.ppr.demo.service.interfaces;

import com.ppr.demo.dto.AddStudentRequestDto;
import com.ppr.demo.dto.StudentResponseDto;
import com.ppr.demo.dto.UpdateStudentRequestDto;
import com.ppr.demo.exception.ConflictException;
import com.ppr.demo.exception.StudentNotFoundException;

import java.util.List;

public interface StudentService {

    List<StudentResponseDto> getStudents();

    StudentResponseDto getStudent(int id) throws StudentNotFoundException;

    StudentResponseDto addStudent(AddStudentRequestDto addStudentRequestDto) throws ConflictException;

    StudentResponseDto updateStudent(UpdateStudentRequestDto updateStudentRequestDto) throws StudentNotFoundException;

    StudentResponseDto updateCollege(int id, String college) throws StudentNotFoundException;

    StudentResponseDto deleteStudent(int id) throws StudentNotFoundException;

    StudentResponseDto deleteStudentByRollNumber(String id) throws StudentNotFoundException;

    List<StudentResponseDto> getStudentsAverageGreaterThan(double average);

}
