package hib;

import hib.model.ClassSubject;
import hib.model.Grade;
import hib.model.Student;

public class MainLombokProblem {

    public static void main(String[] args) {
        Student student = new Student();
        student.setFirstName("Marian");

        Grade grade = new Grade(null, ClassSubject.ENGLISH, 5.0, student);
        student.getGradeSet().add(grade);

        grade = new Grade(null, ClassSubject.CHEMISTRY, 4.0, student);
        student.getGradeSet().add(grade);

        System.out.println(student);
    }
}