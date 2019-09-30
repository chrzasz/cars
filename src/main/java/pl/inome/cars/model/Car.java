package pl.inome.cars.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private CarMark mark;

    @Length(min = 1, max = 32)
    private String model;

    @Enumerated(EnumType.ORDINAL)
    private CarColor color;

    public Car() {
    }


    public Car(CarMark mark, String model, CarColor color) {
        this.mark = mark;
        this.model = model;
        this.color = color;
    }


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CarMark getMark() {
        return mark;
    }

    public void setMark(CarMark mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CarColor getColor() {
        return color;
    }

    public void setColor(CarColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", mark=" + mark +
                ", model='" + model + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id.equals(car.id) &&
                mark == car.mark &&
                Objects.equals(model, car.model) &&
                color == car.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mark, model, color);
    }
}
