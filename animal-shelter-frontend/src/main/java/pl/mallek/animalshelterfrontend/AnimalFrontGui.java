package pl.mallek.animalshelterfrontend;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

@Route("")
public class AnimalFrontGui extends VerticalLayout {
    ApiConnector connector;
    Grid<Animal> grid = new Grid<>(Animal.class);
    List<Animal> animals;




    @Autowired
    public AnimalFrontGui(ApiConnector connector){
        this.connector = connector;
        setAlignItems(Alignment.CENTER);
        Label header = new Label("Schronisko dla zwierząt");
        header.setWidth("80%");
        refresh();
        addAddEditButtons();
        grid.setItems(animals);
        grid.removeColumnByKey("id");


        add(header,grid);
    }

    public void refresh() {
        try {
            animals=connector.getAnimalList();
        }catch (Exception e){
            animals = new ArrayList<>();
        }
    }

    private void addAddEditButtons(){

        grid.addComponentColumn(animal -> {
            HorizontalLayout buttons = new HorizontalLayout();
            Button delete = new Button("Delete");
            delete.addClickListener(buttonClickEvent -> {
                connector.deleteAnimal(animal.getId());
                refresh();
                grid.getDataProvider().refreshAll();
            });
            Button edit = new Button("Edit");
            buttons.add(delete,edit);
            return buttons;
        });
    }


}
