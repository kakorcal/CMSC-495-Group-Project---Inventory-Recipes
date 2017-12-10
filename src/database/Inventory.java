package database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * Name: Kenneth Korcal
 * Date: 12/03/2017
 * Description:
 *
 * Inventory definition. Maps MySQL table to Java object
 *
 */

@Entity
@Table(name = "Inventory") // maps fields to mysql table columns
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // specifies that id is a primary key
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private User user;

    // default constructor
    public Inventory() {
        System.out.println("Hibernate calling default inventory constructor.");
    }

    // for creating new inventory
    public Inventory(String name, int quantity) {
        System.out.println("Hibernate calling new inventory constructor.");
        this.name = name;
        this.quantity = quantity;
    }

    // for updating inventory
    public Inventory(long id, String name, int quantity) {
        System.out.println("Hibernate calling updating inventory constructor.");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String toString() {
        String id = "Id: " + this.getId();
        String name = "Name: " + this.getName();
        String quantity = "Quantity: " + this.getQuantity();
        String userId = "User Id: " + this.getUser().getId();
        return id + "\n" + name + "\n" + quantity + "\n" + userId;
    }
}
