package pl.inome.cars;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarApi {

    List<Car> carList;

    @GetMapping
    public String getCars() {
        return "Car with GET";
    }

    @PostMapping
    public String addCar() {
        return "Car added with POST";
    }

    @PutMapping
    public String modifyCar() {
        return "Car modified with PUT";
    }

    @DeleteMapping
    public String removeCar() {
        return "Car removed with DELETE";
    }
}
