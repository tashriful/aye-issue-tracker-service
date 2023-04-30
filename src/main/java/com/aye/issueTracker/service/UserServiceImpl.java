package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.User;
import com.aye.issueTracker.repository.UserRepository;
import com.aye.issuetrackerdto.entityDto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;


    @Override
    public UserDto saveUsers(UserDto userDto) {


        validateUser(userDto);

        User user = new User();
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

        return this.converToDto(usersRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = usersRepository.findAll();
        return users.stream().map(this::converToDto).collect(Collectors.toList());
    }


    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        validateUpdateUser(userDto);

        Optional<User> user = usersRepository.findById(id);

        if(user.isPresent()){

            User existingUser = user.get();

            existingUser.setName(userDto.getName());
            existingUser.setUsername(userDto.getUsername());
            existingUser.setPassword(userDto.getPassword());

            User updatedUser = usersRepository.save(existingUser);
            return converToDto(updatedUser);

        }
        else {
            throw new ResourceNotFoundException("User Not found With This ID: "+id);
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        return this.converToDto(usersRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with this id ::" +
                id)));
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = usersRepository.findById(id);
        if(user.isPresent()){
            usersRepository.deleteById(id);
        }else {
            throw new ResourceNotFoundException("User not found for this id: " + id);
        }
    }

    private UserDto converToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

//    private UserDto converToDto1(User user){
//        return new UserDto(user.getId(), user.getName(), user.getDesignation(), user.getAddress(), user.getUsername(),
//                user.getPassword(), user.getDepartment().getId(), user.getDepartment().getName(),
//                user.getTeam().getId(), user.getTeam().getName());
//    }

    private User convertToEntity(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    private void validateUser(UserDto user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new InvalidRequestDataException("Name is required");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidRequestDataException("Username is required");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidRequestDataException("Password is required");
        }

        List<User> users = usersRepository.findAllByUsername(user.getUsername());

        if(users.size() != 0){
            throw new InvalidRequestDataException("username already exist!");
        }

    }

    private void validateUpdateUser(UserDto user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new InvalidRequestDataException("Name is required");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidRequestDataException("Username is required");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidRequestDataException("Password is required");
        }

        List<User> users = usersRepository.findAllByUsername(user.getUsername());

        if(users.size() != 0){
            if(!Objects.equals(users.stream().findFirst().get().getId(), user.getId())) {
                throw new InvalidRequestDataException("username already exist!");
            }
        }

    }
}
