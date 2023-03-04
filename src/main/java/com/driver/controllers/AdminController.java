package com.driver.controllers;

import com.driver.services.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminServiceImpl adminService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerAdmin(@RequestParam String username, @RequestParam String password){
        //create an admin and return
        Admin admin = adminService.register(username, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addProvider")
    public ResponseEntity<Void> addServiceProvider(@RequestParam int adminId, @RequestParam String providerName){
        //add a serviceProvider under the admin and return updated admin
        Admin admin = adminService.addServiceProvider(adminId, providerName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addCountry")
    public ResponseEntity<Void> addCountry(@RequestParam int serviceProviderId, @RequestParam String countryName) throws Exception{
        //add a country under the serviceProvider and return respective service provider
        //country name would be a 3-character string out of ind, aus, usa, chi, jpn. Each character can be in uppercase or lowercase. You should create a new Country object based on the given country name and add it to the country list of the service provider. Note that the user attribute of the country in this case would be null.
        //In case country name is not amongst the above mentioned strings, throw "Country not found" exception
        ServiceProvider serviceProvider = adminService.addCountry(serviceProviderId, countryName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
