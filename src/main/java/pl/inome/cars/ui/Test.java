package pl.inome.cars.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;


@Route
public class Test extends VerticalLayout {

    protected TextField textField;
    protected Button buttonAdd;
    protected Button buttonCancel;

    public Test() {

        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        textField = new TextField("texfield");
        buttonAdd = new Button("Add");
        buttonCancel = new Button("Cancel");
        Notification.show("Init test", 2000, Notification.Position.MIDDLE);

        add(textField, buttonAdd, buttonCancel);

        Dialog dialog = new Dialog();
        Input input = new Input();
        dialog.add(input);

        buttonAdd.addClickListener(e -> {
            dialog.open();
        });

        input.addValueChangeListener(e -> Notification.show(e.getValue(), 1000, Notification.Position.MIDDLE));


    }
}
