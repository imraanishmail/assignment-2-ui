package com.ishmail;

import com.ishmail.entity.Store;
import com.ishmail.interceptor.Logged;
import com.ishmail.inventory.InventoryService;
import com.ishmail.entity.Inventory;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/*
* contained throughout all of the pages and is needed when implementing serializable in order to pass objects to pages.
* lasts the lifetime of the session
 */
@SessionScoped
@Named // from the jsf we can access the bean
public class StoreAppBean implements Serializable {

    private Long id;
    // denotes character length and that this field cannot be empty
    @Size(min = 3, max = 20)
    @NotEmpty
    private String name;

    // denotes character length and that this field cannot be empty
    @Size(min = 3, max = 20)
    @NotEmpty
    private String sport;
    private int quantity;
    private double unitPrice;
    private Date dateUpdated;

    private String storeName;
    private String location;
    private List<Inventory> inventoryList;

    @EJB // handles database transactions.  Helps to eliminate boilerplate code
    private InventoryService inventoryService;

    public List<Inventory> getInventoryList() {
        return inventoryService.getInventoryList();
    }

    // method to add an item to the inventory
    @Logged
    public void addItem() {
        Inventory inventory = new Inventory(id, name, sport, quantity, unitPrice, dateUpdated);
        Optional<Inventory> inventoryExists = inventoryService.getInventoryList().stream()
                .filter(i -> i.getName().equals(name) &&
                             i.getSport().equals(sport) &&
                             i.getUnitPrice() == (unitPrice)).findFirst();

        if (inventoryExists.isPresent()) {
            inventoryService.removeFromInventory(inventory);
            inventoryService.addToInventory(inventory);
        }
        else {
            inventoryService.addToInventory(inventory);
        }
        clearFields();
    }

    // method to remove item from inventory
    @Logged
    public void removeItem(Inventory inventory) {
        inventoryService.removeFromInventory(inventory);
    }

    // will clear all fields
    private void clearFields() {
        setId(null);
        setName("");
        setSport("");
        setQuantity(0);
        setUnitPrice(0.0);
        setDateUpdated(null);
    }

    // creation of store
    public String createStore() {
        Store store = new Store(id, storeName, location, inventoryList);
        store.getInventoryList();
        return "inventory";
    }

    // Accessors and Mutators
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }
}
