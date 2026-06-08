package ua.foxminded.school.model;

public class Student {
    private int studentId;
    private String firstName;
    private String secondName;
    private Integer groupId;

    public Student(int studentId, Integer groupId, String firstName, String secondName) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public int getStudentId() {
        return studentId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getfirstName() {
        return firstName;
    }

    public String getsecondName() {
        return secondName;
    }
}
