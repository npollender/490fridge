DROP TABLE IF EXISTS Device_tb, Camera_tb, User_tb, Reviews_tb, ShoppingList_tb, Inventory_tb,
    FoodCategory_tb, Recipes_tb, User_Recipes_tb;


CREATE TABLE Device_tb(
                          id SERIAL PRIMARY KEY NOT NULL,
                          d_name VARCHAR(255) NOT NULL,
                          d_pin INT NOT NULL,
                          wifi_name VARCHAR(255) NOT NULL,
                          wifi_pwd VARCHAR(255) NOT NULL,
                          temperature INT DEFAULT 4,
                          CONSTRAINT Device_uk_1 UNIQUE (d_name),
                          CONSTRAINT Device_uk_2 UNIQUE (d_pin)
);

CREATE TABLE Camera_tb(
                          id SERIAL PRIMARY KEY NOT NULL,
                          image_desc VARCHAR(255) NOT NULL,
                          image_loc VARCHAR(255) NOT NULL,
                          device_name VARCHAR(255) NOT NULL,
                          things_found VARCHAR(255) DEFAULT NULL,
                          processed_img VARCHAR(255) DEFAULT NULL,
                          CONSTRAINT Camera_uk UNIQUE (image_desc),
                          CONSTRAINT Camera_Dev_fk
                              FOREIGN KEY (device_name) REFERENCES Device_tb (d_name) ON UPDATE CASCADE
);

CREATE TABLE User_tb(
                        id SERIAL PRIMARY KEY NOT NULL,
                        device_name VARCHAR(255) NOT NULL,
                        date_of_birth date NOT NULL,
                        u_email VARCHAR(255) NOT NULL,
                        u_name VARCHAR(255) NOT NULL,
                        CONSTRAINT User_uk_1 UNIQUE (u_email),
                        CONSTRAINT User_uk_2 UNIQUE (u_name),
                        CONSTRAINT User_Dev_fk
                            FOREIGN KEY (device_name) REFERENCES Device_tb (d_name) ON UPDATE CASCADE
);

CREATE TABLE Reviews_tb(
                           id SERIAL PRIMARY KEY NOT NULL,
                           description VARCHAR(255) NOT NULL,
                           device_name VARCHAR(255) NOT NULL,
                           user_email VARCHAR(255) NOT NULL,
                           CONSTRAINT Rev_Dev_fk
                               FOREIGN KEY (device_name) REFERENCES Device_tb (d_name) ON UPDATE CASCADE,
                           CONSTRAINT Rev_User_fk
                               FOREIGN KEY (user_email) REFERENCES User_tb (u_email) ON UPDATE CASCADE
);

CREATE TABLE ShoppingList_tb(
                                id SERIAL PRIMARY KEY NOT NULL,
                                item_name VARCHAR(255) NOT NULL,
                                quantity INT NOT NULL,
                                device_name VARCHAR(255) NOT NULL,
                                tag BOOLEAN DEFAULT FALSE, -- from inventory or not
                                CONSTRAINT ShopL_uk UNIQUE (item_name),
                                CONSTRAINT ShopL_Dev_fk
                                    FOREIGN KEY (device_name) REFERENCES Device_tb (d_name) ON UPDATE CASCADE
);

CREATE TABLE Inventory_tb(
                             id SERIAL PRIMARY KEY NOT NULL,
                             partition VARCHAR(255) NOT NULL,
                             item_name VARCHAR(255) NOT NULL,
                             quantity INT NOT NULL,
                             device_name VARCHAR(255) NOT NULL,
                             CONSTRAINT Inv_uk UNIQUE (partition),
                             CONSTRAINT Inv_Dev_fk
                                 FOREIGN KEY (device_name) REFERENCES Device_tb (d_name) ON UPDATE CASCADE
);

CREATE TABLE FoodCategory_tb(
                                id SERIAL PRIMARY KEY NOT NULL,
                                title VARCHAR(255) NOT NULL,
                                CONSTRAINT FoodCat_uk UNIQUE (title)
);

CREATE TABLE Recipes_tb(
                           id SERIAL PRIMARY KEY NOT NULL,
                           name VARCHAR(255) NOT NULL,
                           description VARCHAR(255) NOT NULL,
                           ingredient VARCHAR(255) NOT NULL, -- arraylist wrspt quantity
                           quantity VARCHAR(255) NOT NULL, -- arraylist wrspt ingredient
                           food_cat INT NOT NULL,
                           source VARCHAR(255) NOT NULL,
                           num_of_serv VARCHAR(255) NOT NULL,
                           prep_time VARCHAR(255) NOT NULL,
                           nutritional_vals VARCHAR(255) NOT NULL,
                           attachments VARCHAR(255) NOT NULL,
                           tag BOOLEAN DEFAULT FALSE, -- from inventory or not
                           CONSTRAINT Recipes_FoodCat_fk
                               FOREIGN KEY (food_cat) REFERENCES FoodCategory_tb (id) ON UPDATE CASCADE
);

CREATE TABLE User_Recipes_tb(
                                user_email VARCHAR(255) NOT NULL,
                                recipe_id INT NOT NULL,
                                CONSTRAINT UserRep_pk UNIQUE (user_email, recipe_id),
                                CONSTRAINT UserRep_User_fk
                                    FOREIGN KEY (user_email) REFERENCES User_tb (u_email) ON UPDATE CASCADE,
                                CONSTRAINT UserRep_Recipe_fk
                                    FOREIGN KEY (recipe_id) REFERENCES Recipes_tb (id) ON UPDATE CASCADE
);