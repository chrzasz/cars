package pl.inome.cars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.repository.CarRepository;
import pl.inome.cars.utils.CustomConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private CarRepository carRepository;
    private CustomConverter converter;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
        this.converter = new CustomConverter();
    }

    public List<Car> getAllCars() {
        return converter.getListFromIterable(carRepository.findAll());
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> getCarsByColor(CarColor color) {
        return converter.getListFromIterable(carRepository.findByColor(color));
    }

    public List<Car> getCarsByMark(CarMark mark) {
        return converter.getListFromIterable(carRepository.findByMark(mark));
    }

    public List<Car> getCarsByModel(String model) {
        return converter.getListFromIterable(carRepository.findByModelIsContainingIgnoreCase(model));
    }

    public List<Car> getCarsByYear(String year) {
        return converter.getListFromIterable(carRepository.findByProductionYearIsContaining(year));
    }

    public List<Car> getCarsByYearIsGreaterThanEqual(String year) {
        return converter.getListFromIterable(carRepository.findByProductionYearIsGreaterThanEqual(year));
    }

    public List<Car> getCarsByYearIsLessThanEqual(String year) {
        return converter.getListFromIterable(carRepository.findByProductionYearIsLessThanEqual(year));
    }

    public List<Car> getCarsByYearRange(String min, String max) {
        return converter.getListFromIterable(carRepository.findByProductionYearBetweenOrderByProductionYearDesc(min, max));
    }

    public boolean addCar(Car car) {
        if (car.getColor() != null
                && car.getMark() != null
                && car.getModel() != null
                && car.getProductionYear() != null
                && car.getProductionYear().length() <= 4) {
            carRepository.save(car);
            return true;
        }
        return false;
    }

    public boolean updateCar(Long id, Car car) {
        Optional<Car> first = carRepository.findById(id);
        if (first.isPresent()) {
            if (car.getMark() != null) first.get().setMark(car.getMark());
            if (car.getModel() != null) first.get().setModel(car.getModel());
            if (car.getColor() != null) first.get().setColor(car.getColor());
            if (car.getProductionYear() != null && car.getProductionYear().length() <= 4)
                first.get().setProductionYear(car.getProductionYear());
            first.get().setLastEdited(Timestamp.valueOf(LocalDateTime.now()));
            carRepository.save(first.get());
            return true;
        }
        return false;
    }

    public boolean deleteCar(Long id) {
        if (carRepository.findById(id).isPresent()) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
