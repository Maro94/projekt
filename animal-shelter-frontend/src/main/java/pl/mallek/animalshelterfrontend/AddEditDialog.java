package pl.mallek.animalshelterfrontend;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
 
 
public class AddEditDialog extends Dialog {


    private ApiConnector apiConnector;
    private VerticalLayout layout;
    private Animal animal;
    private boolean isEdit=false;
    private TextField nameField;
    private IntegerField ageField;
    private CloseDialogEvent closeDialogEvent;

    public AddEditDialog() {
    }

    public AddEditDialog(ApiConnector apiConnector,CloseDialogEvent closeDialogEvent) {
        this.closeDialogEvent = closeDialogEvent;
        this.apiConnector = apiConnector;
        setup();
    }

    public AddEditDialog(Animal animal,ApiConnector apiConnector,CloseDialogEvent closeDialogEvent) {
        this.animal = animal;
        this.apiConnector = apiConnector;
        this.closeDialogEvent = closeDialogEvent;
        isEdit=true;
        setup();
    }

    private void setup(){
        layout = new VerticalLayout();
        String text =isEdit ? "Edit" :"Add";
        Text welcomeString = new Text(text+" Animal");
        nameField = new TextField();
        nameField.setLabel("Name");
        ageField = new IntegerField();
        ageField.setLabel("Age");
        ageField.setMin(0);
        ageField.setMax(50);
        if (isEdit) insertEditValues();
        Button button = new Button(text);
        button.addClickListener(click -> {
                String name = nameField.getValue().trim().strip();
                int age = ageField.getValue();
                if (!name.isEmpty()&&age>=ageField.getMin()&&age<=ageField.getMax()){
                    try {
                        if (isEdit)apiConnector.editAnimal(animal.getId(),new Animal(name,age));
                        else apiConnector.addAnimal(new Animal(name,age));
                        this.close();
                        this.closeDialogEvent.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Notification notification = new Notification(
                                "Couldn't "+text+" Animal", 3000);
                        notification.open();
                    }
                }else {
                    Notification notification = new Notification(
                            String.format("Name can't be empty and age should range between %d and %d",ageField.getMin(),ageField.getMax()), 3000);
                    notification.open();
                }


        });
        layout.add(welcomeString,nameField,ageField,button);
        add(layout);
    }

    private void insertEditValues(){
        nameField.setValue(animal.getName());
        ageField.setValue( animal.getAge());
    }
    interface CloseDialogEvent{
        void close();
    }

}
