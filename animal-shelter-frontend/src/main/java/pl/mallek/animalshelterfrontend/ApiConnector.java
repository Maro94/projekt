package pl.mallek.animalshelterfrontend;

import com.fasterxml.jackson.databind.util.JSONPObject;
import elemental.json.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.rmi.ServerException;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiConnector {

    private RestTemplate restTemplate = new RestTemplate();
    @Value("${api.url}")
    String apiUrl;


    public List<Animal> getAnimalList() throws ServerException {
        ResponseEntity<Animal[]> response = restTemplate.getForEntity("http://"+apiUrl+"/getList",Animal[].class);
        if (response.getStatusCode().is5xxServerError())throw new ServerException("There is problem with the server");
        if (response.getStatusCode().is4xxClientError())throw new ServerException("Wrong data");
        List<Animal> animals = Arrays.asList(response.getBody());
        return animals;
    }

    public void addAnimal(Animal animal) throws ServerException {
        ResponseEntity<Animal> response = restTemplate.postForEntity("http://"+apiUrl+"/addAnimal",animal, Animal.class);
        if (response.getStatusCode().is5xxServerError())throw new ServerException("There is problem with the server");
        if (response.getStatusCode().is4xxClientError())throw new ServerException("Wrong data");

    }

    public void deleteAnimal(String id){
        restTemplate.delete("http://"+apiUrl+"/delete/"+id);
    }

}
