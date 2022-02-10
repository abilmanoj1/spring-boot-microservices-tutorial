package com.dexlock.moviecatalogservice.resource;

import com.dexlock.moviecatalogservice.model.CatalogueItem;
import com.dexlock.moviecatalogservice.model.Movie;
import com.dexlock.moviecatalogservice.model.Rating;
import com.dexlock.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder builder ;

    @RequestMapping("/{userId}")
    public List<CatalogueItem> getCatalogue(@PathVariable("userId") String userId){

        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId, UserRating.class);
//        System.out.println("Print statement"+ratings123);

//        List<Rating> ratings = Arrays.asList(
//                new Rating("1234",4),
//                new Rating("5678",3)
//        );
//        return ratings;

        return ratings.getUserRatings().stream().map(rating-> {
            Movie movie = restTemplate.getForObject("http://localhost:8081/movies/"+rating.getMovieId(), Movie.class);
//            Movie movie = builder.build().get().uri("http://localhost:8081/movies/"+rating.getMovieId())
//                    .retrieve().bodyToMono(Movie.class).block();
            return new CatalogueItem(movie.getName(), "This is the description", rating.getRating());
        }).collect(Collectors.toList());
    }
}
