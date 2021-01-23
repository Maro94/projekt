package pl.mallek.animalapimysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnimalApi {

    private AnimalRepo animalRepo;

    @Autowired
    public AnimalApi(AnimalRepo animalRepo) {
        this.animalRepo = animalRepo;
    }


    //get
    @GetMapping("/getList")
    public List<Animal>getAnimal(){
        return animalRepo.findAll();
    }
    //post
    @PostMapping("/addAnimal")
    public Animal addAnimal(@RequestBody Animal animal){
        return animalRepo.save(animal);
    }
    //delete
    @DeleteMapping("/delete/{id}")
    public void deleteAnimal(@PathVariable long id){
        animalRepo.deleteById(id);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Animal> putProduct(@PathVariable long id, @RequestBody Animal animal){
        return animalRepo.findById(id)
                .map(productFROMDB-> {
                    productFROMDB.setName(animal.getName());
                    animalRepo.save(productFROMDB);
                    return ResponseEntity.ok().body(animalRepo.save(productFROMDB));
                }).orElseGet(()->ResponseEntity.notFound().build());
    }

}