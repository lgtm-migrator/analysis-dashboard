package edu.hm.hafner.java.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.util.NoSuchElementException;

/**
 * Service to interact with a database to save and find {@link Issues}.
 *
 * @author Ullrich Hafner
 */
@Service
public class IssuesEntityService {
    private final EntityService entityService;

    @Autowired
    public IssuesEntityService(final EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * Saves the specified set of issues in the database. If there is already an instance with the specified ID then an
     * exception is thrown. Inserts all {@link Issue} entities if they are not present in the database. Returns a new
     * object with the values of the database.
     *
     * @param issues
     *         to insert into the database
     *
     * @return new instance of the issues with the values of the database
     * @throws IllegalArgumentException
     *         if the ID of one of the issues already has been saved in the database
     */
    public Issues<Issue> save(final Issues<Issue> issues) {
        long duplicates = issues.stream()
                .map(issue -> issue.getId())
                .filter(id -> entityService.select(id).isPresent())
                .count();
        if (duplicates > 0) {
            throw new IllegalArgumentException(String.format("There are %d issues already stored in the date base.",
                    duplicates));
        }
        return entityService.insert(issues);
    }

    /**
     * Finds the set of issues with the specified id.
     *
     * @param id
     *         ID of the desired issues
     *
     * @return the issues
     * @throws NoSuchElementException
     *         if the set of issues with the specified ID has not been found
     */
    public Issues<Issue> findByPrimaryKey(final String id) {
        return entityService.select(id).orElseThrow(
                () -> new NoSuchElementException("No issues with id %s found.", id));
    }
}