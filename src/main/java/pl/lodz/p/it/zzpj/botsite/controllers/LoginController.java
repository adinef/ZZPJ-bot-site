package pl.lodz.p.it.zzpj.botsite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Made only for testing purposes. Later, in the process of front-end development it is to be removed.
 * Keep that in mind.
 */
@Controller
@RequestMapping("login")
public class LoginController {
    @GetMapping(
            value = ""
    )
    public String login() {
        return "login.html";
    }
}
