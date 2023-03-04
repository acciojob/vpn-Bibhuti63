package com.driver.controllers;
import com.driver.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String countryName) throws Exception{
        //create a user of given country. The originalIp of the user should be "countryCode.userId" and return the user. Note that right now user is not connected and thus connected would be false and maskedIp would be null
        //Note that the userId is created automatically by the repository layer
        User user = userService.register(username, password, countryName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/subscribe")
    public void subscribe(@RequestParam Integer userId, @RequestParam Integer serviceProviderId){
        //subscribe to the serviceProvider by adding it to the list of providers and return updated User
        User user = userService.subscribe(userId, serviceProviderId);
    }
}
