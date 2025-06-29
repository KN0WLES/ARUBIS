package unicorn.interfaces;

import unicorn.model.*;
import unicorn.exceptions.*;
import java.util.List;

public interface ISubject {
    void addSubject(Subject subject) throws SubjectException;
    Subject getSubjectById(String id) throws SubjectException;
    Subject getSubjectByName(String name) throws SubjectException;
    void updateSubject(Subject subject) throws SubjectException;
    void deleteSubject(String id) throws SubjectException;
    List<Subject> getSubjectsByProfessor(String professorId) throws SubjectException;
    List<Subject> getAllSubjects() throws SubjectException;
}