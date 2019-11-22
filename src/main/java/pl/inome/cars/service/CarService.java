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
        return converter.getListFromIteralbe(carRepository.findAll());
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> getCarsByColor(CarColor color) {
        return converter.getListFromIteralbe(carRepository.findByColor(color));
    }

    public List<Car> getCarsByMark(CarMark mark) {
        return converter.getListFromIteralbe(carRepository.findByMark(mark));
    }

    public List<Car> getCarsByModel(String model) {
        return converter.getListFromIteralbe(carRepository.findByModelIsContainingIgnoreCase(model));
    }

    public List<Car> getCarsByYear(String year) {
        return converter.getListFromIteralbe(carRepository.findByProductionYearIsContaining(year));
    }

    public List<Car> getCarsByYearIsGreaterThanEqual(String year) {
        return converter.getListFromIteralbe(carRepository.findByProductionYearIsGreaterThanEqual(year));
    }

    public List<Car> getCarsByYearIsLessThanEqual(String year) {
        return converter.getListFromIteralbe(carRepository.findByProductionYearIsLessThanEqual(year));
    }

    public List<Car> getFilteredCars(CarMark carMark, String model, CarColor color, String yearMin, String yearMax) {
        return converter.getListFromIteralbe(carRepository.filter(carMark, model, color, yearMin, yearMax));
    }

    public List<Car> getCarsByYearRange(String min, String max) {
        return converter.getListFromIteralbe(carRepository.findByProductionYearBetweenOrderByProductionYearDesc(min, max));
    }

    public boolean addCar(Car car) {
        if (car.getColor() != null
                && car.getMark() != null
                && car.getModel() != null
                && car.getProductionYear() != null
                && car.getProductionYear().length() <= 4) {
            car.setCreated(Timestamp.valueOf(LocalDateTime.now()));
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
