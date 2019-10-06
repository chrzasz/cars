package pl.inome.cars.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.service.CarService;
import pl.inome.cars.utils.CustomConverter;

import java.util.EnumSet;

@Route
public class Main extends VerticalLayout {

    private CarService carService;
    private Grid<Car> carGrid;
    private TextField carModelTextField;
    private ComboBox<CarMark> carMarkComboBox;
    private ComboBox<CarColor> carColorComboBox;
    private Button saveButton;
    private Button deleteButton;
    private Button addButton;
    private Button cancelButton;
    private Notification notification;
    private CustomConverter converter;
    private Car tmpCar;


    public Main(CarService carService) {
        this.carService = carService;
        converter = new CustomConverter();
        tmpCar = new Car();

        // Create the fields
        notification = new Notification();
        notification.setDuration(1500);
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
        cancelButton = new Button("Esc", VaadinIcon.EXIT_O.create());

        // Edit fields bar
        HorizontalLayout editFieldsLayout = new HorizontalLayout();
        editFieldsLayout.add(carMarkComboBox, carModelTextField, carColorComboBox);

        // Button bar
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonsLayout.add(addButton, saveButton, deleteButton, cancelButton);
        addButton.getStyle().set("mariginRight", "10px");
        saveButton.getStyle().set("mariginRight", "10px");
        deleteButton.getStyle().set("mariginRight", "10px");
        enableButtons(false);

        // Grid
        carGrid = new Grid<>();
        carGrid.setHeightByRows(true);
        refreshGrid();
        carGrid.addColumn(Car::getId).setHeader("ID");
        carGrid.addColumn(Car::getMark).setHeader("Mark");
        carGrid.addColumn(Car::getModel).setHeader("Model");
        carGrid.addColumn(Car::getColor).setHeader("Color");

        // Listeners
        addButton.addClickListener(e -> addCar());
        saveButton.addClickListener(e -> saveCar(tmpCar));
        deleteButton.addClickListener(e -> deleteCar(tmpCar));
        cancelButton.addClickListener(e -> enableButtons(false));

        carGrid.addItemClickListener(e -> {
            enableButtons(true);
            tmpCar = e.getItem();
            carMarkComboBox.setValue(tmpCar.getMark());
            carModelTextField.setValue(tmpCar.getModel());
            carColorComboBox.setValue(tmpCar.getColor());
            carModelTextField.focus();
        });

        // add fields to layout
        add(editFieldsLayout);
        add(buttonsLayout);
        add(carGrid);

    }

    private void saveCar(Car car) {
        if ((carMarkComboBox.getValue() != null) &&
                (!carModelTextField.getValue().trim().isEmpty()) &&
                (carColorComboBox.getValue() != null)) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue());
            car.setColor(carColorComboBox.getValue());
            carService.updateCar(car.getId(), car);
            refreshGrid();
            enableButtons(false);
        } else {
            notification.setText("Not saved. Select all fields.");
            notification.open();
        }
    }

    private void deleteCar(Car car) {
        carService.deleteCar(car.getId());
        enableButtons(false);
        refreshGrid();
    }


    private void addCar() {
        Car car = new Car();
        if ((carMarkComboBox.getValue() != null) &&
                (!carModelTextField.getValue().trim().isEmpty()) &&
                (carColorComboBox.getValue() != null)) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue());
            car.setColor(carColorComboBox.getValue());
            carService.addCar(car);
            refreshGrid();
        } else {
            notification.setText("Not added. Select all fields.");
            notification.open();
        }
    }

    private void refreshGrid() {
        carGrid.setItems(converter.getListFromIteralbe(carService.getAllCars()));
        carGrid.getDataProvider().refreshAll();
    }

    private void enableButtons(boolean enable) {
        saveButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
        cancelButton.setEnabled(enable);
        addButton.setEnabled(!enable);
    }


}
