package ru.practicum.explore.with.me.users.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.with.me.exception.ConflictException;
import ru.practicum.explore.with.me.exception.NotFoundException;
import ru.practicum.explore.with.me.users.dto.UsersDto;
import ru.practicum.explore.with.me.users.dto.UsersMapper;
import ru.practicum.explore.with.me.users.model.User;
import ru.practicum.explore.with.me.users.repo.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Override
    @Transactional
    public UsersDto add(UsersDto usersDto) {
        try {
            User user = userRepo.save(UsersMapper.toUser(usersDto));
            return UsersMapper.toUsersDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public UsersDto edit(UsersDto usersDto) {
        User user = userRepo.findById(usersDto.getId())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        if (usersDto.getName() != null && !usersDto.getName().isEmpty()) {
            user.setName(usersDto.getName());
        }
        if (usersDto.getEmail() != null && !usersDto.getEmail().isEmpty()) {
            user.setEmail(usersDto.getEmail());
        }
        try {
            userRepo.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
        return UsersMapper.toUsersDto(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public List<UsersDto> search(List<Long> ids) {
        List<User> users = userRepo.findAllById(ids);
        return users.stream()
                .map(UsersMapper::toUsersDto)
                .collect(Collectors.toList());
    }

}
