package pl.inome.cars.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.service.CarService;
import pl.inome.cars.utils.CustomConverter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route
public class TestLayout extends VerticalLayout {

    private CarService carService;
    private Grid<Car> carGrid;
    private TextField carModelTextField;
    private ComboBox<CarMark> carMarkComboBox;
    private ComboBox<CarColor> carColorComboBox;
    //    private TextField filterCarModelTextField;
//    private ComboBox<CarMark> filterCarMarkComboBox;
//    private ComboBox<CarColor> filterCarColorComboBox;
    private Button saveButton;
    private Button deleteButton;
    private Button addButton;
    private Notification notification;
    private CustomConverter converter;
    //    private ListDataProvider<Car> dataProvider;
//    private Binder<Car> binder;
    private Car tmpCar;


    public TestLayout(CarService carService) {
        this.carService = carService;
        converter = new CustomConverter();
//        binder = new Binder<>(Car.class);
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

        // Edit fields bar
        HorizontalLayout editFieldsLayout = new HorizontalLayout();
        editFieldsLayout.add(carMarkComboBox, carModelTextField, carColorComboBox);

        // Button bar
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonsLayout.add(addButton, saveButton, deleteButton);
        addButton.getStyle().set("mariginRight", "10px");
        saveButton.getStyle().set("mariginRight", "10px");
        enableButtons(false);

        // Grid
        carGrid = new Grid<>();
        carGrid.setHeightByRows(true);
        refreshGrid();
//        dataProvider = new ListDataProvider<>(carList);
//        carGrid.setDataProvider(dataProvider);
//        Grid.Column<Car> idColumn = carGrid
//                .addColumn(Car::getId)
//                .setHeader("ID");
//        Grid.Column<Car> markColumn = carGrid
//                .addColumn(Car::getMark)
//                .setHeader("Mark");
//
//
//        Grid.Column<Car> modelColumn = carGrid
//                .addColumn(Car::getModel)
//                .setHeader("Model");
//        Grid.Column<Car> colorColumn = carGrid
//                .addColumn(Car::getColor)
//                .setHeader("Color");

        carGrid.addColumn(Car::getId).setHeader("ID");
        carGrid.addColumn(Car::getMark).setHeader("Mark");
        carGrid.addColumn(Car::getModel).setHeader("Model");
        carGrid.addColumn(Car::getColor).setHeader("Color");


//        HeaderRow filterRow = carGrid.appendHeaderRow();
//        // First filter
//        filterCarMarkComboBox = new ComboBox<>();
//        filterCarMarkComboBox.setItems(CarMark.values());
//        filterCarMarkComboBox.addValueChangeListener(e -> applyFilter(dataProvider));
//        filterRow.getCell(markColumn).setComponent(filterCarMarkComboBox);
//        filterCarMarkComboBox.setSizeFull();
//        // Second filter
//        filterCarModelTextField = new TextField();
//        filterCarModelTextField.addValueChangeListener(e -> dataProvider.addFilter(
//                car -> StringUtils.containsIgnoreCase(car.getModel(),
//                        filterCarModelTextField.getValue())));
//        filterCarModelTextField.setValueChangeMode(ValueChangeMode.EAGER);
//        filterRow.getCell(modelColumn).setComponent(filterCarModelTextField);
//        filterCarModelTextField.setSizeFull();
//        filterCarModelTextField.setPlaceholder("Filter");
//        // Third filter
//        filterCarColorComboBox = new ComboBox<>();
//        filterCarColorComboBox.setItems(CarColor.values());
//        filterCarColorComboBox.addValueChangeListener(e -> applyFilter(dataProvider));
//        filterRow.getCell(colorColumn).setComponent(filterCarColorComboBox);
//        filterCarColorComboBox.setSizeFull();
//
//
        addButton.addClickListener(e -> addCar());
        saveButton.addClickListener(e -> saveCar(tmpCar));
        deleteButton.addClickListener(e -> deleteCar(tmpCar));


        carGrid.addItemClickListener(e -> {
            enableButtons(true);
            tmpCar = e.getItem();
            notification.setText("tmpCar: " + tmpCar.toString());
            notification.open();
            carMarkComboBox.setValue(tmpCar.getMark());
            carModelTextField.setValue(tmpCar.getModel());
            carColorComboBox.setValue(tmpCar.getColor());
            carModelTextField.focus();
        });

        add(editFieldsLayout);
        add(buttonsLayout);
        add(carGrid);


    }

    private void saveCar(Car car) {
        if ((carMarkComboBox.getValue() != null) &&
                (carModelTextField.getValue() != null) &&
                (carColorComboBox.getValue() != null)) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue());
            car.setColor(carColorComboBox.getValue());
            carService.updateCar(car.getId(), car);
            notification.setText("Saved " + car);
            refreshGrid();
            enableButtons(false);
        } else {
            notification.setText("Not saved. Select all required fields.");
        }
        notification.open();
    }

    private void deleteCar(Car car) {
        carService.deleteCar(car.getId());
        enableButtons(false);
        notification.setText("Deleted " + car);
        notification.open();
        refreshGrid();
    }


    private void addCar() {
        Car car = new Car();
        if ((carMarkComboBox.getValue() != null) &&
                (carModelTextField.getValue() != null) &&
                (carColorComboBox.getValue() != null)) {
            car.setMark(carMarkComboBox.getValue());
            car.setModel(carModelTextField.getValue());
            car.setColor(carColorComboBox.getValue());
            carService.addCar(car);
            refreshGrid();
            notification.setText("Added: " + car);
        } else {
            notification.setText("Not added. Select all required fields.");
        }
        notification.open();
    }

    private void refreshGrid() {
        carGrid.setItems(converter.getListFromIteralbe(carService.getAllCars()));
        carGrid.getDataProvider().refreshAll();
    }

    private void applyFilter(ListDataProvider<Car> dataProvider) {
        dataProvider.clearFilters();
        if (carMarkComboBox.getValue() != null)
            dataProvider.addFilter(car -> carMarkComboBox.getValue() == car.getMark());
        if (carColorComboBox.getValue() != null)
            dataProvider.addFilter(car -> carColorComboBox.getValue() == car.getColor());
    }

    private void enableButtons(boolean enable) {
        saveButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
        addButton.setEnabled(!enable);
    }


}
