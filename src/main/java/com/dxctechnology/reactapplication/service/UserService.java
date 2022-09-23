package com.dxctechnology.reactapplication.service;

import com.dxctechnology.reactapplication.DTO.RouteDTO;
import com.dxctechnology.reactapplication.DTO.UserDTO;
import com.dxctechnology.reactapplication.entities.Route;
import com.dxctechnology.reactapplication.exception.ResourceNotFoundException;
import com.dxctechnology.reactapplication.exception.UsernameAlreadyExistsException;
import com.dxctechnology.reactapplication.entities.User;
import com.dxctechnology.reactapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public  User saveUser(User newUser) {
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);

        }catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }

    }

    public List<User> getUsers() {

        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("User not found with id " + id));
    }


    public void deleteUser(User user) {

        userRepository.delete(user);
    }

    public User upadateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);
        user.setUsername(userDTO.getUsername());
        user.setFullName(userDTO.getFullName());
        user.setPassword(userDTO.getPassword());
        user.setConfirmPassword(userDTO.getConfirmPassword());
        System.out.println(user);
        return saveUser(user);

    }


}
