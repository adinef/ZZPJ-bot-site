package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.ExpiredVerificationTokenException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.VerificationTokenInfoNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;
import pl.lodz.p.it.zzpj.botsite.web.events.OnUserRegistrationCompleteEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(ModelMapper modelMapper,
                          UserService userService,
                          ApplicationEventPublisher applicationEventPublisher) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.eventPublisher = applicationEventPublisher;
    }

    @PostMapping(
            value = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public UserRegistrationDto registerUser(@RequestBody UserRegistrationDto dto, HttpServletRequest request)
            throws UserAdditionException, UsernameAlreadyExistsException {
        User user = this.modelMapper.map(dto, User.class);
        User userRegistered = this.userService.addUser(user);

//        String appUrl = request.getContextPath();
//        this.eventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(user, appUrl));

        return this.modelMapper.map(userRegistered, UserRegistrationDto.class);
    }

    @PostMapping("/verifyAccount")
    public void activateUser(@RequestParam String token) throws VerificationTokenInfoNotFoundException,
            ExpiredVerificationTokenException {

        VerificationTokenInfo tokenInfo = this.userService.findVerificationTokenInfo(token);
        checkIfTokenNotExpired(tokenInfo);
    }

    private void checkIfTokenNotExpired(VerificationTokenInfo tokenInfo) throws ExpiredVerificationTokenException {
        if (tokenInfo.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime() <= 0) {
            throw new ExpiredVerificationTokenException();
        }
    }
}
