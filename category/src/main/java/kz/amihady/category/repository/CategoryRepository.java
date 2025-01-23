package kz.amihady.category.repository;


import kz.amihady.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByUserIdAndCategoryName(Long userId, String categoryName);
    List<Category> findByUserId(Long userId);
}
