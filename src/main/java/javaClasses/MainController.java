package javaClasses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import javaClasses.Glasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер web приложения
 */
@Controller
public class MainController {
    private final GlassesRepository glassesRepository;

    @Autowired
    public MainController(GlassesRepository glassesRepository) {
        this.glassesRepository = glassesRepository;
    }

    @GetMapping("/")
    public String showMenu() {
        return "index";
    }

    @GetMapping("/table")
    public String showTable(Model model) {
        model.addAttribute("glassesList", glassesRepository.findAllByOrderById());
        return "table";
    }

    @GetMapping("/save")
    public String addElementGet(Model model) {
        model.addAttribute("glasses", new Glasses());
        return "save";
    }

    @PostMapping("/save")
    public String addElementPost(@Valid @ModelAttribute("glasses") Glasses glasses,
                                 BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Введите корректные данные");
            return "save";
        }
        glassesRepository.save(glasses);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteElementGet(Model model) {
        model.addAttribute("glassesList", glassesRepository.findAllByOrderById());
        model.addAttribute("func", Arrays.asList("delete", "удаления"));
        return "inputId";
    }

    @PostMapping("/delete")
    public String deleteElementPost(@RequestParam(value = "inputId")
                                        @NotBlank @Pattern(regexp = "\\d+") String deleteId,
                                    Model model) {
        try {
            Optional<Glasses> glasses = glassesRepository.findById(Long.parseLong(deleteId));
            if (glasses.isEmpty()){
                throw new IndexOutOfBoundsException();
            } else {
                glasses.ifPresent(glassesRepository::delete);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException exception){
            model.addAttribute("errorMessage", "Введите корректный id");
            model.addAttribute("glassesList",  glassesRepository.findAllByOrderById());
            model.addAttribute("func", Arrays.asList("delete", "удаления"));
            return "inputId";
        }
        return "redirect:/";
    }

    @GetMapping("/find")
    public String findByDioptersGet() {
        return "find";
    }

    @PostMapping("/find")
    public String findByDioptersPost(@RequestParam(value = "inputDiopters")
                                         @NotBlank @Pattern(regexp = "^-?\\d+(\\.\\d+)?$") String inputDiopters,
                                     Model model) {
        double diopters;
        try {
            diopters = Double.parseDouble(inputDiopters);
        } catch (NumberFormatException exception){
            model.addAttribute("errorMessage", "Введите корректный id");
            model.addAttribute("glassesList",  glassesRepository.findAllByOrderById());
            return "find";
        }
        model.addAttribute("glassesList",
                glassesRepository.findGlassesByDioptersGreaterThan(diopters));
        return "table";
    }

    @GetMapping("/inputEdit")
    public String inputEditGet(Model model) {
        model.addAttribute("glassesList",  glassesRepository.findAllByOrderById());
        model.addAttribute("func", Arrays.asList("inputEdit", "изменения"));
        return "inputId";
    }

    @PostMapping("/inputEdit")
    public String inputEditPost(Model model, @RequestParam(value = "inputId")
    @NotBlank @Pattern(regexp = "\\d+") String editId) {
        try {
            Optional<Glasses> glasses = glassesRepository.findById(Long.parseLong(editId));
            if (glasses.isEmpty()){
                throw new IndexOutOfBoundsException();
            } else {
                model.addAttribute("glasses",  glasses.get());
            }
        } catch (NumberFormatException | IndexOutOfBoundsException exception){
            model.addAttribute("errorMessage", "Введите корректный id");
            model.addAttribute("glassesList",  glassesRepository.findAllByOrderById());
            model.addAttribute("func", Arrays.asList("inputEdit", "изменения"));
            return "inputId";
        }
        return "edit";
    }

    @GetMapping("/edit")
    public String editElementGet(Model model, @ModelAttribute("glasses") Glasses glasses) {
        model.addAttribute("glasses",  glasses);
        return "edit";
    }

    @PostMapping("/edit")
    public String editElementPost(Model model, @Valid @ModelAttribute("glasses") Glasses glasses,
                                  BindingResult result){
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Введите корректные данные");
            model.addAttribute("glasses", glasses);
            return "edit";
        }
        glassesRepository.save(glasses);
        return "redirect:/";
    }

    @GetMapping("/inputFind")
    public String inputFindGet(Model model) {
        model.addAttribute("glassesList",  glassesRepository.findAllByOrderById());
        model.addAttribute("func", Arrays.asList("inputFind", "вывода"));
        model.addAttribute("tableEnable", false);
        return "inputId";
    }

    @PostMapping("/inputFind")
    public String inputFindPost(Model model, @RequestParam(value = "inputId")
    @NotBlank @Pattern(regexp = "\\d+") String inputId) {
        try {
            Optional<Glasses> glasses = glassesRepository.findById(Long.parseLong(inputId));
            if (glasses.isEmpty()){
                throw new IndexOutOfBoundsException();
            } else {
                List<Glasses> glassesList = new ArrayList<>();
                glassesList.add(glasses.get());
                model.addAttribute("glassesList", glassesList);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException exception){
            model.addAttribute("errorMessage", "Введите корректный id");
            model.addAttribute("glassesList",  glassesRepository.findAllByOrderById());
            model.addAttribute("func", Arrays.asList("inputFind", "вывода"));
            model.addAttribute("tableEnable", false);
            return "inputId";
        }
        return "table";
    }

}
