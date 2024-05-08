//import jakarta.persistence.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

  public User() {

  }

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "login")
  private String login;

  @Column(name = "active")
  private Boolean active;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "owner_id")
  private List<Pet> pets = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public List<Pet> getPets() {
    return pets;
  }

  public void setPets(List<Pet> pets) {
    this.pets = pets;
  }

  public void addPet(Pet pet) {
    pets.add(pet);
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", login='" + login + '\'' +
      ", active=" + active +
      '}';
  }
}
