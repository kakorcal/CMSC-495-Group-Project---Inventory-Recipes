package database;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "MenuItem") // maps fields to mysql table columns
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // specifies that id is a primary key
    @Column(name = "id")
    private long id;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Recipe recipe;

    // default constructor
    public MenuItem() {
        System.out.println("Hibernate calling default menu item constructor.");
    }

    // for creating new inventory
    public MenuItem(Double price) {
        System.out.println("Hibernate calling new menu item constructor.");
        this.price = price;
    }

    // for updating inventory
    public MenuItem(long id, Double price) {
        System.out.println("Hibernate calling updating menu item constructor.");
        this.id = id;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String toString() {
        String id = "Id: " + this.getId();
        String title = "Recipe: " + this.getRecipe().getTitle();
        String price = "Price: " + this.getPrice();
        String menuId = "Menu Id: " + this.getMenu().getId();
        String recipeId = "Recipe Id: " + this.getRecipe().getId();
        String userId = "User Id: " + this.getMenu().getUser().getId();
        return id + "\n" + title + "\n" + price + "\n" + menuId + "\n" + recipeId + "\n" + userId;
    }
}
