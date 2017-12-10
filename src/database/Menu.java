package database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "Menu") // maps fields to mysql table columns
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // specifies that id is a primary key
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private User user;

    // default constructor
    public Menu() {
        System.out.println("Hibernate calling default menu constructor.");
    }

    // for creating new inventory
    public Menu(String name) {
        System.out.println("Hibernate calling new menu constructor.");
        this.name = name;
    }

    // for updating inventory
    public Menu(long id, String name) {
        System.out.println("Hibernate calling updating menu constructor.");
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String toString() {
        String id = "Id: " + this.getId();
        String name = "Name: " + this.getName();
        String userId = "User Id: " + this.getUser().getId();
        return id + "\n" + name + "\n" + userId;
    }
}