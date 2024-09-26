package com.starter.fullstack.rest;

import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.dao.InventoryDAO;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inventory Controller.
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {
  private final InventoryDAO inventoryDAO;

  /**
   * Default Constructor.
   * @param inventoryDAO inventoryDAO.
   */
  public InventoryController(InventoryDAO inventoryDAO) {
    Assert.notNull(inventoryDAO, "Inventory DAO must not be null.");
    this.inventoryDAO = inventoryDAO;
  }

  /**
   * Find Inventories.
   * @return List of Inventory.
   */
  @GetMapping
  public List<Inventory> findInventories() {
    return this.inventoryDAO.findAll();
  }

  /**
   * Create a new inventory
   * @param inventory - the Inventory object that is about to be created
   * @return the newly created Inventory Object
   */
  @PostMapping
  public Inventory create(@Valid @RequestBody @Param("Inventory") Inventory inventory) {
    return this.inventoryDAO.create(inventory);
  }

  /**
   * Deletes an Inventory object
   * @param id - the ID of the Inventory object to be deleted
   * @return the deleted Inventory object
   */
  @DeleteMapping
  public Optional<Inventory> delete(@Valid @RequestBody String id) {
    return this.inventoryDAO.delete(id);
  }
}

