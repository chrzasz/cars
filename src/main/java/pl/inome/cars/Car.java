package pl.inome.cars;

public class Car {

    private long id;
    private CarMark mark;
    private String model;
    private CarColor color;

    public Car() {
    }

    public Car(long id, CarMark mark, String model, CarColor color) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }

    public long getId() {
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

    public static void main(String[] args) {

        Car car = new Car();
        car.setId(1L);
        car.setMark(CarMark.AUDI);
        car.setModel("A3");
        car.setColor(CarColor.BLACK);

        System.out.println(car);

        CarMark carMark1 = CarMark.AUDI;
        System.out.println(carMark1);

        for (CarMark carMark : CarMark.values()) {
            System.out.println(carMark.ordinal() + " " + carMark);

        }
    }
}
