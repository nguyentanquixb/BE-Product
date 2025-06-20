package product.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import product.api.entity.Category;
import product.api.exception.NotFoundException;
import product.api.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Category not found"));
    }
}
