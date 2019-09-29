package pl.inome.cars.repository;

import org.springframework.data.repository.CrudRepository;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;

import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, Long> {

    Optional<Car> findById(Long id);

    Iterable<Car> findByColor(CarColor color);

}
