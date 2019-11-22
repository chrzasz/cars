package pl.inome.cars.repository;

import org.springframework.data.repository.CrudRepository;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;

public interface CarRepository extends CrudRepository<Car, Long> {

    Iterable<Car> findByColor(CarColor color);

    Iterable<Car> findByMark(CarMark mark);

    Iterable<Car> findByModelIsContainingIgnoreCase(String model);

    Iterable<Car> findByProductionYearIsContaining(String year);

    Iterable<Car> findByProductionYearIsGreaterThanEqual(String year);

    Iterable<Car> findByProductionYearIsLessThanEqual(String year);

    Iterable<Car> findByProductionYearBetweenOrderByProductionYearDesc(String min, String max);

}
