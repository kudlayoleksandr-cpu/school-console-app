package ua.foxminded.school.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "StudyGroup")
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "group_name", nullable = false, length = 20)
    private String groupName;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    protected Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(Integer groupId, String groupName) {
        this(groupName);
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<Student> getStudents() {
        return students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group other)) {
            return false;
        }
        return groupId != null && groupId.equals(other.getGroupId());
    }

    @Override
    public int hashCode() {
        return Group.class.hashCode();
    }
}
