package com.aye.issueTracker.controller;

import com.aye.issueTracker.common.CustomErrorResponse;
import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.service.SequenceGeneratorService;
import com.aye.issueTracker.service.UserService;
import com.aye.issueTracker.utill.JwtUtil;
import com.aye.issuetrackerdto.entityDto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/")
    private ResponseEntity<?> saveUser(@RequestBody UserDto userDto){
        try {
            UserDto users = userService.saveUsers(userDto);
            return ResponseEntity.ok(users);
        }
        catch (InvalidRequestDataException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestHeader("Authorization") String encodedCredentials) throws Exception {
        try {
            String[] decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials.substring(6))).split(":");
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate
                        (new UsernamePasswordAuthenticationToken(decodedCredentials[0], decodedCredentials[1]));

            } catch (Exception ex) {
//                sout("inavalid username/password" + ex.getMessage());
                return new ResponseEntity<>(new CustomErrorResponse("inavalid username/password" + ex.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now()), HttpStatus.UNAUTHORIZED);
            }
            String jwt = jwtUtil.generateJwtToken(authentication);

            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable("id") Long id){
        try {
            UserDto users = userService.updateUser(userDto, id);
            return ResponseEntity.ok(users);
        }
        catch (InvalidRequestDataException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/")
    private ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    private ResponseEntity <?> getUserById(@PathVariable("id") Long id){

        try {
            return ResponseEntity.ok(userService.getUserById(id));

        } catch (ResourceNotFoundException e ){
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND,  ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND,  ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
