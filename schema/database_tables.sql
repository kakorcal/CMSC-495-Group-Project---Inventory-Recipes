-- Straightforward table for user. We can make it more complex by adding password hashing if we have time.
-- SERIAL DEFAULT VALUE means the id is required and will auto increment without passing the id as a param.
-- methods: create, read, delete
-- additional constraints: each user must have a unique username
CREATE TABLE User (
  id int SERIAL DEFAULT VALUE,
  username varchar(60) NOT NULL UNIQUE,
  password varchar(60) NOT NULL
);

-- Inventory table is also simple. Notice that there is a user_id foreign key constraint.
-- This means that in order to create an inventory, the user must be created first.
-- ON DELETE CASCADE means that if the user is deleted, the inventory associated to the user is also deleted.
-- methods: create, read, update, delete, list
-- additional constraints: users cannot have duplicate inventory names (need to select where user_id=id and name=arg_name)
CREATE TABLE Inventory (
  id int SERIAL DEFAULT VALUE,
  name varchar(60) NOT NULL,
  quantity int UNSIGNED NOT NULL,
  user_id int NOT NULL,
  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- The recipe table maps the RecipeObject class but also has the user_id property.
-- methods: create, read, update, delete, list
-- additional constraints: users cannot have duplicate recipe names
CREATE TABLE Recipe (
  id int SERIAL DEFAULT VALUE,
  title text NOT NULL,
  source_url text,
  image_url text,
  user_id int NOT NULL,
  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- menu is essentially a collection of recipes. therefore, we will create a MenuItem table to 
-- create the one-to-many relationship.
-- methods: create, read, update, delete, list
-- additional constraints: menu name must be unique per user
CREATE TABLE Menu (
  id int SERIAL DEFAULT VALUE,
  name varchar(60) NOT NULL,
  user_id int NOT NULL,
  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- methods: create, read, update, delete
CREATE TABLE MenuItem (
  id int SERIAL DEFAULT VALUE,
  price double UNSIGNED,
  menu_id int NOT NULL,
  recipe_id int NOT NULL,
  FOREIGN KEY (menu_id) REFERENCES Menu(id) ON DELETE CASCADE,
  FOREIGN KEY (recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE
);