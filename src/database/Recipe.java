package database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "Recipe") // maps fields to mysql table columns
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // specifies that id is a primary key
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private User user;

    // default constructor
    public Recipe() { System.out.println("Hibernate calling default recipe constructor."); }

    // for creating new recipe when price is still undetermined
    public Recipe(String title, String sourceUrl, String imageUrl) {
        System.out.println("Hibernate calling new recipe constructor.");
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
    }

    // for creating new recipe
    public Recipe(String title, String sourceUrl, String imageUrl, double price) {
        System.out.println("Hibernate calling new recipe constructor.");
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    // for updating recipe
    public Recipe(long id, String title, String sourceUrl, String imageUrl, double price) {
        System.out.println("Hibernate calling updating recipe constructor.");
        this.id = id;
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSourceUrl() { return sourceUrl; }

    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String toString() {
        String id = "Id: " + this.getId();
        String title = "Name: " + this.getTitle();
        String sourceUrl = "Source Url: " + this.getSourceUrl();
        String imageUrl = "Image Url: " + this.getImageUrl();
        String price = "Price: " + this.getPrice();
        String userId = "User Id: " + this.getUser().getId();
        return id + "\n" + title + "\n" + sourceUrl + "\n" + imageUrl + "\n" + price + "\n" + userId;
    }
}
