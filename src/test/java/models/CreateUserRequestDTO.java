package models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateUserRequestDTO {
    public String name;
    public String email;
    public String password;
    public String type;
    public models.settings settings;

}
