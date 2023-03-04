package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        //1. If the user is already connected to any service provider, throw "Already connected" exception.
        //2. Else if the countryName corresponds to the original country of the user, do nothing. This means that
        // the user wants to connect to its original country, for which we do not require a connection. Thus, return the user as it is.
        //3. Else, the user should be subscribed under a serviceProvider having option to connect to the given country.
        User user=userRepository2.findById(userId).get();

        if(user.getConnected()){ //user.getMaskedIp!=null
            throw new Exception("Already connected");
        }
        else if (countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
            return user;
        }
        else {
            //If the connection can not be made (As user does not have a serviceProvider or serviceProvider does not have given country,
             // throw "Unable to connect" exception.
            //Else, establish the connection where the maskedIp is "updatedCountryCode.serviceProviderId.userId" and return the updated user.
                // If multiple service providers allow you to connect to the country, use the service provider having smallest id.
            if(user.getServiceProviderList().size()==0){ //==null
                throw new Exception("Unable to connect");
            }

            List<ServiceProvider>serviceProviderList=user.getServiceProviderList();
            int a=Integer.MAX_VALUE;
            ServiceProvider serviceProvider=null;
            Country country=null;

            Boolean flag=false;

            //traversing each service providers
            for(ServiceProvider serviceProvider1: serviceProviderList){

                List<Country>countryList=serviceProvider1.getCountryList();
                //traversing each country of service provider
                for(Country country1: countryList){
                    if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && a>serviceProvider1.getId()){
                        a=serviceProvider1.getId();
                        serviceProvider=serviceProvider1;
                        country=country1;

                        flag=true;
                    }
                }
            }

            if(!flag){
                throw new Exception("Unable to connect");
            }
            //here we have service provider present with given countryname so make a connection
            Connection connection=new Connection();
            connection.setUser(user);
            connection.setServiceProvider(serviceProvider);

            //set the maskedIp as "updatedCountryCode.serviceProviderId.userId"
            String countryCode=country.getCode();
            String mask=countryCode+"."+serviceProvider.getId()+"."+userId;

            user.setMaskedIp(mask);
            user.setConnected(true);

            user.getConnectionList().add(connection);

            serviceProvider.getConnectionList().add(connection);

            userRepository2.save(user);
            serviceProviderRepository2.save(serviceProvider);
        }
        return user;

    }
    @Override
    public User disconnect(int userId) throws Exception {
        //If the given user was not connected to a vpn, throw "Already disconnected" exception.
        //Else, disconnect from vpn, make masked Ip as null, update relevant attributes and return updated user.
        User user=userRepository2.findById(userId).get();
        if(!user.getConnected()){
            throw new Exception("Already disconnected");
        }
        user.setMaskedIp(null);
        user.setConnected(false);
        userRepository2.save(user);

        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        //Establish a connection between sender and receiver users
        //To communicate to the receiver, sender should be in the current country of the receiver.
        //If the receiver is connected to a vpn, his current country is the one he is connected to.
        //If the receiver is not connected to vpn, his current country is his original country.
        //The sender is initially not connected to any vpn. If the sender's original country does not match receiver's current country,
           // we need to connect the sender to a suitable vpn. If there are multiple options, connect using the service provider having smallest id
        //If the sender's original country matches receiver's current country, we do not need to do anything as they can communicate.
          // Return the sender as it is.
        //If communication can not be established due to any reason, throw "Cannot establish communication" exception

        User sender=userRepository2.findById(senderId).get(); //user
        User receiver=userRepository2.findById(receiverId).get(); //user1

        if(receiver.getMaskedIp()!=null){// receiver.getConnected()==true
            //receiver is connected to vpn , so its current country is the one he is connected to
            String mask=receiver.getMaskedIp();//maskedIp=(updatedCountryCode.serviceProviderId.userId)
            String cc=mask.substring(0,3);//to get the current country code

            if(cc.equals(sender.getOriginalCountry().getCode())){
                //both are in same country return sender as it is
                return sender;
            }
            else{
                //Senders original country doesnt match receivers currentCountry so connect sender with suiteble VPN
                String countryName="";

                if(cc.equalsIgnoreCase(CountryName.IND.toCode())){
                    countryName=CountryName.IND.toString();
                }
                if(cc.equalsIgnoreCase(CountryName.USA.toCode())){
                    countryName=CountryName.USA.toString();
                }
                if(cc.equalsIgnoreCase(CountryName.AUS.toCode())){
                    countryName=CountryName.AUS.toString();
                }
                if(cc.equalsIgnoreCase(CountryName.JPN.toCode())){
                    countryName=CountryName.JPN.toString();
                }
                if(cc.equalsIgnoreCase(CountryName.CHI.toCode())){
                    countryName=CountryName.CHI.toString();
                }

                //using DRY principle//calling existing function i.e connect

//                User user2;
//                try{
//                     user2=connect(senderId,countryName);
//                }
//                catch (Exception e){
//                    throw new Exception("Unable to connect");
//                }
                User user2=connect(senderId,countryName);


                if(!user2.getConnected()){
//                    throw new Exception("Cannot establish communication");
                    throw new Exception("Unable to connect");
                }
                else return user2;
            }
        }
        else{
            //receiver is not connected to vpn so original country is current country
            if(receiver.getOriginalCountry().equals(sender.getOriginalCountry())){
                return sender;
            }
            String countryName=receiver.getOriginalCountry().getCountryName().toString();
//            User user2;
//            try{
//                user2=connect(senderId,countryName);
//            }
//            catch (Exception e){
//                throw new Exception("Unable to connect");
//            }

            User user2=connect(senderId,countryName);

            if(!user2.getConnected()){
//                throw new Exception("Cannot establish communication");
                throw new Exception("Unable to connect");
            }
            else return user2;
        }

    }
}
