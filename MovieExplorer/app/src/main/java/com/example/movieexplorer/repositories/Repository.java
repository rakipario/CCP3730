package com.example.movieexplorer.repositories;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repository<Model> {

    /**
     * Gets all datas from source
     * @param page page number to get. Pass "-1" to get all datas
     * @return List of Models
     */
    List<Model> getAll(int page);

    /**
     * Get Single model by it's id
     * @param id
     * @return Optional: Empty if not exist, Else Model that requested
     */
    Optional<Model> findOneById(int id);

    /**
     * Similar to Repository::findOneById(id) but this method allows to get data with more
     * complex comparison instructions.
     * @param predicate Comparison instructions for filtering
     * @return Optional: Empty if not exist, Else Model that requested
     */
    Optional<Model> findOne(Predicate<Model> predicate);

    /**
     * Similar to Repository::findOneById(id) but this method allows to get data with more
     * complex comparison instructions.
     * @param predicate Comparison instructions for filtering
     * @return Model list that filtered by predicate
     */
    List<Model> getAllMatched(Predicate<Model> predicate);

}
