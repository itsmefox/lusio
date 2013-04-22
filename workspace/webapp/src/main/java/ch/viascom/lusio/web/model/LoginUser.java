package ch.viascom.lusio.web.model;

import javax.enterprise.inject.Model;

import lombok.Data;

@Data
@Model
public class LoginUser {
    private String username; 
    private String password; 
}
