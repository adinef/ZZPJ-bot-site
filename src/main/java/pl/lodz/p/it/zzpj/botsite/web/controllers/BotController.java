package pl.lodz.p.it.zzpj.botsite.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotCreationDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotViewDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bot")
@Slf4j
public class BotController {

    private final ModelMapper modelMapper;
    private final BotService botService;
    private final PrincipalProvider principalProvider;
    private final UserService userService;

    @Autowired
    public BotController(ModelMapper modelMapper, BotService botService, PrincipalProvider principalProvider, UserService userService) {
        this.modelMapper = modelMapper;
        this.botService = botService;
        this.principalProvider = principalProvider;
        this.userService = userService;
    }


    // HAS TO BE SECURED
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


    // HAS TO BE SECURED (GET USER BY PRINCIPAL)
    @GetMapping(
            value = "user/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<BotViewDTO> getAllBotsForUser(@PathVariable("id") Long id) throws BotRetrievalException {
        List<Bot> bots = this.botService.findAllForUserId(id);
        return mapList(bots);
    }


    private List<BotViewDTO> mapList(List<Bot> bots) {
        List<BotViewDTO> dtoList = new ArrayList<>();
        bots.forEach( b -> this.modelMapper.map(b, BotViewDTO.class));
        return dtoList;
    }
}