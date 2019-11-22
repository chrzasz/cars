package pl.inome.cars.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;

public interface CarRepository extends CrudRepository<Car, Long> {

    Iterable<Car> findByColor(CarColor color);

    Iterable<Car> findByMark(CarMark mark);

    //    @Query("select c from Car c where lower(c.model) like lower(concat('%', :model, '%') )")
    Iterable<Car> findByModelIsContainingIgnoreCase(String model);

    Iterable<Car> findByProductionYearIsContaining(String year);

    Iterable<Car> findByProductionYearIsGreaterThanEqual(String year);

    Iterable<Car> findByProductionYearIsLessThanEqual(String year);

//    @Query(value = "SELECT * FROM car WHERE car.production_year >= :min AND car.production_year <= :max", nativeQuery = true)
//    Iterable<Car> findByYearRange(@Param("min") String min, @Param("max") String max);

    Iterable<Car> findByProductionYearBetweenOrderByProductionYearDesc(String min, String max);

    @Query("select c from Car c where " +
            "c.mark like :carMark and " +
            "lower(c.model) like lower(concat('%', :model, '%') ) and " +
            "c.color like :color and " +
            "c.productionYear>= :yearMin and " +
            "c.productionYear<=:yearMax")
    Iterable<Car> filter(CarMark carMark, String model, CarColor color, String yearMin, String yearMax);


}
