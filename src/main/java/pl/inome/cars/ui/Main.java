package pl.inome.cars.ui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import pl.inome.cars.model.Car;
import pl.inome.cars.model.CarColor;
import pl.inome.cars.model.CarMark;
import pl.inome.cars.service.CarService;
import pl.inome.cars.utils.CustomConverter;

import java.util.concurrent.atomic.AtomicReference;

@Route
public class Main extends VerticalLayout {

    private CarService carService;
    private Grid<Car> carGrid;
    private AtomicReference<ListDataProvider<Car>> carListDataProvider;
    private CarEditForm carEditForm;
    private CustomConverter converter;
    private ComboBox<CarMark> carMarkComboBox;
    private ComboBox<CarColor> carColorComboBox;

    public Main(CarService carService) {

        this.carService = carService;
        converter = new CustomConverter();
        carGrid = new Grid<>();
        carGrid.setHeightByRows(true);

        carListDataProvider = new AtomicReference<>(
                new ListDataProvider<>(converter.getListFromIteralbe(carService.getAllCars())));
        carGrid.setDataProvider(carListDataProvider.get());

        Grid.Column<Car> markColumn = carGrid
                .addColumn(Car::getMark)
                .setHeader("Mark");
        Grid.Column<Car> modelColumn = carGrid
                .addColumn(Car::getModel)
                .setHeader("Model");
        Grid.Column<Car> colorColumn = carGrid
                .addColumn(Car::getColor)
                .setHeader("Color");

        HeaderRow filterRow = carGrid.appendHeaderRow();
        // First filter
        carMarkComboBox = new ComboBox<>();
        carMarkComboBox.setItems(CarMark.values());
        carMarkComboBox.addValueChangeListener(e -> applyFilter(carListDataProvider.get()));
        filterRow.getCell(markColumn).setComponent(carMarkComboBox);
        carMarkComboBox.setSizeFull();
        // Second filter
        TextField modelTextField = new TextField();
        modelTextField.addValueChangeListener(e -> carListDataProvider.get().addFilter(
                car -> StringUtils.containsIgnoreCase(car.getModel(),
                        modelTextField.getValue())));
        modelTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(modelColumn).setComponent(modelTextField);
        modelTextField.setSizeFull();
        modelTextField.setPlaceholder("Filter");
        // Third filter
        carColorComboBox = new ComboBox<>();
        carColorComboBox.setItems(CarColor.values());
        carColorComboBox.addValueChangeListener(e -> applyFilter(carListDataProvider.get()));
        filterRow.getCell(colorColumn).setComponent(carColorComboBox);
        carColorComboBox.setSizeFull();

        add(carGrid);

        carEditForm = new CarEditForm(carService);

        carListDataProvider.set(DataProvider.ofCollection(converter.getCollectionFromIteralbe(carService.getAllCars())));
        carListDataProvider.get().setSortOrder(Car::getMark, SortDirection.ASCENDING);
        carGrid.setDataProvider(carListDataProvider.get());

        carGrid.addItemClickListener(e -> {
            carListDataProvider.set(DataProvider.ofCollection(converter.getCollectionFromIteralbe(carService.getAllCars())));
            carListDataProvider.get().setSortOrder(Car::getMark, SortDirection.ASCENDING);
            carGrid.setDataProvider(carListDataProvider.get());

            carGrid.getDataProvider().refreshAll();
            carEditForm.setCar(e.getItem());
            add(carEditForm);
        });

    }

    private void applyFilter(ListDataProvider<Car> dataProvider) {
        dataProvider.clearFilters();
        if (carMarkComboBox.getValue() != null)
            dataProvider.addFilter(car -> carMarkComboBox.getValue() == car.getMark());
        if (carColorComboBox.getValue() != null)
            dataProvider.addFilter(car -> carColorComboBox.getValue() == car.getColor());
    }
}
