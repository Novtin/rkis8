package javaClasses;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javaClasses.entity.Glasses;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class RestMainController {

    private final GlassesRepository glassesRepository;

    @Autowired
    public RestMainController(GlassesRepository glassesRepository) {
        this.glassesRepository = glassesRepository;
    }

    @GetMapping("/glasses")
    public ResponseEntity<List<Glasses>> findElements(@RequestParam(name = "diopters", required = false)
                                                                       Double diopters) {
        if (glassesRepository.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if (diopters != null) {
            return new ResponseEntity<>(glassesRepository.findGlassesByDioptersGreaterThan(diopters), HttpStatus.OK);
        }
        return new ResponseEntity<>(glassesRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/glasses/{id}")
    public ResponseEntity<Glasses> showElement(@PathVariable("id") Long id) {
        if (glassesRepository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(glassesRepository.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping("/glasses")
    public ResponseEntity<String> addElementPost(@Valid @RequestBody Glasses glasses) {
        glassesRepository.save(glasses);
        return ResponseEntity.ok("Очки успешно добавлены");
    }

    @PutMapping("/glasses/{id}")
    public ResponseEntity<String> editElementPut(@PathVariable Long id, @Valid @RequestBody Glasses glasses) {
        Optional<Glasses> existingGlasses = glassesRepository.findById(id);
        if (existingGlasses.isPresent()) {
            glasses.setId(id);
            glassesRepository.save(glasses);
            return ResponseEntity.ok("Очки успешно обновлены");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Очки не найдены");
        }
    }

    @DeleteMapping("/glasses/{id}")
    public ResponseEntity<String> deleteElementDelete(@PathVariable Long id) {
        Optional<Glasses> glasses = glassesRepository.findById(id);
        if (glasses.isPresent()) {
            glassesRepository.delete(glasses.get());
            return ResponseEntity.ok("Очки успешно удалены");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Очки не найдены");
        }
    }

}
