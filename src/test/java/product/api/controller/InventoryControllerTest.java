package product.api.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import product.api.filters.JwtFilter;
import product.api.response.ProductResponse;
import product.api.service.InventoryService;
import product.api.utils.JwtUtil;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    @WithMockUser(authorities = "VIEW_INVENTORY")
    public void getProductsByWarehouseTest() throws Exception {
        List<ProductResponse> productResponseList = new ArrayList<>();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("product1");
        productResponse.setQuantity(10);
        productResponseList.add(productResponse);

        when(inventoryService.getProductsByWarehouse(anyLong(), anyString()))
                .thenReturn(productResponseList);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/inventory")
                        .param("warehouseId", "1")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("product1"))
                .andExpect(jsonPath("$.data[0].quantity").value(10));
    }
}