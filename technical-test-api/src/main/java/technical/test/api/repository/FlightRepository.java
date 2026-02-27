package technical.test.api.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import technical.test.api.record.FlightRecord;

import java.util.UUID;

@Repository
public interface FlightRepository extends ReactiveMongoRepository<FlightRecord, UUID> ,FlightRepositoryCustom {
}
