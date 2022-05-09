package entities;


import dto.CarCategoryUrls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarCategoryUrlRepository  extends JpaRepository<CarCategoryUrls, Long> {

}
