package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "connection")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    //child w.r.t ServiceProvider
    @ManyToOne
    @JoinColumn
    private ServiceProvider serviceProvider;

    //child w.r.t User
    @ManyToOne
    @JoinColumn
    private User user;


    //constructor, getters, setters


    public Connection() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
