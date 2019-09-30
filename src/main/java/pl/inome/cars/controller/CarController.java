package pl.inome.cars.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.service.CarService;

import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Car>> getCars() {
        if (!carService.getAllCars().iterator().hasNext())
            return new ResponseEntity<>(carService.getAllCars(), HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "get car by Id")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> first = carService.getCarById(id);
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}")
    @ApiOperation(value = "get all cars by color")
    public ResponseEntity<Iterable<Car>> getCarByColor(@PathVariable CarColor color) {
        Iterable<Car> carsColored = carService.getCarsByColor(color);
        if (!carsColored.iterator().hasNext()) return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity(carsColored, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "add/update car (ID is not required for adding new car)")
    public ResponseEntity<Car> addCar(@RequestBody Car newCar) {
        return (carService.addCar(newCar)) ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    @ApiOperation(value = "update car")
    public ResponseEntity updateCar(@RequestBody Car car) {
        return (carService.updateCar(car.getId(), car))
                ? new ResponseEntity<>(HttpStatus.ACCEPTED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping
    @ApiOperation(value = "update car")
    public ResponseEntity modifyCarById(@PathVariable long id,
                                        @RequestBody Car car) {
        Optional<Car> first = carService.getCarById(id);
        if (first.isPresent()) {
            carService.updateCar(id, car);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
