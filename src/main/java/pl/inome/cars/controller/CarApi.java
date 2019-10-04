package pl.inome.cars.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.service.CarService;

@RestController
@RequestMapping("/api/v1/cars")
public class CarApi {

    private CarService carService;

    @Autowired
    public CarApi(CarService carService) {
        this.carService = carService;
    }

    // get

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "get all available cars")
    public ResponseEntity<Iterable<Car>> getCars() {
        if (carService.getAllCars().iterator().hasNext())
            return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "get car by ID")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id).map(car ->
                new ResponseEntity<>(car, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/color/{color}")
    @ApiOperation(value = "get all cars by color")
    public ResponseEntity<Iterable<Car>> getCarByColor(@PathVariable CarColor color) {
        if (carService.getCarsByColor(color).iterator().hasNext())
            return new ResponseEntity<>(carService.getCarsByColor(color), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/mark/{mark}")
    @ApiOperation(value = "get all cars by mark")
    public ResponseEntity<Iterable<Car>> getCarByMark(@PathVariable CarMark mark) {
        if (carService.getCarsByMark(mark).iterator().hasNext())
            return new ResponseEntity<>(carService.getCarsByMark(mark), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/model/{model}")
    @ApiOperation(value = "get all cars by model")
    public ResponseEntity<Iterable<Car>> getCarByModel(@PathVariable String model) {
        if (carService.getCarsByModel(model).iterator().hasNext())
            return new ResponseEntity<>(carService.getCarsByModel(model), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // add

    @PostMapping
    @ApiOperation(value = "add/update car (ID is not required for adding new car)")
    public ResponseEntity<Car> addCar(@RequestBody Car newCar) {
        return (carService.addCar(newCar))
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // update

    @PutMapping
    @ApiOperation(value = "update car")
    public ResponseEntity updateCar(@RequestBody Car car) {
        return (carService.updateCar(car.getId(), car))
                ? new ResponseEntity(HttpStatus.ACCEPTED)
                : new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "update car by ID")
    public ResponseEntity modifyCarById(@PathVariable long id,
                                        @RequestBody Car car) {
        return (carService.updateCar(id, car))
                ? new ResponseEntity(HttpStatus.ACCEPTED)
                : new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    // delete

    @DeleteMapping("/{id}")
    @ApiOperation(value = "remove car by ID")
    public ResponseEntity removeCarById(@PathVariable Long id) {
        return (carService.deleteCar(id))
                ? new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
