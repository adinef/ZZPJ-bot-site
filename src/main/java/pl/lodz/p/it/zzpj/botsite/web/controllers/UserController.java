package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserService userService;

    @PostMapping("/")
    public void registerUser(@RequestBody UserRegistrationDto dto) throws UsernameAlreadyExistsException {
        User user = modelMapper.map(dto, User.class);
        this.userService.addUser(user);
    }

}
