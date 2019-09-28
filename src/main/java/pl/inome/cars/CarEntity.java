package pl.inome.cars;

import javax.persistence.*;

@Entity
@Table(name = "car")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CarMark mark;
    private String model;
    private CarColor color;

    public CarEntity() {
    }


    public CarEntity(CarMark mark, String model, CarColor color) {
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
