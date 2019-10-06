package pl.inome.cars.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.service.CarService;

import java.util.EnumSet;

public class CarEditForm extends FormLayout {

    private CarService carService;

    private TextField carModelTextField;
    private ComboBox<CarMark> carMarkComboBox;
    private ComboBox<CarColor> carColorComboBox;
    private Button saveButton;
    private Button deleteButton;
    private Button addButton;
    private Notification notification;
    private Binder<Car> binder;


    public CarEditForm(CarService carService) {
        this.carService = carService;


        // Create the fields
        notification = new Notification();
        notification.setDuration(3000);
        carModelTextField = new TextField("Model");
        carModelTextField.setPlaceholder("Model of the car");
        carModelTextField.setValueChangeMode(ValueChangeMode.EAGER);
        carMarkComboBox = new ComboBox<>("Mark");
        carMarkComboBox.setItems(EnumSet.allOf(CarMark.class));
        carColorComboBox = new ComboBox<>("Color");
        carColorComboBox.setItems(EnumSet.allOf(CarColor.class));
        saveButton = new Button("Save", VaadinIcon.CHECK.create());
        deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
        addButton = new Button("Add Car", VaadinIcon.PLUS.create());

        add(carMarkComboBox, carModelTextField, carColorComboBox);
        // Setting the desired responsive steps for the columns in the layout
        setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));


        // binding data
        binder = new Binder<>(Car.class);
        binder.forField(carModelTextField).bind(Car::getModel, Car::setModel);
        binder.forField(carMarkComboBox).bind(Car::getMark, Car::setMark);
        binder.forField(carColorComboBox).bind(Car::getColor, Car::setColor);

        saveButton.addClickListener(e -> saveCar());
        deleteButton.addClickListener(e -> deleteCar());
        addButton.addClickListener(e -> addCar());

    }

    private void addCar() {
        Car car = binder.getBean();
        if ((car.getModel() != null) &&
                (car.getColor() != null) &&
                (car.getMark() != null)) {
            car.setModel(carModelTextField.getValue());
            car.setId(0);
            carService.addCar(car);
            notification.setText("Added: " + car);
        } else {
            notification.setText("Not added. Select all required fields.");
        }
        notification.open();
    }

    public void setCar(Car car) {
        binder.setBean(car);
        if (car == null) {
            setVisible(false);
        } else {
            setVisible(true);
            carModelTextField.setValue(car.getModel());
            carModelTextField.focus();
        }
    }

    private void saveCar() {
        Car car = binder.getBean();
        if ((car.getModel() != null) &&
                (car.getColor() != null) &&
                (car.getMark() != null)) {
            car.setModel(carModelTextField.getValue());
            carService.updateCar(car.getId(), car);
            notification.setText("Saved");
        } else {
            notification.setText("Not saved. Select all required fields.");
        }
        notification.open();
    }


    private void deleteCar() {
        Car car = binder.getBean();
        try {
            carService.deleteCar(car.getId());
            notification.setText("Deleted" + car);
        } catch (Exception e) {
            notification.setText(e.toString());
        }
        notification.open();
    }

}
