package pl.inome.cars.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CustomConverter {

    public <T> Collection<T> getCollectionFromIterable(Iterable<T> itr) {
        // Create an empty Collection to hold the result
        Collection<T> cltn = new ArrayList<>();

        return StreamSupport.stream(itr.spliterator(), false)
                .collect(Collectors.toList());
    }

    public <T> List<T> getListFromCollection(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    public <T> List<T> getListFromIterable(Iterable<T> itr) {
        // Create an empty Collection to hold the result
        Collection<T> cltn = new ArrayList<>();

        return StreamSupport.stream(itr.spliterator(), false)
                .collect(Collectors.toList());
    }


}
