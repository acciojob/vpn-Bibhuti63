package com.driver.controllers;

import com.driver.model.User;
import com.driver.services.impl.ConnectionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connection")
public class ConnectionController {
    @Autowired
    ConnectionServiceImpl connectionService;

    @PostMapping("/connect")
    public ResponseEntity<Void> connect(@RequestParam int userId, @RequestParam String countryName) throws Exception{
        //Connect the user to a vpn by considering the following priority order.
        //1. If the user is already connected to any service provider, throw "Already connected" exception.
        //2. Else if the countryName corresponds to the original country of the user, do nothing. This means that the user wants to connect to its original country, for which we do not require a connection. Thus, return the user as it is.
        //3. Else, the user should be subscribed under a serviceProvider having option to connect to the given country.
            //If the connection can not be made (As user does not have a serviceProvider or serviceProvider does not have given country, throw "Unable to connect" exception.
            //Else, establish the connection where the maskedIp is "updatedCountryCode.serviceProviderId.userId" and return the updated user. If multiple service providers allow you to connect to the country, use the service provider having smallest id.
        User user = connectionService.connect(userId, countryName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/disconnect")
    public ResponseEntity<Void> disconnect(@RequestParam int userId) throws Exception{
        //If the given user was not connected to a vpn, throw "Already disconnected" exception.
        //Else, disconnect from vpn, make masked Ip as null, update relevant attributes and return updated user.
        User user = connectionService.disconnect(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/communicate")
    public ResponseEntity<Void> communicate(@RequestParam int senderId, @RequestParam int receiverId) throws Exception{
        //Establish a connection between sender and receiver users
        //To communicate to the receiver, sender should be in the current country of the receiver.
        //If the receiver is connected to a vpn, his current country is the one he is connected to.
        //If the receiver is not connected to vpn, his current country is his original country.
        //The sender is initially not connected to any vpn. If the sender's original country does not match receiver's current country, we need to connect the sender to a suitable vpn. If there are multiple options, connect using the service provider having smallest id
        //If the sender's original country matches receiver's current country, we do not need to do anything as they can communicate. Return the sender as it is.
        //If communication can not be established due to any reason, throw "Cannot establish communication" exception
        User updatedSender = connectionService.communicate(senderId, receiverId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
