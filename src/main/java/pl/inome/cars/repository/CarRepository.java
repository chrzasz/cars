package pl.inome.cars.repository;

import org.springframework.data.repository.CrudRepository;
import pl.inome.cars.CarEntity;

import java.awt.*;

public interface CarRepository extends CrudRepository<CarEntity, Long> {

    CarEntity findByColor(Color color);

}
