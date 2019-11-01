package ar.edu.itba.pod.api.keypredicates;

import ar.edu.itba.pod.api.Movement;
import com.hazelcast.mapreduce.KeyPredicate;

public class CabotageKeyPredicate implements KeyPredicate<Movement> {
    @Override
    public boolean evaluate(Movement movement) {
        return movement.getFlightType() == Movement.FlightType.CABOTAGE;
    }
}
