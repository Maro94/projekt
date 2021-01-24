package pl.mallek.animalshelterfrontend;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@StyleSheet("http://fonts.googleapis.com/css?family=Cabin+Sketch")
@Route("")
public class AnimalFrontGui extends VerticalLayout {
    ApiConnector connector;
    Grid<Animal> grid = new Grid<>(Animal.class);
    List<Animal> animals;


    @Autowired
    public AnimalFrontGui(ApiConnector connector){
        this.connector = connector;
        setAlignItems(Alignment.CENTER);
        H1 header = new H1("Animal shelter");
        H2 animalText  = new H2("Available animals");
        refresh();
        addDeleteEditButtons();
        grid.setItems(animals);
        grid.removeColumnByKey("id");


        add(header,animalText,grid,addButton());
    }

    public void refresh() {
        try {
            animals=connector.getAnimalList();
            grid.setItems(animals);
        }catch (Exception e){
            animals = new ArrayList<>();
        }
    }

    private void addDeleteEditButtons(){

        grid.addComponentColumn(animal -> {
            HorizontalLayout buttons = new HorizontalLayout();
            Button delete = new Button("Delete");
            delete.addClickListener(buttonClickEvent -> {
                connector.deleteAnimal(animal.getId());
                refresh();

            });
            Button edit = new Button("Edit");
            edit.addClickListener(buttonClickEvent -> {
                AddEditDialog dialog = new AddEditDialog(animal,connector, this::refresh);
                dialog.open();

            });
            buttons.add(delete,edit);
            return buttons;
        });
    }

    private Button addButton(){
        Button button = new Button("Add animal");
        button.addClickListener(buttonClickEvent -> {
            AddEditDialog dialog = new AddEditDialog(connector, this::refresh);
            dialog.open();
        });
        return button;
    }


}
