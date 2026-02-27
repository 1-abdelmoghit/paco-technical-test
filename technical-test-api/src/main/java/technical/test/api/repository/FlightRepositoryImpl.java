package technical.test.api.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import technical.test.api.record.FlightRecord;

import java.util.regex.Pattern;

@Repository
public class FlightRepositoryImpl implements FlightRepositoryCustom {

    private final ReactiveMongoTemplate mongo;

    public FlightRepositoryImpl(ReactiveMongoTemplate mongo) {
        this.mongo = mongo;
    }

    /**
     * Get flights with pagination and sorting.
     * @param sortBy
     * @param order
     * @param page
     * @param size
     * @return Flux<FlightRecord>
     */
    @Override
    public Flux<FlightRecord> getFlights(String sortBy,
                                         String order,
                                         int page,
                                         int size) {
        Query query = new Query();
        String sortField =
                "location".equalsIgnoreCase(sortBy) ? "origin" : "price";

        Sort.Direction direction =
                "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        query.with(Sort.by(direction, sortField));
        int p = Math.max(0, page);
        int s = Math.max(1, size);
        query.skip((long) p * s).limit(s);
        return mongo.find(query, FlightRecord.class);
    }
}
