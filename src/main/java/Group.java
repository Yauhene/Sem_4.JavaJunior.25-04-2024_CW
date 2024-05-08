import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "group_table")
public class Group {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "group_name")
    private String group_name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private List<Student> students;

    public Group() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id ='" + id + '\'' +
                ", group_name ='" + group_name + '\'' +
                ", students =" + students +
                '}';
    }
}

