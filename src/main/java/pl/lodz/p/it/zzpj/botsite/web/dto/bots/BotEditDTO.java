package pl.lodz.p.it.zzpj.botsite.web.dto.bots;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BotEditDTO {
    String name;
    String channel;
}
