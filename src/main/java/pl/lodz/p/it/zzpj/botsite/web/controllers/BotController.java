package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.web.dto.BotCreationDTO;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    BotService botService;

    @PostMapping("/newBot")
    public void registerUser(@RequestBody BotCreationDTO dto) throws BotAlreadyExistsException {
        Bot bot = modelMapper.map(dto, Bot.class);
        this.botService.addBot(bot);
    }

}