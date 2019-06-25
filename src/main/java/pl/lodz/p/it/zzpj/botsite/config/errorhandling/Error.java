package pl.lodz.p.it.zzpj.botsite.config.errorhandling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Error {
    @JsonIgnore
    private String error;

    @JsonIgnore
    private Exception exception;

    @JsonProperty("error")
    public String errorMessage() {
        return String.format("%s: %s.", error, exception.getLocalizedMessage());
    }
}
