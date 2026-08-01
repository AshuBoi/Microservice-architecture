package Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AcoountController {

    @GetMapping("/status/check")
    public String statusCheck(){
        return "Working";
    }
}
