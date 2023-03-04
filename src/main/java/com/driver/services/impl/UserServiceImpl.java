package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        //create a user of given country. The originalIp of the user should be "countryCode.userId" and return the user.
        // Note that right now user is not connected and thus connected would be false and maskedIp would be null

        User user=new User();
        //setting the attribute of user
        user.setUsername(username);
        user.setPassword(password);

        //creating new country object based on countryname
        Country country=new Country();

        if(countryName.equalsIgnoreCase("IND")){
            country.setCountryName(CountryName.IND);
            country.setCode(CountryName.IND.toCode());
        }
        else if(countryName.equalsIgnoreCase("AUS")){
            country.setCountryName(CountryName.AUS);
            country.setCode(CountryName.AUS.toCode());
        } else if (countryName.equalsIgnoreCase("USA")) {
            country.setCountryName(CountryName.USA);
            country.setCode(CountryName.USA.toCode());
        }
        else if(countryName.equalsIgnoreCase("CHI")){
            country.setCountryName(CountryName.CHI);
            country.setCode(CountryName.CHI.toCode());
        } else if (countryName.equalsIgnoreCase("JPN")) {
            country.setCountryName(CountryName.JPN);
            country.setCode(CountryName.JPN.toCode());
        }
        else{
            throw new Exception("Country not found");
        }

//        user=userRepository3.save(user);

        country.setUser(user); //setting the foreign key of country
//        country=countryRepository3.save(country);

        user.setCountry(country);
        user.setConnected(false);


        String currentCode=country.getCode()+"."+userRepository3.save(user).getId();
        user.setOriginalIP(currentCode);

        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        //subscribe to the serviceProvider by adding it to the list of providers and return updated User

        User user=userRepository3.findById(userId).get();

        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        userRepository3.save(user);
//        serviceProviderRepository3.save(serviceProvider); //doubt

        return user;
    }
}
