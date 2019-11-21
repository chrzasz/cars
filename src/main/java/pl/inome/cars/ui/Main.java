package pl.inome.cars.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Route
public class Main extends VerticalLayout {

    private CarService carService;
    private Grid<Car> carGrid;
    private ComboBox<CarMark> carMarkComboBox;
    private ComboBox<CarColor> carColorComboBox;
    private TextField carModelTextField;
    private TextField carProductionYearField;
    private Button saveButton;
    private Button deleteButton;
    private Button addButton;
    private Button cancelButton;
    private Notification notification;
    private CustomConverter converter;
    private Car tmpCar;
    private List<Car> carList;
    private TextField minYearPicker;
    private TextField maxYearPicker;


    public Main(CarService carService) {
        this.carService = carService;
        tmpCar = new Car();

        // Create the fields
        notification = new Notification();
        notification.setDuration(1500);
        carModelTextField = new TextField("Model");
        carProductionYearField = new TextField("Year");
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
        minYearPicker = new TextField();
        maxYearPicker = new TextField();
        ;

        // Edit fields bar
        HorizontalLayout editFieldsLayout = new HorizontalLayout();
        editFieldsLayout.add(carMarkComboBox, carModelTextField, carColorComboBox, carProductionYearField);

        // Button bar
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonsLayout.add(addButton, saveButton, deleteButton, cancelButton);
        addButton.getStyle().set("mariginRight", "10px");
        saveButton.getStyle().set("mariginRight", "10px");
        deleteButton.getStyle().set("mariginRight", "10px");
        enableButtons(false);

        // Filters bar
        HorizontalLayout filtersLayout = new HorizontalLayout();
        minYearPicker.setPlaceholder("min Year");
        minYearPicker.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        minYearPicker.setClearButtonVisible(true);
        maxYearPicker.setPlaceholder("max Year");
        maxYearPicker.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        maxYearPicker.setClearButtonVisible(true);
        filtersLayout.add(minYearPicker, maxYearPicker);

        // Grid
        carGrid = new Grid<>(Car.class);
        converter = new CustomConverter();
        carList = converter.getListFromIteralbe(carService.getAllCars());
        carGrid.setHeightByRows(true);
        carGrid.removeColumnByKey("id");
        // The Grid<>(Car.class) sorts the properties and to reorder use 'setColumns' method
        carGrid.setColumns("mark", "model", "color", "productionYear");
        carGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        refreshGrid(carList);


        // Listeners
        addButton.addClickListener(e -> addCar());
        saveButton.addClickListener(e -> saveCar(tmpCar));
        deleteButton.addClickListener(e -> deleteCar(tmpCar));
        cancelButton.addClickListener(e -> enableButtons(false));
        minYearPicker.addValueChangeListener(e -> updateCarListByYearRange());
        maxYearPicker.addValueChangeListener(e -> updateCarListByYearRange());

        carGrid.addItemClickListener(e -> {
            enableButtons(true);
            tmpCar = e.getItem();
            carMarkComboBox.setValue(tmpCar.getMark());
            carModelTextField.setValue(tmpCar.getModel());
            carColorComboBox.setValue(tmpCar.getColor());
            carProductionYearField.setValue(tmpCar.getProductionYear());
            carModelTextField.focus();
        });


        // add fields to layout
        add(editFieldsLayout);
        add(buttonsLayout);
        add(filtersLayout);
        add(carGrid);
    }

    private void updateCarListByYearRange() {
        List<Car> cars = new ArrayList<>();
        if (!minYearPicker.isEmpty() && maxYearPicker.isEmpty())
            cars = converter.getListFromIteralbe(
                    carService.getCarsByYearIsGreaterThanEqual(minYearPicker.getValue()));
        else if (minYearPicker.isEmpty() && !maxYearPicker.isEmpty())
            cars = converter.getListFromIteralbe(
                    carService.getCarsByYearIsLessThanEqual(maxYearPicker.getValue()));
        else if (!minYearPicker.isEmpty() && !maxYearPicker.isEmpty())
            cars = converter.getListFromIteralbe(
                    carService.getCarsByYearRange(minYearPicker.getValue(), maxYearPicker.getValue()));
        else cars = converter.getListFromIteralbe(carService.getAllCars());

        refreshGrid(cars);
    }

    private void saveCar(Car car) {
        if ((carMarkComboBox.getValue() != null) &&
                (!carModelTextField.getValue().trim().isEmpty()) &&
                (carColorComboBox.getValue() != null) &&
                (!carProductionYearField.getValue().trim().isEmpty())) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue().trim());
            car.setColor(carColorComboBox.getValue());
            car.setProductionYear(carProductionYearField.getValue().trim());
            carService.updateCar(car.getId(), car);
            carList.set(carList.indexOf(car), car);
            refreshGrid(carList);
            enableButtons(false);
        } else {
            notification.setText("Not saved. Select all fields.");
            notification.open();
        }
    }

    private void deleteCar(Car car) {
        carService.deleteCar(car.getId());
        carList.remove(car);
        enableButtons(false);
        refreshGrid(carList);
    }


    private void addCar() {
        Car car = new Car();
        if ((carMarkComboBox.getValue() != null) &&
                (!carModelTextField.getValue().trim().isEmpty()) &&
                (carColorComboBox.getValue() != null) &&
                (!carProductionYearField.getValue().trim().isEmpty())) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue().trim());
            car.setColor(carColorComboBox.getValue());
            car.setProductionYear(carProductionYearField.getValue().trim());
            carService.addCar(car);
            carList.add(car);
            refreshGrid(carList);
        } else {
            notification.setText("Not added. Select all fields.");
            notification.open();
        }
    }

    private void refreshGrid(List<Car> carList) {
        carGrid.setItems(carList);
        carGrid.getDataProvider().refreshAll();
    }

    private void enableButtons(boolean enable) {
        saveButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
        cancelButton.setEnabled(enable);
        addButton.setEnabled(!enable);
    }


}
