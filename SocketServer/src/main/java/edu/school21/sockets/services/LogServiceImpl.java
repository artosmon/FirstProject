package edu.school21.sockets.services;

import edu.school21.sockets.models.Log;
import edu.school21.sockets.models.Room;
import edu.school21.sockets.repositories.LogRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("logService")
public class LogServiceImpl implements LogService{

    private LogRepository logRepository;

    public LogServiceImpl( @Qualifier("logRepo") LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    public Log checkEnter(long room_id, long user_id) {
        Optional<List<Log>> logs = logRepository.findByIdRoom(room_id);

        if(logs.isPresent())
            for(Log log : logs.get()) {
                if(log.getUserId() == user_id)
                    return log;
            }

        return null;
    }
}
