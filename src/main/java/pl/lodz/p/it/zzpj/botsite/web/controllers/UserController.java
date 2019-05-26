package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.ExpiredVerificationTokenException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.VerificationTokenInfoNotFoundException;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;
import pl.lodz.p.it.zzpj.botsite.web.events.OnUserRegistrationCompleteEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    UserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegistrationDto dto, HttpServletRequest request)
            throws UsernameAlreadyExistsException {
        User user = modelMapper.map(dto, User.class);
        this.userService.addUser(user);

        String appUrl = request.getContextPath();
        this.eventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(user, appUrl));
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
