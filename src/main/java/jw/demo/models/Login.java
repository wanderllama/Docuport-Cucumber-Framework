package jw.demo.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Login {

    private String username;
    private String userBase;
    private String passwd;
}
