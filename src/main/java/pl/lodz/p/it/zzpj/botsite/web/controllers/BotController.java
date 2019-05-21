package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.web.dto.BotCreationDTO;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    private final ModelMapper modelMapper;
    private final BotService botService;

    @Autowired
    public BotController(ModelMapper modelMapper, BotService botService) {
        this.modelMapper = modelMapper;
        this.botService = botService;
    }

    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public BotCreationDTO createBot(@RequestBody BotCreationDTO dto) throws BotAlreadyExistsException, BotAdditionException {
        Bot bot = this.modelMapper.map(dto, Bot.class);
        Bot addedBot = this.botService.addBot(bot);
        return this.modelMapper.map(addedBot, BotCreationDTO.class);
    }
}