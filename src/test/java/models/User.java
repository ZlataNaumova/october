package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class User {
    public String email;
    public String firstName;
    public String lastName;
    public String password;


}
