package hib;

import com.mysql.cj.xdevapi.SessionFactory;
import hib.database.EntityDao;
import hib.database.HibernateUtil;
import hib.database.StudentDao;
import hib.model.ClassSubject;
import hib.model.Gender;
import hib.model.Grade;
import hib.model.Student;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //SessionFactory factory = HibernateUtil.getSessionFactory();
        StudentDao dao = new StudentDao();
        Scanner scanner = new Scanner(System.in);
        String command;
        do {
            System.out.println("Podaj komendę [insert,list,delete,modify]");
            command = scanner.nextLine();
            if (command.startsWith("insert")) {         // insert Paweł Gaweł 20 true MALE
                String[] words = command.split(" ");
                Student student = Student.builder()
                        .firstName(words[1])
                        .lastName(words[2])
                        .age(Integer.parseInt(words[3]))
                        .awarded(Boolean.parseBoolean(words[4]))
                        .gender(Gender.valueOf(words[5].toUpperCase()))
                        .id(Long.parseLong(words[6]))
                        .build();

                dao.insertOrUpdate(student);

            } else if (command.startsWith("list")) {    // list
                EntityDao<Student> studentEntityDao = new EntityDao<>();
                List<Student> studentList = studentEntityDao.getAll(Student.class);
                /*log.info("List:");
                studentList.forEach(log::info);
                lub*/
                System.out.println("List:");
                studentList.forEach(System.out::println);
                System.out.println("");

            } else if (command.startsWith("delete")) {  // delete 1
                String[] words = command.split(" ");

                System.out.println("Success: " + dao.deleteStudent(Long.valueOf(words[1])));

            } else if (command.startsWith("modify")) {  // modify mariusz kowalski 30 true MALE 1
                String[] words = command.split(" ");
                Student student = Student.builder()
                        .firstName(words[1])
                        .lastName(words[2])
                        .age(Integer.parseInt(words[3]))
                        .awarded(Boolean.parseBoolean(words[4]))
                        .gender(Gender.valueOf(words[5].toUpperCase()))
                        .id(Long.parseLong(words[6]))
                        .build();

                    dao.insertOrUpdate(student);

            } else if (command.startsWith("gradeadd")) { //gradeadd 1 5.0 ENGLISH (dodaj ocenę studentowi o id 1)
                String[] words = command.split(" ");
                EntityDao<Grade> gradeEntityDao = new EntityDao<>();
                EntityDao<Student> studentEntityDao = new EntityDao<>();
                Optional<Student> studentOptional = dao.findById(Long.parseLong(words[1]));
                if (studentOptional.isPresent()) {
                    Student student = studentOptional.get();
                    Grade grade = Grade.builder()
                            .grade(Double.parseDouble(words[2]))
                            .subject(ClassSubject.valueOf(words[3]))
                            .student(student)
                            .build();

                    gradeEntityDao.insertOrUpdate(grade);
                }

                //to do: przekazujemy DAO do zapisu

            } else if(command.startsWith("gradelist")){ // gradelist 1 (wypisz oceny studenta o id 1)
                EntityDao<Student> studentEntityDao = new EntityDao<>();
                String[] words = command.split(" ");

                Optional<Student> studentOptional = studentEntityDao.findById(Long.parseLong(words[1]), Student.class);
                if (studentOptional.isPresent()) {
                    Student student = studentOptional.get();
                    System.out.println("List: ");
                    student.getGradeSet().forEach(System.out::println);
                } else {
                    System.out.println("Student not found.");
                    }

            }else if(command.startsWith("gradedelete")){ // gradedelete 1 (usuń ocenę o identyfikatorze 1)
                EntityDao<Student> studentEntityDao = new EntityDao<>();
                String[] words = command.split(" ");
                System.out.println("Success: " + studentEntityDao.delete(Long.valueOf(words[1]), Student.class));

            }else if(command.startsWith("gradeupdate")){ // gradeupdate 1 4.5 ENGLISH (aktualizuj ocenę o idenyfikatorze 1)
                    EntityDao<Grade> gradeEntityDao = new EntityDao<>();
                    String[] words = command.split(" ");
                    Optional<Grade> gradeOptional = gradeEntityDao.findById(Long.parseLong(words[1]), Grade.class);
                    if(gradeOptional.isPresent()) {
                        Grade grade = gradeOptional.get();
                        grade.setGrade(Double.parseDouble(words[2]));
                        grade.setSubject(ClassSubject.valueOf(words[3]));
                        gradeEntityDao.insertOrUpdate(grade);
                        System.out.println("Update complete.");
                    } else{
                        System.out.println("Grade not found.");
                    }
                }

        } while (!command.equalsIgnoreCase("quit"));
    }
}