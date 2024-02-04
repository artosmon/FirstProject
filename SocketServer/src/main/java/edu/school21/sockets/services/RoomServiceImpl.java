package edu.school21.sockets.services;

import edu.school21.sockets.repositories.RoomsRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("roomService")
public class RoomServiceImpl implements RoomService {

    private final RoomsRepositoryImpl roomsRepository;

    @Autowired
    public RoomServiceImpl( @Qualifier("roomRepo") RoomsRepositoryImpl roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    @Override
    public boolean checkExist(String name) {
        if(roomsRepository.findByName(name).isPresent())
            return true;

        return false;
    }
}
