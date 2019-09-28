package pl.inome.cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.inome.cars.repository.CarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cars")
public class CarApi {

    List<Car> carList;

    CarRepository carRepository;

    @Autowired
    public CarApi(CarRepository carRepository) {

        this.carList = new ArrayList<>();
        carList.add(new Car(1L, CarMark.AUDI, "A3", CarColor.black));
        carList.add(new Car(2L, CarMark.BMW, "X3", CarColor.white));
        carList.add(new Car(3L, CarMark.WV, "Golf", CarColor.red));
    }


    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Car>> getCars() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/color/{color}")
    public ResponseEntity<List<Car>> getCarByColor(@PathVariable CarColor color) {

        List<Car> carsColored = carList.stream()
                .filter(car -> car.getColor() == color)
                .collect(Collectors.toList());

        if (carsColored.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(carsColored, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car newCar) {
        boolean add = carList.add(newCar);
        if (add) {
            return new ResponseEntity(HttpStatus.CREATED);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity modifyCar(@RequestBody Car newCar) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            carList.add(newCar);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeCar(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modifyCarById(@PathVariable long id,
                                        @RequestParam(required = false) CarMark mark,
                                        @RequestParam(required = false) String model,
                                        @RequestParam(required = false) CarColor color) {

        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            Car newCar = first.get();
            if (mark != null) newCar.setMark(mark);
            if (model != null) newCar.setModel(model);
            if (color != null) newCar.setColor(color);
            carList.remove(first.get());
            carList.add(newCar);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
