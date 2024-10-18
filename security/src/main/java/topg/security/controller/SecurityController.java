package topg.security.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class SecurityController {
    @GetMapping
    @PreAuthorize("hasRole('client_user')")
    public String Hello(){
        return"Hello Daiki";
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('client_admin')")
    public String Hell_double(){
        return"Hello Baki";
    }

}





