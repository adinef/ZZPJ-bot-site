package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.NotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.RetrievalTimeException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.ExpiredVerificationTokenException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.StateNotConsistentException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.services.VerificationTokenService;
import pl.lodz.p.it.zzpj.botsite.web.dto.StatusDto;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserAccountDataDto;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserUpdateDto;
import pl.lodz.p.it.zzpj.botsite.web.events.OnUserRegistrationCompleteEvent;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final ApplicationEventPublisher eventPublisher;

    private final PrincipalProvider principalProvider;

    @Autowired
    public UserController(ModelMapper modelMapper,
                          UserService userService,
                          VerificationTokenService verificationTokenService,
                          ApplicationEventPublisher applicationEventPublisher,
                          PrincipalProvider principalProvider
    ) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.eventPublisher = applicationEventPublisher;
        this.verificationTokenService = verificationTokenService;
        this.principalProvider = principalProvider;
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
        this.userService.registerUser(user);
        this.eventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(user));
    }

    @GetMapping(
            value = "activate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public StatusDto activateUser(@RequestParam String token)
            throws StateNotConsistentException, RetrievalTimeException, NotFoundException, UserUpdateException {
        VerificationTokenInfo tokenInfo = this.verificationTokenService.findVerificationTokenInfo(token);
        if (tokenInfo.getExpirationTime().isAfter(LocalDateTime.now())) {
            throw new ExpiredVerificationTokenException();
        }
        User user = tokenInfo.getUser();
        this.userService.updateUser(user, user.getId());
        return new StatusDto("Successfully activated");
    }

    @PutMapping
    public StatusDto updateOwnAccount(@RequestBody UserUpdateDto userUpdateDto)
            throws UserRetrievalException, UserUpdateException {

        User user = this.userService.findByLogin(principalProvider.getName());
        User updateUser = modelMapper.map(userUpdateDto, User.class);
        updateUser.setLogin(user.getLogin());
        this.userService.updateUser(updateUser, user.getId());

        return new StatusDto("Successfully updated");
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserAccountDataDto getAccountData() throws UserRetrievalException {
        User loggedUser = this.userService.findByLogin(this.principalProvider.getName());
        return modelMapper.map(loggedUser, UserAccountDataDto.class);
    }


}
