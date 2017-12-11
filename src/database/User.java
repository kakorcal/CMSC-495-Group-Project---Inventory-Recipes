package database;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // specifies that id is a primary key
    @Column(name = "id")
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    public User() { System.out.println("Hibernate calling default user constructor."); }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        String id = "Id: " + this.getId();
        String name = "Name: " + this.getUsername();
        return id + "\n" + name;
    }
}
