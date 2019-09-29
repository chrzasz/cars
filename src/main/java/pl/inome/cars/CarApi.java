package pl.inome.cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
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
    public ResponseEntity<Iterable<Car>> getCars() {
        return new ResponseEntity<>(carRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> first = carRepository.findById(id);
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "/color/{color}")
    public ResponseEntity<Iterable<Car>> getCarByColor(@PathVariable CarColor color) {
        Iterable<Car> carsColored = carRepository.findByColor(color);
        if (!carsColored.iterator().hasNext()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(carsColored, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car newCar) {
        if (carRepository.equals(newCar)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } else {
            carRepository.save(newCar);
            return new ResponseEntity(HttpStatus.OK);
        }
    }
//
//    @PutMapping
//    public ResponseEntity modifyCar(@RequestBody Car newCar) {
//        Optional<Car> first = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
//        if (first.isPresent()) {
//            carList.remove(first.get());
//            carList.add(newCar);
//            return new ResponseEntity(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity removeCar(@PathVariable long id) {
//        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
//        if (first.isPresent()) {
//            carList.remove(first.get());
//            return new ResponseEntity<>(first.get(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity modifyCarById(@PathVariable long id,
//                                        @RequestParam(required = false) CarMark mark,
//                                        @RequestParam(required = false) String model,
//                                        @RequestParam(required = false) CarColor color) {
//
//        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
//        if (first.isPresent()) {
//            Car newCar = first.get();
//            if (mark != null) newCar.setMark(mark);
//            if (model != null) newCar.setModel(model);
//            if (color != null) newCar.setColor(color);
//            carList.remove(first.get());
//            carList.add(newCar);
//            return new ResponseEntity(HttpStatus.ACCEPTED);
//        }
//        return new ResponseEntity(HttpStatus.NOT_FOUND);
//    }

}
