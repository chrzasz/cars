package pl.inome.cars.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;

public interface CarRepository extends CrudRepository<Car, Long> {

    Iterable<Car> findByColor(CarColor color);

    Iterable<Car> findByMark(CarMark mark);

    Iterable<Car> findByModelIsContaining(String model);

    Iterable<Car> findByProductionYearIsContaining(String year);

    Iterable<Car> findByProductionYearIsGreaterThanEqual(String year);

    Iterable<Car> findByProductionYearIsLessThanEqual(String year);

    @Query(value = "SELECT * FROM car WHERE car.production_year >= :min AND car.production_year <= :max", nativeQuery = true)
    Iterable<Car> findByYearRange(String min, String max);
}
