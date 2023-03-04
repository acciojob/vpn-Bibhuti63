package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin=new Admin();
        admin.setPassword(password);
        admin.setUsername(username);

        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin=adminRepository1.findById(adminId).get();

        //creating a new service provider
        ServiceProvider serviceProvider=new ServiceProvider();
        //setting attribute of serviceProvider
        serviceProvider.setAdmin(admin);
        serviceProvider.setName(providerName);

        //setting attribute of admin
        admin.getServiceProviders().add(serviceProvider);
        adminRepository1.save(admin);

        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{

        //add a country under the serviceProvider and return respective service provider
        //country name would be a 3-character string out of ind, aus, usa, chi, jpn. Each character can be in uppercase or lowercase.
        // You should create a new Country object based on the given country name and add it to the country list of the service provider.
        // Note that the user attribute of the country in this case would be null.
        //In case country name is not amongst the above mentioned strings, throw "Country not found" exception

        if(countryName.equalsIgnoreCase("ind")||countryName.equalsIgnoreCase("aus") ||countryName.equalsIgnoreCase("usa") ||
                countryName.equalsIgnoreCase("chi") ||countryName.equalsIgnoreCase("jpn")){

            ServiceProvider serviceProvider=serviceProviderRepository1.findById(serviceProviderId).get();

            Country country=new Country();

            //setting attributes of Country
            if(countryName.equalsIgnoreCase("IND")){
                country.setCountryName(CountryName.IND);
                country.setCode(CountryName.IND.toCode());
            }
            if(countryName.equalsIgnoreCase("AUS")){
                country.setCountryName(CountryName.AUS);
                country.setCode(CountryName.AUS.toCode());
            }
            if(countryName.equalsIgnoreCase("USA")){
                country.setCountryName(CountryName.USA);
                country.setCode(CountryName.USA.toCode());
            }
            if(countryName.equalsIgnoreCase("CHI")){
                country.setCountryName(CountryName.CHI);
                country.setCode(CountryName.CHI.toCode());
            }
            if(countryName.equalsIgnoreCase("JPN")){
                country.setCountryName(CountryName.JPN);
                country.setCode(CountryName.JPN.toCode());
            }

            country.setServiceProvider(serviceProvider);

            //adding country to listOfcountry of ServiceProvider
            serviceProvider.getCountryList().add(country);
            //here user attribute of serviceProvider is null

            serviceProviderRepository1.save(serviceProvider);

            return serviceProvider;
        }
        else{
            throw new Exception("Country not found");
        }

    }

}
