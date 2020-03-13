package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
    public String email;
    public String firstName;
    public String lastName;
    public String password;


}
