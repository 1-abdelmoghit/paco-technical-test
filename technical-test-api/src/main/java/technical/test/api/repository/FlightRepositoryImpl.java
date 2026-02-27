package technical.test.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import technical.test.api.record.FlightRecord;

import java.util.regex.Pattern;

@Repository
public class FlightRepositoryImpl implements FlightRepositoryCustom {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public FlightRepositoryImpl(final ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    /**
     * Get flights with pagination and sorting.
     * @param origin
     * @param destination
     * @param pageable
     * @return Flux<FlightRecord>
     */
    @Override
    public Flux<FlightRecord> getFlights(final String origin, final String destination, final Pageable pageable) {
        final int safePage = Math.max(0, pageable.getPageNumber());
        final int safeSize = Math.max(1, pageable.getPageSize());

        Query query = new Query();

        if (origin != null && !origin.trim().isEmpty()) {
            final String escaped = Pattern.quote(origin.trim());
            query.addCriteria(Criteria.where("origin").regex("(?i).*" + escaped + ".*"));
        }

        if (destination != null && !destination.trim().isEmpty()) {
            final String escapedDest = Pattern.quote(destination.trim());
            query.addCriteria(Criteria.where("destination").regex("(?i).*" + escapedDest + ".*"));
        }

        if (pageable.getSort().isSorted()) {
            query.with(pageable.getSort());
        }

        query.skip((long) safePage * safeSize).limit(safeSize);

        return reactiveMongoTemplate.find(query, FlightRecord.class);
    }
}
