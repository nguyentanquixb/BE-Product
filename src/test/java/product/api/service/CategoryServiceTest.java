package product.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import product.api.entity.Category;
import product.api.exception.NotFoundException;
import product.api.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findCategory_SuccessTest() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        Category foundCategory = categoryService.findCategory(1L);
        assertEquals("Test Category", foundCategory.getName());
    }

    @Test
    void findCategory_NotFoundTest() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> categoryService.findCategory(1L));

        assertEquals("Category not found", notFoundException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, notFoundException.getStatus());
    }
}
