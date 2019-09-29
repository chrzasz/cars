package pl.inome.cars.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CarMark mark;

    @Length(min = 1, max = 32)
    private String model;

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

}
