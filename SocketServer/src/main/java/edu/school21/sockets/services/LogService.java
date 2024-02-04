package edu.school21.sockets.services;

import edu.school21.sockets.models.Log;

public interface LogService {

    Log checkEnter(long room_id, long user_id);
}
