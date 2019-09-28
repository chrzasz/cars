package pl.inome.cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.inome.cars.repository.CarRepository;

@RestController
@RequestMapping(path = "/api/car")
public class CarEntityController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping(path = "/all")
    public Iterable<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

}
