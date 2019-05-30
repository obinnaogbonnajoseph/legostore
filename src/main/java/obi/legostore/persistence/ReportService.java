package obi.legostore.persistence;

import obi.legostore.model.AvgRatingModel;
import obi.legostore.model.LegoSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Service
public class ReportService {

    public ReportService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private MongoTemplate mongoTemplate;

    public List<AvgRatingModel> getAvgRatingReport() {
        ProjectionOperation projectToMatchModel = project()
                .andExpression("name").as("productName")
                .andExpression("{$avg : '$reviews.rating'}").as("avgRating");
        Aggregation avgRatingAggregation = newAggregation(LegoSet.class, projectToMatchModel);

        return mongoTemplate
                .aggregate(avgRatingAggregation, LegoSet.class, AvgRatingModel.class)
                .getMappedResults();
    }
}
