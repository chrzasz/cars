package pl.inome.cars;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.repository.CarRepository;

import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarApi {

    CarRepository carRepository;

    @Autowired
    public CarApi(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "get all available cars")
    public ResponseEntity<Iterable<Car>> getCars() {
        return new ResponseEntity<>(carRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "get car by Id")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> first = carRepository.findById(id);
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/mark/{mark}")
    @ApiOperation(value = "get all cars by mark")
    public ResponseEntity<Iterable<Car>> getCarByMark(@PathVariable CarMark mark) {
        if (!carRepository.findByMark(mark).iterator().hasNext())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(carRepository.findByMark(mark), HttpStatus.OK);
    }

    @GetMapping(value = "/model/{model}")
    @ApiOperation(value = "get all cars by model")
    public ResponseEntity<Iterable<Car>> getCarByModel(@PathVariable String model) {
        if (!carRepository.findByModelIsContaining(model).iterator().hasNext())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(carRepository.findByModelIsContaining(model), HttpStatus.OK);
    }

    @GetMapping(value = "/color/{color}")
    @ApiOperation(value = "get all cars by color")
    public ResponseEntity<Iterable<Car>> getCarByColor(@PathVariable CarColor color) {
        Iterable<Car> carsColored = carRepository.findByColor(color);
        if (!carsColored.iterator().hasNext()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(carsColored, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "add/update car (ID is not required for new car)")
    public Car addCar(@RequestBody Car newCar) {
        carRepository.save(newCar);
        return newCar;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "update car ")
    public Car updateCar(@RequestBody Car newCar) {
        carRepository.save(newCar);
        return newCar;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeCar(@PathVariable Long id) {
        Optional<Car> first = carRepository.findById(id);
        if (first.isPresent()) {
            carRepository.delete(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modifyCarById(@PathVariable long id,
                                        @RequestParam(required = false) CarMark mark,
                                        @RequestParam(required = false) String model,
                                        @RequestParam(required = false) CarColor color) {

        Optional<Car> first = carRepository.findById(id);
        if (first.isPresent()) {
            Car newCar = first.get();
            if (mark != null) newCar.setMark(mark);
            if (model != null) newCar.setModel(model);
            if (color != null) newCar.setColor(color);
            carRepository.delete(first.get());
            carRepository.save(newCar);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
