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
     * Récupère les vols en fonction de l'origine, de la destination et de la pagination.
     * @param pageable
     * @return Flux<FlightRecord>
     */
    @Override
    public Flux<FlightRecord> getFlights(final Pageable pageable) {
        Query q = new Query();
        if (pageable.getSort().isSorted()) {
            q.with(pageable.getSort());
        }
        int p = Math.max(0, pageable.getPageNumber());
        int s = Math.max(1, pageable.getPageSize());
        q.skip((long) p * s).limit(s);
        return reactiveMongoTemplate.find(q, FlightRecord.class);
    }
}
