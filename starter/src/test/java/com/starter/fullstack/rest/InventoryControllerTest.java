package com.starter.fullstack.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starter.fullstack.api.Inventory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InventoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private Inventory inventory;

  @Before
  public void setup() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("ID");
    this.inventory.setName("TEST");
    this.mongoTemplate.save(this.inventory);
  }

  @After
  public void teardown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  /**
   * Test findAll endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void findAll() throws Throwable {
    this.mockMvc.perform(get("/inventory")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[" + this.objectMapper.writeValueAsString(inventory) + "]"));
  }

  /**
   * Test create endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void create() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("ID 2");
    this.inventory.setName("INVENTORY 2");
    this.inventory.setProductType("Product type 2");

    this.mockMvc.perform(post("/inventory")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(this.inventory)))
            .andExpect(status().isOk());

    List<Inventory> inventoryList = this.mongoTemplate.findAll(Inventory.class);

    //checks if the inventory size is now 2 (1 from setup, 1 from test)
    Assert.assertEquals(2, inventoryList.size());

    //check if the names are the same
    Assert.assertEquals("INVENTORY 2", inventoryList.get(1).getName());

    //check if the product types are the same
    Assert.assertEquals("Product type 2", inventoryList.get(1).getProductType());

    //check if the IDs are different
    Assert.assertNotEquals("ID 2", inventoryList.get(1).getId());
  }

  /**
   * Test remove endpoint.
   * @throws Throwable see MockMvc
   */
  @Test
  public void remove() throws Throwable{
    this.mockMvc.perform(delete("/inventory")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mongoTemplate.findAll(Inventory.class).get(0).getId()))
            .andExpect(status().isOk());

    // check the inventory size after the deletion
    List<Inventory> inventoryList = this.mongoTemplate.findAll(Inventory.class);
    Assert.assertEquals(0, inventoryList.size());
  }
}