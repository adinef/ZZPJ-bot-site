package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.BotCreationDTO;

import java.security.Principal;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    private final ModelMapper modelMapper;
    private final BotService botService;
    private final UserService userService;
    private final PrincipalProvider principalProvider;

    @Autowired
    public BotController(ModelMapper modelMapper, BotService botService, UserService userService, PrincipalProvider principalProvider) {
        this.modelMapper = modelMapper;
        this.botService = botService;
        this.userService = userService;
        this.principalProvider = principalProvider;
    }

    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public BotCreationDTO createBot(@RequestBody BotCreationDTO dto) throws BotAlreadyExistsException, BotAdditionException, UserRetrievalException {
        Bot bot = this.modelMapper.map(dto, Bot.class);
        bot.setUser(userService.findByLogin(principalProvider.getName()));
        Bot addedBot = this.botService.addBot(bot);
        return this.modelMapper.map(addedBot, BotCreationDTO.class);
    }
}