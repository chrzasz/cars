package pl.inome.cars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.repository.CarRepository;

import java.util.Optional;

@Service
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Iterable<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Iterable<Car> getCarsByColor(CarColor color) {
        return carRepository.findByColor(color);
    }

    public Iterable<Car> getCarsByMark(CarMark mark) {
        return carRepository.findByMark(mark);
    }

    public Iterable<Car> getCarsByModel(String model) {
        return carRepository.findByModelIsContaining(model);
    }

    public Iterable<Car> getCarsByYear(String year) {
        return carRepository.findByProductionYearIsContaining(year);
    }

    public Iterable<Car> getCarsByYearIsGreaterThanEqual(String year) {
        return carRepository.findByProductionYearIsGreaterThanEqual(year);
    }

    public Iterable<Car> getCarsByYearIsLessThanEqual(String year) {
        return carRepository.findByProductionYearIsLessThanEqual(year);
    }

    public Iterable<Car> getCarsByYearRange(String min, String max) {
        return carRepository.findByYearRange(min, max);
    }

    public boolean addCar(Car car) {
        if (car.getColor() != null
                && car.getMark() != null
                && car.getModel() != null) {
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
