package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.VerificationTokenInfoNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.ExpiredVerificationTokenException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.services.VerificationTokenService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;
import pl.lodz.p.it.zzpj.botsite.web.events.OnUserRegistrationCompleteEvent;
import java.util.Calendar;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(ModelMapper modelMapper,
                          UserService userService,
                          VerificationTokenService verificationTokenService,
                          ApplicationEventPublisher applicationEventPublisher) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.eventPublisher = applicationEventPublisher;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping(
            value = "register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void registerUser(@RequestBody UserRegistrationDto dto)
            throws UserAdditionException, UsernameAlreadyExistsException {
        User user = this.modelMapper.map(dto, User.class);
        this.userService.addUser(user);
        this.eventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(user));
    }

    @GetMapping(
            value = "activate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void activateUser(@RequestParam String token) throws VerificationTokenInfoNotFoundException,
            ExpiredVerificationTokenException, UserRetrievalException {

        VerificationTokenInfo tokenInfo = this.verificationTokenService.findVerificationTokenInfo(token);

        if (tokenInfo.getExpirationDate().getTime() - Calendar.getInstance().getTime().getTime() <= 0) {
            throw new ExpiredVerificationTokenException();
        }

        User user = tokenInfo.getUser();

        this.userService.updateUser(user);

    }

}
