package pl.inome.cars.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
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
    private Button toggleButton;
    private Notification notification;
    private CustomConverter converter;
    private Car tmpCar;
    private List<Car> carList;
    private ComboBox<CarMark> carMarkPicker;
    private TextField minYearPicker;
    private TextField maxYearPicker;
    private Accordion accordion;

    public Main(CarService carService) {
        this.carService = carService;
        converter = new CustomConverter();
        tmpCar = new Car();
        carList = new ArrayList<>();
        getAllCars();


        // Create general fields
        notification = new Notification();
        notification.setDuration(1500);
        accordion = new Accordion();
        accordion.close();

        // MODIFY ACCORDION
        FormLayout editFieldsLayout = new FormLayout();
        carModelTextField = new TextField("Model");
        carModelTextField.setPlaceholder("Model of the car");
        carModelTextField.setValueChangeMode(ValueChangeMode.EAGER);
        carModelTextField.setAutoselect(true);
        carProductionYearField = new TextField("Year");
        carProductionYearField.setPlaceholder("Production year");
        carProductionYearField.setValueChangeMode(ValueChangeMode.EAGER);
        carProductionYearField.setAutoselect(true);
        carMarkComboBox = new ComboBox<>("Mark");
        carMarkComboBox.setItems(EnumSet.allOf(CarMark.class));
        carColorComboBox = new ComboBox<>("Color");
        carColorComboBox.setItems(EnumSet.allOf(CarColor.class));
        addButton = new Button("Add Car", VaadinIcon.PLUS.create(), e -> addCar());
        saveButton = new Button("Save", VaadinIcon.CHECK.create(), e -> saveCar(tmpCar));
        deleteButton = new Button("Delete", VaadinIcon.TRASH.create(), e -> deleteCar(tmpCar));
        cancelButton = new Button("Esc", VaadinIcon.EXIT_O.create(), e -> enableButtons(false));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.getStyle().set("mariginRight", "10px");
        saveButton.getStyle().set("mariginRight", "10px");
        deleteButton.getStyle().set("mariginRight", "10px");
        toggleButton = new Button(VaadinIcon.CIRCLE.create(), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });
        editFieldsLayout.add(carMarkComboBox, carModelTextField, carColorComboBox, carProductionYearField, addButton, saveButton, deleteButton, cancelButton, toggleButton);
        editFieldsLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("12em", 1),
                new FormLayout.ResponsiveStep("18em", 2),
                new FormLayout.ResponsiveStep("24em", 3),
                new FormLayout.ResponsiveStep("30em", 4),
                new FormLayout.ResponsiveStep("36em", 5));
        enableButtons(false);
        accordion.add("Modify", editFieldsLayout).addThemeVariants(DetailsVariant.FILLED);
        ;

        // FILTERS ACCORDION
        FormLayout filtersLayout = new FormLayout();
        carMarkPicker = new ComboBox<>("mark");
        carMarkPicker.setItems(EnumSet.allOf(CarMark.class));
        carMarkPicker.addValueChangeListener(e -> filterCarList());
        minYearPicker = new TextField("min year", e -> updateCarListByYearRange());
        maxYearPicker = new TextField("max year", e -> updateCarListByYearRange());
        minYearPicker.setPlaceholder("filter");
        maxYearPicker.setPlaceholder("filter");
        minYearPicker.setValueChangeMode(ValueChangeMode.EAGER);
        maxYearPicker.setValueChangeMode(ValueChangeMode.EAGER);
        minYearPicker.setClearButtonVisible(true);
        maxYearPicker.setClearButtonVisible(true);
        filtersLayout.add(minYearPicker, maxYearPicker, carMarkPicker);
        filtersLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("12em", 1),
                new FormLayout.ResponsiveStep("18em", 2),
                new FormLayout.ResponsiveStep("24em", 3));

        accordion.add("Filters", filtersLayout).addThemeVariants(DetailsVariant.FILLED);

        // GRID
        carGrid = new Grid<>(Car.class);
        carGrid.setHeightByRows(true);
        carGrid.removeColumnByKey("id");
        // The Grid<>(Car.class) sorts the properties and to reorder use 'setColumns' method
        carGrid.setColumns("mark", "model", "color", "productionYear");
        carGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        refreshGrid(carList);

        // GRID Listeners
        carGrid.addItemClickListener(e -> {
            enableButtons(true);
            tmpCar = e.getItem();
            carMarkComboBox.setValue(tmpCar.getMark());
            carModelTextField.setValue(tmpCar.getModel());
            carColorComboBox.setValue(tmpCar.getColor());
            carProductionYearField.setValue(tmpCar.getProductionYear());
            carModelTextField.focus();
            accordion.open(0);
        });


        // add fields to layout
        add(accordion);
        add(carGrid);
    }

    private void filterCarList() {

        if (carMarkPicker.getValue() != null &&
                minYearPicker.getValue().trim().isEmpty() &&
                maxYearPicker.getValue().trim().isEmpty()) {
            carList = converter.getListFromIteralbe(carService.getCarsByMark(carMarkPicker.getValue()));
        } else {
            getAllCars();
        }
        refreshGrid(carList);

    }


    private void updateCarListByYearRange() {
        if (!minYearPicker.isEmpty() && minYearPicker.getValue().length() <= 4 && maxYearPicker.isEmpty()) {
            carList = converter.getListFromIteralbe(
                    carService.getCarsByYearIsGreaterThanEqual(minYearPicker.getValue()));
        } else if (minYearPicker.isEmpty() && !maxYearPicker.isEmpty() && maxYearPicker.getValue().length() <= 4) {
            carList = converter.getListFromIteralbe(
                    carService.getCarsByYearIsLessThanEqual(maxYearPicker.getValue()));
        } else if (!minYearPicker.isEmpty() && minYearPicker.getValue().length() <= 4 &&
                !maxYearPicker.isEmpty() && maxYearPicker.getValue().length() <= 4) {
            carList = converter.getListFromIteralbe(
                    carService.getCarsByYearRange(minYearPicker.getValue(), maxYearPicker.getValue()));
        } else getAllCars();
        refreshGrid(carList);
    }

    private void saveCar(Car car) {
        if ((carMarkComboBox.getValue() != null) &&
                (!carModelTextField.getValue().trim().isEmpty()) &&
                (carColorComboBox.getValue() != null) &&
                (!carProductionYearField.getValue().trim().isEmpty()) &&
                (carProductionYearField.getValue().length() <= 4)) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue().trim());
            car.setColor(carColorComboBox.getValue());
            car.setProductionYear(carProductionYearField.getValue().trim());
            carService.updateCar(car.getId(), car);
            carList.set(carList.indexOf(car), car);
            refreshGrid(carList);
            enableButtons(false);
            accordion.close();
        } else {
            notification.setText("Not saved. Select all fields.");
            notification.open();
        }
    }

    private void deleteCar(Car car) {
        carService.deleteCar(car.getId());
        carList.remove(car);
        refreshGrid(carList);
        enableButtons(false);
        accordion.close();
    }

    private void addCar() {
        Car car = new Car();
        if ((carMarkComboBox.getValue() != null) &&
                (!carModelTextField.getValue().trim().isEmpty()) &&
                (carColorComboBox.getValue() != null) &&
                (!carProductionYearField.getValue().trim().isEmpty()) &&
                (carProductionYearField.getValue().length() <= 4)) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue().trim());
            car.setColor(carColorComboBox.getValue());
            car.setProductionYear(carProductionYearField.getValue().trim());
            if (carService.addCar(car)) carList.add(car);
            refreshGrid(carList);
            accordion.close();
        } else {
            notification.setText("Not added. Select all fields.");
            notification.open();
        }
    }

    private void getAllCars() {
        carList = converter.getListFromIteralbe(carService.getAllCars());
    }

    private void refreshGrid(List<Car> carList) {
        carGrid.setItems(carList);
        carGrid.getDataProvider().refreshAll();
    }

    private void clearFilters() {
        getAllCars();
        carMarkPicker.clear();
        minYearPicker.clear();
        maxYearPicker.clear();
        accordion.close();
    }

    private void enableButtons(boolean enable) {
        saveButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
        cancelButton.setEnabled(enable);
        addButton.setEnabled(!enable);
    }

}
